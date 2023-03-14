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
        testLongPath();
    }

    private static void testLongPath()
    {
        System.out.println("PathStoreTest.testLongPath");
        final int leakContext = 5;
        final int rootContext = 5;
        final PathStore store = new PathStore(1, leakContext, rootContext);
        final int depth = 20;
        final String leak = "B";
        store.addPathElement(0, new UnsignedWord(""), leak, 0);
        for (int i = 1; i < depth; i++)
        {
            store.addPathElement(i, new UnsignedWord("field" + i), "A" + i, 0);
        }

        List<Path> path = collectPaths(store, List.of(leak)).get(leak);
        final int pathSize = path.size();
        assert leakContext + rootContext == pathSize : pathSize;
        assert List.of(
            new Path("B", "")
            , new Path("A1", "field1")
            , new Path("A2", "field2")
            , new Path("A3", "field3")
            , new Path("A4", "field4")
            , new Path("A15", "field15")
            , new Path("A16", "field16")
            , new Path("A17", "field17")
            , new Path("A18", "field18")
            , new Path("A19", "field19")
        ).equals(path) : path;
    }

    private static void testMultiLinkPath()
    {
        System.out.println("PathStoreTest.testMultiLinkPath");
        final PathStore store = new PathStore(1);
        final int depth = 4;
        final String leak = "B";
        store.addPathElement(0, new UnsignedWord(""), leak, 0);
        for (int i = 1; i < depth; i++)
        {
            store.addPathElement(i, new UnsignedWord("field" + i), "A" + i, 0);
        }

        Map<String, List<Path>> paths = collectPaths(store, List.of(leak));
        final List<Path> path = paths.get(leak);
        assert depth == path.size() : path.size();
        assert List.of(
            new Path("B", "")
            , new Path("A1", "field1")
            , new Path("A2", "field2")
            , new Path("A3", "field3")
        ).equals(path) : path;
    }

    private static void testSingleLinkPath()
    {
        System.out.println("PathStoreTest.testSingleLinkPath");
        final PathStore store = new PathStore(1);
        final String leak = "B";
        store.addPathElement(0, new UnsignedWord(""), leak, 0);
        store.addPathElement(1, new UnsignedWord("fieldB"), "A", 0);

        Map<String, List<Path>> paths = collectPaths(store, List.of(leak));
        assert 1 == paths.size();
        assert List.of(
            new Path("B", "")
            , new Path("A", "fieldB")
        ).equals(paths.get(leak));
    }

    private static Map<String, List<Path>> collectPaths(PathStore store, List<String> leaks)
    {
        final Map<String, List<Path>> result = new HashMap<>();
        for (String leak : leaks)
        {
            List<Path> paths = new ArrayList<>();

            final int pathIndex = store.getPathIndex(leak);
            assert 0 == pathIndex;

            Object next;
            int elementIndex = 0;
            while (null != (next = store.getElement(elementIndex, pathIndex)))
            {
                final UnsignedWord location = store.getElementLocation(elementIndex, pathIndex);
                final Path path = new Path(next, location.fieldName);
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

        Path(Object object, String fieldName) {
            this.object = object;
            this.fieldName = fieldName;
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
