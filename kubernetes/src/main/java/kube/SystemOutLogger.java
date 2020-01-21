package kube;

public class SystemOutLogger implements Logger
{
    @Override
    public void log(String message)
    {
        System.out.println(message);
    }
}
