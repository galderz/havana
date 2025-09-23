package org.example.byteman.logging;

public class Main
{
    public static void main(String[] args)
    {
        final App app = new App();
        app.hello(1);
        app.hello(2);
        app.hello(3);
    }
}
