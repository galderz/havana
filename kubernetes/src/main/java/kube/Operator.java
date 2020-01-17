package kube;

public class Operator
{
    public void reconcile(Kubernetes kube)
    {
        String namespace = "my-namespace";

        final Boolean exists = kube.existsNamespace().apply(namespace);
        if (!exists)
        {
            final Boolean created = kube.createNamespace().apply("my-namespace");
            if (created)
            {
                System.out.println("Namespace created");
            }
            else
            {
                System.out.println("Namespace not created");
            }
        }
        else
        {
            System.out.println("Namespace exists");
        }
    }
}
