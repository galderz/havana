package kube;

import kube.Kubernetes.Event;
import org.junit.jupiter.api.Test;

import static kube.Kubernetes.EventType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OperatorTest
{
    @Test
    public void testReconcileNoState()
    {
        var operator = new Operator();
        var kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        var logger = new RecordingLogger();
        operator.reconcile(functions, logger);
        assertThat(logger.messages.remove(), is("Namespace created"));
    }

    @Test
    public void testReconcileNamespaceExists()
    {
        var operator = new Operator();
        var kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        var logger = new RecordingLogger();
        kube.events.add(new Event("my-namespace", CREATED_NAMESPACE));
        operator.reconcile(functions, logger);
        assertThat(logger.messages.remove(), is("Namespace exists"));
    }

    @Test
    public void testReconcileNamespaceError()
    {
        var operator = new Operator();
        var kube = new Kubernetes();
        var functions = new KubernetesFunctions(kube::existsNamespace, kube::createNamespace);
        var logger = new RecordingLogger();
        kube.events.add(new Event("error", ERROR));
        operator.reconcile(functions, logger);
        assertThat(logger.messages.remove(), is("Namespace not created"));
    }

}
