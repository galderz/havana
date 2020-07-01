package svm.sourcecache.client;

import org.jboss.slf4j.JBossLoggerFactory;
import svm.sourcecache.library.Greeter;

public class Main
{
    public static void main(String[] args)
    {
        final var greet = Greeter.greet(args[0]);
        final var loggerFactory = new JBossLoggerFactory();
        final var logger = loggerFactory.getLogger(args[0]);
        System.out.println("Stdout: " + greet);
        logger.info("Logger: " + greet);
    }
}
