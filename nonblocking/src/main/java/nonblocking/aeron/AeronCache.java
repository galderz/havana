package nonblocking.aeron;

import io.aeron.ImageFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import nonblocking.BinaryCache;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.collections.MutableInteger;
import org.agrona.collections.MutableReference;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.NanoClock;

import static io.aeron.Aeron.NULL_VALUE;
import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_OUT_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;

public final class AeronCache implements BinaryCache
{

    public static final byte[] EMPTY_BYTES = new byte[0];

    private final CacheProxy cacheProxy;
    private final NanoClock nanoClock;
    private final long messageTimeoutNs;

    public AeronCache()
    {
        this.cacheProxy = new CacheProxy();
        this.nanoClock = AERON.aeron.context().nanoClock();
        this.messageTimeoutNs = Constants.MESSAGE_TIMEOUT_NS;
    }

    @Override
    public boolean putIfAbsent(byte[] key, byte[] value)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so could not put it
        if (!cacheProxy.putIfAbsent(key, value, correlationId)) return false;

        // If deadline for wait passed, return false to indicate no put
        return pollForSuccess(correlationId);
    }

    @Override
    public byte[] getOrNull(byte[] key)
    {
        final long correlationId = AERON.aeron.nextCorrelationId();

        // Could not publish message, so not found
        if (!cacheProxy.getOrNull(key, correlationId)) return null;

        return pollForBytes(correlationId);
    }

    private boolean pollForSuccess(final long correlationId)
    {
        try (Subscription subs = AERON.aeron.addSubscription(CHANNEL, CACHE_OUT_STREAM))
        {
            final CorrelatingSuccessHandler handler =
                new CorrelatingSuccessHandler(correlationId);

            final IdleStrategy idleStrategy = Constants.cacheOutIdleStrategy();

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
    }

    private byte[] pollForBytes(final long correlationId)
    {
        try (Subscription subs = AERON.aeron.addSubscription(CHANNEL, CACHE_OUT_STREAM))
        {
            final CorrelatingBytesHandler handler =
                new CorrelatingBytesHandler(correlationId);

            // byte[] value could be big, so buffer it if does not fit MTU
            final FragmentHandler assembler = new ImageFragmentAssembler(handler);

            final IdleStrategy idleStrategy = Constants.cacheOutIdleStrategy();

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


}
