package util;

import java.util.Objects;

import static util.JfrOldObjectSamplePriorityQueue.span;

public class JfrOldObjectSamplePriorityQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        testIsFull();
    }

    private static void testOfferThenPoll()
    {
        JfrOldObjectSamplePriorityQueue queue = new JfrOldObjectSamplePriorityQueue(10);
        queue.push(200);
        queue.push(400);
        queue.push(300);
        queue.push(500);
        queue.push(100);

        assert 100 == span(queue.poll());
        assert 200 == span(queue.poll());
        assert 300 == span(queue.poll());
        assert 400 == span(queue.poll());
        assert 500 == span(queue.poll());
        assert Objects.isNull(queue.poll());
    }

    private static void testIsFull()
    {
        JfrOldObjectSamplePriorityQueue queue = new JfrOldObjectSamplePriorityQueue(3);
        queue.push(300);
        assert !queue.isFull();
        queue.push(200);
        assert !queue.isFull();
        queue.push(100);
        assert queue.isFull();
    }
}
