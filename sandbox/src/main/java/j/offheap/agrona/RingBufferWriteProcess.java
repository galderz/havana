package j.offheap.agrona;

import org.agrona.BitUtil;
import org.agrona.IoUtil;
import org.agrona.concurrent.MappedResizeableBuffer;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class RingBufferWriteProcess {

   public static void main(String[] args) throws Exception {
      long size = 2048;
      String path = IoUtil.tmpDirName() + "/ipc-buffer";

      final RandomAccessFile file = new RandomAccessFile(path, "rw");
      file.setLength(size);
      final FileChannel channel = file.getChannel();

      final MappedResizeableBuffer buffer = new MappedResizeableBuffer(channel, 0, 128 + TRAILER_LENGTH);
      final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(buffer.addressOffset(), (int)buffer.capacity());
      final RingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);

      final UnsafeBuffer srcBuffer = new UnsafeBuffer(new byte[1024]);
      final int producerId = 100;
      final int numMsgs = 3;
      final int length = BitUtil.SIZE_OF_INT * 2;
      final int repsValueOffset = BitUtil.SIZE_OF_INT;
      final int msgTypeId = 7;

      srcBuffer.putInt(0, producerId);
      for (int i = 0; i < numMsgs; i++) {
         srcBuffer.putInt(repsValueOffset, i);

         while (!ringBuffer.write(msgTypeId, srcBuffer, 0, length))
         {
            Thread.yield();
         }
      }
   }

}
