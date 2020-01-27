//usr/bin/env jbang "$0" "$@" ; exit $?

//DEPS log4j:log4j:1.2.17

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.Arrays;

public class classpath
{
    static final Logger logger = Logger.getLogger(classpath.class);

    public static void main(String[] args)
    {
        BasicConfigurator.configure(); // (2)
        logger.info("Welcome to jbang");

        Arrays.asList(args).forEach(arg -> logger.warn("arg: " + arg));
        logger.info("Hello from Java!");
    }
}
