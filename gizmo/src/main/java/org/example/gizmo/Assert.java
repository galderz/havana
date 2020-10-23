package org.example.gizmo;

import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.BranchResult;
import io.quarkus.gizmo.BytecodeCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.FieldCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import io.quarkus.gizmo.TestClassLoader;

public final class Assert
{
    public static void check()
    {
        boolean enabled = false;
        assert enabled = true;
        if (!enabled)
            throw new AssertionError("assert not enabled");
    }

    private Assert()
    {
    }
}
