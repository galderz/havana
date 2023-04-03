package util.bitmap.v1;

import util.Asserts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        addPaths(store, 14, "B", "A");
        addPaths(store, 13, "Z", "Y");

        Set<Path> pathB = collectPaths(store, List.of("B"), 0).get("B");
        assert 10 == pathB.size() : pathB.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 4, "A9")
            , new Path("A9", "field9", 0, "A10")
            , new Path("A10", "field10", 0, "A11")
            , new Path("A11", "field11", 0, "A12")
            , new Path("A12", "field12", 0, "A13")
            , new Path("A13", "field13", 0, null)
        ).equals(pathB) : pathB;
        assert "A13".equals(store.getRoot(0));

        Set<Path> pathZ = collectPaths(store, List.of("Z"), 1).get("Z");
        assert 10 == pathZ.size() : pathZ.size();
        assert Set.of(
            new Path("Z", "", 0, "Y1")
            , new Path("Y1", "field1", 0, "Y2")
            , new Path("Y2", "field2", 0, "Y3")
            , new Path("Y3", "field3", 0, "Y4")
            , new Path("Y4", "field4", 3, "Y8")
            , new Path("Y8", "field8", 0, "Y9")
            , new Path("Y9", "field9", 0, "Y10")
            , new Path("Y10", "field10", 0, "Y11")
            , new Path("Y11", "field11", 0, "Y12")
            , new Path("Y12", "field12", 0, null)
        ).equals(pathZ) : pathZ;
        assert "Y12".equals(store.getRoot(1));
    }

    private static void testRootContextDepthPlusTwo()
    {
        System.out.println("PathStoreTest.testRootContextDepthPlusTwo");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 12, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 10 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 2, "A7")
            , new Path("A7", "field7", 0, "A8")
            , new Path("A8", "field8", 0, "A9")
            , new Path("A9", "field9", 0, "A10")
            , new Path("A10", "field10", 0, "A11")
            , new Path("A11", "field11", 0, null)
        ).equals(path) : path;
        assert "A11".equals(store.getRoot(0));
    }

    private static void testRootContextDepthPlusOne()
    {
        System.out.println("PathStoreTest.testRootContextDepthPlusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 11, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 10 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 1, "A6")
            , new Path("A6", "field6", 0, "A7")
            , new Path("A7", "field7", 0, "A8")
            , new Path("A8", "field8", 0, "A9")
            , new Path("A9", "field9", 0, "A10")
            , new Path("A10", "field10", 0, null)
        ).equals(path) : path;
        assert "A10".equals(store.getRoot(0));
    }

    private static void testRootContextDepth()
    {
        System.out.println("PathStoreTest.testRootContextDepth");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 10, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 10 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 0, "A5")
            , new Path("A5", "field5", 0, "A6")
            , new Path("A6", "field6", 0, "A7")
            , new Path("A7", "field7", 0, "A8")
            , new Path("A8", "field8", 0, "A9")
            , new Path("A9", "field9", 0, null)
        ).equals(path) : path;
        assert "A9".equals(store.getRoot(0));
    }

    private static void testRootContextDepthMinusOne()
    {
        System.out.println("PathStoreTest.testRootContextDepthMinusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 9, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 9 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 0, "A5")
            , new Path("A5", "field5", 0, "A6")
            , new Path("A6", "field6", 0, "A7")
            , new Path("A7", "field7", 0, "A8")
            , new Path("A8", "field8", 0, null)
        ).equals(path) : path;
        assert "A8".equals(store.getRoot(0));
    }

    private static void testLeakContextDepthPlusOne()
    {
        System.out.println("PathStoreTest.testLeakContextDepthPlusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 6, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 6 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 0, "A5")
            , new Path("A5", "field5", 0, null)
        ).equals(path) : path;
        assert "A5".equals(store.getRoot(0));
    }

    private static void testLeakContextDepth()
    {
        System.out.println("PathStoreTest.testLeakContextDepth");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 5, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 5 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 0, null)
        ).equals(path) : path;
        assert "A4".equals(store.getRoot(0));
    }

    private static void testLeakContextDepthMinusOne()
    {
        System.out.println("PathStoreTest.testLeakContextDepthMinusOne");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 4, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 4 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, null)
        ).equals(path) : path;
        assert "A3".equals(store.getRoot(0));
    }

    private static void testLongPath()
    {
        System.out.println("PathStoreTest.testLongPath");
        final PathStore store = new PathStore(1, 5, 5);
        final String leak = "B";
        addPaths(store, 30, leak, "A");

        Set<Path> path = collectPaths(store, List.of(leak), 0).get(leak);
        assert 10 == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, "A4")
            , new Path("A4", "field4", 20, "A25")
            , new Path("A25", "field25", 0, "A26")
            , new Path("A26", "field26", 0, "A27")
            , new Path("A27", "field27", 0, "A28")
            , new Path("A28", "field28", 0, "A29")
            , new Path("A29", "field29", 0, null)
        ).equals(path) : path;
        assert "A29".equals(store.getRoot(0));
    }

    private static void testMultiLinkPath()
    {
        System.out.println("PathStoreTest.testMultiLinkPath");
        final PathStore store = new PathStore(1);
        final int depth = 4;
        final String leak = "B";
        addPaths(store, depth, leak, "A");

        Map<String, Set<Path>> paths = collectPaths(store, List.of(leak), 0);
        final Set<Path> path = paths.get(leak);
        assert depth == path.size() : path.size();
        assert Set.of(
            new Path("B", "", 0, "A1")
            , new Path("A1", "field1", 0, "A2")
            , new Path("A2", "field2", 0, "A3")
            , new Path("A3", "field3", 0, null)
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

        Map<String, Set<Path>> paths = collectPaths(store, List.of(leak), 0);
        assert 1 == paths.size();
        assert Set.of(
            new Path("B", "", 0, "A")
            , new Path("A", "fieldB", 0, null)
        ).equals(paths.get(leak));
        assert "A".equals(store.getRoot(0));
    }

    private static void addPaths(PathStore store, int depth, String leak, String prefix)
    {
        final int path = store.addPathElement(0, new UnsignedWord(""), leak);
        for (int i = 1; i < depth; i++)
        {
            store.addPathElement(i, new UnsignedWord("field" + i), prefix + i, path);
        }
    }

    private static Map<String, Set<Path>> collectPaths(PathStore store, List<String> leaks, int expectedPathIndex)
    {
        final Map<String, Set<Path>> result = new HashMap<>();
        for (String leak : leaks)
        {
            Set<Path> paths = new HashSet<>();

            final int path = store.findPath(leak);
            assert expectedPathIndex == path;

            Object current;
            int position = 0;
            while (null != (current = store.getElement(position, path)))
            {
                final int skipLength = store.getSkipLength(position, path);
                final UnsignedWord location = store.getElementLocation(position, path);
                final Object parent = store.getElementParent(position, path);
                paths.add(new Path(current, location.fieldName, skipLength, parent));
                position++;
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
        final Object parent;

        Path(Object object, String fieldName, int skipLength, Object parent) {
            this.object = object;
            this.fieldName = fieldName;
            this.skipLength = skipLength;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Path path = (Path) o;
            return skipLength == path.skipLength && object.equals(path.object) && fieldName.equals(path.fieldName) && Objects.equals(parent, path.parent);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(object, fieldName, skipLength, parent);
        }

        @Override
        public String toString()
        {
            return fieldName.isEmpty()
                ? "%s(%d)->%s".formatted(object, skipLength, parent)
                : "%s.%s(%d)->%s".formatted(object, fieldName, skipLength, parent);
        }
    }
}

