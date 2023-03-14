package util.edges.v0;

import util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class EdgeQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testIsFull();
        testPushBeyondFull();
        testIterate();
        testClear();
        testFindTo();
        testLeafToRootPath();
    }

    private static void testLeafToRootPath()
    {
        System.out.println("EdgeQueueTest.testLeafToRootPath");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);
        for (int i = 0; i < capacity; i++)
        {
            Object to = i == capacity - 1 ? null : String.valueOf(i + 1);
            queue.push(String.valueOf(i), i, to);
        }

        final String leaf = "3";
        final List<Object> path = new ArrayList<>();
        path.add(leaf);

        Object current = leaf;
        Object from;
        int index;
        while ((index = queue.findTo(current)) != -1 && (from = queue.getFrom(index)) != null)
        {
            path.add(from);
            current = from;
        }
        assert 4 == path.size() : path.size();
        assert List.of("3", "2", "1", "0").equals(path) : path;
    }

    private static void testFindTo()
    {
        System.out.println("EdgeQueueTest.testFindTo");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);
        IntStream.range(0, capacity).forEach(i -> queue.push(String.valueOf(i), i, String.valueOf(i)));
        assert 0 == queue.findTo("0");
        assert 1 == queue.findTo("1");
        assert 2 == queue.findTo("2");
        assert 3 == queue.findTo("3");
    }

    private static void testClear()
    {
        System.out.println("EdgeQueueTest.testClear");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);

        IntStream.range(0, capacity).forEach(i -> queue.push(new Object(), i, new Object()));
        queue.clear();
        IntStream.range(0, capacity).forEach(i -> queue.push(new Object(), i + capacity, new Object()));
        IntStream.range(0, queue.size()).forEach(i ->
        {
            assert Objects.nonNull(queue.getFrom(i));
            assert i + 4 == queue.getLocation(i);
            assert Objects.nonNull(queue.getTo(i));
        });
    }

    private static void testIterate()
    {
        System.out.println("EdgeQueueTest.testIterate");
        final int capacity = 4;
        final EdgeQueue queue = new EdgeQueue(capacity);

        IntStream.range(0, capacity).forEach(i -> queue.push(new Object(), i, new Object()));
        IntStream.range(0, queue.size()).forEach(i ->
        {
            assert Objects.nonNull(queue.getFrom(i));
            assert i == queue.getLocation(i);
            assert Objects.nonNull(queue.getTo(i));
        });
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
