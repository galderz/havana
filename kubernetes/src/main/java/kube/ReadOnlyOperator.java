package kube;

public class ReadOnlyOperator
{
    public void reconcile(KubernetesFunctions kubernetes, Logger logger)
    {
        String namespace = "my-namespace";

        final Boolean exists = kubernetes.existsNamespace.apply(namespace);
        if (exists)
        {
            logger.log("Namespace exists");
        }
        else
        {
            logger.log("Namespace does not exist");
        }
    }

    public static void main(String[] args)
    {
        final var operator = new ReadOnlyOperator();
        final var logger = new SystemOutLogger();
        try (var shell = new KubernetesShell()) {
            final var functions = new KubernetesFunctions(shell::existsNamespace, null);
            operator.reconcile(functions, logger);
        }
    }
}
