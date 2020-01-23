package kube;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class KubernetesTest
{
    @Test
    public void testCreateNamespace() {
        final Kubernetes kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        functions.createNamespace.apply("hello");
        assertThat(functions.existsNamespace.apply("hello"), is(true));
    }

}
