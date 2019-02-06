package j.poet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;

public class TryCatchPoet {

   public static void main(String[] args) throws Exception {
      MethodSpec main = MethodSpec.methodBuilder("main")
         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
         .returns(void.class)
         .addParameter(String[].class, "args")
         .beginControlFlow("try")
         .addStatement("throw new Exception($S)", "Failed")
         .nextControlFlow("catch ($T e)", Exception.class)
         .addStatement("throw new $T(e)", RuntimeException.class)
         .endControlFlow()
         .build();

      TypeSpec helloWorld = TypeSpec.classBuilder("TryCatch")
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(main)
         .build();

      JavaFile javaFile = JavaFile.builder("j.poet.gen", helloWorld)
         .build();

      //javaFile.writeTo(System.out);
      javaFile.writeTo(new File("src/main/java"));
   }


}
