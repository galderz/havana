package util.edges.v1;

import util.Asserts;
import util.edges.v1.EdgeQueue.Edge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EdgeQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPushPoll();
        testPushPollAlternate();
        testPushBeyond();
    }

    private static void testPushBeyond()
    {
        System.out.println("EdgeQueueTest.testPushBeyond");
        final EdgeQueue queue = new EdgeQueue(3);
        for (int i = 1; i <= 4; i++)
        {
            final boolean success = queue.push(i, i * 100, i + 1);
            if (i < 4)
            {
                assert success;
            }
            else
            {
                assert !success;
            }
        }
    }

    private static void testPushPollAlternate()
    {
        System.out.println("EdgeQueueTest.testPushPollAlternate");
        final Map<Integer, Integer> graph = Map.of(1, 10, 2, 20, 3, 30, 10, 40, 20, 50);

        final EdgeQueue queue = new EdgeQueue(10);
        for (int i = 1; i <= 3; i++)
        {
            final boolean success = queue.push(i, -1, graph.get(i));
            assert success;
        }

        List<Edge> result = new ArrayList<>();
        Edge current;
        while ((current = queue.poll()) != null)
        {
            result.add(current);
            final Integer next = graph.get((Integer) current.to);
            if (next != null)
            {
                final boolean success = queue.push(current.to, -1, next);
                assert success;
            }
        }

        assert 5 == result.size();
        expect(1, -1, 10, result.get(0));
        expect(2, -1, 20, result.get(1));
        expect(3, -1, 30, result.get(2));
        expect(10, -1, 40, result.get(3));
        expect(20, -1, 50, result.get(4));
    }

    private static void testPushPoll()
    {
        System.out.println("EdgeQueueTest.testPushPoll");
        final EdgeQueue queue = new EdgeQueue(3);
        for (int i = 1; i <= 3; i++)
        {
            final boolean success = queue.push(i, i * 100, i + 1);
            assert success;
        }

        List<Edge> result = new ArrayList<>();
        Edge current;
        while ((current = queue.poll()) != null)
        {
            result.add(current);
        }

        assert 3 == result.size();
        expect(1, 100, 2, result.get(0));
        expect(2, 200, 3, result.get(1));
        expect(3, 300, 4, result.get(2));
    }

    private static void expect(Object from, int location, Object to, Edge result)
    {
        assert from.equals(result.from);
        assert location == result.location;
        assert to.equals(result.to);
    }
}
