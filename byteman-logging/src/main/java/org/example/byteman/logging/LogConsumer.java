package org.example.byteman.logging;

import com.lmax.disruptor.EventHandler;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class LogConsumer implements EventHandler<Log.Event>, AutoCloseable
{
    private FileChannel channel;
    private ByteBuffer bb;

    public LogConsumer()
    {
        try
        {
            channel = FileChannel.open(
                Files.createTempFile("example-buffer-log-", ".log")
                , StandardOpenOption.APPEND
            );
            bb = ByteBuffer.allocate(14 * 4096);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void onEvent(Log.Event event, long sequence, boolean endOfBatch) throws Exception
    {
        bb.putLong(System.currentTimeMillis());
        bb.putShort((short) event.context.ordinal());
        bb.putInt(event.count);

        if (!bb.hasRemaining())
        {
            bb.flip();
            channel.write(bb);
            bb.clear();
        }
    }

    @Override
    public void close() throws IOException
    {
        // Write any leftover elements
        if (bb.hasRemaining())
        {
            bb.flip();
            channel.write(bb);
            bb.clear();
        }
        channel.close();
    }
}
