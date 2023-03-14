package util.bitmap.v1;

final class PathStore
{
    // [0..99]    leak context
    // [100..199] root context

    private static final int DEFAULT_LEAK_CONTEXT = 100;
    private static final int DEFAULT_ROOT_CONTEXT = 100;

    private final Object[][] paths;
    private final UnsignedWord[][] locations;
    private final int rootContext;
    private final int leakContext;
    private final int maxRefChainDepth;
    private int rootContextIndex;

    public PathStore(int capacity)
    {
        this(capacity, DEFAULT_LEAK_CONTEXT, DEFAULT_ROOT_CONTEXT);
    }

    public PathStore(int capacity, int leakContext, int rootContext)
    {
        this.leakContext = leakContext;
        this.rootContext = rootContext;
        this.maxRefChainDepth = leakContext + rootContext;
        this.rootContextIndex = rootContext;

        paths = new Object[capacity][];
        locations = new UnsignedWord[capacity][];
        for (int i = 0; i < paths.length; i++)
        {
            paths[i] = new Object[maxRefChainDepth];
        }
        for (int i = 0; i < locations.length; i++)
        {
            locations[i] = new UnsignedWord[maxRefChainDepth];
        }
    }

    void addPathElement(int elementIndex, UnsignedWord location, Object from, int pathIndex)
    {
        if (elementIndex <= maxRefChainDepth - 1)
        {
            set(pathIndex, location, from, elementIndex);
            if (elementIndex >= leakContext)
            {
                rootContextIndex = elementIndex;
            }
        }
        else
        {
            int writeElementIndex = leakContext + (elementIndex % rootContext);
            set(pathIndex, location, from, writeElementIndex);
            rootContextIndex = writeElementIndex + 1;
        }
    }

    private void set(int pathIndex, UnsignedWord location, Object from, int elementIndex)
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
        if (elementIndex < maxRefChainDepth)
        {
            return paths[pathIndex][getReadElementIndex(elementIndex)];
        }
        return null;
    }

    public UnsignedWord getElementLocation(int elementIndex, int pathIndex)
    {
        return locations[pathIndex][getReadElementIndex(elementIndex)];
    }

    private int getReadElementIndex(int elementIndex)
    {
        if (elementIndex >= rootContextIndex)
        {
            return leakContext + (elementIndex % rootContext);
        }
        return elementIndex;
    }
}
