package reflection;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Enums
{
    public static void main(String[] args)
    {
        final Class<TimeUnit> clazz = TimeUnit.class;
        System.out.println(Arrays.toString(clazz.getEnumConstants()));
        System.out.println(getEnumConstant("DAYS", clazz));
        System.out.println(getEnumConstant("aaa", clazz));
    }

    static <T> T getEnumConstant(String name, Class<T> clazz)
    {
        Optional<T> found = Stream.of(clazz.getEnumConstants())
            .filter(e -> name.equals(e.toString()))
            .findFirst();

        if (found.isPresent())
            return found.get();

        throw new RuntimeException(String.format(
            "Enum value '%s' not found in %s"
            , name
            , clazz
        ));
    }
}
