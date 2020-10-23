package org.example.gizmo;

public class ConsoleLogger extends JBossLogger
{
    @Override
    public boolean isTraceEnabled()
    {
        return true;
    }
}
