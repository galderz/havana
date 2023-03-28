package util.edges.v0;

final class EdgeQueue
{
    private final Edge[] edges;

    private int count;

    EdgeQueue(int capacity)
    {
        this.edges = new Edge[capacity];
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Edge();
        }
    }

    boolean isFull() {
        return count == edges.length;
    }

    void push(Object from, int location, Object to)
    {
        set(from, location, to, count);
        count++;
    }

    Edge pop()
    {
        if (count == 0)
        {
            return null;
        }

        Edge head = edges[count - 1];
        count--;
        return head;
    }

    int size() {
        return count;
    }

    Object getFrom(int index) {
        return edges[index].from;
    }

    int getLocation(int index) {
        return edges[index].location;
    }

    Object getTo(int index) {
        return edges[index].to;
    }

    int findTo(Object target)
    {
        for (int i = 0; i < edges.length; i++)
        {
            final Object to = getTo(i);
            if (target.equals(to)) {
                return i;
            }
        }
        return -1;
    }

    void clear() {
        count = 0;
    }

    private void set(Object from, int location, Object to, int index)
    {
        edges[index].from = from;
        edges[index].to = to;
        edges[index].location = location;
    }

    static class Edge
    {
        Object from;
        Object to;
        int location;
    }
}
