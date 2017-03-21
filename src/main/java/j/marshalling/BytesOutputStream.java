package j.marshalling;

import java.io.IOException;
import java.io.OutputStream;

public final class BytesOutputStream extends OutputStream {

   public static final int DEFAULT_DOUBLING_SIZE = 4 * 1024 * 1024; // 4MB

   byte bytes[];
   int count;

   public BytesOutputStream(byte bytes[]) {
      this.bytes = bytes;
   }

   @Override
   public void write(int b) throws IOException {
      // TODO: Dup
      int newcount = count + 1;
      if (newcount > bytes.length) {
         byte newbuf[] = new byte[getNewBufferSize(bytes.length, newcount)];
         System.arraycopy(bytes, 0, newbuf, 0, count);
         bytes = newbuf;
      }

      bytes[count] = (byte) b;
      count = newcount;
   }

   @Override
   public void write(byte[] b, int off, int len) {
      if ((off < 0) || (off > b.length) || (len < 0) ||
            ((off + len) > b.length) || ((off + len) < 0)) {
         throw new IndexOutOfBoundsException();
      } else if (len == 0) {
         return;
      }

      int newcount = count + len;
      if (newcount > bytes.length) {
         byte newbuf[] = new byte[getNewBufferSize(bytes.length, newcount)];
         System.arraycopy(bytes, 0, newbuf, 0, count);
         bytes = newbuf;
      }

      System.arraycopy(b, off, bytes, count, len);
      count = newcount;
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
