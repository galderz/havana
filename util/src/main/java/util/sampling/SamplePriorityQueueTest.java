package util.sampling;

import util.Asserts;

import java.util.ArrayList;
import java.util.List;

public class SamplePriorityQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
        testPeek();
        testIterateAllocationTimes();
        testIterateAllocationTimesSizeMinusOne();
        testIterateAllocationTimesSize();
        testIterateAllocationTimesSizePlusOnePopOldest();
        testIterateAllocationTimesSizePlusOnePopMiddle();
        testIterateAllocationTimesSizePlusOnePopYoungest();
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
        final SampleEntry[] sampleArray = queue.getArray();
        int count = 0;
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                count++;
            }
        }

        assert 256 == count;
    }

    private static void testOfferThenPoll()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push("200", 200, 1, 0, 0, 0);
        queue.push("400", 400, 2, 0, 0, 0);
        queue.push("300", 300, 3, 0, 0, 0);
        queue.push("500", 500, 4, 0, 0, 0);
        queue.push("100", 100, 5, 0, 0, 0);

        assert 100 == queue.peekSpan();
        assert "100" == queue.peekObject();
        queue.poll();
        assert 200 == queue.peekSpan();
        assert "200" == queue.peekObject();
        queue.poll();
        assert 300 == queue.peekSpan();
        assert "300" == queue.peekObject();
        queue.poll();
        assert 400 == queue.peekSpan();
        assert "400" == queue.peekObject();
        queue.poll();
        assert 500 == queue.peekSpan();
        assert "500" == queue.peekObject();
        queue.poll();
        assert -1 == queue.peekSpan();
        assert null == queue.peekObject();
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

    private static void testPeek()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(3);
        assert -1 == queue.peekSpan();
        assert null == queue.peekObject();
        queue.push("300", 300, 1, 0, 0, 0);
        assert 300 == queue.peekSpan();
        assert "300" == queue.peekObject();
    }

    private static void testIterateAllocationTimes()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push("200", 200, 1, 0, 0, 0);
        queue.push("400", 400, 2, 0, 0, 0);
        queue.push("300", 300, 3, 0, 0, 0);
        queue.push("500", 500, 4, 0, 0, 0);
        queue.push("100", 100, 5, 0, 0, 0);

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(5L, 1L, 3L, 4L, 2L)) : allocationTimes;
        assert objects.equals(List.of("100", "200", "300", "500", "400")) : allocationTimes;
    }

    private static void testIterateAllocationTimesSizeMinusOne()
    {
        final int size = 8;
        SamplePriorityQueue queue = new SamplePriorityQueue(size);

        for (int i = 0; i < size - 1; i++)
        {
            final int allocationTime = i;
            final int span = i * 100;
            queue.push(String.valueOf(allocationTime), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6")) : objects;
    }

    private static void testIterateAllocationTimesSize()
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

            queue.push(String.valueOf(allocationTime), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "7")) : objects;
    }

    // Pop oldest because that's the one with the lowest span
    private static void testIterateAllocationTimesSizePlusOnePopOldest()
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

            queue.push(String.valueOf(allocationTime), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(1L, 3L, 2L, 7L, 4L, 5L, 6L, 8L)) : allocationTimes;
        assert objects.equals(List.of("1", "3", "2", "7", "4", "5", "6", "8")) : objects;
    }

    // Pop middle because that is the one with the lowest span
    private static void testIterateAllocationTimesSizePlusOnePopMiddle()
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

            queue.push(String.valueOf(allocationTime), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(7L, 0L, 2L, 3L, 1L, 5L, 6L, 8L)) : allocationTimes;
        assert objects.equals(List.of("7", "0", "2", "3", "1", "5", "6", "8")) : objects;
    }

    private static void testIterateAllocationTimesSizePlusOnePopYoungest()
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

            queue.push(String.valueOf(allocationTime), span, allocationTime, 0, 0, 0);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        final SampleEntry[] sampleArray = queue.getArray();
        for (int i = 0; i < sampleArray.length; i++)
        {
            final SampleEntry entry = sampleArray[i];
            if (entry != null)
            {
                allocationTimes.add(entry.getAllocationTime());
                objects.add((String) entry.getObject());
            }
        }

        assert allocationTimes.equals(List.of(3L, 0L, 2L, 1L, 4L, 5L, 6L, 8L)) : allocationTimes;
        assert objects.equals(List.of("3", "0", "2", "1", "4", "5", "6", "8")) : objects;
    }
}
