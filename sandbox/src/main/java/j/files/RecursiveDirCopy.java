package j.files;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class RecursiveDirCopy {

   /**
    * Copy source file to target location.
    */
   static void copyFile(Path source, Path target) {
      CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES, REPLACE_EXISTING };
      try {
         Files.copy(source, target, options);
      } catch (IOException x) {
         System.err.format("Unable to copy: %s: %s%n", source, x);
      }
   }

   /**
    * A {@code FileVisitor} that copies a file-tree ("cp -r")
    */
   static class TreeCopier implements FileVisitor<Path> {
      private final Path source;
      private final Path target;

      TreeCopier(Path source, Path target) {
         this.source = source;
         this.target = target;
      }

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
         // before visiting entries in a directory we copy the directory
         // (okay if directory already exists).
         CopyOption[] options = new CopyOption[] { COPY_ATTRIBUTES };

         Path newdir = target.resolve(source.relativize(dir));
         try {
            Files.copy(dir, newdir, options);
         } catch (FileAlreadyExistsException x) {
            // ignore
         } catch (IOException x) {
            System.err.format("Unable to create: %s: %s%n", newdir, x);
            return SKIP_SUBTREE;
         }
         return CONTINUE;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
         copyFile(file, target.resolve(source.relativize(file)));
         return CONTINUE;
      }

      @Override
      public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
         // fix up modification time of directory when done
         if (exc == null) {
            Path newdir = target.resolve(source.relativize(dir));
            try {
               FileTime time = Files.getLastModifiedTime(dir);
               Files.setLastModifiedTime(newdir, time);
            } catch (IOException x) {
               System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
            }
         }
         return CONTINUE;
      }

      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc) {
         if (exc instanceof FileSystemLoopException) {
            System.err.println("cycle detected: " + file);
         } else {
            System.err.format("Unable to copy: %s: %s%n", file, exc);
         }
         return CONTINUE;
      }
   }

   public static void main(String[] args) throws IOException {
      EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
      Path source = Paths.get("/Users/g/0/infinispan/git/server/integration/build/target/infinispan-server-9.0.0-SNAPSHOT");
      Path target = Paths.get("/var/folders/jv/dckshh_s5w51msdh411068cr0000gn/T/infinispan-test-domain8117094420451534461");
      TreeCopier tc = new TreeCopier(source, target);
      Files.walkFileTree(source, opts, Integer.MAX_VALUE, tc);
   }

}
