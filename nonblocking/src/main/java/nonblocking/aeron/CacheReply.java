package nonblocking.aeron;

import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.IdleStrategy;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static nonblocking.aeron.AeronSystem.AERON;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

class CacheReply
{

    private final Publication publication;
    private final UnsafeBuffer buffer;
    private final IdleStrategy idleStrategy;

    CacheReply()
    {
        // TODO should be exclusive publication
        publication = AERON.aeron
                .addPublication(Constants.CHANNEL, Constants.CACHE_OUT_STREAM);

        final ByteBuffer byteBuffer =
                BufferUtil.allocateDirectAligned(
                        publication.maxMessageLength(), CACHE_LINE_LENGTH);

        buffer = new UnsafeBuffer(byteBuffer);
        idleStrategy = Constants.CACHE_REPLY_IDLE_STRATEGY;
    }

    // TODO use return and maybe log it?
    boolean completeSuccess(boolean success, long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, (byte) (success ? 1 : 0));
        index += 1;

        return offer(index);
    }

    // TODO use return and maybe log it?
    boolean completeBytes(byte[] bytes, long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        final int length = bytes == null ? 0 : bytes.length;
        buffer.putInt(index, length);
        index += 4;

        if (length > 0)
        {
            buffer.putBytes(index, bytes);
            index += bytes.length;
        }

        return offer(index);
    }

    // TODO use return and maybe log it?
    boolean completeEmpty(long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        return offer(index);
    }

    // TODO use return and maybe log it?
    boolean completeLong(long correlationId, long value)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putLong(index, value);
        index += 8;

        return offer(index);
    }

    private boolean offer(final int length)
    {
        int attempts = Constants.REPLY_ATTEMPTS;

        idleStrategy.reset();

        do
        {
            // TODO use tryClaim for small messages (under MTU)
            final long result = publication.offer(buffer, 0, length);
            if (result > 0)
            {
                // System.out.println("Attempts: " + attempts);
                return true;
            }

            idleStrategy.idle();
        } while (--attempts > 0);

        System.out.println("No reply attempts left");

        return false;
    }
}
