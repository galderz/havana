package org.example.vavr;

import io.vavr.CheckedFunction2;

import java.lang.reflect.Method;

public class MethodInvoke
{
    public static void main(String[] args)
    {
        final Class<?> doubleClass = Double.class;

        final Method doubleToRawLongBits = CheckedFunction2.<String, Class<?>[], Method>of(doubleClass::getMethod)
            .unchecked()
            .apply("doubleToRawLongBits", new Class[]{double.class});

        System.out.println(
            Long.toHexString(
                invoke(doubleToRawLongBits, doubleClass, Double.MAX_VALUE)
            )
        );

        System.out.println(
            Long.toHexString(
                (Long)
                    CheckedFunction2.<Object, Object[], Object>of(doubleToRawLongBits::invoke)
                    .unchecked()
                    .apply(doubleClass, new Object[]{Double.MAX_VALUE})
            )
        );
    }

    private static <T> T invoke(Method m, Object obj, Object... args)
    {
        try
        {
            return (T) m.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
