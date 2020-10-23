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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class MultiCategoryTraceLogger
{
    public static void main(String[] args) throws Exception
    {
        Assert.check();

        assert !multiTraceLogger("blah", "org.infinispan").isTraceEnabled();

        assert multiTraceLogger("org.infinispan", "org.infinispan").isTraceEnabled();
        assert multiTraceLogger("org.infinispan.core", "org.infinispan").isTraceEnabled();

        assert multiTraceLogger("org.infinispan", "io.quarkus", "org.infinispan").isTraceEnabled();
        assert multiTraceLogger("org.infinispan.core", "io.quarkus", "org.infinispan").isTraceEnabled();

        assert !multiTraceLogger("blah", "io.quarkus", "org.infinispan").isTraceEnabled();
    }

    private static BasicLogger multiTraceLogger(String name, String... categories) throws Exception
    {
        TestClassLoader cl = new TestClassLoader(UniCategoryTraceLogger.class.getClassLoader());
        try (ClassCreator creator = ClassCreator.builder().classOutput(cl).className("com.MyLogger").interfaces(BasicLogger.class).build())
        {
            AnnotationCreator annotationCreator = creator.addAnnotation(UniCategoryTraceLogger.TargetClass.class);
            annotationCreator.addValue("className", "org.jboss.logging.BasicLogger");

            final var nameField = creator.getFieldCreator("name", String.class);
            nameField.addAnnotation(UniCategoryTraceLogger.Alias.class);

            MethodCreator method = creator.getMethodCreator("isTraceEnabled", boolean.class);
            method.addAnnotation(UniCategoryTraceLogger.Substitute.class);
            method.addAnnotation(UniCategoryTraceLogger.Fold.class);

            method.writeInstanceField(nameField.getFieldDescriptor(), method.getThis(), method.load(name));

            BytecodeCreator current = method;
            for (String category : categories)
            {
                final var branches = categoryMatch(category, nameField, current);
                branches[0].trueBranch().returnValue(branches[0].trueBranch().load(true));
                branches[1].trueBranch().returnValue(branches[1].trueBranch().load(true));
                current = branches[1].falseBranch();
            }

            current.returnValue(current.load(false));
        }
        return (BasicLogger) cl.loadClass("com.MyLogger").newInstance();
    }

    private static BranchResult[] categoryMatch(String category, FieldCreator nameField, BytecodeCreator creator)
    {
        ResultHandle equalsResult = creator.invokeVirtualMethod(
            MethodDescriptor.ofMethod(Object.class, "equals", boolean.class, Object.class)
            , creator.readInstanceField(nameField.getFieldDescriptor(), creator.getThis())
            , creator.load(category)
        );

        final var branches = new BranchResult[2];
        BranchResult equalsBranch = creator.ifTrue(equalsResult);
        branches[0] = equalsBranch;
        try (BytecodeCreator false1 = equalsBranch.falseBranch())
        {
            ResultHandle startsWithResult = false1.invokeVirtualMethod(
                MethodDescriptor.ofMethod(String.class, "startsWith", boolean.class, String.class)
                , false1.readInstanceField(nameField.getFieldDescriptor(), false1.getThis())
                , false1.load(category)
            );

            branches[1] = false1.ifTrue(startsWithResult);
        }

        return branches;
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
}
