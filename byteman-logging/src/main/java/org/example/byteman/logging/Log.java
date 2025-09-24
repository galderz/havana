package org.example.byteman.logging;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.io.IOException;

public class Log implements AutoCloseable
{
    private final Disruptor<Event> disruptor;
    private final RingBuffer<Event> ringBuffer;
    private final LogConsumer logConsumer;

    public Log()
    {
        int bufferSize = 8192;

        logConsumer = new LogConsumer();

        disruptor = new Disruptor<>(Event::new, bufferSize, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith(logConsumer);
        disruptor.start();

        ringBuffer = disruptor.getRingBuffer();
    }

    public void log(Context context, int count)
    {
        long sequence = ringBuffer.next();

        Event event = ringBuffer.get(sequence);
        event.context = context;
        event.count = count;

        ringBuffer.publish(sequence);
    }

    @Override
    public void close() throws IOException
    {
        logConsumer.close();
        disruptor.shutdown();
    }

    public static class Event
    {
        public Context context;
        public int count;
    }

    public enum Context
    {
        HELLO_METHOD_ENTRY, HELLO_METHOD_EXIT
    }
}
