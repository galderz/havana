package concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CloneArrayVisibility
{
    public static void main(String[] args) throws Exception
    {
        Asserts.needEnabledAsserts();

        ExecutorService service = Executors.newFixedThreadPool(2, r ->
        {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        for (int c = 0; c < 10000; c++)
        {
            final Test[] tests = new Test[10000];
            final ZII_Result[] results = new ZII_Result[10000];
            for (int t = 0; t < tests.length; t++)
            {
                tests[t] = new Test();
            }

            Future<?> f1 = service.submit(() ->
            {
                ZII_Result r = new ZII_Result();
                for (int t = 0; t < tests.length; t++)
                {
                    final Test test = tests[t];
                    test.actor2(r);
                    results[t] = r;
                }
            });
            Future<?> f2 = service.submit(() ->
            {
                for (Test test : tests)
                {
                    test.actor1();
                }
            });

            f1.get();
            f2.get();

            for (ZII_Result result : results)
            {
                assert result.r1 : "type not equals";
                assert result.r2 == 1 : "array length not equals";
                assert result.r3 == 0 || result.r3 == -1 : "unexpected array content";
            }
        }
    }

    public static class ZII_Result {
        boolean r1;
        int r2, r3;
    }

    public static class Test
    {
        static int[] src;
        static
        {
            src = new int[1];
            src[0] = -1;
        }

        int[] copy;

        public void actor1()
        {
            copy = src.clone();
        }

        public void actor2(ZII_Result r)
        {
            int[] t = copy;
            if (t != null)
            {
                r.r1 = (t.getClass() == int[].class);
                r.r2 = t.length;
                r.r3 = t[0];
            }
            else
            {
                r.r1 = true;
                r.r2 = 1;
                r.r3 = -1;
            }
        }
    }
}







