package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class Aspects
{
    public static void main(String[] args) throws Exception
    {
        MemoryDatabase loggingDatabase = new ByteBuddy()
            .subclass(MemoryDatabase.class)
            .method(named("load")).intercept(MethodDelegation.to(LoggerInterceptor.class))
            .make()
            .load(Aspects.class.getClassLoader())
            .getLoaded()
            .newInstance();

        LoggerInterceptor.infrastructure = new Infrastructure();
        System.out.println(loggingDatabase.load("message"));
    }

    public static class MemoryDatabase
    {
        public List<String> load(String info)
        {
            return Arrays.asList(info + ": foo", info + ": bar");
        }
    }

    public static class LoggerInterceptor
    {
        static Infrastructure infrastructure;

        public static List<String> log(@SuperCall Callable<List<String>> zuper) throws Exception
        {
            System.out.println("Calling database");
            long startTime = System.nanoTime();
            try
            {
                List<String> result;
                do
                {
                    result = zuper.call();
                    System.out.println(result);
                } while (!infrastructure.isDone(startTime));

                return result;
            }
            finally
            {
                System.out.println("Returned from database");
            }
        }
    }

    public static class Infrastructure
    {
        boolean isDone(long startTime)
        {
            return TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime) >= 2;
        }
    }
}
