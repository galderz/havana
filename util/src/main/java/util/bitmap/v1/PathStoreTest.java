package util.bitmap.v1;

import util.Asserts;

import java.util.ArrayList;
import java.util.List;

public class PathStoreTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSingleLinkPath();
    }

    private static void testSingleLinkPath()
    {
        System.out.println("PathStoreTest.testSingleLinkPath");
        final PathStore store = new PathStore(1);
        final String gcRoot = "A";
        final String leak = "B";
        store.addPathLeaf(0, leak);
        final UnsignedWord location = new UnsignedWord("fieldB");
        store.addPathElement(0, location, gcRoot, 1);

        List<UnsignedWord> locations = new ArrayList<>();
        List<String> path = new ArrayList<>();

        final int pathIndex = store.getPathIndex(leak);
        assert 0 == pathIndex;

        Object next;
        int elementIndex = 0;
        while (null != (next = store.getElement(elementIndex, pathIndex)))
        {
            path.add((String) next);
            locations.add(store.getElementLocation(elementIndex, pathIndex));
            elementIndex++;
        }

        assert List.of("B", "A").equals(path);
        assert List.of(new UnsignedWord(""), new UnsignedWord("fieldB")).equals(locations);
    }
}
