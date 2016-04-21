package g.java.jboss.marshalling;

public interface DataInput {

   void readFully(byte b[]);
   void readFully(byte b[], int off, int len);
   boolean readBoolean();
   byte readByte();
   int readUnsignedByte();
   short readShort();
   int readUnsignedShort();
   char readChar();
   int readInt();
   long readLong();
   float readFloat();
   double readDouble();

   void beforeExternal();
   void afterExternal();
}
