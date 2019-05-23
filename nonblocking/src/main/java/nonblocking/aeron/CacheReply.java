package nonblocking.aeron;

import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static nonblocking.aeron.AeronSystem.AERON;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

class CacheReply
{

    private final Publication publication;
    private final UnsafeBuffer buffer;

    CacheReply()
    {
        // TODO should be exclusive publication
        publication = AERON.aeron
                .addPublication(Constants.CHANNEL, Constants.CACHE_OUT_STREAM);

        final ByteBuffer byteBuffer =
                BufferUtil.allocateDirectAligned(
                        publication.maxMessageLength(), CACHE_LINE_LENGTH);

        buffer = new UnsafeBuffer(byteBuffer);
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

    private boolean offer(final int length)
    {
        int attempts = Constants.REPLY_ATTEMPTS;
        do
        {
            // TODO use tryClaim for small messages (under MTU)
            final long result = publication.offer(buffer, 0, length);
            if (result > 0)
            {
                return true;
            }
        } while (--attempts > 0);

        System.out.println("No attempts left");

        return false;
    }

}
