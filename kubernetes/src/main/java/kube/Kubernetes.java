package kube;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

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
        final var event = events.peek();
        if (event == null)
            return false;

        switch (event.type) {
            case NAMESPACES_LIST:
                final Stream<String> namespaces = (Stream<String>) event.param;
                return namespaces.anyMatch(n -> n.equals(namespaceName));
            default:
                throw new RuntimeException("NYI");
        }
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
        GET_NAMESPACES,
        NAMESPACES_LIST,

        EXISTS_NAMESPACE,
        NAMESPACE_PRESENT,
        NAMESPACE_NOT_PRESENT,

        CREATED_NAMESPACE,
        ERROR,
    }

    private static boolean isError(Event event)
    {
        return event != null && event.type == EventType.ERROR;
    }
}
