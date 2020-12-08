///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.2
//DEPS com.squareup:javapoet:1.13.0
//DEPS org.jooq:joor:0.9.13

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.joor.Reflect;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(
    name = "poet"
    , mixinStandardHelpOptions = true
    , version = "poet 0.1"
    , description = "poet made with jbang"
)
class poet implements Callable<Integer>
{
    @Parameters(index = "0", description = "The greeting to print", defaultValue = "World!")
    private String greeting;

    public static void main(String... args)
    {
        int exitCode = new CommandLine(new poet()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception
    {
        MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("$T.xxx.println($S)", System.class, "Hello, " + greeting)
            .build();

        TypeSpec poemType = TypeSpec.classBuilder("poem")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main)
            .build();

        JavaFile javaFile = JavaFile.builder("", poemType)
            .build();

        try
        {
            Reflect.compile(
                "poem",
                javaFile.toString()).create().get();
        }
        finally
        {
            javaFile.writeTo(Path.of("."));
        }

        return 0;
    }
}
