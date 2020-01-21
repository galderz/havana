package kube;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Function;

public class Kubernetes
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
            events.add(new Event(namespaceName, EventType.CREATE_NAMESPACE));
            return true;
        };
    }

    public Function<String, Boolean> existsNamespace()
    {
        return namespaceName ->
        {
            if (isNamespaceCreated())
            {
                return true;
            }

            return false;
        };
    }

    private boolean isNamespaceCreated()
    {
        // TODO check if retrived
        return events.stream().anyMatch(op -> op.type == EventType.CREATE_NAMESPACE);
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
        CREATE_NAMESPACE,
        GET_NAMESPACE,
        ERROR,
    }

    private static boolean isError(Event event)
    {
        return event != null && event.type == EventType.ERROR;
    }
}
