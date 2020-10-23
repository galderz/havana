package org.example.gizmo;

import io.quarkus.gizmo.AnnotationCreator;
import io.quarkus.gizmo.BranchResult;
import io.quarkus.gizmo.BytecodeCreator;
import io.quarkus.gizmo.ClassCreator;
import io.quarkus.gizmo.MethodCreator;
import io.quarkus.gizmo.MethodDescriptor;
import io.quarkus.gizmo.ResultHandle;
import io.quarkus.gizmo.TestClassLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UniCategoryTraceLogger
{
    public static void main(String[] args) throws Exception
    {
        Assert.check();

        assert new ConsoleLogger().isTraceEnabled();
        generateTraceLoggingDisabled();

        checkAnnotation();
        generateNoLogger();
        generateExactLoggerName1();
        generateStartsWithLoggerName1();
    }

    private static void generateNoLogger() throws Exception
    {
        TestClassLoader cl = gizmoTraceFilterLogger("blah");
        BasicLogger myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert !myLogger.isTraceEnabled();
    }

    private static void generateExactLoggerName1() throws Exception
    {
        TestClassLoader cl = gizmoTraceFilterLogger("mylogger");
        BasicLogger myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert myLogger.isTraceEnabled();

        cl = gizmoTraceFilterLogger("mylogger.mysublogger");
        assert "org.jboss.logging.BasicLogger".equals(
            cl.loadClass("com.MyLogger").getAnnotation(TargetClass.class).className()
        );
        myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert myLogger.isTraceEnabled();
    }

    private static void generateStartsWithLoggerName1() throws Exception
    {
        TestClassLoader cl = gizmoTraceFilterLogger("mylogger.mysublogger");
        assert "org.jboss.logging.BasicLogger".equals(
            cl.loadClass("com.MyLogger").getAnnotation(TargetClass.class).className()
        );
        BasicLogger myLogger = (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
        assert myLogger.isTraceEnabled();
    }

    private static void checkAnnotation() throws Exception
    {
        TestClassLoader cl = gizmoTraceFilterLogger("mylogger");
        assert "org.jboss.logging.BasicLogger".equals(cl.loadClass("com.MyLogger").getAnnotation(TargetClass.class).className());
    }

    private static TestClassLoader gizmoTraceFilterLogger(String name)
    {
        TestClassLoader cl = new TestClassLoader(UniCategoryTraceLogger.class.getClassLoader());
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

            BranchResult equalsBranch = method.ifTrue(equalsResult);
            try (BytecodeCreator false1 = equalsBranch.falseBranch())
            {
                ResultHandle startsWithResult = false1.invokeVirtualMethod(
                    MethodDescriptor.ofMethod(String.class, "startsWith", boolean.class, String.class)
                    , false1.readInstanceField(nameField.getFieldDescriptor(), false1.getThis())
                    , false1.load("mylogger")
                );

                BranchResult startsWithBranch = false1.ifTrue(startsWithResult);
                startsWithBranch.trueBranch().returnValue(startsWithBranch.trueBranch().load(true));
                startsWithBranch.falseBranch().returnValue(startsWithBranch.falseBranch().load(false));
            }

            equalsBranch.trueBranch().returnValue(equalsBranch.trueBranch().load(true));
        }
        return cl;
    }

    private static void generateTraceLoggingDisabled() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        TestClassLoader cl = new TestClassLoader(UniCategoryTraceLogger.class.getClassLoader());
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
