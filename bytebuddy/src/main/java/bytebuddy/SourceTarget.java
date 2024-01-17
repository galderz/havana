package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class SourceTarget
{
    public static void main(String[] args) throws Exception
    {
        String helloWorld = new ByteBuddy()
            .subclass(Source.class)
            .method(named("hello")).intercept(MethodDelegation.to(Target.class))
            .make()
            .load(SourceTarget.class.getClassLoader())
            .getLoaded()
            .newInstance()
            .hello("World");

        System.out.println(helloWorld);
    }

    public static class Source
    {
        public String hello(String name)
        {
            return null;
        }
    }

    public static class Target
    {
        public static String hello(String name)
        {
            return "Hello " + name + "!";
        }
    }
}
