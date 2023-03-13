package util.bitmap.v1;

final class PathStore
{
    // [0..99]    leak context
    // [100]      skip index
    // [101..200] root context

    private static final int LEAK_CONTEXT = 100;
    private static final int ROOT_CONTEXT = 100;
    private static final int MAX_REF_CHAIN_DEPTH = LEAK_CONTEXT + ROOT_CONTEXT;
    private static final int SKIP_INDEX = LEAK_CONTEXT;

    private final Object[][] paths;
    private final UnsignedWord[][] locations;

    public PathStore(int capacity)
    {
        paths = new Object[capacity][];
        locations = new UnsignedWord[capacity][];
        for (int i = 0; i < paths.length; i++)
        {
            paths[i] = new Object[MAX_REF_CHAIN_DEPTH + 1];
        }
        for (int i = 0; i < locations.length; i++)
        {
            locations[i] = new UnsignedWord[MAX_REF_CHAIN_DEPTH + 1];
        }
    }

    void addPathLeaf(int pathIndex, Object leaf)
    {
        paths[pathIndex][0] = leaf;
        locations[pathIndex][0] = new UnsignedWord("");
    }

    void addPathElement(int pathIndex, UnsignedWord location, Object from, int elementIndex)
    {
        paths[pathIndex][elementIndex] = from;
        locations[pathIndex][elementIndex] = location;
    }

    int getPathIndex(Object leaf)
    {
        for (int i = 0; i < paths.length; i++)
        {
            if (paths[i][0] == leaf)
                return i;
        }

        return -1;
    }

    public Object getElement(int elementIndex, int pathIndex)
    {
        return paths[pathIndex][elementIndex];
    }

    public UnsignedWord getElementLocation(int elementIndex, int pathIndex)
    {
        return locations[pathIndex][elementIndex];
    }
}
