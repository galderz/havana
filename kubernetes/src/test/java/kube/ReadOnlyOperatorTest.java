package kube;

import kube.Kubernetes.Event;
import org.junit.jupiter.api.Test;

import static kube.Kubernetes.EventType.NAMESPACE_NOT_PRESENT;
import static kube.Kubernetes.EventType.NAMESPACE_PRESENT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ReadOnlyOperatorTest
{
    @Test
    public void testReconcileNoState()
    {
        var operator = new ReadOnlyOperator();
        var kube = new Kubernetes();
        var logger = new RecordingLogger();
        operator.reconcile(kube, logger);
        assertThat(logger.messages.remove(), is("Namespace does not exist"));
    }

    @Test
    public void testReconcileNamespacePresent()
    {
        var operator = new ReadOnlyOperator();
        var kube = new Kubernetes();
        var logger = new RecordingLogger();
        kube.events.add(new Event("my-namespace", NAMESPACE_PRESENT));
        operator.reconcile(kube, logger);
        assertThat(logger.messages.remove(), is("Namespace exists"));
    }
}
