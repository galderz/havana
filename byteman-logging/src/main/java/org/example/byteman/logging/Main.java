package org.example.byteman.logging;

public class Main
{
    public static void main(String[] args)
    {
        final App app = new App();
        app.hello();
        app.hello();
        app.hello();
    }
}
