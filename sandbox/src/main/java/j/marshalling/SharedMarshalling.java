package j.marshalling;

import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.ByteOutput;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.marshalling.river.RiverMarshallerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

public class SharedMarshalling {

   static MarshallerFactory factory = new RiverMarshallerFactory();
   static MarshallingConfiguration cfg = new MarshallingConfiguration();

   public static void main(String[] args) throws Exception {
      BytesDataOutput out = new BytesDataOutput(1);

      // Manual writes
      out.writeBoolean(true);
      out.writeByte(2);
      out.writeByte(Byte.MIN_VALUE);
      out.writeByte(Byte.MAX_VALUE);
      out.writeShort(3);
      out.writeShort(Short.MIN_VALUE);
      out.writeShort(Short.MAX_VALUE);
      out.writeInt(4);
      out.writeInt(Integer.MIN_VALUE);
      out.writeInt(Integer.MAX_VALUE);
      out.writeLong(5);
      out.writeLong(Long.MIN_VALUE);
      out.writeLong(Long.MAX_VALUE);
      out.writeFloat(6);
      out.writeFloat(Float.MIN_VALUE);
      out.writeFloat(Float.MAX_VALUE);
      out.writeDouble(7);
      out.writeDouble(Double.MIN_VALUE);
      out.writeDouble(Double.MAX_VALUE);
      out.writeByte((int) (Math.pow(2, 8) - 1));
      out.writeShort((int) (Math.pow(2, 16) - 1));
      out.writeChar('G');

      out.beforeExternal();
//      out.before(); <-- allocate 4 bytes

      // JBoss Marshalling writes
      Marshaller marshaller = factory.createMarshaller(cfg);
      marshaller.start(Marshalling.createByteOutput(new DataOutputByteOutput(out)));
      marshaller.writeObject(new Car("megane", "renault", 1996));
      marshaller.finish();

//      out.after(); <- save pos in previous 4 bytes
      out.afterExternal();

      // More writes
      out.writeByte(8);

      byte[] bytes = out.bytes;

      System.out.println(Arrays.toString(bytes));
      ////////////////////////////////////////////////////////////////////

      // Manual reads
      BytesDataInput in = new BytesDataInput(bytes);

      assert in.readBoolean();
      assert in.readByte() == 2;
      assert in.readByte() == Byte.MIN_VALUE;
      assert in.readByte() == Byte.MAX_VALUE;
      assert in.readShort() == 3;
      assert in.readShort() == Short.MIN_VALUE;
      assert in.readShort() == Short.MAX_VALUE;
      assert in.readInt() == 4;
      assert in.readInt() == Integer.MIN_VALUE;
      assert in.readInt() == Integer.MAX_VALUE;
      assert in.readLong() == 5;
      assert in.readLong() == Long.MIN_VALUE;
      assert in.readLong() == Long.MAX_VALUE;
      assert in.readFloat() == 6;
      assert in.readFloat() == Float.MIN_VALUE;
      assert in.readFloat() == Float.MAX_VALUE;
      assert in.readDouble() == 7;
      assert in.readDouble() == Double.MIN_VALUE;
      assert in.readDouble() == Double.MAX_VALUE;
      assert in.readUnsignedByte() == 255;
      assert in.readUnsignedShort() == 65535;
      assert in.readChar() == 'G';

//      in.before(); <-- read position and store somewhere
      in.beforeExternal();

      // TODO: Get position?

      Unmarshaller unmarshaller = factory.createUnmarshaller(cfg);
      unmarshaller.start(Marshalling.createByteInput(new DataInputByteInput(in)));
      System.out.println(unmarshaller.readObject());
      unmarshaller.finish();

//      in.after(); <-- set post to saved one
      in.afterExternal();

      // TODO: Rewind to position?

      assert in.readByte() == 8;
   }

   static final class DataOutputByteOutput extends OutputStream implements ByteOutput {
      final DataOutput out;

      DataOutputByteOutput(DataOutput out) {
         this.out = out;
      }

      @Override
      public void write(int b) throws IOException {
         out.writeByte(b);
      }

      @Override
      public void write(byte[] b) throws IOException {
         out.write(b);
      }

      @Override
      public void write(byte[] b, int off, int len) throws IOException {
         out.write(b, off, len);
      }
   }

   static final class DataInputByteInput extends InputStream implements ByteInput {
      final DataInput in;

      DataInputByteInput(DataInput in) {
         this.in = in;
      }

      @Override
      public int read() throws IOException {
         try {
            return in.readUnsignedByte();
         } catch(ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
         }
      }
   }

   static final class Car implements Serializable {
      final String name;
      final String brand;
      final int year;

      Car(String name, String brand, int year) {
         this.name = name;
         this.brand = brand;
         this.year = year;
      }

      @Override
      public String toString() {
         return "Car{" +
               "name='" + name + '\'' +
               ", brand='" + brand + '\'' +
               ", year=" + year +
               '}';
      }
   }
}
