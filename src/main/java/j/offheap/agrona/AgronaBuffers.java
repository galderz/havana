package j.offheap.agrona;

import org.agrona.IoUtil;
import org.agrona.concurrent.MappedResizeableBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class AgronaBuffers {

   /**
    * Agrona does not use unsafe to allocate memory.
    * Instead, it enables a FileChannel to be created and has it memory mapped (off-heap).
    * Then, it uses unsafe to walk this off-heap space.
    */
   public static void main(String[] args) throws Exception {
      long size = 2 * (long)Integer.MAX_VALUE;
      String path = IoUtil.tmpDirName() + "/eg-buffer";

      final RandomAccessFile file = new RandomAccessFile(path, "rw");
      file.setLength(size);
      final FileChannel channel = file.getChannel();

      final MappedResizeableBuffer buffer = new MappedResizeableBuffer(channel, 0, 100);
      final long value = 0x5555555555555555L;
      // Write to buffer directly
      buffer.putLong(24, value);

      final UnsafeBuffer offHeapDirectBuffer = new UnsafeBuffer(buffer.addressOffset(), (int)buffer.capacity());
      // Take data from mapped buffer and copy it to another address
      buffer.putBytes(96, offHeapDirectBuffer, 24, 4);

      System.out.println("buffer[96] = " + buffer.getInt(96));
      System.out.println("expected value = " + (int)value);
   }

}
