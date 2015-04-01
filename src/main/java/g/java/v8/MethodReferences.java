package g.java.v8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

// https://leanpub.com/whatsnewinjava8/read
public class MethodReferences {

   public static class FileFilters {
      public static boolean fileIsPdf(File file) { return true; }
      public static boolean fileIsTxt(File file) { return true; }
      public static boolean fileIsRtf(File file) { return true; }
   }

   public static void main(String... args) throws IOException {
      List<File> files = new LinkedList<>(Arrays.asList(new File("/tmp")));
      files.stream().filter(FileFilters::fileIsRtf).forEach(System.out::println);

      Files.lines(Paths.get("javag.iml"))
         .map(String::trim)
         .filter(s -> !s.isEmpty())
         .forEach(System.out::println);
   }

}
