package j.poet;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class CharacterPoet {

   public static void main(String[] args) throws IOException {
      //Character c = '\u03A9'; // uppercase Greek omega character
      //Character c = '\t';

      //Character c = '\\';

      // Creates:
      // System.out.println(''');
      // Instead of:
      // System.out.println('\'');
      char c = '\'';

      MethodSpec main = MethodSpec.methodBuilder("main")
         .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
         .returns(void.class)
         .addParameter(String[].class, "args")
         .addStatement("$T.out.println('$L')", System.class, c)
         .build();

      TypeSpec typeSpec = TypeSpec.classBuilder("UnicodeCharacter")
         .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
         .addMethod(main)
         .build();

      JavaFile javaFile = JavaFile.builder("j.poet.gen", typeSpec)
         .build();

      //javaFile.writeTo(System.out);
      javaFile.writeTo(new File("src/main/java"));

   }

}
