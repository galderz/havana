package j.aeron;

import io.aeron.Aeron;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.FragmentHandler;
import org.agrona.CloseHelper;
import org.agrona.concurrent.SigInt;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This is a Basic Aeron subscriber application
 * The application subscribes to a default channel and stream ID.  These defaults can
 * be overwritten by changing their value in {@link SampleConfiguration} or by
 * setting their corresponding Java system properties at the command line, e.g.:
 * -Daeron.sample.channel=aeron:udp?endpoint=localhost:5555 -Daeron.sample.streamId=20
 * This application only handles non-fragmented data. A DataHandler method is called
 * for every received message or message fragment.
 * For an example that implements reassembly of large, fragmented messages, see
 * {link@ MultipleSubscribersWithFragmentAssembly}.
 */
public class BasicSubscriber
{
   private static final int STREAM_ID = SampleConfiguration.STREAM_ID;

   // ipc does not work for some reason...
   private static final String CHANNEL = SampleConfiguration.CHANNEL;
   //private static final String CHANNEL = "aeron:ipc";

   private static final int FRAGMENT_COUNT_LIMIT = SampleConfiguration.FRAGMENT_COUNT_LIMIT;

   private static final boolean EMBEDDED_MEDIA_DRIVER = true;
   //private static final boolean EMBEDDED_MEDIA_DRIVER = SampleConfiguration.EMBEDDED_MEDIA_DRIVER;

   public static void main(final String[] args)
   {
      System.out.println("Subscribing to " + CHANNEL + " on stream Id " + STREAM_ID);

      final MediaDriver driver = EMBEDDED_MEDIA_DRIVER ? MediaDriver.launchEmbedded() : null;
      final Aeron.Context ctx = new Aeron.Context()
         .availableImageHandler(SamplesUtil::printAvailableImage)
         .unavailableImageHandler(SamplesUtil::printUnavailableImage);

      if (EMBEDDED_MEDIA_DRIVER)
      {
         ctx.aeronDirectoryName(driver.aeronDirectoryName());
      }

      final FragmentHandler fragmentHandler = SamplesUtil.printStringMessage(STREAM_ID);
      final AtomicBoolean running = new AtomicBoolean(true);

      // Register a SIGINT handler for graceful shutdown.
      SigInt.register(() -> running.set(false));

      // Create an Aeron instance using the configured Context and create a
      // Subscription on that instance that subscribes to the configured
      // channel and stream ID.
      // The Aeron and Subscription classes implement "AutoCloseable" and will automatically
      // clean up resources when this try block is finished
      try (Aeron aeron = Aeron.connect(ctx);
           Subscription subscription = aeron.addSubscription(CHANNEL, STREAM_ID))
      {
         SamplesUtil.subscriberLoop(fragmentHandler, FRAGMENT_COUNT_LIMIT, running).accept(subscription);

         System.out.println("Shutting down...");
      }

      CloseHelper.quietClose(driver);
   }
}
