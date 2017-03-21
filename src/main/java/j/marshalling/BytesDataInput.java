package j.marshalling;

public final class BytesDataInput implements DataInput {

   final byte bytes[];

   int position;

   int count;

   // TODO: Avoid this instance variable
   int rewindPosition;

   public BytesDataInput(byte[] bytes) {
      this.bytes = bytes;
   }

   @Override
   public void readFully(byte[] b) {
      readFully(b, 0, b.length);
   }

   @Override
   public void readFully(byte[] b, int off, int len) {
      if (len < 0)
         throw new IndexOutOfBoundsException();
      int n = 0;
      while (n < len) {
         int count = read(b, off + n, len - n);
         if (count < 0)
            throw eofOnRead();
         n += count;
      }
   }

   private int read(byte b[], int off, int len) {
      if (off < 0 || len < 0 || len > b.length - off)
         throw new IndexOutOfBoundsException();

      final int position = this.position;
      if (position >= count)
         return -1;

      int available = count - position;
      if (len > available)
         len = available;

      if (len <= 0)
         return 0;

      System.arraycopy(bytes, position, b, off, len);
      this.position = position + len;
      return len;
   }

   private static RuntimeException eofOnRead() {
      return new RuntimeException("Read past end of file");
   }

   @Override
   public boolean readBoolean() {
      return read() != 0;
   }

   private byte read() {
      return bytes[position++];
   }

   @Override
   public byte readByte() {
      return read();
   }

   @Override
   public int readUnsignedByte() {
      return read() & 0xff;
   }

   @Override
   public short readShort() {
      short v = (short) (bytes[position] << 8 | (bytes[position + 1] & 0xff));
      this.position = position + 2;
      return v;
   }

   @Override
   public int readUnsignedShort() {
      int v = (bytes[position] & 0xff) << 8 | (bytes[position + 1] & 0xff);
      this.position = position + 2;
      return v;
   }

   @Override
   public char readChar() {
      char v = (char) (bytes[position] << 8 | (bytes[position + 1] & 0xff));
      this.position = position + 2;
      return v;
   }

   @Override
   public int readInt() {
      int v = bytes[position] << 24
            | (bytes[position + 1] & 0xff) << 16
            | (bytes[position + 2] & 0xff) << 8
            | (bytes[position + 3] & 0xff);
      this.position = position + 4;
      return v;
   }

   @Override
   public long readLong() {
      return (long) readInt() << 32L | (long) readInt() & 0xffffffffL;
   }

   @Override
   public float readFloat() {
      return Float.intBitsToFloat(readInt());
   }

   @Override
   public double readDouble() {
      return Double.longBitsToDouble(readLong());
   }

   @Override
   public void beforeExternal() {
      rewindPosition = readInt();
   }

   @Override
   public void afterExternal() {
      position = rewindPosition;
   }

}
