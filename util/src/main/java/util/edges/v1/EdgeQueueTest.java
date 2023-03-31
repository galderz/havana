package util.edges.v1;

import util.Asserts;
import util.edges.v1.EdgeQueue.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeQueueTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testPushPop();
        testPushPopAlternate();
        testPushBeyond();
        testSizeWrapped();
        testFrontiers();
    }

    private static void testFrontiers()
    {
        System.out.println("EdgeQueueTest.testFrontiers");
        final Map<Integer, Integer> graph = new HashMap<>();
        graph.put(1, 10);
        graph.put(2, 20);
        graph.put(3, 30);
        graph.put(4, 40);
        graph.put(5, 50);
        graph.put(6, 60);
        graph.put(7, 70);
        graph.put(8, 80);
        graph.put(10, 100);
        graph.put(20, 200);
        graph.put(30, 300);
        graph.put(40, 400);
        graph.put(100, 1_000);
        graph.put(200, 2_000);
        graph.put(1_000, 10_000);

        final EdgeQueue queue = new EdgeQueue(16);
        for (int i = 1; i <= 8; i++)
        {
            final boolean success = queue.push(i, -1, graph.get(i), null);
            assert success;
        }

        List<Edge> result = new ArrayList<>();
        final FrontierLevels frontiers = new FrontierLevels();
        frontiers.next = queue.tail();
        while (!isComplete(frontiers, queue))
        {
            final Edge edge = queue.pop();
            result.add(edge);
            final Integer next = graph.get(edge.to);
            if (next != null)
            {
                queue.push(edge.to, -1, next, edge);
            }

        }
        assert 0 == queue.size();
        assert !queue.isFull();

        assert graph.size() == result.size();
        expect(1, -1, 10, null, result.get(0));
        expect(2, -1, 20, null, result.get(1));
        expect(3, -1, 30, null, result.get(2));
        expect(4, -1, 40, null, result.get(3));
        expect(5, -1, 50, null, result.get(4));
        expect(6, -1, 60, null, result.get(5));
        expect(7, -1, 70, null, result.get(6));
        expect(8, -1, 80, null, result.get(7));
        expect(10, -1, 100, result.get(0), result.get(8));
        expect(20, -1, 200, result.get(1), result.get(9));
        expect(30, -1, 300, result.get(2), result.get(10));
        expect(40, -1, 400, result.get(3), result.get(11));
        expect(100, -1, 1_000, result.get(8), result.get(12));
        expect(200, -1, 2_000, result.get(9), result.get(13));
        expect(1_000, -1, 10_000, result.get(12), result.get(14));
    }

    private static class FrontierLevels
    {
        long current;
        long next;
        long prev;
    }

    private static boolean isComplete(FrontierLevels frontiers, EdgeQueue queue)
    {
        if (queue.head() < frontiers.next)
        {
            return false;
        }
        if (queue.head() > frontiers.next)
        {
            return true;
        }
        if (queue.isEmpty())
        {
            return true;
        }
        stepFrontier(frontiers, queue);
        return false;
    }

    private static void stepFrontier(FrontierLevels frontiers, EdgeQueue queue)
    {
        logCompletedFrontier(frontiers);
        frontiers.current++;
        frontiers.prev = frontiers.next;
        frontiers.next = queue.tail();
    }

    private static void logCompletedFrontier(FrontierLevels frontiers)
    {
        long numberOfEdgesInFrontier = frontiers.next - frontiers.prev;
        System.out.println("BFS front: " + frontiers.current + " edges: " + numberOfEdgesInFrontier);
    }

    private static void testSizeWrapped()
    {
        System.out.println("EdgeQueueTest.testSizeWrapped");
        final EdgeQueue queue = new EdgeQueue(3);
        for (int i = 1; i <= 3; i++)
        {
            final boolean success = queue.push(i, i * 100, i + 1, null);
            assert success;
        }
        assert 3 == queue.size();
        final Edge first = queue.pop();
        expect(1, 100, 2, null, first);
        assert 2 == queue.size();
        final boolean success = queue.push(4, 400, 500, null);
        assert !success;
        assert 2 == queue.size();
    }

    private static void testPushBeyond()
    {
        System.out.println("EdgeQueueTest.testPushBeyond");
        final EdgeQueue queue = new EdgeQueue(3);
        for (int i = 1; i <= 4; i++)
        {
            final boolean success = queue.push(i, i * 100, i + 1, null);
            if (i < 4)
            {
                assert success;
            }
            else
            {
                assert !success;
            }
        }
        assert 3 == queue.size();
    }

    private static void testPushPopAlternate()
    {
        System.out.println("EdgeQueueTest.testPushPopAlternate");
        final Map<Integer, Integer> graph = Map.of(1, 10, 2, 20, 3, 30, 10, 40, 20, 50);

        final EdgeQueue queue = new EdgeQueue(10);
        for (int i = 1; i <= 3; i++)
        {
            final boolean success = queue.push(i, -1, graph.get(i), null);
            assert success;
        }
        assert 3 == queue.size();

        List<Edge> result = new ArrayList<>();
        Edge current;
        while ((current = queue.pop()) != null)
        {
            result.add(current);
            final Integer next = graph.get((Integer) current.to);
            if (next != null)
            {
                final boolean success = queue.push(current.to, -1, next, current);
                assert success;
            }
        }
        assert 0 == queue.size();

        assert 5 == result.size();
        expect(1, -1, 10, null, result.get(0));
        expect(2, -1, 20, null, result.get(1));
        expect(3, -1, 30, null, result.get(2));
        expect(10, -1, 40, result.get(0), result.get(3));
        expect(20, -1, 50, result.get(1), result.get(4));
    }

    private static void testPushPop()
    {
        System.out.println("EdgeQueueTest.testPushPop");
        final EdgeQueue queue = new EdgeQueue(3);
        for (int i = 1; i <= 3; i++)
        {
            final boolean success = queue.push(i, i * 100, i + 1, null);
            assert success;
        }
        assert 3 == queue.size();
        assert queue.isFull();
        // queue.show(new Log());

        List<Edge> result = new ArrayList<>();
        Edge current;
        while ((current = queue.pop()) != null)
        {
            result.add(current);
        }
        assert 0 == queue.size();
        assert !queue.isFull();

        assert 3 == result.size();
        expect(1, 100, 2, null, result.get(0));
        expect(2, 200, 3, null, result.get(1));
        expect(3, 300, 4, null, result.get(2));
    }

    private static void expect(Object from, int location, Object to, Edge parent, Edge result)
    {
        assert from.equals(result.from);
        assert location == result.location;
        assert to.equals(result.to);
        assert parent == result.parent;
    }
}
