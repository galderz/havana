package kube;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kube.Kubernetes.Event;

import java.util.stream.Stream;

import static kube.Kubernetes.EventType.NAMESPACES_LIST;

public class RemoteKubernetes
{
    KubernetesClient client = new DefaultKubernetesClient();

    public RemoteKubernetes()
    {
        System.out.println(client.getMasterUrl());
    }

    public Event read(Event event)
    {
        switch (event.type)
        {
            case GET_NAMESPACES:
                return new Event(listNamespaces(), NAMESPACES_LIST);
            default:
                throw new RuntimeException("NYI");
        }
    }

    private Stream<String> listNamespaces()
    {
        final var namespaces = client.namespaces().list();
        return namespaces.getItems().stream()
            .map(namespace -> namespace.getMetadata().getName());
    }

}
