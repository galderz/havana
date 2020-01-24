package kube;

import kube.Kubernetes.Event;

import static kube.Kubernetes.EventType.GET_NAMESPACES;

public class KubernetesShell implements AutoCloseable
{
    public static final Event EVENT_GET_NAMESPACES = new Event(null, GET_NAMESPACES);
    RemoteKubernetes remoteKubernetes = new RemoteKubernetes();
    Kubernetes kubernetes = new Kubernetes();

    public boolean existsNamespace(String namespaceName)
    {
        final var namespaceList = remoteKubernetes.read(EVENT_GET_NAMESPACES);
        kubernetes.events.add(namespaceList);
        return kubernetes.existsNamespace(namespaceName);
    }

    @Override
    public void close()
    {
        remoteKubernetes.client.close();
    }
}
