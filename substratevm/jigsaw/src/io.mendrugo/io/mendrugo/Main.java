package io.mendrugo;

import org.graalvm.compiler.serviceprovider.JavaVersionUtil;

public class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello mendrugo!");
        System.out.println(JavaVersionUtil.JAVA_SPEC);
    }
}
