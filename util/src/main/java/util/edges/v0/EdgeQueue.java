package util.edges.v0;

final class EdgeQueue
{
    private static final int FROM_SLOT = 0;
    private static final int TO_SLOT = 1;

    private final Object[][] edges;
    private final Object[] locations;

    private int count;

    EdgeQueue(int capacity)
    {
        this.edges = new Object[capacity][];
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Object[TO_SLOT + 1];
        }
        this.locations = new Object[capacity];
    }

    boolean isFull() {
        return count == edges.length;
    }

    void push(Object from, int location, Object to)
    {
        set(from, location, to, count);
        count++;
    }

    int size() {
        return count;
    }

    Object getFrom(int index) {
        return edges[index][FROM_SLOT];
    }

    int getLocation(int index) {
        return (int) locations[index];
    }

    Object getTo(int index) {
        return edges[index][TO_SLOT];
    }

    private void set(Object from, int location, Object to, int index)
    {
        edges[index][FROM_SLOT] = from;
        edges[index][TO_SLOT] = to;
        locations[index] = location;
    }
}
