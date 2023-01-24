package util.sampling.v3;

import java.util.concurrent.atomic.AtomicLong;

public class SpinLock
{
    private static final int NOT_HELD = -1;

    private final AtomicLong lock = new AtomicLong(NOT_HELD);

    public boolean tryLock()
    {
        final long threadId = Thread.currentThread().getId();
        if (!lock.compareAndSet(NOT_HELD, threadId))
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
        while (!lock.compareAndSet(NOT_HELD, threadId))
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
        if (!lock.compareAndSet(threadId, NOT_HELD))
        {
            throw new IllegalStateException("Unlock called without holding the lock");
        }
    }
}
