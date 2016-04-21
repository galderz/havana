package g.java.jboss.marshalling;

public interface DataOutput {
   void write(byte b[]);
   void write(byte b[], int off, int len);
   void writeBoolean(boolean v);
   void writeByte(int v);
   void writeShort(int v);
   void writeChar(int v);
   void writeInt(int v);
   void writeLong(long v);
   void writeFloat(float v);
   void writeDouble(double v);

   void beforeExternal();
   void afterExternal();
}
