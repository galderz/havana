package kube;

import java.util.function.Function;

public interface KubernetesFunctions
{
    Function<String, Boolean> existsNamespace();
}
