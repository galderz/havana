package kube;

import kube.Kubernetes.Event;

import static kube.Kubernetes.EventType.CREATE_NAMESPACE;
import static kube.Kubernetes.EventType.GET_NAMESPACES;

public class KubernetesShell implements AutoCloseable
{
    RemoteKubernetes remoteKubernetes = new RemoteKubernetes();
    Kubernetes kubernetes = new Kubernetes();

    public boolean existsNamespace(String namespaceName)
    {
        final var namespaceList = remoteKubernetes.process(new Event(null, GET_NAMESPACES));
        kubernetes.events.add(namespaceList);
        return kubernetes.existsNamespace(namespaceName);
    }

    public boolean createNamespace(String namespaceName)
    {
        final var createdNamespace = remoteKubernetes.process(new Event(namespaceName, CREATE_NAMESPACE));
        kubernetes.events.add(createdNamespace);
        return kubernetes.createNamespace(namespaceName);
    }

    @Override
    public void close()
    {
        remoteKubernetes.client.close();
    }
}
