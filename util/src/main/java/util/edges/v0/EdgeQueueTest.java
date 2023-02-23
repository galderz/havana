package util.edges.v0;

import util.Asserts;

public class EdgeQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testIsFull();
        testPushBeyondFull();
    }

    private static void testPushBeyondFull()
    {
        System.out.println("EdgeQueueTest.testPushBeyondFull");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);
        for (int i = 0; i < capacity; i++)
        {
            queue.push(new Object(), i, new Object());
        }
        assert queue.isFull();
        try
        {
            queue.push(new Object(), capacity, new Object());
            assert false : "should have failed";
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            assert "Index 4 out of bounds for length 4".equals(e.getMessage());
        }
    }

    private static void testIsFull()
    {
        System.out.println("EdgeQueueTest.testIsFull");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);
        assert !queue.isFull();

        for (int i = 0; i < capacity; i++)
        {
            queue.push(new Object(), i, new Object());
            if (i == capacity - 1)
                assert queue.isFull();
            else
                assert !queue.isFull();
        }
    }
}
