package g.java.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MimeConversions {

   public static void main(String[] args) {
      System.out.printf("Default charset: %s%n", Charset.defaultCharset());

      // text/plain <=> application/octet-stream
      // ^ default charset: UTF-8
      textPlainToAppOctetStream();

      // text/plain;charset=UTF-16 <=> application/octet-stream
      textPlainUtf16ToAppOctetStream();

      // application/x-java-object <=> application/x-java-serialized-object
      javaObjToSerialized();
   }

   private static void textPlainToAppOctetStream() {
      Function<String, byte[]> f = String::getBytes;
      System.out.println(Arrays.toString(f.apply("abc")));

      Function<byte[], String> reverse = String::new;
      System.out.println(reverse.apply(new byte[] {97, 98, 99}));
   }

   private static void textPlainUtf16ToAppOctetStream() {
      BiFunction<String, Charset, byte[]> f = String::getBytes;
      System.out.println(Arrays.toString(f.apply("abc", Charset.forName("UTF-16"))));

      BiFunction<byte[], Charset, String> reverse = String::new;
      System.out.println(reverse.apply(new byte[] {-2, -1, 0, 97, 0, 98, 0, 99}, Charset.forName("UTF-16")));
   }

   private static void javaObjToSerialized() {
      Function<Object, byte[]> f = o -> {
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         ObjectOutputStream oos = mkObjectOS(os);
         writeObject(o, oos);
         return os.toByteArray();
      };
      System.out.println(Arrays.toString(f.apply(new Person("boo", "moo"))));
      
      Function<byte[], Object> reverse = b -> {
         InputStream is = new ByteArrayInputStream(b);
         return readObject(mkObjectIS(is));
      };
      System.out.println(reverse.apply(new byte[] {
         -84, -19, 0, 5, 115, 114, 0, 18, 103, 46, 106, 97, 118, 97, 46, 109, 105, 109, 101, 46, 80, 101, 114, 115, 111, 110, -100, -105, -126, 124, -41, 106, 60, -19, 2, 0, 2, 76, 0, 9, 102, 105, 114, 115, 116, 78, 97, 109, 101, 116, 0, 18, 76, 106, 97, 118, 97, 47, 108, 97, 110, 103, 47, 83, 116, 114, 105, 110, 103, 59, 76, 0, 8, 108, 97, 115, 116, 78, 97, 109, 101, 113, 0, 126, 0, 1, 120, 112, 116, 0, 3, 98, 111, 111, 116, 0, 3, 109, 111, 111
      }));
   }

   private static void writeObject(Object o, ObjectOutputStream objos) {
      try {
         objos.writeObject(o);
      } catch (IOException e) {
         throw new AssertionError(e);
      }
   }

   private static ObjectOutputStream mkObjectOS(ByteArrayOutputStream os) {
      try {
         return new ObjectOutputStream(os);
      } catch (IOException e) {
         throw new AssertionError(e);
      }
   }

   private static ObjectInputStream mkObjectIS(InputStream is) {
      try {
         return new ObjectInputStream(is);
      } catch (IOException e) {
         throw new AssertionError(e);
      }
   }

   private static Object readObject(ObjectInputStream is) {
      try {
         return is.readObject();
      } catch (IOException | ClassNotFoundException e) {
         throw new AssertionError(e);
      }
   }

}
