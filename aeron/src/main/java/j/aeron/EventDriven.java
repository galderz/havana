package j.aeron;

import io.aeron.Aeron;
import io.aeron.CommonContext;
import io.aeron.Image;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.BufferUtil;
import org.agrona.CloseHelper;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NoOpIdleStrategy;
import org.agrona.concurrent.SigInt;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.agrona.BitUtil.CACHE_LINE_LENGTH;
import static org.agrona.UnsafeAccess.UNSAFE;

public class EventDriven {

   private static final String CHANNEL = CommonContext.IPC_CHANNEL;
   private static final int PUT_STREAM = 10;

   interface EventProcessor {
      void handlePut(PutEvent e);
      void handleNoOp(NoOpEvent e);
   }

   enum NoOpEvent {
      INSTANCE;
   }

   final class PutEvent {
      byte[] key;
      byte[] value;
   }

   @SuppressWarnings("Duplicates")
   public static void main(String[] args) throws InterruptedException {
      final AtomicBoolean running = new AtomicBoolean(true);
      SigInt.register(() -> running.set(false));

      final MediaDriver.Context ctx = new MediaDriver.Context()
         .threadingMode(ThreadingMode.SHARED)
         .sharedIdleStrategy(new NoOpIdleStrategy());

      try (MediaDriver ignore = MediaDriver.launch(ctx);
           Aeron aeron = Aeron.connect();
           Publication publication = aeron.addPublication(CHANNEL, PUT_STREAM);
           Subscription subscription = aeron.addSubscription(CHANNEL, PUT_STREAM))
      {
         final Thread subscriberThread = startSubscriber(running, subscription);
         subscriberThread.setName("subscriber");

//         final Thread publisherThread = new Thread(new Publisher(running, publication));
//         publisherThread.setName("publisher");
//         final Thread rateReporterThread = new Thread(new RateReporter(running, subscriber));
//         rateReporterThread.setName("rate-reporter");

//         rateReporterThread.start();
         subscriberThread.start();
//         publisherThread.start();

         final String key = "key";
         final String value = "value";
         final byte[] keyBytes = key.getBytes();
         final byte[] valueBytes = value.getBytes();
         final int length = keyBytes.length + valueBytes.length;

         final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
            publication.maxMessageLength(), CACHE_LINE_LENGTH);
         final UnsafeBuffer buffer = new UnsafeBuffer(byteBuffer);

         buffer.putBytes(0, keyBytes);
         buffer.putBytes(keyBytes.length, valueBytes);

         final long result = publication.offer(buffer, 0, length);
         if (result < 0L)
         {
            if (result == Publication.BACK_PRESSURED)
            {
               System.out.println("Offer failed due to back pressure");
            }
            else if (result == Publication.NOT_CONNECTED)
            {
               System.out.println("Offer failed because publisher is not connected to subscriber");
            }
            else if (result == Publication.ADMIN_ACTION)
            {
               System.out.println("Offer failed because of an administration action in the system");
            }
            else if (result == Publication.CLOSED)
            {
               System.out.println("Offer failed publication is closed");
               return;
            }
            else if (result == Publication.MAX_POSITION_EXCEEDED)
            {
               System.out.println("Offer failed due to publication reaching max position");
               return;
            }
            else
            {
               System.out.println("Offer failed due to unknown reason");
            }
         }
         else
         {
            System.out.println("yay!");
         }


         subscriberThread.join();
//         publisherThread.join();
//         rateReporterThread.join();
      }

      Thread.sleep(60000);
   }

   private static Thread startSubscriber(final AtomicBoolean running, final Subscription subscription) {
      return new Thread(() -> {
            final int fragmentCountLimit = 10;
            final FragmentHandler fragmentHandler = SamplesUtil.printStringMessage(PUT_STREAM);

            // Avoid busy spin idle strategy
            //final IdleStrategy idleStrategy = new BusySpinIdleStrategy();
            final IdleStrategy idleStrategy = new BackoffIdleStrategy(1,1,1,1);

            SamplesUtil.subscriberLoop(fragmentHandler, fragmentCountLimit, running, idleStrategy).accept(subscription);
            System.out.println("Shutting down...");
      });
   }


//   public static void main(String[] args) throws InterruptedException {
//      // Use IPC Media - Inter Process Communication
//      // If shared memory is to be used for communications on the same machine between threads or processes then the IPC media value can be applied.
//      // This is the highest throughput and lowest latency means of communicating between two process or threads on the same machine.
//      final UnsafeBuffer buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(256, 64));
//      final int lingerTimeoutMs = 60000;
//
//      final MediaDriver driver = MediaDriver.launchEmbedded();
//      final Aeron.Context ctx = new Aeron.Context();
//      // Hook with embedded media driver
//      ctx.aeronDirectoryName(driver.aeronDirectoryName());
//
//      final Thread subscriberThread = startSubscriber();
//      subscriberThread.start();
//
//      Thread.sleep(5000);
//
//      System.out.println("Publishing to " + CHANNEL + " on stream Id " + PUT_STREAM);
//
//      try (Aeron aeron = Aeron.connect(ctx);
//           Publication publication = aeron.addPublication(CHANNEL, PUT_STREAM)) {
//         final String key = "key";
//         final String value = "value";
//         final byte[] keyBytes = key.getBytes();
//         final byte[] valueBytes = value.getBytes();
//         final int length = keyBytes.length + valueBytes.length;
//
//         buffer.putBytes(0, keyBytes);
//         buffer.putBytes(keyBytes.length, valueBytes);
//
//         final long result = publication.offer(buffer, 0, length);
//
//         if (result < 0L)
//         {
//            if (result == Publication.BACK_PRESSURED)
//            {
//               System.out.println("Offer failed due to back pressure");
//            }
//            else if (result == Publication.NOT_CONNECTED)
//            {
//               System.out.println("Offer failed because publisher is not connected to subscriber");
//            }
//            else if (result == Publication.ADMIN_ACTION)
//            {
//               System.out.println("Offer failed because of an administration action in the system");
//            }
//            else if (result == Publication.CLOSED)
//            {
//               System.out.println("Offer failed publication is closed");
//               return;
//            }
//            else if (result == Publication.MAX_POSITION_EXCEEDED)
//            {
//               System.out.println("Offer failed due to publication reaching max position");
//               return;
//            }
//            else
//            {
//               System.out.println("Offer failed due to unknown reason");
//            }
//         }
//         else
//         {
//            System.out.println("yay!");
//         }
//
//         if (!publication.isConnected())
//         {
//            System.out.println("No active subscribers detected");
//         }
//
//         Thread.sleep(TimeUnit.SECONDS.toMillis(1));
//
//         System.out.println("Done sending.");
//
//         if (lingerTimeoutMs > 0)
//         {
//            System.out.println("Lingering for " + lingerTimeoutMs + " milliseconds...");
//            Thread.sleep(lingerTimeoutMs);
//         }
//      }
//
//      CloseHelper.quietClose(driver);
//   }
//
//   private static Thread startSubscriber() {
//      return new Thread(() ->
//      {
//         final int fragmentCountLimit = 10;
//
//         System.out.println("Subscribing to " + CHANNEL + " on stream Id " + PUT_STREAM);
//
//         final MediaDriver driver = MediaDriver.launchEmbedded();
//
//         final Aeron.Context ctx = new Aeron.Context()
//            .availableImageHandler(SamplesUtil::printAvailableImage)
//            .unavailableImageHandler(SamplesUtil::printUnavailableImage);
//         // Hook with embedded media driver
//         ctx.aeronDirectoryName(driver.aeronDirectoryName());
//
//         final FragmentHandler fragmentHandler = SamplesUtil.printStringMessage(PUT_STREAM);
//         final AtomicBoolean running = new AtomicBoolean(true);
//
//         // Create an Aeron instance using the configured Context and create a
//         // Subscription on that instance that subscribes to the configured
//         // channel and stream ID.
//         // The Aeron and Subscription classes implement "AutoCloseable" and will automatically
//         // clean up resources when this try block is finished
//         try (Aeron aeron = Aeron.connect(ctx);
//              Subscription subscription = aeron.addSubscription(CHANNEL, PUT_STREAM))
//         {
//            SamplesUtil.subscriberLoop(fragmentHandler, fragmentCountLimit, running).accept(subscription);
//
//            System.out.println("Shutting down...");
//         }
//      });
//
//   }

}
