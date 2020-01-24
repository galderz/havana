package kube;

import kube.Kubernetes.Event;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kube.Kubernetes.EventType.NAMESPACES_LIST;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReadOnlyOperatorTest
{
    @Test
    public void testReconcileNoState()
    {
        var operator = new ReadOnlyOperator();
        var kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        var logger = new RecordingLogger();
        operator.reconcile(functions, logger);
        assertThat(logger.messages.remove(), is("Namespace does not exist"));
    }

    @Test
    public void testReconcileNamespacePresent()
    {
        var operator = new ReadOnlyOperator();
        var kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        var logger = new RecordingLogger();
        kube.events.add(new Event(List.of("my-namespace").stream(), NAMESPACES_LIST));
        operator.reconcile(functions, logger);
        assertThat(logger.messages.remove(), is("Namespace exists"));
    }
}
