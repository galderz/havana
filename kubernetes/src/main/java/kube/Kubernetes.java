package kube;

import java.util.ArrayDeque;
import java.util.Queue;

public class Kubernetes
{
    Queue<Event> events = new ArrayDeque<>();

    public boolean createNamespace(String namespaceName)
    {
        final Event top = events.peek();
        if (isError(top))
        {
            events.remove();
            return false;
        }
        events.add(new Event(namespaceName, EventType.CREATED_NAMESPACE));
        return true;
    }

    public boolean existsNamespace(String namespaceName)
    {
        if (isNamespaceCreated() || isNamespacePresent())
        {
            return true;
        }

        return false;
    }

    private boolean isNamespacePresent()
    {
        return events.stream().anyMatch(op -> op.type == EventType.NAMESPACE_PRESENT);
    }

    private boolean isNamespaceCreated()
    {
        return events.stream().anyMatch(op -> op.type == EventType.CREATED_NAMESPACE);
    }

    public final static class Event
    {
        Object param;
        EventType type;

        public Event(Object param, EventType type)
        {
            this.param = param;
            this.type = type;
        }
    }

    public enum EventType
    {
        EXISTS_NAMESPACE,
        NAMESPACE_PRESENT,
        NAMESPACE_NOT_PRESENT,

        CREATED_NAMESPACE,
        GET_NAMESPACE,
        ERROR,
    }

    private static boolean isError(Event event)
    {
        return event != null && event.type == EventType.ERROR;
    }
}
