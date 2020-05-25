package lang;

import java.lang.invoke.MethodHandles;

public class ClassValueNPE
{
    public static void main(String[] args)
    {
        final var g1 = MethodHandles.arrayElementGetter(Object[].class);
        System.out.println(g1);
        final var g2 = MethodHandles.arrayElementGetter(Object[].class);
        System.out.println(g2);
    }
}
