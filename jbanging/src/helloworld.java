//usr/bin/env jbang "$0" "$@" ; exit $?

import java.lang.management.ManagementFactory;

public class helloworld
{
    public static void main(String[] args)
    {
        System.out.printf("hola mundo (java %s)%n", ManagementFactory.getRuntimeMXBean().getVmVersion());
    }
}
