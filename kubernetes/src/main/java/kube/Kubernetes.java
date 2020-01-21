package kube;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

public class Kubernetes implements KubernetesFunctions
{
    Queue<Event> events = new ArrayDeque<>();

    public Function<String, Boolean> createNamespace()
    {
        return namespaceName ->
        {
            final Event top = events.peek();
            if (isError(top))
            {
                events.remove();
                return false;
            }
            events.add(new Event(namespaceName, EventType.CREATED_NAMESPACE));
            return true;
        };
    }

    public Function<String, Boolean> existsNamespace()
    {
        return namespaceName ->
        {
            if (isNamespaceCreated() || isNamespacePresent())
            {
                return true;
            }

            return false;
        };
    }

    private boolean isNamespacePresent()
    {
        return events.stream().anyMatch(op -> op.type == EventType.NAMESPACE_PRESENT);
    }

    private boolean isNamespaceCreated()
    {
        return events.stream().anyMatch(op -> op.type == EventType.CREATED_NAMESPACE);
    }

//    public static Consumer<Kubernetes> apply()
//    {
//        return null;
//    }

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
