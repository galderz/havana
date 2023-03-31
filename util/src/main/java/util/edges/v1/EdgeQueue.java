package util.edges.v1;

/**
 * A queue for edge nodes that enables computing the path to the root from any node.
 * To avoid losing information in the path to the root,
 * the queue does not write over any previously queued edge nodes.
 * So this queue does not behave like a ring or circular queue.
 */
public final class EdgeQueue
{
    private final Edge[] edges;
    private long tail = 0;
    private long head = 0;

    public EdgeQueue(int capacity)
    {
        this.edges = new Edge[capacity];
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Edge();
        }
    }

    public boolean push(Object from, int location, Object to, Edge parent)
    {
        if (tail - head < edges.length) {
            int pos = (int) (tail % edges.length);
            if (pos < head) {
                 return false; // wrapping around not supported
            }
            set(from, location, to, parent, pos);
            tail++;
            return true;
        }

        return false;
    }

    public Edge pop()
    {
        if (head < tail) {
            int pos = (int) (head % edges.length);
            Edge e = edges[pos];
            // set(null, 0, null, pos);
            head++;
            return e;
        }

        return null;
    }

    public int size()
    {
        return (int) (tail - head);
    }

    public long tail()
    {
        return tail;
    }

    public long head()
    {
        return head;
    }

    public boolean isEmpty()
    {
        return tail == head;
    }

    public boolean isFull()
    {
        return size() == edges.length;
    }

    public void show(Log log)
    {
        long showTail = tail;
        long showHead = head;

        Edge current;
        while ((current = peek(showTail, showHead)) != null)
        {
            showHead++;
            show(current.from, log);
            log.string("->");
            show(current.to, log);
            log.newline();
        }
    }

    private static void show(Object obj, Log log) {
        if (obj == null) {
            log.string("null");
        } else {
            log.string(obj.getClass().getName()).string("@").zhex(System.identityHashCode(obj));
        }
    }

    private Edge peek(long peekTail, long peekHead)
    {
        if (peekHead < peekTail) {
            int pos = (int) (peekHead % edges.length);
            Edge e = edges[pos];
            return e;
        }

        return null;
    }

    private void set(Object from, int location, Object to, Edge parent, int index)
    {
        edges[index].from = from;
        edges[index].location = location;
        edges[index].to = to;
        edges[index].parent = parent;
    }

    static class Edge
    {
        Object from;
        int location;
        Object to;
        Edge parent;
    }
}
