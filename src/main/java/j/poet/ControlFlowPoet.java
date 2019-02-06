package j.poet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;

public class ControlFlowPoet {

   public static void main(String[] args) throws Exception {
      MethodSpec main = MethodSpec.methodBuilder("main")
         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
         .returns(void.class)
         .addParameter(String[].class, "args")
         .addStatement("long now = $T.currentTimeMillis()", System.class)
         .beginControlFlow("if ($T.currentTimeMillis() < now) ", System.class)
         .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
         .nextControlFlow("else if ($T.currentTimeMillis() == now)", System.class)
         .addStatement("$T.out.println($S)", System.class, "Time stood still!")
         .nextControlFlow("else")
         .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
         .endControlFlow()
         .build();

      TypeSpec helloWorld = TypeSpec.classBuilder("ControlFlow")
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(main)
         .build();

      JavaFile javaFile = JavaFile.builder("j.poet.gen", helloWorld)
         .build();

      //javaFile.writeTo(System.out);
      javaFile.writeTo(new File("src/main/java"));
   }

}
