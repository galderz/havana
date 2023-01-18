package util.sampling.v2;

import util.Asserts;
import util.sampling.v2.SamplePriorityWeakRefFIFOQueue.SampleList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SamplePriorityWeakRefFIFOQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
        testPeek();
        testIterateAllocationTimesFIFO();
        testIterateAllocationTimesFIFOSizeMinusOne();
        testIterateAllocationTimesFIFOSize();
        testIterateAllocationTimesFIFOSizePlusOnePopOldest();
        testIterateAllocationTimesFIFOSizePlusOnePopMiddle();
        testIterateAllocationTimesFIFOSizePlusOnePopYoungest();
        testPushAndIterateMany();
    }

    private static void testPushAndIterateMany()
    {
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(256);

        // Attempt to push a large number of entries
        for (int i = 0; i < 1_000_000; i++)
        {
            int allocated = i;

            if (queue.isFull())
            {
                if (queue.peekSpan() > allocated)
                {
                    // Sample will not fit, return early
                    return;
                }
                // Offered element has a higher priority,
                // vacate from the lowest priority one and insert the element.
                queue.poll();
            }

            try
            {
                queue.push(new WeakReference<>(new Object()), allocated, 0, 0, 0, 0, 0);
            }
            catch (NullPointerException npe)
            {
                System.out.printf("Null pointer at iteration %d%n", i);
                throw npe;
            }
        }

        // Attempt to iterate over the contents of the queue
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        int count = 0;
        while (current != null)
        {
            count++;
            current = sampleList.next(current);
        }

        assert 256 == count;
    }

    private static void testOfferThenPoll()
    {
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(10);
        queue.push(new WeakReference<>("200"), 200, 1, 0, 0, 0, 0);
        queue.push(new WeakReference<>("400"), 400, 2, 0, 0, 0, 0);
        queue.push(new WeakReference<>("300"), 300, 3, 0, 0, 0, 0);
        queue.push(new WeakReference<>("500"), 500, 4, 0, 0, 0, 0);
        queue.push(new WeakReference<>("100"), 100, 5, 0, 0, 0, 0);

        assert 100 == queue.peekSpan();
        assert "100" == queue.peekObject().get();
        queue.poll();
        assert 200 == queue.peekSpan();
        assert "200" == queue.peekObject().get();
        queue.poll();
        assert 300 == queue.peekSpan();
        assert "300" == queue.peekObject().get();
        queue.poll();
        assert 400 == queue.peekSpan();
        assert "400" == queue.peekObject().get();
        queue.poll();
        assert 500 == queue.peekSpan();
        assert "500" == queue.peekObject().get();
        queue.poll();
        assert -1 == queue.peekSpan();
        assert null == queue.peekObject();
    }

    private static void testIsFull()
    {
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(3);
        queue.push(new WeakReference<>(new Object()), 300, 1, 0, 0, 0, 0);
        assert !queue.isFull();
        queue.push(new WeakReference<>(new Object()), 200, 2, 0, 0, 0, 0);
        assert !queue.isFull();
        queue.push(new WeakReference<>(new Object()), 100, 3, 0, 0, 0, 0);
        assert queue.isFull();
    }

    private static void testPeek()
    {
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(3);
        assert -1 == queue.peekSpan();
        assert null == queue.peekObject();
        queue.push(new WeakReference<>("300"), 300, 1, 0, 0, 0, 0);
        assert 300 == queue.peekSpan();
        assert "300" == queue.peekObject().get();
    }

    private static void testIterateAllocationTimesFIFO()
    {
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(10);
        queue.push(new WeakReference<>("200"), 200, 1, 0, 0, 0, 0);
        queue.push(new WeakReference<>("400"), 400, 2, 0, 0, 0, 0);
        queue.push(new WeakReference<>("300"), 300, 3, 0, 0, 0, 0);
        queue.push(new WeakReference<>("500"), 500, 4, 0, 0, 0, 0);
        queue.push(new WeakReference<>("100"), 100, 5, 0, 0, 0, 0);

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L)) : allocationTimes;
        assert objects.equals(List.of("200", "400", "300", "500", "100")) : allocationTimes;
    }

    private static void testIterateAllocationTimesFIFOSizeMinusOne()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size - 1; i++)
        {
            final int allocationTime = i;
            final int span = i * 100;
            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6")) : objects;
    }

    private static void testIterateAllocationTimesFIFOSize()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size; i++)
        {
            int allocationTime = i;
            int span = i * 100;
            if (queue.isFull())
            {
                if (queue.peekSpan() > span)
                {
                    return;
                }
                queue.poll();
            }

            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "7")) : objects;
    }

    // Pop oldest because that's the one with the lowest span
    private static void testIterateAllocationTimesFIFOSizePlusOnePopOldest()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i * 100;
            if (queue.isFull())
            {
                if (queue.peekSpan() > span)
                {
                    return;
                }
                queue.poll();
            }

            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)) : allocationTimes;
        assert objects.equals(List.of("1", "2", "3", "4", "5", "6", "7", "8")) : objects;
    }

    // Pop middle because that is the one with the lowest span
    private static void testIterateAllocationTimesFIFOSizePlusOnePopMiddle()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i == (size / 2) ? 100 : 200;
            if (queue.isFull())
            {
                if (queue.peekSpan() > span)
                {
                    return;
                }
                queue.poll();
            }

            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 5L, 6L, 7L, 8L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "5", "6", "7", "8")) : objects;
    }

    private static void testIterateAllocationTimesFIFOSizePlusOnePopYoungest()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i == (size - 1) ? 100 : 200;
            if (queue.isFull())
            {
                if (queue.peekSpan() > span)
                {
                    return;
                }
                queue.poll();
            }

            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        while (current != null)
        {
            allocationTimes.add(queue.getAllocationTime(current));
            objects.add((String) queue.getReference(current).get());
            current = sampleList.next(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 8L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "8")) : objects;
    }

    private static void testRemoveNonNull()
    {
        final int size = 8;
        SamplePriorityWeakRefFIFOQueue queue = new SamplePriorityWeakRefFIFOQueue(size);

        for (int i = 0; i < size; i++)
        {
            int allocationTime = i;
            long span = i * 100;
            if (queue.isFull())
            {
                if (queue.peekSpan() > span)
                {
                    return;
                }
                queue.poll();
            }

            queue.push(new WeakReference<>(String.valueOf(allocationTime)), span, allocationTime, 0, 0, 0, 0);
        }

        List<String> objects = new ArrayList<>();
        final SampleList sampleList = queue.list();
        Object[] current = sampleList.head();
        int removed = 0;
        while (current != null)
        {
            Object[] next = sampleList.next(current);

            String value = (String) queue.getReference(current).get();
            if (value != null)
            {
                queue.remove(current);
                removed++;
                objects.add(value);
            }

            current = next;
        }

        assert 8 == removed;
        assert sampleList.head() == null : sampleList.head();
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "8")) : objects;
    }
}
