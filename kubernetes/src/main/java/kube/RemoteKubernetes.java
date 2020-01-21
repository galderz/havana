package kube;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kube.Kubernetes.Event;

import java.util.function.Function;

import static kube.Kubernetes.EventType.CREATED_NAMESPACE;
import static kube.Kubernetes.EventType.NAMESPACE_NOT_PRESENT;
import static kube.Kubernetes.EventType.NAMESPACE_PRESENT;

public class RemoteKubernetes
{
    KubernetesClient client = new DefaultKubernetesClient();

    public RemoteKubernetes()
    {
        System.out.println(client.getMasterUrl());
    }

    public Function<Event, Event> read()
    {
        return event ->
        {
            switch (event.type)
            {
                case EXISTS_NAMESPACE:
                    var exists = findNamespace(event);
                    if (exists)
                    {
                        return new Event(event.param, NAMESPACE_PRESENT);
                    }
                    return new Event(event.param, NAMESPACE_NOT_PRESENT);
                default:
                    throw new RuntimeException("NYI");
            }
        };
    }

    private boolean findNamespace(Event event)
    {
        final String namespaceName = (String) event.param;
        final var namespaces = client.namespaces().list();
        return namespaces.getItems().stream()
            .anyMatch(namespace ->
                namespace.getMetadata().getName().equals(namespaceName));
    }

}
