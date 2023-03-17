package util.bitmap.v1;

import java.util.Arrays;

final class PathStore
{
    // [0..99]    leak context
    // [100..199] root context

    private static final int DEFAULT_LEAK_CONTEXT = 100;
    private static final int DEFAULT_ROOT_CONTEXT = 100;

    static final Object SKIP = new Object();

    private final Object[][] paths;
    private final UnsignedWord[][] locations;
    private final int[] rootIndexes;
    private final int rootContext;
    private final int leakContext;
    private final int maxRefChainDepth;

    public PathStore(int capacity)
    {
        this(capacity, DEFAULT_LEAK_CONTEXT, DEFAULT_ROOT_CONTEXT);
    }

    public PathStore(int capacity, int leakContext, int rootContext)
    {
        this.leakContext = leakContext;
        this.rootContext = rootContext;
        this.maxRefChainDepth = leakContext + rootContext;

        paths = new Object[capacity][];
        locations = new UnsignedWord[capacity][];
        for (int i = 0; i < paths.length; i++)
        {
            paths[i] = new Object[maxRefChainDepth];
            locations[i] = new UnsignedWord[maxRefChainDepth];
        }
        rootIndexes = new int[capacity];
        Arrays.fill(rootIndexes, -1);
    }

    Object getRoot(int pathIndex)
    {
        final int rootIndex = findRootIndex(pathIndex);
        return paths[pathIndex][rootIndex];
    }

    private int findRootIndex(int pathIndex)
    {
        final int rootIndex = rootIndexes[pathIndex];
        if (rootIndex < maxRefChainDepth)
        {
            return rootIndex;
        }
        return leakContext + (rootIndex % rootContext);
    }

    void addPathElement(int elementIndex, UnsignedWord location, Object from, int pathIndex)
    {
        int elementWriteIndex = elementIndex < maxRefChainDepth
            ? elementIndex
            : leakContext + (elementIndex % rootContext);
        
        set(pathIndex, location, from, elementWriteIndex);
        rootIndexes[pathIndex] = elementIndex;
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

    int getSkipLength(int elementIndex, int pathIndex)
    {
        if (isSkip(elementIndex) && isWrap(pathIndex))
        {
            return rootIndexes[pathIndex] - maxRefChainDepth + 1;
        }
        return 0;
    }

    Object getElement(int elementIndex, int pathIndex)
    {
        if (elementIndex < maxRefChainDepth)
        {
            return paths[pathIndex][getElementReadIndex(elementIndex, pathIndex)];
        }
        if (isSkip(elementIndex) && isWrap(pathIndex))
        {
            return SKIP;
        }
        return null;
    }

    UnsignedWord getElementLocation(int elementIndex, int pathIndex)
    {
        if (isSkip(elementIndex) && isWrap(pathIndex))
        {
            return new UnsignedWord("");
        }
        return locations[pathIndex][getElementReadIndex(elementIndex, pathIndex)];
    }

    Object getElementParent(int elementIndex, int pathIndex)
    {
        if (isWrap(pathIndex))
        {
            if (isLeakContextLast(elementIndex))
            {
                return SKIP;
            }
            if (isSkip(elementIndex))
            {
                return getNextOfRoot(pathIndex);
            }
            if (isRoot(elementIndex))
            {
                return null;
            }
        }

        return getElement(elementIndex + 1, pathIndex);
    }

    private boolean isSkip(int elementIndex)
    {
        return elementIndex == maxRefChainDepth;
    }

    private int getElementReadIndex(int elementIndex, int pathIndex)
    {
        if (isRootContext(elementIndex) && isWrap(pathIndex))
        {
            return leakContext + ((elementIndex + rootIndexes[pathIndex] + 1) % rootContext);
        }

        return elementIndex;
    }

    private boolean isRootContext(int elementIndex)
    {
        return elementIndex >= leakContext;
    }
    
    private boolean isWrap(int pathIndex)
    {
        return rootIndexes[pathIndex] >= maxRefChainDepth;
    }

    private boolean isLeakContextLast(int elementIndex)
    {
        return elementIndex == leakContext - 1;
    }

    private boolean isRoot(int elementIndex)
    {
        return elementIndex == maxRefChainDepth -1;
    }

    private Object getNextOfRoot(int pathIndex)
    {
        return paths[pathIndex][leakContext + ((rootIndexes[pathIndex] + 1) % rootContext)];
    }
}
