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
    }

    private static void testSingleLinkPath()
    {
        System.out.println("PathStoreTest.testSingleLinkPath");
        final PathStore store = new PathStore(1);
        final String gcRoot = "A";
        final String leak = "B";
        store.addPathLeaf(0, leak);
        store.addPathElement(0, new UnsignedWord("fieldB"), gcRoot, 1);

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
    }
}
