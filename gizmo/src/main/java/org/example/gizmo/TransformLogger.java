package org.example.gizmo;

import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.BranchResult;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import io.quarkus.gizmo.TestClassLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TransformLogger
{
    public static void main(String[] args) throws Exception
    {
        Assert.check();

        assert new ConsoleLogger().isTraceEnabled();
        generateTraceLoggingDisabled();

        generateSubstitutedTraceLoggingPerPackage();
    }

    private static void generateSubstitutedTraceLoggingPerPackage() throws Exception
    {
        TestClassLoader cl = gizmoTraceFilterLogger("mylogger");
        assert "org.jboss.logging.BasicLogger".equals(
            cl.loadClass("com.MyLogger").getAnnotation(TargetClass.class).className()
        );
        BasicLogger myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert myLogger.isTraceEnabled();

        cl = gizmoTraceFilterLogger("blah");
        assert "org.jboss.logging.BasicLogger".equals(
            cl.loadClass("com.MyLogger").getAnnotation(TargetClass.class).className()
        );
        myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert !myLogger.isTraceEnabled();
    }

    private static TestClassLoader gizmoTraceFilterLogger(String name)
    {
        TestClassLoader cl = new TestClassLoader(TransformLogger.class.getClassLoader());
        try (ClassCreator creator = ClassCreator.builder().classOutput(cl).className("com.MyLogger").interfaces(BasicLogger.class).build())
        {
            AnnotationCreator annotationCreator = creator.addAnnotation(TargetClass.class);
            annotationCreator.addValue("className", "org.jboss.logging.BasicLogger");

            final var nameField = creator.getFieldCreator("name", String.class);
            nameField.addAnnotation(Alias.class);

            MethodCreator method = creator.getMethodCreator("isTraceEnabled", boolean.class);
            method.addAnnotation(Substitute.class);
            method.addAnnotation(Fold.class);

            method.writeInstanceField(nameField.getFieldDescriptor(), method.getThis(), method.load(name));

            ResultHandle equalsResult = method.invokeVirtualMethod(
                MethodDescriptor.ofMethod(Object.class, "equals", boolean.class, Object.class)
                , method.readInstanceField(nameField.getFieldDescriptor(), method.getThis())
                , method.load("mylogger")
            );
            BranchResult branch = method.ifNonZero(equalsResult);
            branch.trueBranch().returnValue(branch.trueBranch().load(true));
            branch.falseBranch().returnValue(branch.falseBranch().load(false));
        }
        return cl;
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
        assert !myLogger.isTraceEnabled();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TargetClass
    {
        String className() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.FIELD})
    public @interface Substitute {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Fold {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
    public @interface Alias {
    }

    public static final class Assert
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
}
