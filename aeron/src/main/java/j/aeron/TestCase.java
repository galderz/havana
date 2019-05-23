package j.aeron;

import io.aeron.Aeron;
import io.aeron.CommonContext;
import io.aeron.ImageFragmentAssembler;
import io.aeron.Publication;
import io.aeron.Subscription;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import org.agrona.BufferUtil;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.collections.MutableInteger;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NanoClock;
import org.agrona.concurrent.NoOpIdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.aeron.Aeron.NULL_VALUE;
import static io.aeron.driver.Configuration.IDLE_MAX_PARK_NS;
import static io.aeron.driver.Configuration.IDLE_MAX_SPINS;
import static io.aeron.driver.Configuration.IDLE_MAX_YIELDS;
import static io.aeron.driver.Configuration.IDLE_MIN_PARK_NS;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

public class TestCase {

   static final AtomicBoolean RUNNING = new AtomicBoolean(true);

   public static void main(String[] args) throws InterruptedException {
      final MediaDriver.Context ctx = new MediaDriver.Context()
         .threadingMode(ThreadingMode.SHARED)
         .sharedIdleStrategy(new NoOpIdleStrategy());

      final MediaDriver driver = MediaDriver.launch(ctx);
      Aeron aeron = Aeron.connect();


      final int inStream = 10;
      final int outStream = 20;
      final Subscription backendInSubscription =
         aeron.addSubscription(CommonContext.IPC_CHANNEL, inStream);
      final Publication backendOutPublication =
         aeron.addPublication(CommonContext.IPC_CHANNEL, outStream);

      Thread t = new Thread(new BackendRunnable(backendInSubscription, backendOutPublication));
      t.start();

      final Publication frontendInPublication =
         aeron.addPublication(CommonContext.IPC_CHANNEL, inStream);

      Frontend frontend = new Frontend(aeron, frontendInPublication, outStream);
      int result = frontend.execute();
      System.out.println(result);

      result = frontend.execute();
      System.out.println(result);

      RUNNING.set(false);
      frontendInPublication.close();
      t.join();
      backendOutPublication.close();
      backendInSubscription.close();
      aeron.close();
      driver.close();
   }

   static final class Frontend {

      private final Aeron aeron;
      private final Publication publication;
      private final UnsafeBuffer publicationBuffer;
      private final int outStream;
      private final NanoClock nanoClock;

      public Frontend(Aeron aeron, Publication publication, int outStream) {
         this.aeron = aeron;
         this.publication = publication;
         this.outStream = outStream;
         this.nanoClock = aeron.context().nanoClock();

         final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
            publication.maxMessageLength(), CACHE_LINE_LENGTH);

         publicationBuffer = new UnsafeBuffer(byteBuffer);
      }

      public int execute() {
         final long deadlineNs = nanoClock.nanoTime() + TimeUnit.SECONDS.toNanos(30);
         final long correlationId = aeron.nextCorrelationId();

         int index = 0;
         publicationBuffer.putLong(index, correlationId);
         index += 8;

         long offer = publication.offer(publicationBuffer, 0, index);
         if (offer <= 0)
            throw new RuntimeException("Could not offer: " + offer);

         try (Subscription frontendOutSubscription =
            aeron.addSubscription(CommonContext.IPC_CHANNEL, outStream)) {

            final MutableInteger result = new MutableInteger(NULL_VALUE);

            final IdleStrategy idleStrategy =
               new BackoffIdleStrategy(IDLE_MAX_SPINS, IDLE_MAX_YIELDS, IDLE_MIN_PARK_NS, IDLE_MAX_PARK_NS);

            FragmentHandler handler = (buffer, offset, length, header) -> {
               int fragmentIndex = offset;

               long id = buffer.getLong(fragmentIndex);
               fragmentIndex += 8;

               if (correlationId == id) {
                  int readByte = buffer.getByte(fragmentIndex);
                  result.set(readByte);
               }
            };

            do {
               if (frontendOutSubscription.poll(handler, 1) == 0) {
                  if (Thread.interrupted())
                     LangUtil.rethrowUnchecked(new InterruptedException());

                  if (deadlineNs - nanoClock.nanoTime() < 0)
                     return -1;

                  idleStrategy.idle();
               }
            } while (NULL_VALUE == result.intValue());

            return result.intValue();
         }
      }

   }

   static final class BackendRunnable implements Runnable {

      final Subscription subscription;
      final Publication publication;

      BackendRunnable(Subscription subscription, Publication publication) {
         this.subscription = subscription;
         this.publication = publication;
      }

      @Override
      public void run() {
         final IdleStrategy idleStrategy = new BackoffIdleStrategy(1, 1, 1, 1);

         // Requests could have arbitrary length, so wait for complete requests
         final FragmentHandler fragmentHandler =
            new ImageFragmentAssembler(new BackendFragmentHandler(publication));

         try {
            while (RUNNING.get()) {
               final int fragmentsRead =
                  subscription.poll(fragmentHandler, 10);

               idleStrategy.idle(fragmentsRead);
            }
         } catch (final Exception ex) {
            LangUtil.rethrowUnchecked(ex);
         }
      }

   }

   static final class BackendFragmentHandler implements FragmentHandler {

      final Publication publication;
      final UnsafeBuffer publicationBuffer;

      final IdleStrategy retryIdleStrategy =
         new BackoffIdleStrategy(IDLE_MAX_SPINS, IDLE_MAX_YIELDS, IDLE_MIN_PARK_NS, IDLE_MAX_PARK_NS);

      BackendFragmentHandler(Publication publication) {
         this.publication = publication;

         final ByteBuffer byteBuffer = BufferUtil.allocateDirectAligned(
            publication.maxMessageLength(), CACHE_LINE_LENGTH);

         publicationBuffer = new UnsafeBuffer(byteBuffer);
      }

      @Override
      public void onFragment(DirectBuffer buffer, int offset, int length, Header header) {
         int index = offset;

         long correlationId = buffer.getLong(index);

         reply(correlationId);
      }

      private void reply(long correlationId) {
         int index = 0;

         publicationBuffer.putLong(index, correlationId);
         index += 8;

         publicationBuffer.putByte(index, (byte) 1);
         index += 1;

         offer(index);
      }

      private boolean offer(final int length) {
         retryIdleStrategy.reset();

         int attempts = 3 * 2;
         while (true) {
            System.out.println("Attempt: " + attempts);

            final long result = publication.offer(publicationBuffer, 0, length);
            if (result > 0)
               return true;

            if (result == Publication.CLOSED) {
               throw new RuntimeException("connection to the archive has been closed");
            }

            if (result == Publication.MAX_POSITION_EXCEEDED) {
               throw new RuntimeException("offer failed due to max position being reached");
            }

            if (--attempts <= 0) {
               System.out.println("No more attempts");
               return false;
            }

            retryIdleStrategy.idle();
         }
      }

   }



}
