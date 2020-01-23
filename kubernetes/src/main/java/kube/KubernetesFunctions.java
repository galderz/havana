package kube;

import java.util.function.Function;

public class KubernetesFunctions
{
    final Function<String, Boolean> existsNamespace;
    final Function<String, Boolean> createNamespace;

    public KubernetesFunctions(
        Function<String, Boolean> existsNamespace,
        Function<String, Boolean> createNamespace
    )
    {
        this.existsNamespace = existsNamespace;
        this.createNamespace = createNamespace;
    }
}
