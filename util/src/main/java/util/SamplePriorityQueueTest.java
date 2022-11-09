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
    }

    private static void testOfferThenPoll()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push(new Object(), 200, 1);
        queue.push(new Object(), 400, 2);
        queue.push(new Object(), 300, 3);
        queue.push(new Object(), 500, 4);
        queue.push(new Object(), 100, 5);

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
        assert Objects.isNull(queue.peekSpan());
    }

    private static void testIsFull()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(3);
        queue.push(new Object(), 300, 1);
        assert !queue.isFull();
        queue.push(new Object(), 200, 2);
        assert !queue.isFull();
        queue.push(new Object(), 100,3);
        assert queue.isFull();
    }

    private static void testPeekSpan()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(3);
        assert null == queue.peekSpan();
        queue.push(new Object(), 300, 1);
        assert 300 == queue.peekSpan();
    }

    private static void testIterateAllocationTimesFIFO()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push(new Object(), 200, 1);
        queue.push(new Object(), 400, 2);
        queue.push(new Object(), 300, 3);
        queue.push(new Object(), 500, 4);
        queue.push(new Object(), 100, 5);

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
