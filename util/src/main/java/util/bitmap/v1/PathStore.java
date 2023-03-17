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
    private final int[] rootPositions;
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
        rootPositions = new int[capacity];
        Arrays.fill(rootPositions, -1);
    }

    Object getRoot(int path)
    {
        return paths[path][getIndex(rootPositions[path])];
    }

    void addPathElement(int position, UnsignedWord location, Object from, int path)
    {
        set(path, location, from, getIndex(position));
        rootPositions[path] = position;
    }

    private void set(int path, UnsignedWord location, Object from, int index)
    {
        paths[path][index] = from;
        locations[path][index] = location;
    }

    int findPath(Object firstElement)
    {
        for (int i = 0; i < paths.length; i++)
        {
            if (paths[i][0] == firstElement)
                return i;
        }

        return -1;
    }

    int getSkipLength(int position, int path)
    {
        if (isSkip(position) && isWrapped(path))
        {
            return rootPositions[path] - maxRefChainDepth + 1;
        }
        return 0;
    }

    Object getElement(int position, int path)
    {
        if (position < maxRefChainDepth)
        {
            return paths[path][getElementReadIndex(position, path)];
        }
        if (isSkip(position) && isWrapped(path))
        {
            return SKIP;
        }
        return null;
    }

    UnsignedWord getElementLocation(int elementIndex, int pathIndex)
    {
        if (isSkip(elementIndex) && isWrapped(pathIndex))
        {
            return new UnsignedWord("");
        }
        return locations[pathIndex][getElementReadIndex(elementIndex, pathIndex)];
    }

    Object getElementParent(int position, int pathIndex)
    {
        if (isWrapped(pathIndex))
        {
            if (isLeakContextLast(position))
            {
                return SKIP;
            }
            if (isSkip(position))
            {
                return getSkipParent(pathIndex);
            }
            if (isRoot(position))
            {
                return null;
            }
        }

        return getElement(position + 1, pathIndex);
    }

    private boolean isSkip(int position)
    {
        return position == maxRefChainDepth;
    }

    // todo can this be simplified further?
    private int getElementReadIndex(int position, int pathIndex)
    {
        if (isRootContext(position) && isWrapped(pathIndex))
        {
            return leakContext + ((position + rootPositions[pathIndex] + 1) % rootContext);
        }

        return position;
    }

    private boolean isRootContext(int position)
    {
        return position >= leakContext;
    }
    
    private boolean isWrapped(int path)
    {
        return rootPositions[path] >= maxRefChainDepth;
    }

    private boolean isLeakContextLast(int position)
    {
        return position == leakContext - 1;
    }

    private boolean isRoot(int position)
    {
        return position == maxRefChainDepth -1;
    }

    private Object getSkipParent(int path)
    {
        return paths[path][leakContext + ((rootPositions[path] + 1) % rootContext)];
    }

    private int getIndex(int position)
    {
        if (position < maxRefChainDepth)
        {
            return position;
        }
        return leakContext + (position % rootContext);
    }
}
