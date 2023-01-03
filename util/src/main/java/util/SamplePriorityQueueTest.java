package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SamplePriorityQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
        testPeekSpan();
        testIterateAllocationTimesFIFO();
        testLoadUntilFull();
    }

    private static void testLoadUntilFull()
    {
        System.out.println("testLoadUntilFull");
        SamplePriorityQueue queue = new SamplePriorityQueue(256);

        for (int i = 0; i < 300; i++)
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
}
