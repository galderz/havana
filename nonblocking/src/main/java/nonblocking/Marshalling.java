package nonblocking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.function.Function;

interface Marshalling {

   Function<Object, byte[]> marshall();

   Function<byte[], Object> unmarshall();

   Marshalling JAVA = new JavaMarshalling();
   Marshalling UTF8 = new StringMarshalling();

   final class JavaMarshalling implements Marshalling {

      private JavaMarshalling() {
      }

      @Override
      public Function<Object, byte[]> marshall() {
         return obj -> {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutput oo = new ObjectOutputStream(baos)) {
               oo.writeObject(obj);
               return baos.toByteArray();
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         };
      }

      @Override
      public Function<byte[], Object> unmarshall() {
         return bytes -> {
            if (bytes == null)
               return null;

            try (ObjectInput oi = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
               return oi.readObject();
            } catch (Exception e) {
               throw new RuntimeException(e);
            }
         };
      }

   }

   final class StringMarshalling implements Marshalling {

      private static Charset UTF8 = Charset.forName("UTF-8");

      private StringMarshalling() {
      }

      @Override
      public Function<Object, byte[]> marshall() {
         return obj -> obj == null ? null : ((String) obj).getBytes(UTF8);
      }

      @Override
      public Function<byte[], Object> unmarshall() {
         return bytes -> bytes == null ? null : new String(bytes, UTF8);
      }

   }

}
