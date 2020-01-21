package kube;

import java.util.ArrayDeque;
import java.util.Queue;

public class RecordingLogger implements Logger
{
    Queue<String> messages = new ArrayDeque<>();

    @Override
    public void log(String message)
    {
        messages.add(message);
    }
}
