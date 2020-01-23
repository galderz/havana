package kube;

import kube.Kubernetes.Event;

import java.util.function.Function;

import static kube.Kubernetes.EventType.EXISTS_NAMESPACE;

public class KubernetesShell implements AutoCloseable
{
    RemoteKubernetes remoteKubernetes = new RemoteKubernetes();
    Kubernetes kubernetes = new Kubernetes();

    public boolean existsNamespace(String namespaceName)
    {
        return toExistsNamespace()
            .andThen(remoteKubernetes.read())
            .andThen(recordEvent())
            .andThen(kubernetes::existsNamespace)
            .apply(namespaceName);
    }

    private Function<Event, String> recordEvent()
    {
        return event ->
        {
            kubernetes.events.add(event);
            return (String) event.param;
        };
    }

    private Function<String, Event> toExistsNamespace()
    {
        return namespaceName -> new Event(namespaceName, EXISTS_NAMESPACE);
    }

    @Override
    public void close()
    {
        remoteKubernetes.client.close();
    }
}
