package org.example.gizmo;

import io.quarkus.gizmo.BranchResult;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import io.quarkus.gizmo.TestClassLoader;

public class TransformLogger
{
    public static void main(String[] args) throws Exception
    {
        System.out.println(new ConsoleLogger().isTraceEnabled());
        generateTraceLoggingDisabled();
    }

    private static void generateTraceLoggingDisabled() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        TestClassLoader cl = new TestClassLoader(TransformLogger.class.getClassLoader());
        try (ClassCreator creator = ClassCreator.builder().classOutput(cl).className("com.MyLogger").interfaces(BasicLogger.class).build())
        {
            MethodCreator method = creator.getMethodCreator("isTraceEnabled", boolean.class);
            ResultHandle equalsResult = method.invokeVirtualMethod(
                MethodDescriptor.ofMethod(Object.class, "equals", boolean.class, Object.class)
                , method.load("not-trace")
                , method.load("trace")
            );
            BranchResult branch = method.ifNonZero(equalsResult);
            branch.trueBranch().returnValue(branch.trueBranch().load(true));
            branch.falseBranch().returnValue(branch.falseBranch().load(false));
        }
        BasicLogger myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        System.out.println(myLogger.isTraceEnabled());
    }
}
