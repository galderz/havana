package nonblocking.aeron;

import io.aeron.CommonContext;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;

import java.util.concurrent.TimeUnit;

import static io.aeron.driver.Configuration.IDLE_MAX_PARK_NS;
import static io.aeron.driver.Configuration.IDLE_MAX_SPINS;
import static io.aeron.driver.Configuration.IDLE_MAX_YIELDS;
import static io.aeron.driver.Configuration.IDLE_MIN_PARK_NS;

final class Constants
{
    static final String CHANNEL = CommonContext.IPC_CHANNEL;
    static final int CACHE_IN_STREAM = 10;
    static final int CACHE_OUT_STREAM = 20;

    static final int FRAGMENT_LIMIT = 10;

    // TODO make it configurable
    static final int REPLY_ATTEMPTS = 1 << 5;

    // TODO make it configurable
    static final int SEND_ATTEMPTS = 1 << 3;

    // TODO make it configurable
    static final long MESSAGE_TIMEOUT_NS = TimeUnit.SECONDS.toNanos(5);

    // TODO make it configurable
    static final IdleStrategy CACHE_IN_IDLE_STRATEGY =
        new BackoffIdleStrategy(1, 1, 1, 1);

    // TODO make it configurable
    static IdleStrategy pollIdleStrategy()
    {
        return new BackoffIdleStrategy(IDLE_MAX_SPINS, IDLE_MAX_YIELDS, IDLE_MIN_PARK_NS, IDLE_MAX_PARK_NS);
    }

    // TODO make it configurable (default based on unit testing, adjust for prod)
    static final IdleStrategy CACHE_REPLY_IDLE_STRATEGY =
        new BackoffIdleStrategy(1, 1, 1, 1);

    static final byte PUT_IF_ABSENT = 0;
    static final byte GET_OR_NULL = 1;
    static final byte PUT = 2;
    static final byte INVALIDATE_ALL = 3;
    static final byte INVALIDATE = 4;
    static final byte COUNT = 5;

}
