package kube;

import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;

public class KubeClientPlay
{
    public static void main(String[] args)
    {
        KubernetesClient client = new DefaultKubernetesClient();
        System.out.println(client.getMasterUrl());

        // List namespaces
        NamespaceList myNs = client.namespaces().list();
        System.out.println(myNs);

        // Create a namespace
        client.namespaces().createNew()
            .withNewMetadata()
                .withName("my-namespace")
            .endMetadata()
            .done();

        client.close();
    }
}
