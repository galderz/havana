package nonblocking.aeron;

import io.aeron.CommonContext;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;

import java.util.concurrent.TimeUnit;

import static io.aeron.driver.Configuration.IDLE_MAX_PARK_NS;
import static io.aeron.driver.Configuration.IDLE_MAX_SPINS;
import static io.aeron.driver.Configuration.IDLE_MAX_YIELDS;
import static io.aeron.driver.Configuration.IDLE_MIN_PARK_NS;

final class Constants {

   static final String CHANNEL = CommonContext.IPC_CHANNEL;
   static final int CACHE_IN_STREAM = 10;
   static final int CACHE_OUT_STREAM = 20;

   static final int FRAGMENT_LIMIT = 10;

   // TODO make it configurable
   static final int REPLY_ATTEMPTS = 1_000;

   // TODO make it configurable
   static final long MESSAGE_TIMEOUT_NS = TimeUnit.SECONDS.toNanos(5);

   // TODO make it configurable
   static final IdleStrategy CACHE_IN_IDLE_STRATEGY =
      new BackoffIdleStrategy(1, 1, 1, 1);

   // TODO make it configurable
   static IdleStrategy cacheOutIdleStrategy() {
      return new BackoffIdleStrategy(
         IDLE_MAX_SPINS, IDLE_MAX_YIELDS, IDLE_MIN_PARK_NS, IDLE_MAX_PARK_NS);
   }

}
