package util.sampling.v3;

import java.util.concurrent.atomic.AtomicLong;

public class SpinLock
{
    private static final int UNHELD = -1;

    private final AtomicLong lock = new AtomicLong(UNHELD);

    public boolean tryLock()
    {
        final long threadId = Thread.currentThread().getId();
        if (!lock.compareAndSet(UNHELD, threadId))
        {
            if (threadId == lock.get())
            {
                throw new IllegalStateException("This lock is not reentrant");
            }

            return false;
        }

        return true;
    }

    public void lock()
    {
        final long threadId = Thread.currentThread().getId();
        while (!lock.compareAndSet(UNHELD, threadId))
        {
            if (threadId == lock.get())
            {
                throw new IllegalStateException("This lock is not reentrant");
            }
        }
    }

    public void unlock()
    {
        final long threadId = Thread.currentThread().getId();
        if (!lock.compareAndSet(threadId, UNHELD))
        {
            throw new IllegalStateException("Unlock called without holding the lock");
        }
    }
}
