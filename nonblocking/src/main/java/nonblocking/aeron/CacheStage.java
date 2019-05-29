package nonblocking.aeron;

import io.aeron.ImageFragmentAssembler;
import io.aeron.Subscription;
import io.aeron.logbuffer.FragmentHandler;
import io.aeron.logbuffer.Header;
import nonblocking.BinaryStore;
import org.agrona.DirectBuffer;
import org.agrona.LangUtil;
import org.agrona.concurrent.IdleStrategy;

import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_IN_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;

public class CacheStage implements Runnable, AutoCloseable
{
    private final Subscription subscription;
    private final Thread thread;

    public CacheStage()
    {
        this.subscription = AERON.aeron.addSubscription(CHANNEL, CACHE_IN_STREAM);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run()
    {
        final IdleStrategy idleStrategy = Constants.CACHE_IN_IDLE_STRATEGY;

        // Requests could have arbitrary length, so wait for complete requests
        final FragmentHandler fragmentHandler = new ImageFragmentAssembler(new CacheFragmentHandler());

        try
        {
            while (AERON.running.get())
            {
                final int fragmentsRead =
                    subscription.poll(fragmentHandler, Constants.FRAGMENT_LIMIT);

                idleStrategy.idle(fragmentsRead);
            }
        } catch (final Exception ex)
        {
            // TODO propagate error somehow
            LangUtil.rethrowUnchecked(ex);
        }
    }

    @Override
    public void close() throws InterruptedException
    {
        thread.join();
    }

    private static final class CacheFragmentHandler implements FragmentHandler
    {
        private final BinaryStore store = new BinaryStore();
        private final CacheReply reply = new CacheReply();

        @Override
        public void onFragment(DirectBuffer buffer, int offset, int length, Header header)
        {
            int index = offset;

            long correlationId = buffer.getLong(index);
            index += 8;

            byte method = buffer.getByte(index);
            index++;

            switch (method)
            {
                case Constants.PUT_IF_ABSENT:
                    putIfAbsent(correlationId, buffer, index);
                    break;
                case Constants.GET_OR_NULL:
                    getOrNull(correlationId, buffer, index);
                    break;
                case Constants.PUT:
                    put(correlationId, buffer, index);
                    break;
                case Constants.INVALIDATE_ALL:
                    invalidateAll(correlationId);
                case Constants.INVALIDATE:
                    invalidate(correlationId, buffer, index);
                    break;
                case Constants.COUNT:
                    count(correlationId);
                    break;
            }

        }

        private void putIfAbsent(long correlationId, DirectBuffer buffer, int index)
        {
            int keyLength = buffer.getInt(index);
            index += 4;

            byte[] key = new byte[keyLength];
            buffer.getBytes(index, key, 0, key.length);
            index += key.length;

            int valueLength = buffer.getInt(index);
            index += 4;

            byte[] value = new byte[valueLength];
            buffer.getBytes(index, value, 0, value.length);

            boolean success = store.putIfAbsent(key, value);
            reply.completeSuccess(success, correlationId);
        }

        private void getOrNull(long correlationId, DirectBuffer buffer, int index)
        {
            int keyLength = buffer.getInt(index);
            index += 4;

            byte[] key = new byte[keyLength];
            buffer.getBytes(index, key, 0, key.length);
            index += key.length;

            final byte[] value = store.getOrNull(key);
            reply.completeBytes(value, correlationId);
        }

        private void put(long correlationId, DirectBuffer buffer, int index)
        {
            int keyLength = buffer.getInt(index);
            index += 4;

            byte[] key = new byte[keyLength];
            buffer.getBytes(index, key, 0, key.length);
            index += key.length;

            int valueLength = buffer.getInt(index);
            index += 4;

            byte[] value = new byte[valueLength];
            buffer.getBytes(index, value, 0, value.length);

            store.put(key, value);
            reply.completeEmpty(correlationId);
        }

        private void invalidateAll(long correlationId)
        {
            store.clear();
            reply.completeEmpty(correlationId);
        }

        private void invalidate(long correlationId, DirectBuffer buffer, int index)
        {
            int keyLength = buffer.getInt(index);
            index += 4;

            byte[] key = new byte[keyLength];
            buffer.getBytes(index, key, 0, key.length);
            index += key.length;

            store.remove(key);
            reply.completeEmpty(correlationId);
        }

        private void count(long correlationId)
        {
            long count = store.size();
            reply.completeLong(correlationId, count);
        }
    }
}
