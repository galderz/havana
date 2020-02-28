package kube;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import static kube.Kubernetes.EventType.NAMESPACES_LIST;
import static kube.Kubernetes.EventType.NAMESPACE_CREATED;

public class Kubernetes
{
    Queue<Event> events = new ArrayDeque<>();

    public boolean createNamespace(String namespaceName)
    {
        final var event = events.peek();
        if (event == null)
            return false;

        return events.stream()
            .anyMatch(e ->
                e.type == NAMESPACE_CREATED
                    && e.<String>param().equals(namespaceName)
            );
    }

    public boolean existsNamespace(String namespaceName)
    {
        final var event = events.peek();
        if (event == null)
            return false;

        return events.stream()
            .anyMatch(e ->
                e.type == NAMESPACES_LIST
                && e.<Stream<String>>param().anyMatch(n -> n.equals(namespaceName))
            );
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

        @SuppressWarnings("unchecked")
        <T> T param() {
            return (T) param;
        }
    }

    public enum EventType
    {
        GET_NAMESPACES,
        NAMESPACES_LIST,

        CREATE_NAMESPACE,
        NAMESPACE_CREATED,

        ERROR,
    }

    private static boolean isError(Event event)
    {
        return event != null && event.type == EventType.ERROR;
    }
}
