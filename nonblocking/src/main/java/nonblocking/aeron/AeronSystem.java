package nonblocking.aeron;

import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import io.aeron.driver.ThreadingMode;
import org.agrona.concurrent.NoOpIdleStrategy;

import java.util.concurrent.atomic.AtomicBoolean;

public enum AeronSystem implements AutoCloseable
{
    AERON;

    // TODO volatile?
    final AtomicBoolean running = new AtomicBoolean(true);
    final Aeron aeron;

    private final MediaDriver driver;

    AeronSystem()
    {
        final MediaDriver.Context ctx = new MediaDriver.Context()
            .threadingMode(ThreadingMode.SHARED)
            .dirDeleteOnStart(true)
            .sharedIdleStrategy(new NoOpIdleStrategy());

        driver = MediaDriver.launch(ctx);
        aeron = io.aeron.Aeron.connect();
    }

    @Override
    public void close()
    {
        AERON.running.set(false);
        aeron.close();
        driver.close();
    }

}
