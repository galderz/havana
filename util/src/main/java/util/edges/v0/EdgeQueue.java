package util.edges.v0;

final class EdgeQueue
{
    private static final int FROM_SLOT = 0;
    private static final int LOCATION_SLOT = 1;
    private static final int TO_SLOT = 2;

    private final Object[][] edges;
    private int count;

    EdgeQueue(int capacity)
    {
        this.edges = new Object[capacity][];
        for (int i = 0; i < this.edges.length; i++)
        {
            this.edges[i] = new Object[TO_SLOT + 1];
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

    int size() {
        return count;
    }

    Object getFrom(int index) {
        return edges[index][FROM_SLOT];
    }

    int getLocation(int index) {
        return (int) edges[index][LOCATION_SLOT];
    }

    Object getTo(int index) {
        return edges[index][TO_SLOT];
    }

    private void set(Object from, int location, Object to, int index)
    {
        edges[index][FROM_SLOT] = from;
        edges[index][LOCATION_SLOT] = location;
        edges[index][TO_SLOT] = to;
    }
}
