package g.java.jboss.marshalling;

public final class BytesDataOutput implements DataOutput {

   public static final int DEFAULT_DOUBLING_SIZE = 4 * 1024 * 1024; // 4MB

   public byte bytes[];
   int count;

   // TODO: Avoid this instance variable
   int beforeExternalPos;

   public BytesDataOutput(int size) {
      this.bytes = new byte[size];
   }

   @Override
   public void write(byte[] b, int off, int len) {
      if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
         throw new IndexOutOfBoundsException();
      } else if (len == 0) {
         return;
      }

      int newcount = checkCapacity(len);
      System.arraycopy(b, off, bytes, count, len);
      count = newcount;
   }

   private int checkCapacity(int len) {
      int newcount = count + len;
      if (newcount > bytes.length) {
         byte newbuf[] = new byte[getNewBufferSize(bytes.length, newcount)];
         System.arraycopy(bytes, 0, newbuf, 0, count);
         bytes = newbuf;
      }
      return newcount;
   }

   @Override
   public void write(byte[] b) {
      write(b);
   }

   @Override
   public void writeBoolean(boolean v) {
      writeByte((byte) (v ? 1 : 0));
   }

   @Override
   public void writeByte(int v) {
      int newcount = checkCapacity(1);
      bytes[count] = (byte) v;
      count = newcount;
   }

   @Override
   public void writeShort(int v) {
      writeByte((byte) (v >> 8));
      writeByte((byte) v);
   }

   @Override
   public void writeChar(int v) {
      writeByte((byte) (v >> 8));
      writeByte((byte) v);
   }

   @Override
   public void writeInt(int v) {
      writeByte((byte) (v >> 24));
      writeByte((byte) (v >> 16));
      writeByte((byte) (v >> 8));
      writeByte((byte) v);
   }

   @Override
   public void writeLong(long v) {
      writeByte((byte) (v >> 56L));
      writeByte((byte) (v >> 48L));
      writeByte((byte) (v >> 40L));
      writeByte((byte) (v >> 32L));
      writeByte((byte) (v >> 24L));
      writeByte((byte) (v >> 16L));
      writeByte((byte) (v >> 8L));
      writeByte((byte) v);
   }

   @Override
   public void writeFloat(float v) {
      final int bits = Float.floatToIntBits(v);
      writeByte((byte) (bits >> 24));
      writeByte((byte) (bits >> 16));
      writeByte((byte) (bits >> 8));
      writeByte((byte) bits);
   }

   @Override
   public void writeDouble(double v) {
      final long bits = Double.doubleToLongBits(v);
      writeLong(bits);
   }

   @Override
   public void beforeExternal() {
      // Save position where to store position
      beforeExternalPos = count;
      // Allocate 4 bytes to store the position when afterExternal gets called
      count = checkCapacity(4);
   }

   @Override
   public void afterExternal() {
      bytes[beforeExternalPos++] = ((byte) (count >> 24));
      bytes[beforeExternalPos++] = ((byte) (count >> 16));
      bytes[beforeExternalPos++] = ((byte) (count >> 8));
      bytes[beforeExternalPos++] = (byte) count;
   }

   /**
    * Gets the number of bytes to which the internal buffer should be resized.
    *
    * @param curSize    the current number of bytes
    * @param minNewSize the minimum number of bytes required
    * @return the size to which the internal buffer should be resized
    */
   private int getNewBufferSize(int curSize, int minNewSize) {
      if (curSize <= DEFAULT_DOUBLING_SIZE)
         return Math.max(curSize << 1, minNewSize);
      else
         return Math.max(curSize + (curSize >> 2), minNewSize);
   }

}
