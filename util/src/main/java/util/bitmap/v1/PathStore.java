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
        Arrays.fill(rootIndexes, Integer.MAX_VALUE);
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
        // rootIndexes[pathIndex] = elementWriteIndex;
        rootIndexes[pathIndex] = elementIndex;

//        if (elementIndex <= maxRefChainDepth - 1)
//        {
//            set(pathIndex, location, from, elementIndex);
//            if (elementIndex >= leakContext)
//            {
//                rootContextIndex = elementIndex;
//            }
//        }
//        else
//        {
//            int writeElementIndex = leakContext + (elementIndex % rootContext);
//            set(pathIndex, location, from, writeElementIndex);
//            rootContextIndex = writeElementIndex + 1;
//        }
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
            return paths[pathIndex][getElementReadIndex(elementIndex, pathIndex)];
        }
        return null;
    }

    public UnsignedWord getElementLocation(int elementIndex, int pathIndex)
    {
        return locations[pathIndex][getElementReadIndex(elementIndex, pathIndex)];
    }

    private int getElementReadIndex(int elementIndex, int pathIndex)
    {
//        if (elementIndex >= leakContext)
//        {
//            final int rootIndex = rootIndexes[pathIndex];
//            return (elementIndex + (rootIndex + 1 % rootContext)) % maxRefChainDepth;
//        }
//        return elementIndex;

//        if (elementIndex >= leakContext)
//        {
//            return leakContext + ((elementIndex + rootContextIndex) % rootContext);
//        }

//        final int rootIndex = rootIndexes[pathIndex];
//        if (elementIndex <= rootIndex)
//        {
//            return elementIndex;
//        }

//        if (elementIndex >= leakContext)
//        {
//            final int rootIndex = rootIndexes[pathIndex];
//            if (elementIndex <= rootIndex)
//            {
//                return elementIndex;
//            }
//            throw new RuntimeException();
//        }

//        final int rootIndex = rootIndexes[pathIndex];
//        if (elementIndex > rootIndex)
//        {
//
//        }

        if (elementIndex >= leakContext)
        {
            final int rootIndex = rootIndexes[pathIndex];
            if (rootIndex < maxRefChainDepth)
            {
                return elementIndex;
            }

            return leakContext + ((elementIndex + rootIndex + 1) % rootContext);

//            if (elementIndex <= rootIndex)
//            {
//                return elementIndex;
//            }
//            return leakContext + ((elementIndex + rootIndex) % rootContext);

//            final int relativeRootIndex = (rootIndex + 1) % rootContext;
//            final int relativeElementIndex = elementIndex % rootContext;
//            final int elementReadIndex = relativeRootIndex + relativeElementIndex;
//            return elementReadIndex;
        }

        return elementIndex;
    }
}
