package nonblocking.aeron;

import io.aeron.ImageFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import nonblocking.BinaryCache;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.collections.MutableInteger;
import org.agrona.collections.MutableLong;
import org.agrona.collections.MutableReference;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NanoClock;

import static io.aeron.Aeron.NULL_VALUE;
import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_OUT_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;

public final class AeronCache implements BinaryCache, AutoCloseable
{
    public static final byte[] EMPTY_BYTES = new byte[0];

    private final CacheProxy cacheProxy;
    private final NanoClock nanoClock;
    private final long messageTimeoutNs;
    private final Subscription subs;

    public AeronCache()
    {
        this.cacheProxy = new CacheProxy();
        this.nanoClock = AERON.aeron.context().nanoClock();
        this.messageTimeoutNs = Constants.MESSAGE_TIMEOUT_NS;
        this.subs = AERON.aeron.addSubscription(CHANNEL, CACHE_OUT_STREAM);
    }

    @Override
    public boolean putIfAbsent(byte[] key, byte[] value)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put it
        if (!cacheProxy.putIfAbsent(key, value, correlationId))
            return false;

        // If deadline for wait passed, return false to indicate no put
        return pollForSuccess(correlationId);
    }

    @Override
    public byte[] getOrNull(byte[] key)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so not found
        if (!cacheProxy.getOrNull(key, correlationId))
            return null;

        return pollForBytes(correlationId);
    }

    @Override
    public boolean put(byte[] key, byte[] value)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put
        if (!cacheProxy.put(key, value, correlationId))
            return false;

        // If deadline for wait passed, return false to indicate no put
        return pollForEmpty(correlationId);
    }

    @Override
    public void invalidateAll()
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put
        if (!cacheProxy.invalidateAll(correlationId))
            return;

        pollForEmpty(correlationId);
    }

    @Override
    public void invalidate(byte[] key)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put
        if (!cacheProxy.invalidate(key, correlationId))
            return;

        pollForEmpty(correlationId);
    }

    @Override
    public long count()
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put
        if (!cacheProxy.count(correlationId))
            return NULL_VALUE;

        return pollForLong(correlationId);
    }

    private boolean pollForSuccess(final long correlationId)
    {
        final CorrelatingSuccessHandler handler =
            new CorrelatingSuccessHandler(correlationId);

        final IdleStrategy idleStrategy = Constants.pollIdleStrategy();

        final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
        do
        {
            if (subs.poll(handler, 1) == 0)
            {
                if (Thread.interrupted())
                    LangUtil.rethrowUnchecked(new InterruptedException());

                if (deadlineNs - nanoClock.nanoTime() < 0)
                {
                    return false;
                }

                idleStrategy.idle();
            }
        } while (NULL_VALUE == handler.result.intValue());

        return handler.result.intValue() == 1;
    }

    private byte[] pollForBytes(final long correlationId)
    {
        final CorrelatingBytesHandler handler =
            new CorrelatingBytesHandler(correlationId);

        // byte[] value could be big, so buffer it if does not fit MTU
        final FragmentHandler assembler = new ImageFragmentAssembler(handler);

        final IdleStrategy idleStrategy = Constants.pollIdleStrategy();

        final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
        do
        {
            if (subs.poll(assembler, 1) == 0)
            {
                if (Thread.interrupted())
                    LangUtil.rethrowUnchecked(new InterruptedException());

                if (deadlineNs - nanoClock.nanoTime() < 0) return null;

                idleStrategy.idle();
            }
        } while (null == handler.result.get());

        final byte[] bytes = handler.result.get();
        return bytes.length == 0 ? null : bytes;
    }

    private boolean pollForEmpty(final long correlationId)
    {
        final CorrelatingEmptyHandler handler = new CorrelatingEmptyHandler();

        final IdleStrategy idleStrategy = Constants.pollIdleStrategy();

        final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
        do
        {
            if (subs.poll(handler, 1) == 0)
            {
                if (Thread.interrupted())
                    LangUtil.rethrowUnchecked(new InterruptedException());

                if (deadlineNs - nanoClock.nanoTime() < 0)
                {
                    return false;
                }

                idleStrategy.idle();
            }
        } while (correlationId != handler.correlationId.longValue());

        return true;
    }

    private long pollForLong(final long correlationId)
    {
        final CorrelatingLongHandler handler =
            new CorrelatingLongHandler(correlationId);

        final IdleStrategy idleStrategy = Constants.pollIdleStrategy();

        final long deadlineNs = nanoClock.nanoTime() + messageTimeoutNs;
        do
        {
            if (subs.poll(handler, 1) == 0)
            {
                if (Thread.interrupted())
                    LangUtil.rethrowUnchecked(new InterruptedException());

                if (deadlineNs - nanoClock.nanoTime() < 0)
                {
                    return NULL_VALUE;
                }

                idleStrategy.idle();
            }
        } while (NULL_VALUE == handler.result.longValue());

        return handler.result.longValue();
    }

    @Override
    public void close()
    {
        subs.close();
    }

    static final class CorrelatingSuccessHandler implements FragmentHandler
    {
        private final MutableInteger result = new MutableInteger(NULL_VALUE);
        private final long correlationId;

        CorrelatingSuccessHandler(long correlationId)
        {
            this.correlationId = correlationId;
        }

        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
        {
            int index = offset;

            long id = buffer.getLong(index);
            index += 8;

            if (correlationId == id)
            {
                result.set(buffer.getByte(index));
            }
        }
    }

    static final class CorrelatingBytesHandler implements FragmentHandler
    {
        private final MutableReference<byte[]> result = new MutableReference<>();
        private final long correlationId;

        CorrelatingBytesHandler(long correlationId)
        {
            this.correlationId = correlationId;
        }

        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
        {
            int index = offset;

            long id = buffer.getLong(index);
            index += 8;

            if (correlationId == id)
            {
                int len = buffer.getInt(index);
                index += 4;

                if (len == 0) result.set(EMPTY_BYTES);

                byte[] value = new byte[len];
                buffer.getBytes(index, value, 0, value.length);
                result.set(value);
            }
        }
    }

    static final class CorrelatingEmptyHandler implements FragmentHandler
    {
        private final MutableLong correlationId = new MutableLong(NULL_VALUE);

        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
        {
            long id = buffer.getLong(offset);
            correlationId.set(id);
        }
    }

    static final class CorrelatingLongHandler implements FragmentHandler
    {
        private final MutableLong result = new MutableLong(NULL_VALUE);
        private final long correlationId;

        CorrelatingLongHandler(long correlationId)
        {
            this.correlationId = correlationId;
        }

        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
        {
            int index = offset;

            long id = buffer.getLong(index);
            index += 8;

            if (correlationId == id)
            {
                result.set(buffer.getLong(index));
            }
        }
    }
}
