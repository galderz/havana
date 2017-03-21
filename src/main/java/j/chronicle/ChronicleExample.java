package j.chronicle;

import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;

import java.io.File;
import java.io.IOException;

// Could be used for logging, for persistent data stores, for messaging
// This library is an ultra low latency, high throughput, persisted, messaging and event driven in memory database.
// Basically, it allows an application to write and read high amount of messages
// which will be persisted to disc by operating system. Chronicle library supposed
// to be much faster that simple write to file, since it is using memory mapped
// files with direct access, which gets persisted to disc by OS.

// More info: http://binarybuffer.com/2013/04/java-chronicle-library-tutorial-1-basic-examples
public class ChronicleExample {

   // Check data in log with `od -t cx1 /var/folders/h6/y5gv6s4n0h58hsjkqwlttx8h0000gn/T/test.data`
   public static void main(String[] args) throws IOException {
      String basePath = System.getProperty("java.io.tmpdir") + File.separator + "test";
      IndexedChronicle chronicle = new IndexedChronicle(basePath);
      Excerpt excerpt = chronicle.createExcerpt();
      final int[] consolidates = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
      excerpt.startExcerpt(8 + 4 + 4 * consolidates.length);
      excerpt.writeLong(System.nanoTime());
      excerpt.writeUnsignedShort(consolidates.length);
      for (final int consolidate : consolidates) {
         excerpt.writeStopBit(consolidate);
      }
      excerpt.finish();
      chronicle.close();
   }

}
