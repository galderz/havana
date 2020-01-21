package kube;

public class Operator
{
    public void reconcile(Kubernetes kube, Logger logger)
    {
        String namespace = "my-namespace";

        final Boolean exists = kube.existsNamespace().apply(namespace);
        if (!exists)
        {
            final Boolean created = kube.createNamespace().apply("my-namespace");
            if (created)
            {
                logger.log("Namespace created");
            }
            else
            {
                logger.log("Namespace not created");
            }
        }
        else
        {
            logger.log("Namespace exists");
        }
    }
}
