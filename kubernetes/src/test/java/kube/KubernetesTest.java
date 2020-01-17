package kube;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class KubernetesTest
{
    @Test
    public void testCreateNamespace() {
        final Kubernetes kube = new Kubernetes();
        kube.createNamespace().apply("hello");
        assertThat(kube.existsNamespace().apply("hello"), is(true));
    }

}
