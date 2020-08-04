package lang.invoke;

import lang.Assert;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodHandling
{
    public static void main(String[] args) throws Throwable
    {
        Assert.check();

        MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        {
            // In case we want to be more restrictive
            // in the way we execute a method handle (number of arguments and their type),
            // we have to use the invokeExact() method.
            MethodType mt = MethodType.methodType(int.class, int.class, int.class);
            MethodHandle sumMH = lookup.findStatic(Integer.class, "sum", mt);
            int sum = (int) sumMH.invokeExact(1, 11);
            assert 12 == sum : sum;

            try
            {
                sumMH.invokeExact(Long.MAX_VALUE, Long.MIN_VALUE);
                assert false : "expected exception";
            }
            catch (WrongMethodTypeException ignored)
            {
            }
        }

        {
            // Invoking a method handle using the invokeWithArguments method,
            // is the least restrictive of the three options.
            // In fact, it allows a variable arity invocation,
            // in addition to the casting and boxing/unboxing of the arguments and of the return types.
            MethodType mt = MethodType.methodType(List.class, Object[].class);
            MethodHandle asList = publicLookup.findStatic(Arrays.class, "asList", mt);
            Object list = asList.invokeWithArguments(1, 2);
            assert Arrays.asList(1,2).equals(list) : list;
        }

        {
            // When using the invoke() method,
            // we enforce the number of the arguments (arity) to be fixed,
            // but we allow the performing of casting and boxing/unboxing of the arguments and return types.
            MethodType mt = MethodType.methodType(String.class, char.class, char.class);
            MethodHandle replaceMH = publicLookup.findVirtual(String.class, "replace", mt);
            String output = (String) replaceMH.invoke("jovo", 'o', 'a');
            assert "java".equals(output) : output;
        }

        {
            // Method handle for private methods
            Method formatBookMethod = Book.class.getDeclaredMethod("formatBook");
            formatBookMethod.setAccessible(true);
            MethodHandle formatBookMH = lookup.unreflect(formatBookMethod);
        }

        {
            // Method handle for fields
            MethodHandle getTitleMH = lookup.findGetter(Book.class, "title", String.class);
        }

        {
            // Method handle for constructors
            MethodType mt = MethodType.methodType(void.class, String.class);
            MethodHandle newIntegerMH = publicLookup.findConstructor(Integer.class, mt);
        }

        {
            // Method handle for static methods
            MethodType mt = MethodType.methodType(List.class, Object[].class);
            MethodHandle asListMH = publicLookup.findStatic(Arrays.class, "asList", mt);
        }

        {
            // Method handle for methods
            MethodType mt = MethodType.methodType(String.class, String.class);
            MethodHandle concatMH = publicLookup.findVirtual(String.class, "concat", mt);
        }

        {
            // Creating a method type
            MethodType mt = MethodType.methodType(List.class, Object[].class);
            MethodType mt2 = MethodType.methodType(int.class, Object.class);
        }
    }

    static class Book
    {
        String id;
        String title;

        private String formatBook()
        {
            return id + " > " + title;
        }
    }
}
