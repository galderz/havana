package compilation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.HexFormat;

public class CompileApp
{
    public static void main(String[] args)
    {
        final byte[] bytes = {-54, -2, -70, -66};
        System.out.println("Smoke test: " + HexFormatting.encodeHexString(bytes));

        final JavaFile javaFile = greetingJavaFile();
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

    static JavaFile greetingJavaFile()
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
}
