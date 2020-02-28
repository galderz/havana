package kube;

public class Operator
{
    public void reconcile(KubernetesFunctions kube, Logger logger)
    {
        String namespace = "my-namespace";

        final Boolean exists = kube.existsNamespace.apply(namespace);
        if (!exists)
        {
            final Boolean created = kube.createNamespace.apply("my-namespace");
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

    public static void main(String[] args)
    {
        final var operator = new Operator();
        final var logger = new SystemOutLogger();
        try (var shell = new KubernetesShell()) {
            final var functions = new KubernetesFunctions(shell::existsNamespace, shell::createNamespace);
            operator.reconcile(functions, logger);
        }
    }

}
