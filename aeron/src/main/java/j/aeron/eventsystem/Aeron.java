package j.aeron.eventsystem;

import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.NoOpIdleStrategy;

import java.util.concurrent.atomic.AtomicBoolean;

enum Aeron {
   AERON;

   final AtomicBoolean running = new AtomicBoolean(true);
   final io.aeron.Aeron aeron;

   private final MediaDriver driver;

   Aeron() {
      final MediaDriver.Context ctx = new MediaDriver.Context()
         .threadingMode(ThreadingMode.SHARED)
         .sharedIdleStrategy(new NoOpIdleStrategy());

      driver = MediaDriver.launch(ctx);
      aeron = io.aeron.Aeron.connect();
   }

   void stop() {
      Aeron.AERON.running.set(false);
      aeron.close();
      driver.close();
   }

   //   static final AtomicBoolean RUNNING = new AtomicBoolean(true);
//
//   static io.aeron.Aeron AERON;
//
//   static void start() {
//   }

}
