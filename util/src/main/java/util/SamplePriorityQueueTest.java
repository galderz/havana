package util;

import java.util.Objects;

import static util.SamplePriorityQueue.span;

public class SamplePriorityQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
        testPeekSpan();
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
}
