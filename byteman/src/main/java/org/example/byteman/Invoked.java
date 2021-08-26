package org.example.byteman;

import java.util.ArrayList;
import java.util.List;

public class Invoked
{
    final List<String> list = new ArrayList<>();

    void add(String s)
    {
        list.add(s);
    }

    private void end()
    {
        System.out.println("end");
    }

    public static void main(String[] args)
    {
        final Invoked invoked = new Invoked();
        invoked.add("z");
        invoked.add("b");
        invoked.add("a");
        invoked.add("c");
        invoked.end();
        System.out.println("Out: " + invoked.list);
    }
}
