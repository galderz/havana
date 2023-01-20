package util.sampling.v3;

import java.lang.ref.WeakReference;

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

    Sampler(int size)
    {
        this.samples = new SampleArray(size);
        this.queue = new SampleQueue(this.samples);
        this.list = new SampleList(this.samples);
    }

    void sample(WeakReference<?> obj, long allocatedSize, long allocatedTime)
    {
        if (queue.isFull())
        {
            if (getSpan(queue.peek()) > allocatedSize)
            {
                return;
            }

            evict();
        }

        store(obj, allocatedSize, allocatedTime);
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

    void remove(Object[] sample)
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
}
