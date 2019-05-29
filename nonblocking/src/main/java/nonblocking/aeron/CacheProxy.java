package nonblocking.aeron;

import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static nonblocking.aeron.AeronSystem.AERON;
import static nonblocking.aeron.Constants.CACHE_IN_STREAM;
import static nonblocking.aeron.Constants.CHANNEL;
import static nonblocking.aeron.Constants.COUNT;
import static nonblocking.aeron.Constants.GET_OR_NULL;
import static nonblocking.aeron.Constants.INVALIDATE;
import static nonblocking.aeron.Constants.INVALIDATE_ALL;
import static nonblocking.aeron.Constants.PUT;
import static nonblocking.aeron.Constants.PUT_IF_ABSENT;
import static nonblocking.aeron.Constants.SEND_ATTEMPTS;
import static org.agrona.BitUtil.CACHE_LINE_LENGTH;

class CacheProxy
{
    private final Publication publication;
    private final UnsafeBuffer buffer;

    CacheProxy()
    {
        publication = AERON.aeron.addPublication(CHANNEL, CACHE_IN_STREAM);

        final ByteBuffer byteBuffer =
            BufferUtil.allocateDirectAligned(
                publication.maxMessageLength(), CACHE_LINE_LENGTH);

        buffer = new UnsafeBuffer(byteBuffer);
    }

    boolean putIfAbsent(
        final byte[] key,
        final byte[] value,
        final long correlationId)
    {
        return offerKeyValue(key, value, correlationId, PUT_IF_ABSENT);
    }

    boolean put(
        final byte[] key,
        final byte[] value,
        final long correlationId)
    {
        return offerKeyValue(key, value, correlationId, PUT);
    }

    private boolean offerKeyValue(
        final byte[] key,
        final byte[] value,
        final long correlationId,
        final byte method)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, method);
        index++;

        buffer.putInt(index, key.length);
        index += 4;

        buffer.putBytes(index, key);
        index += key.length;

        buffer.putInt(index, value.length);
        index += 4;

        buffer.putBytes(index, value);
        index += value.length;

        return offer(index);
    }

    boolean getOrNull(final byte[] key, final long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, GET_OR_NULL);
        index++;

        buffer.putInt(index, key.length);
        index += 4;

        buffer.putBytes(index, key);
        index += key.length;

        return offer(index);
    }

    boolean invalidateAll(final long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, INVALIDATE_ALL);
        index++;

        return offer(index);
    }

    boolean invalidate(final byte[] key, final long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, INVALIDATE);
        index++;

        buffer.putInt(index, key.length);
        index += 4;

        buffer.putBytes(index, key);
        index += key.length;

        return offer(index);
    }

    boolean count(long correlationId)
    {
        int index = 0;

        buffer.putLong(index, correlationId);
        index += 8;

        buffer.putByte(index, COUNT);
        index++;

        return offer(index);
    }

    private boolean offer(final int length)
    {
        int attempts = SEND_ATTEMPTS;
        do
        {
            final long result = publication.offer(buffer, 0, length);
            if (result > 0) {
                return true;
            }
        } while (--attempts > 0);

        return false;
    }
}
