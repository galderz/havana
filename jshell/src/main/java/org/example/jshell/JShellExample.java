package org.example.jshell;

import jdk.jshell.JShell;
import jdk.jshell.SnippetEvent;

import java.util.List;

public class JShellExample
{
    public static void main(String[] args)
    {
        JShell jshell = JShell.create();

        {
            List<SnippetEvent> list = jshell.eval("int x = 7+3*4;");
            System.out.println("Size of list: " + list.size());
            System.out.println("Value of the expression is : " + list.get(0).value());
        }

        {
            List<SnippetEvent> list = jshell.eval("1.0 < 2.0");
            System.out.println("Size of list: " + list.size());
            System.out.println("Value of the expression is : " + list.get(0).value());
        }
    }
}
