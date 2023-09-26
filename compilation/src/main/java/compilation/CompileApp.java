package compilation;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.HexFormat;

public class CompileApp
{
    public static void main(String[] args)
    {
        final JavaFile javaFile = greetingJavaFile();
        compileToClass(javaFile);

        final byte[] bytes = {-54, -2, -70, -66};
        System.out.println(HexFormatting.encodeHexString(bytes));
    }

    private static void compileToClass(JavaFile javaFile)
    {
        final Class<?> clazz = InMemoryCompiler.compile(javaFile);
        System.out.println(clazz);
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
