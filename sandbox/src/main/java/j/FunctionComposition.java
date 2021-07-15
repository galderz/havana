package j;

import java.lang.reflect.Field;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionComposition
{
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException
    {
        imperative();
        Functional0.functional0();
        Functional1.functional1();
    }

    static class Functional1
    {
        // Problems
        // * Verbose when it comes to the final step.
        //   Would prefer a single parameter.
        static void functional1()
        {
            field()
                .andThen(RuntimeRegistration::register)
                .apply(forName("java.lang.String"), "value");
        }

        static BiFunction<Class<?>, String, Field> field()
        {
            return (clazz, fieldName) ->
            {
                try
                {
                    return clazz.getDeclaredField(fieldName);
                }
                catch (NoSuchFieldException e)
                {
                    throw new RuntimeException(e);
                }
            };
        }

        static Class<?> forName(String className)
        {
            try
            {
                return Class.forName(className);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    static class Functional0
    {
        // Two problems:
        // * Capturing lambda.
        // * Cannot cache forName() + register.
        private static void functional0()
        {
            forName()
                .andThen(field("value"))
                .andThen(RuntimeRegistration::register)
                .apply("java.lang.String");
        }

        static Function<Class<?>, Field> field(String fieldName)
        {
            return clazz ->
            {
                try
                {
                    return clazz.getDeclaredField(fieldName);
                }
                catch (NoSuchFieldException e)
                {
                    throw new RuntimeException(e);
                }
            };
        }

        static Function<String, Class<?>> forName()
        {
            return className ->
            {
                try
                {
                    return Class.forName(className);
                }
                catch (ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    private static void imperative() throws ClassNotFoundException, NoSuchFieldException
    {
        String className = "java.lang.String";
        final var clazz = Class.forName(className);
        final var field = clazz.getDeclaredField("value");
        RuntimeRegistration.register(field);
    }

    static class RuntimeRegistration
    {
        static Void register(Field field)
        {
            System.out.println(field);
            return null;
        }
    }
}
