package util.sampling.v3;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantLock;

import static util.sampling.v3.SampleArray.clearSample;
import static util.sampling.v3.SampleArray.getPrevious;
import static util.sampling.v3.SampleArray.getSpan;
import static util.sampling.v3.SampleArray.setSample;
import static util.sampling.v3.SampleArray.setSpan;

public class Sampler
{
    final SampleArray samples;
    final SampleQueue queue;
    final SampleList list;
    final ReentrantLock lock;

    Sampler(int size)
    {
        this.samples = new SampleArray(size);
        this.queue = new SampleQueue(this.samples);
        this.list = new SampleList(this.samples);
        this.lock = new ReentrantLock();
    }

    /**
     * Protected by lock
     */
    boolean sample(WeakReference<?> obj, long allocatedSize, long allocatedTime)
    {
        final boolean success = lock.tryLock();
        if (!success)
        {
            return false;
        }

        try
        {
            if (queue.isFull())
            {
                if (getSpan(queue.peek()) > allocatedSize)
                {
                    return false;
                }

                evict();
            }

            store(obj, allocatedSize, allocatedTime);
            return true;
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Protected by lock
     */
    void remove(Object[] sample)
    {
        while (!lock.tryLock()) {}

        try
        {
            final Object[] prev = getPrevious(sample);
            if (prev != null)
            {
                queue.remove(prev);
                setSpan(getSpan(sample) + getSpan(prev), prev);
                queue.push(prev);
            }
            queue.remove(sample);
            list.remove(sample);
            clearSample(sample);
        }
        finally
        {
            lock.unlock();
        }
    }

    private void evict()
    {
        final Object[] head = queue.poll();
        list.remove(head);
        clearSample(head);
    }

    private void store(WeakReference<?> ref, long allocatedSize, long allocatedTime)
    {
        final int index = queue.getCount();
        final Object[] sample = samples.getSample(index);
        setSample(ref, allocatedSize, allocatedTime, sample);
        queue.push(sample);
        list.prepend(sample);
    }
}
