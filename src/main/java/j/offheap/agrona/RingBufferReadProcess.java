package j.offheap.agrona;

import org.agrona.BitUtil;
import org.agrona.IoUtil;
import org.agrona.concurrent.MappedResizeableBuffer;
import org.agrona.concurrent.MessageHandler;
import org.agrona.concurrent.UnsafeBuffer;
import org.agrona.concurrent.ringbuffer.OneToOneRingBuffer;
import org.agrona.concurrent.ringbuffer.RingBuffer;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import static org.agrona.concurrent.ringbuffer.RingBufferDescriptor.TRAILER_LENGTH;

public class RingBufferReadProcess {

   public static void main(String[] args) throws Exception {
      long size = 2048;
      String path = IoUtil.tmpDirName() + "/ipc-buffer";

      final RandomAccessFile file = new RandomAccessFile(path, "rw");
      file.setLength(size);
      final FileChannel channel = file.getChannel();

      final MappedResizeableBuffer buffer = new MappedResizeableBuffer(channel, 0, 128 + TRAILER_LENGTH);
      final UnsafeBuffer unsafeBuffer = new UnsafeBuffer(buffer.addressOffset(), (int)buffer.capacity());
      final RingBuffer ringBuffer = new OneToOneRingBuffer(unsafeBuffer);

      final MessageHandler handler =
         (msgTypeId, buf, index, length) -> {
            final int producerId = buffer.getInt(index);
            System.out.println("Producer id: " + producerId);

            final int iteration = buffer.getInt(index + BitUtil.SIZE_OF_INT);

            System.out.println("Message: " + iteration);
         };

      final int numMsgs = 3;
      for (int i = 0; i < numMsgs; i++) {
         final int readCount = ringBuffer.read(handler);
         if (0 == readCount)
         {
            Thread.yield();
         }
      }

   }

}
