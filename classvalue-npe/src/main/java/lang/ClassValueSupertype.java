package lang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;

public class ClassValueSupertype
{
    public static void main(String[] args) throws Throwable
    {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        final var fooBar = new FooBar();
        fooBar.afield = "value for a field";

//        MethodHandle getAfieldMH = lookup.findGetter(FooBar.class, "afield", String.class);
//        final var result = getAfieldMH.invoke(fooBar);
//        System.out.println(result);

        Method amethod = FooBar.class.getDeclaredMethod("amethod");
        final var result = amethod.invoke(fooBar);
        System.out.println(result);
    }

    static class FooBar extends Foo
    {
        String afield;

        private String amethod() {
            return afield;
        }
    }

    static class Foo {}

    static void log(Object... args)
    {
        final var threadName = Thread.currentThread().getName();
        final var msg = String.format("%s", args);
        System.out.printf("[%s] %s%n", threadName, msg);
    }
}
