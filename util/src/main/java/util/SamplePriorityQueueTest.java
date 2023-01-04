package util;

import java.util.ArrayList;
import java.util.List;

public class SamplePriorityQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
        testPeekSpan();
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
        SamplePriorityQueue queue = new SamplePriorityQueue(256);

        // Attempt to push a large number of entries
        for (int i = 0; i < 1_000_000; i++)
        {
            int allocated = i;

            if (queue.isFull()) {
                if (queue.peekSpan() > allocated) {
                    // Sample will not fit, return early
                    return;
                }
                // Offered element has a higher priority,
                // vacate from the lowest priority one and insert the element.
                queue.poll();
            }

            try
            {
                queue.push(new Object(), allocated, 0, 0, 0, 0);
            }
            catch (NullPointerException npe)
            {
                System.out.printf("Null pointer at iteration %d%n", i);
                throw npe;
            }
        }

        // Attempt to iterate over the contents of the queue
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        int count = 0;
        while (current >= 0)
        {
            count++;
            current = sampleList.prevIndex(current);
        }

        assert 256 == count;
    }

    private static void testOfferThenPoll()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push(new Object(), 200, 1, 0, 0, 0);
        queue.push(new Object(), 400, 2, 0, 0, 0);
        queue.push(new Object(), 300, 3, 0, 0, 0);
        queue.push(new Object(), 500, 4, 0, 0, 0);
        queue.push(new Object(), 100, 5, 0, 0, 0);

        assert 100 == queue.peekSpan();
        queue.poll();
        assert 200 == queue.peekSpan();
        queue.poll();
        assert 300 == queue.peekSpan();
        queue.poll();
        assert 400 == queue.peekSpan();
        queue.poll();
        assert 500 == queue.peekSpan();
        queue.poll();
        assert -1 == queue.peekSpan();
    }

    private static void testIsFull()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(3);
        queue.push(new Object(), 300, 1, 0, 0, 0);
        assert !queue.isFull();
        queue.push(new Object(), 200, 2, 0, 0, 0);
        assert !queue.isFull();
        queue.push(new Object(), 100,3, 0, 0, 0);
        assert queue.isFull();
    }

    private static void testPeekSpan()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(3);
        assert -1 == queue.peekSpan();
        queue.push(new Object(), 300, 1, 0, 0, 0);
        assert 300 == queue.peekSpan();
    }

    private static void testIterateAllocationTimesFIFO()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push(new Object(), 200, 1, 0, 0, 0);
        queue.push(new Object(), 400, 2, 0, 0, 0);
        queue.push(new Object(), 300, 3, 0, 0, 0);
        queue.push(new Object(), 500, 4, 0, 0, 0);
        queue.push(new Object(), 100, 5, 0, 0, 0);

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L)) : allocationTimes;
    }

    private static void testIterateAllocationTimesFIFOSizeMinusOne()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size - 1; i++)
        {
            final int allocationTime = i;
            final int span = i * 100;
            queue.push(new Object(), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L)) : allocationTimes;
    }

    private static void testIterateAllocationTimesFIFOSize()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size; i++)
        {
            int allocationTime = i;
            int span = i * 100;
            if (queue.isFull()) {
                if (queue.peekSpan() > span) {
                    return;
                }
                queue.poll();
            }

            queue.push(new Object(), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L)) : allocationTimes;
    }

    // Pop oldest because that's the one with the lowest span
    private static void testIterateAllocationTimesFIFOSizePlusOnePopOldest()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i * 100;
            if (queue.isFull()) {
                if (queue.peekSpan() > span) {
                    return;
                }
                queue.poll();
            }

            queue.push(new Object(), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)) : allocationTimes;
    }

    // Pop middle because that is the one with the lowest span
    private static void testIterateAllocationTimesFIFOSizePlusOnePopMiddle()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i == (size / 2) ? 100 : 200;
            if (queue.isFull()) {
                if (queue.peekSpan() > span) {
                    return;
                }
                queue.poll();
            }

            queue.push(new Object(), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 5L, 6L, 7L, 8L)) : allocationTimes;
    }

    private static void testIterateAllocationTimesFIFOSizePlusOnePopYoungest()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size + 1; i++)
        {
            int allocationTime = i;
            int span = i == (size - 1) ? 100 : 200;
            if (queue.isFull()) {
                if (queue.peekSpan() > span) {
                    return;
                }
                queue.poll();
            }

            queue.push(new Object(), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        final SamplePriorityQueue.SampleList sampleList = queue.asList();
        int current = sampleList.firstIndex();
        while (current >= 0)
        {
            allocationTimes.add(sampleList.allocationTimeAt(current));
            current = sampleList.prevIndex(current);
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 8L)) : allocationTimes;
    }
}
