package j.aeron;

import io.aeron.CommonContext;
import io.aeron.Image;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.protocol.HeaderFlyweight;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.concurrent.BusySpinIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.status.CountersReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static io.aeron.CncFileDescriptor.*;
import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/**
 * Utility functions for the samples.
 */
public class SamplesUtil
{
   /**
    * Return a reusable, parametrised event loop that calls a default idler when no messages are received
    *
    * @param fragmentHandler to be called back for each message.
    * @param limit           passed to {@link Subscription#poll(FragmentHandler, int)}
    * @param running         indication for loop
    * @return loop function
    */
   public static Consumer<Subscription> subscriberLoop(
      final FragmentHandler fragmentHandler, final int limit, final AtomicBoolean running)
   {
      final IdleStrategy idleStrategy = new BusySpinIdleStrategy();

      return subscriberLoop(fragmentHandler, limit, running, idleStrategy);
   }

   /**
    * Return a reusable, parametrised event loop that calls and idler when no messages are received
    *
    * @param fragmentHandler to be called back for each message.
    * @param limit           passed to {@link Subscription#poll(FragmentHandler, int)}
    * @param running         indication for loop
    * @param idleStrategy    to use for loop
    * @return loop function
    */
   public static Consumer<Subscription> subscriberLoop(
      final FragmentHandler fragmentHandler,
      final int limit,
      final AtomicBoolean running,
      final IdleStrategy idleStrategy)
   {
      return
         (subscription) ->
         {
            try
            {
               while (running.get())
               {
                  final int fragmentsRead = subscription.poll(fragmentHandler, limit);
                  idleStrategy.idle(fragmentsRead);
               }
            }
            catch (final Exception ex)
            {
               LangUtil.rethrowUnchecked(ex);
            }
         };
   }

   /**
    * Return a reusable, parametrised {@link FragmentHandler} that prints to stdout
    *
    * @param streamId to show when printing
    * @return subscription data handler function that prints the message contents
    */
   public static FragmentHandler printStringMessage(final int streamId)
   {
      return (buffer, offset, length, header) ->
      {
         final byte[] data = new byte[length];
         buffer.getBytes(offset, data);

         System.out.println(String.format(
            "Message to stream %d from session %d (%d@%d) <<%s>>",
            streamId, header.sessionId(), length, offset, new String(data)));
      };
   }

   /**
    * Return a reusable, parametrised {@link FragmentHandler} that calls into a
    * {@link RateReporter}.
    *
    * @param reporter for the rate
    * @return {@link FragmentHandler} that records the rate information
    */
   public static FragmentHandler rateReporterHandler(final RateReporter reporter)
   {
      return (buffer, offset, length, header) -> reporter.onMessage(1, length);
   }

   /**
    * Generic error handler that just prints message to stdout.
    *
    * @param channel   for the error
    * @param streamId  for the error
    * @param sessionId for the error, if source
    * @param message   indicating what the error was
    * @param cause     of the error
    */
   @SuppressWarnings("unused")
   public static void printError(
      final String channel,
      final int streamId,
      final int sessionId,
      final String message,
      final HeaderFlyweight cause)
   {
      System.out.println(message);
   }

   /**
    * Print the rates to stdout
    *
    * @param messagesPerSec being reported
    * @param bytesPerSec    being reported
    * @param totalMessages  being reported
    * @param totalBytes     being reported
    */
   public static void printRate(
      final double messagesPerSec,
      final double bytesPerSec,
      final long totalMessages,
      final long totalBytes)
   {
      System.out.println(String.format(
         "%.02g msgs/sec, %.02g payload bytes/sec, totals %d messages %d MB",
         messagesPerSec, bytesPerSec, totalMessages, totalBytes / (1024 * 1024)));
   }

   /**
    * Print the information for an available image to stdout.
    *
    * @param image that has been created
    */
   public static void printAvailableImage(final Image image)
   {
      final Subscription subscription = image.subscription();
      System.out.println(String.format(
         "Available image on %s streamId=%d sessionId=%d from %s",
         subscription.channel(), subscription.streamId(), image.sessionId(), image.sourceIdentity()));
   }

   /**
    * Print the information for an unavailable image to stdout.
    *
    * @param image that has gone inactive
    */
   public static void printUnavailableImage(final Image image)
   {
      final Subscription subscription = image.subscription();
      System.out.println(String.format(
         "Unavailable image on %s streamId=%d sessionId=%d",
         subscription.channel(), subscription.streamId(), image.sessionId()));
   }

   /**
    * Map an existing file as a read only buffer.
    *
    * @param location of file to map.
    * @return the mapped file.
    */
   public static MappedByteBuffer mapExistingFileReadOnly(final File location)
   {
      if (!location.exists())
      {
         final String msg = "file not found: " + location.getAbsolutePath();
         throw new IllegalStateException(msg);
      }

      MappedByteBuffer mappedByteBuffer = null;
      try (RandomAccessFile file = new RandomAccessFile(location, "r");
           FileChannel channel = file.getChannel())
      {
         mappedByteBuffer = channel.map(READ_ONLY, 0, channel.size());
      }
      catch (final IOException ex)
      {
         LangUtil.rethrowUnchecked(ex);
      }

      return mappedByteBuffer;
   }

   /**
    * Map an existing CnC file.
    *
    * @return the {@link CountersReader} over the CnC file.
    */
   public static CountersReader mapCounters()
   {
      final File cncFile = CommonContext.newDefaultCncFile();
      System.out.println("Command `n Control file " + cncFile);

      final MappedByteBuffer cncByteBuffer = mapExistingFileReadOnly(cncFile);
      final DirectBuffer cncMetaData = createMetaDataBuffer(cncByteBuffer);
      final int cncVersion = cncMetaData.getInt(cncVersionOffset(0));

      if (CNC_VERSION != cncVersion)
      {
         throw new IllegalStateException("CnC version not supported: file version=" + cncVersion);
      }

      return new CountersReader(
         createCountersMetaDataBuffer(cncByteBuffer, cncMetaData),
         createCountersValuesBuffer(cncByteBuffer, cncMetaData));
   }
}