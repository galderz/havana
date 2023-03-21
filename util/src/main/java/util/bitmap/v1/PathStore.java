package util.bitmap.v1;

import java.util.Arrays;

final class PathStore
{
    // [0..99]    leak context
    // [100..199] root context

    private static final int DEFAULT_LEAK_CONTEXT = 100;
    private static final int DEFAULT_ROOT_CONTEXT = 100;

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
        if (isWrapped(path) && isLeakContextLast(position))
        {
            return rootPositions[path] - maxRefChainDepth + 1;
        }
        return 0;
    }

    Object getElement(int position, int path)
    {
        if (position < maxRefChainDepth)
        {
            return paths[path][getIndex(position)];
        }

        return null;
    }

    UnsignedWord getElementLocation(int position, int path)
    {
        if (position < maxRefChainDepth)
        {
            return locations[path][getIndex(position)];
        }

        return null;
    }

    Object getElementParent(int position, int path)
    {
        if (isWrapped(path))
        {
            if (isLeakContextLast(position))
            {
                return paths[path][getIndex(rootPositions[path] + 1)];
            }
            if (isWrappedRoot(position, path))
            {
                return null;
            }
        } else if (isNonWrappedRoot(position))
        {
            return null;
        }

        return paths[path][getIndex(position + 1)];
    }

    private boolean isWrapped(int path)
    {
        return rootPositions[path] >= maxRefChainDepth;
    }

    private boolean isLeakContextLast(int position)
    {
        return position == leakContext - 1;
    }

    private boolean isWrappedRoot(int position, int path)
    {
        return position == rootContext + (rootPositions[path] % rootContext);
    }

    private boolean isNonWrappedRoot(int position)
    {
        return position == maxRefChainDepth - 1;
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
