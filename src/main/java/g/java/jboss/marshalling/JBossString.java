package g.java.jboss.marshalling;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.marshalling.river.RiverMarshallerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Random;

public class JBossString {

   static MarshallerFactory factory = new RiverMarshallerFactory();
   static MarshallingConfiguration cfg = new MarshallingConfiguration();

   public static void main(String[] args) throws Exception {
      Marshaller marshaller = factory.createMarshaller(cfg);
      ByteArrayOutputStream os = new ByteArrayOutputStream(1);
      marshaller.start(Marshalling.createByteOutput(os));
      marshaller.writeObject(generateRandomString(144));
      marshaller.finish();

      byte[] bytes = os.toByteArray();
      System.out.println(Arrays.toString(bytes));

      Unmarshaller unmarshaller = factory.createUnmarshaller(cfg);
      unmarshaller.start(Marshalling.createByteInput(new ByteArrayInputStream(bytes)));
      assert unmarshaller.readObject().equals("a");
      unmarshaller.finish();
   }

   public static String generateRandomString(int numberOfChars) {
      Random r = new Random(System.currentTimeMillis());
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < numberOfChars; i++) sb.append((char) (64 + r.nextInt(26)));
      return sb.toString();
   }

}
