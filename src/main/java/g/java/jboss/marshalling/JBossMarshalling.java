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

public class JBossMarshalling {

   static MarshallerFactory factory = new RiverMarshallerFactory();
   static MarshallingConfiguration cfg = new MarshallingConfiguration();

   public static void main(String[] args) throws Exception {
      Marshaller marshaller = factory.createMarshaller(cfg);
      ByteArrayOutputStream os = new ByteArrayOutputStream(1);
      marshaller.start(Marshalling.createByteOutput(os));
      marshaller.writeBoolean(true);
      marshaller.writeShort(3);
      marshaller.finish();

      byte[] bytes = os.toByteArray();
      System.out.println(Arrays.toString(bytes));

      Unmarshaller unmarshaller = factory.createUnmarshaller(cfg);
      unmarshaller.start(Marshalling.createByteInput(new ByteArrayInputStream(bytes)));
      assert unmarshaller.readBoolean();
      assert unmarshaller.readShort() == 3;
      unmarshaller.finish();
   }

}
