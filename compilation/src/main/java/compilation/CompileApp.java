package compilation;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

import javax.lang.model.element.Modifier;
import java.util.Arrays;

public class CompileApp
{
    public static void main(String[] args)
    {
        final byte[] bytes = {-54, -2, -70, -66};
        System.out.println("Smoke test: " + HexFormatting.encodeHexString(bytes));

        compileHelloWorld();
        compileAppLifecycleBean();
    }

    private static void compileHelloWorld()
    {
        final JavaFile javaFile = helloWorldJavaFile();
        compileToClass(javaFile);
        compileToBytes(javaFile);
    }

    private static void compileAppLifecycleBean()
    {
        final JavaFile javaFile = appLifecycleBeanJavaFile();
        compileToClass(javaFile);
        compileToBytes(javaFile);
    }

    private static void compileToClass(JavaFile javaFile)
    {
        final Class<?> clazz = InMemoryCompiler.compileAsClass(javaFile);
        System.out.println(clazz);
    }

    private static void compileToBytes(JavaFile javaFile)
    {
        final byte[] classBytes = InMemoryCompiler.compileAsBytes(javaFile);
        System.out.println(Arrays.toString(classBytes));
        final byte[] markerBytes = {classBytes[0], classBytes[1], classBytes[2], classBytes[3]};
        System.out.println("The marker for the class byte[] is: " + HexFormatting.encodeHexString(markerBytes));
    }

    static JavaFile helloWorldJavaFile()
    {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
            .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        return JavaFile.builder("com.example.helloworld", helloWorld)
            .build();
    }

    static JavaFile appLifecycleBeanJavaFile()
    {
        MethodSpec onStart = MethodSpec.methodBuilder("onStart")
            .returns(void.class)
            .addParameter(
                ParameterSpec.builder(StartupEvent.class, "ev")
                    .addAnnotation(Observes.class)
                    .build()
            )
            .addStatement("LOGGER.info($S)", "The application is starting...")
            .build();

        TypeSpec appLifecycleBean = TypeSpec.classBuilder("AppLifecycleBean")
            .addField(
                FieldSpec.builder(Logger.class, "LOGGER")
                    .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("$T.getLogger($S)", Logger.class, "ListenerBean")
                    .build()
            )
            .addModifiers(Modifier.PUBLIC)
            .addMethod(onStart)
            .build();

        return JavaFile.builder("org.acme.lifecycle", appLifecycleBean)
            .build();
    }
}
