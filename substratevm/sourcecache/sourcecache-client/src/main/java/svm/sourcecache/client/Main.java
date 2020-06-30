package svm.sourcecache.client;

import org.jboss.slf4j.JBossLoggerFactory;
import svm.sourcecache.library.Greeter;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println(Greeter.greet(args[0]));
        final var hashCode = System.identityHashCode(JBossLoggerFactory.class);
        System.out.println("A value: " + hashCode / 41);
    }
}
