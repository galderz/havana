package util.edges.v1;

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

    public boolean push(Object from, int location, Object to)
    {
        if (tail - head < edges.length) {
            int pos = (int) (tail % edges.length);
            set(from, location, to, pos);
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

    private void set(Object from, int location, Object to, int index)
    {
        edges[index].from = from;
        edges[index].location = location;
        edges[index].to = to;
    }

    static class Edge
    {
        Object from;
        int location;
        Object to;
    }
}
