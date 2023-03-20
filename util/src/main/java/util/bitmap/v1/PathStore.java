package util.bitmap.v1;

import java.util.Arrays;

// todo do we need getIndex and getOrdered*Index?
//      instead of providing an ordered iteration
//      provide any iteration because the parents will establish the order
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
        if (isSkip(position) && isWrapped(path))
        {
            return SKIP;
        }

        if (position < maxRefChainDepth)
        {
            return paths[path][getOrderedIndex(position, path)];
        }

        return null;
    }

    UnsignedWord getElementLocation(int position, int path)
    {
        if (isSkip(position) && isWrapped(path))
        {
            return new UnsignedWord("");
        }

        if (position < maxRefChainDepth)
        {
            return locations[path][getOrderedIndex(position, path)];
        }

        return null;
    }

    private int getOrderedIndex(int position, int path)
    {
        if (isWrapped(path) && isRootContext(position))
        {
            return getOrderedWrappedIndex(position, path);
        }

        return position;
    }

    Object getElementParent(int position, int path)
    {
        if (isWrapped(path))
        {
            if (isLeakContextLast(position))
            {
                return SKIP;
            }
            if (isSkip(position))
            {
                return paths[path][getOrderedWrappedIndex(0, path)];
            }
            if (isRoot(position))
            {
                return null;
            }
        }

        return getElement(position + 1, path);
    }

    private boolean isSkip(int position)
    {
        return position == maxRefChainDepth;
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

    private int getOrderedWrappedIndex(int offset, int path)
    {
        return leakContext + ((rootPositions[path] + offset + 1) % rootContext);
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
