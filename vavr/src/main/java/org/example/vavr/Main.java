package org.example.vavr;

import io.vavr.Function1;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Main
{
    // Class method value
    // value

    interface Expression {}

    record Value(Object v) implements Expression {}

    record Call(Expression expr, String methodName, String className) implements Expression {}

    public static void main(String[] args)
    {
        System.out.println(
            Function1.of(Double::doubleToRawLongBits)
                .compose(Function1.of(Double::longBitsToDouble))
                .apply(Long.MAX_VALUE)
        );

        // Double.doubleToRawLongBits(%L) => Function1<Double, Long> f = v -> Double.doubleToRawLongBits(v);
        System.out.println(Function1.of(Double::doubleToRawLongBits).apply(Double.MAX_VALUE));
        System.out.println(from("doubleToRawLongBits", "java.lang.Double").apply(Double.MAX_VALUE));
    }

    static <T1, R> Function1<T1, R> from(String methodName, String className)
    {
        try
        {
            final var clazz = Class.forName(className);
            final var methodParams = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(methodName))
                .map(Method::getParameterTypes)
                .findFirst();

            if (methodParams.isEmpty())
                throw new RuntimeException("Not found");

            final var paramTypes = methodParams.get();

            return v -> invoke(classMethod(paramTypes, methodName, clazz), clazz, v);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
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

    private static Method classMethod(Class<?>[] paramTypes, String methodName, Class<?> clazz)
    {
        try
        {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
    }

//    static final class Statements
//    {
//        Statement of(String format)
//        {
//
//        }
//    }

//    sealed interface Statement
//    {
//        final record Statement1<T1, R>(
//            Function1<T1, R> fn
//        ) implements Statement {}
//    }

//    static final class CodeBlock
//    {
//        public static CodeBlock of(String format)
//        {
//            return new Builder().add(format).build();
//        }
//
//        public Builder add(String format, Object... args)
//        {
//
//        }
//
//        public static final class Builder
//        {
//            // final List<Object> args = new ArrayList<>();
//
//            public Builder add(String format)
//            {
//                // this.args.addAll(Arrays.asList(args));
//                return this;
//            }
//        }
//    }
}
