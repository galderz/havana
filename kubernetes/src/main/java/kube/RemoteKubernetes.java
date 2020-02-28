package kube;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import kube.Kubernetes.Event;

import java.util.stream.Stream;

import static kube.Kubernetes.EventType.NAMESPACES_LIST;
import static kube.Kubernetes.EventType.NAMESPACE_CREATED;

public class RemoteKubernetes
{
    KubernetesClient client = new DefaultKubernetesClient();

    public RemoteKubernetes()
    {
        System.out.println(client.getMasterUrl());
    }

    public Event process(Event event)
    {
        switch (event.type)
        {
            case GET_NAMESPACES:
                return new Event(listNamespaces(), NAMESPACES_LIST);
            case CREATE_NAMESPACE:
                final var namespace = (String) event.param;
                createNamespace(namespace);
                return new Event(namespace, NAMESPACE_CREATED);
            default:
                throw new RuntimeException("NYI");
        }
    }

    private void createNamespace(String namespaceName)
    {
        client.namespaces().createNew()
            .withNewMetadata()
            .withName(namespaceName)
            .endMetadata()
            .done();
    }

    private Stream<String> listNamespaces()
    {
        final var namespaces = client.namespaces().list();
        return namespaces.getItems().stream()
            .map(namespace -> namespace.getMetadata().getName());
    }
}
