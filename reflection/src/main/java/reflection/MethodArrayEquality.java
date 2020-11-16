package reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class MethodArrayEquality
{
    public static void main(String[] args) throws Exception
    {
        Assert.check();

        final Method[] declaredPublicMethods = privateGetDeclaredMethods(true);
        final Method[] declaredMethods = privateGetDeclaredMethods(false);
        final Method[] publicMethods = privateGetPublicMethods();

        assert declaredPublicMethods != declaredMethods;
        assert declaredPublicMethods != publicMethods;
        assert declaredMethods != publicMethods;

        assert methodArrayEquals(declaredPublicMethods, declaredMethods);
        assert methodArrayEquals(declaredPublicMethods, publicMethods) : String.format(
            "%ndeclaredPublicMethods:%n%s%npublicMethods:%n%s%n"
            , Arrays.toString(declaredPublicMethods)
            , Arrays.toString(publicMethods)
        );
        assert methodArrayEquals(declaredMethods, publicMethods);
    }

    static boolean methodArrayEquals(Method[] a, Method[] a2)
    {
        // Check if contains Object methods
        if (containsNoObjectMethods(a) && containsNoObjectMethods(a2))
            return Arrays.equals(a, a2);

        for (int i = 0; i < a.length; i++)
        {
            if (isNoObjectMethod(a[i]) && isNoObjectMethod(a2[i]))
            {
                if (!Objects.equals(a[i], a2[i]))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isNoObjectMethod(Method method)
    {
        return method.getDeclaringClass() != Object.class;
    }

    static boolean containsNoObjectMethods(Method[] array)
    {
        return isNoObjectMethod(array[array.length - 1]);
    }

    static Method[] privateGetDeclaredMethods(boolean publicOnly) throws Exception
    {
        final Method method = Class.class.getDeclaredMethod("privateGetDeclaredMethods", boolean.class);
        method.setAccessible(true);
        return (Method[]) method.invoke(A.class, publicOnly);
    }

    static Method[] privateGetPublicMethods() throws Exception
    {
        final Method method = Class.class.getDeclaredMethod("privateGetPublicMethods");
        method.setAccessible(true);
        return (Method[]) method.invoke(A.class);
    }

    static final class A
    {
        public int a1;
        public int a2;
        public int a3;

        public int getA1()
        {
            return a1;
        }

        public void setA1(int a1)
        {
            this.a1 = a1;
        }

        public int getA2()
        {
            return a2;
        }

        public void setA2(int a2)
        {
            this.a2 = a2;
        }

        public int getA3()
        {
            return a3;
        }

        public void setA3(int a3)
        {
            this.a3 = a3;
        }
    }

}
