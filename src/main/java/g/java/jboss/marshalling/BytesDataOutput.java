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
      write(bytes, 0, bytes.length);
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
      int newcount = checkCapacity(2);
      final int s = count;
      bytes[s] = (byte) (v >> 8);
      bytes[s+1] = (byte) v;
      count = newcount;
   }

   @Override
   public void writeChar(int v) {
      int newcount = checkCapacity(2);
      final int s = count;
      bytes[s] = (byte) (v >> 8);
      bytes[s+1] = (byte) v;
      count = newcount;
   }

   @Override
   public void writeInt(int v) {
      int newcount = checkCapacity(4);
      final int s = count;
      bytes[s] = (byte) (v >> 24);
      bytes[s+1] = (byte) (v >> 16);
      bytes[s+2] = (byte) (v >> 8);
      bytes[s+3] = (byte) v;
      count = newcount;
   }

   @Override
   public void writeLong(long v) {
      int newcount = checkCapacity(8);
      final int s = count;
      bytes[s] = (byte) (v >> 56L);
      bytes[s+1] = (byte) (v >> 48L);
      bytes[s+2] = (byte) (v >> 40L);
      bytes[s+3] = (byte) (v >> 32L);
      bytes[s+4] = (byte) (v >> 24L);
      bytes[s+5] = (byte) (v >> 16L);
      bytes[s+6] = (byte) (v >> 8L);
      bytes[s+7] = (byte) v;
      count = newcount;
   }

   @Override
   public void writeFloat(float v) {
      final int bits = Float.floatToIntBits(v);
      int newcount = checkCapacity(4);
      final int s = count;
      bytes[s] = (byte) (bits >> 24);
      bytes[s+1] = (byte) (bits >> 16);
      bytes[s+2] = (byte) (bits >> 8);
      bytes[s+3] = (byte) bits;
      count = newcount;
   }

   @Override
   public void writeDouble(double v) {
      final long bits = Double.doubleToLongBits(v);
      int newcount = checkCapacity(8);
      final int s = count;
      bytes[s] = (byte) (bits >> 56L);
      bytes[s+1] = (byte) (bits >> 48L);
      bytes[s+2] = (byte) (bits >> 40L);
      bytes[s+3] = (byte) (bits >> 32L);
      bytes[s+4] = (byte) (bits >> 24L);
      bytes[s+5] = (byte) (bits >> 16L);
      bytes[s+6] = (byte) (bits >> 8L);
      bytes[s+7] = (byte) bits;
      count = newcount;
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
