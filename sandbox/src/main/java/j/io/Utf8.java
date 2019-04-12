package j.io;

import okio.ByteString;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.UUID;

public class Utf8 {

   public static void main(String[] args) {
      {
         byte[] invalidUtf8 = {(byte)0xFF};
         ByteString b = ByteString.of(invalidUtf8);
         System.out.println(b.utf8()); // �
      }

      {
         byte[] invalidUtf8 = {(byte)0xFF};
         String s = new String(invalidUtf8, Charset.forName("UTF-8"));
         System.out.println(s);
      }

      {
         byte[] invalidUtf8 = {(byte)0xff};
         //byte[] invalidUtf8 = {(byte)0xFF, (byte)49, (byte)50, (byte)51};
         //byte[] invalidUtf8 = "abc".getBytes();
         //byte[] invalidUtf8 = {(byte)98, (byte)99, (byte)100, (byte)0xFF};
         //byte[] invalidUtf8 = "�".getBytes();

         final Charset utf8 = Charset.forName("UTF-8");
         final CharsetDecoder decoder = utf8.newDecoder();
         decoder.onMalformedInput(CodingErrorAction.REPORT);
         decoder.onUnmappableCharacter(CodingErrorAction.REPORT);

         ByteBuffer buffer = ByteBuffer.wrap(invalidUtf8);
         try {
            decoder.decode(buffer);
         } catch (CharacterCodingException e) {
            e.printStackTrace();
         }
      }

      {
         String s = new String("Ã¼zÃ¼m baÄlarÄ±");
         Charset ch = Charset.forName("UTF-16");
         byte[] bytes = s.getBytes(ch);
         System.out.println(new String(bytes, ch));
      }

      {
         String input = "€Tes¶ti©ng [§] al€l o€f i¶t _ - À ÆÑ with some 9umbers as"
            + " w2921**#$%!@# well Ü, or ü, is a chaŒracte⚽";
         System.out.println(input);
      }

      {
         char[] chars = {'\uD800', 'a'};  // Unpaired surrogate
         String str = new String(chars);
         System.out.println(str); // ?a
      }

      {
         char[] chars = {'\uD800', 'a'};  // Unpaired surrogate
         final ByteString byteString = ByteString.encodeString(new String(chars), Charset.forName("UTF-8"));
         System.out.println(byteString.utf8()); // ?a
      }

      {
         byte[] invalidUtf8 = {(byte)0xff};

java.nio.ByteBuffer buffer = ByteBuffer.wrap(invalidUtf8);
try {
   Charset.forName("UTF-8").newDecoder().decode(buffer);
} catch (CharacterCodingException e) {
   e.printStackTrace();
}
      }
   }

}
