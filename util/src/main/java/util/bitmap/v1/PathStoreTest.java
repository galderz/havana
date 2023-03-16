package util.bitmap.v1;

import util.Asserts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PathStoreTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testSingleLinkPath();
        testMultiLinkPath();
        testRootContextDepthMinusOne();
        testRootContextDepth();
        testRootContextDepthPlusOne();
        testRootContextDepthPlusTwo();
        testLeakContextDepthMinusOne();
        testLeakContextDepth();
        testLeakContextDepthPlusOne();
        testLongPath();
        testMultiLongPaths();
    }

    private static void testMultiLongPaths()
    {
        System.out.println("PathStoreTest.testMultiLongPaths");
        final PathStore store = new PathStore(2, 5, 5);
        addPaths(store, 14, "B", 0, "A");
        addPaths(store, 13, "Z", 1, "Y");

        List<Path> pathB = collectPaths(store, List.of("B"), 0).get("B");
        assert 11 == pathB.size() : pathB.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A9", "field9", 0)
            , new Path("A10", "field10", 0)
            , new Path("A11", "field11", 0)
            , new Path("A12", "field12", 0)
            , new Path("A13", "field13", 0)
            , new Path(PathStore.SKIP, "", 4)
        ).equals(pathB) : pathB;
        assert "A13".equals(store.getRoot(0));

        List<Path> pathZ = collectPaths(store, List.of("Z"), 1).get("Z");
        assert 11 == pathZ.size() : pathZ.size();
        assert List.of(
            new Path("Z", "", 0)
            , new Path("Y1", "field1", 0)
            , new Path("Y2", "field2", 0)
            , new Path("Y3", "field3", 0)
            , new Path("Y4", "field4", 0)
            , new Path("Y8", "field8", 0)
            , new Path("Y9", "field9", 0)
            , new Path("Y10", "field10", 0)
            , new Path("Y11", "field11", 0)
            , new Path("Y12", "field12", 0)
            , new Path(PathStore.SKIP, "", 3)
        ).equals(pathZ) : pathZ;
        assert "Y12".equals(store.getRoot(1));
    }

    private static void testRootContextDepthPlusTwo()
    {
        System.out.println("PathStoreTest.testRootContextDepthPlusTwo");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 12, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 11 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A7", "field7", 0)
            , new Path("A8", "field8", 0)
            , new Path("A9", "field9", 0)
            , new Path("A10", "field10", 0)
            , new Path("A11", "field11", 0)
            , new Path(PathStore.SKIP, "", 2)
        ).equals(path) : path;
        assert "A11".equals(store.getRoot(0));
    }

    private static void testRootContextDepthPlusOne()
    {
        System.out.println("PathStoreTest.testRootContextDepthPlusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 11, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 11 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A6", "field6", 0)
            , new Path("A7", "field7", 0)
            , new Path("A8", "field8", 0)
            , new Path("A9", "field9", 0)
            , new Path("A10", "field10", 0)
            , new Path(PathStore.SKIP, "", 1)
        ).equals(path) : path;
        assert "A10".equals(store.getRoot(0));
    }

    private static void testRootContextDepth()
    {
        System.out.println("PathStoreTest.testRootContextDepth");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 10, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 10 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A5", "field5", 0)
            , new Path("A6", "field6", 0)
            , new Path("A7", "field7", 0)
            , new Path("A8", "field8", 0)
            , new Path("A9", "field9", 0)
        ).equals(path) : path;
        assert "A9".equals(store.getRoot(0));
    }

    private static void testRootContextDepthMinusOne()
    {
        System.out.println("PathStoreTest.testRootContextDepthMinusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 9, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 9 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A5", "field5", 0)
            , new Path("A6", "field6", 0)
            , new Path("A7", "field7", 0)
            , new Path("A8", "field8", 0)
        ).equals(path) : path;
        assert "A8".equals(store.getRoot(0));
    }

    private static void testLeakContextDepthPlusOne()
    {
        System.out.println("PathStoreTest.testLeakContextDepthPlusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 6, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 6 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A5", "field5", 0)
        ).equals(path) : path;
        assert "A5".equals(store.getRoot(0));
    }

    private static void testLeakContextDepth()
    {
        System.out.println("PathStoreTest.testLeakContextDepth");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 5, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 5 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
        ).equals(path) : path;
        assert "A4".equals(store.getRoot(0));
    }

    private static void testLeakContextDepthMinusOne()
    {
        System.out.println("PathStoreTest.testLeakContextDepthMinusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 4, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 4 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
        ).equals(path) : path;
        assert "A3".equals(store.getRoot(0));
    }

    private static void testLongPath()
    {
        System.out.println("PathStoreTest.testLongPath");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 30, leak, 0, "A");

        List<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 11 == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
            , new Path("A4", "field4", 0)
            , new Path("A25", "field25", 0)
            , new Path("A26", "field26", 0)
            , new Path("A27", "field27", 0)
            , new Path("A28", "field28", 0)
            , new Path("A29", "field29", 0)
            , new Path(PathStore.SKIP, "", 20)
        ).equals(path) : path;
        assert "A29".equals(store.getRoot(0));
    }

    private static void testMultiLinkPath()
    {
        System.out.println("PathStoreTest.testMultiLinkPath");
        final PathStore store = new PathStore(1);
        final int depth = 4;
        final String leak = "B";
        addPaths(store, depth, leak, 0, "A");

        Map<String, List<Path>> paths = collectPaths(store, List.of(leak), 0);
        final List<Path> path = paths.get(leak);
        assert depth == path.size() : path.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A1", "field1", 0)
            , new Path("A2", "field2", 0)
            , new Path("A3", "field3", 0)
        ).equals(path) : path;
        assert "A3".equals(store.getRoot(0));
    }

    private static void testSingleLinkPath()
    {
        System.out.println("PathStoreTest.testSingleLinkPath");
        final PathStore store = new PathStore(1);
        final String leak = "B";
        store.addPathElement(0, new UnsignedWord(""), leak, 0);
        store.addPathElement(1, new UnsignedWord("fieldB"), "A", 0);

        Map<String, List<Path>> paths = collectPaths(store, List.of(leak), 0);
        assert 1 == paths.size();
        assert List.of(
            new Path("B", "", 0)
            , new Path("A", "fieldB", 0)
        ).equals(paths.get(leak));
        assert "A".equals(store.getRoot(0));
    }

    private static void addPaths(PathStore store, int depth, String leak, int pathIndex, String prefix)
    {
        store.addPathElement(0, new UnsignedWord(""), leak, pathIndex);
        for (int i = 1; i < depth; i++)
        {
            store.addPathElement(i, new UnsignedWord("field" + i), prefix + i, pathIndex);
        }
    }

    private static Map<String, List<Path>> collectPaths(PathStore store, List<String> leaks, int expectedPathIndex)
    {
        final Map<String, List<Path>> result = new HashMap<>();
        for (String leak : leaks)
        {
            List<Path> paths = new ArrayList<>();

            final int pathIndex = store.getPathIndex(leak);
            assert expectedPathIndex == pathIndex;

            Object current;
            int elementIndex = 0;
            while (null != (current = store.getElement(elementIndex, pathIndex)))
            {
                final int skipLength = store.getSkipLength(elementIndex, pathIndex);
                final UnsignedWord location = store.getElementLocation(elementIndex, pathIndex);
                final Path path = new Path(current, location.fieldName, skipLength);
                paths.add(path);
                elementIndex++;
            }

            result.put(leak, paths);
        }
        return result;
    }

    static final class Path
    {
        final Object object;
        final String fieldName;
        final int skipLength;

        Path(Object object, String fieldName, int skipLength) {
            this.object = object;
            this.fieldName = fieldName;
            this.skipLength = skipLength;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path = (Path) o;
            return object.equals(path.object) && fieldName.equals(path.fieldName);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(object, fieldName);
        }

        @Override
        public String toString()
        {
            return fieldName.isEmpty()
                ? object.toString()
                : "%s.%s".formatted(object, fieldName);
        }
    }
}
