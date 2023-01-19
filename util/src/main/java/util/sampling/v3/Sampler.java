package util.sampling.v3;

import java.lang.ref.WeakReference;

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
            final Object[] head = queue.peek();
            if (SampleArray.getSpan(head) > allocatedSize)
            {
                return;
            }

            evictSample(head);
        }

        storeSample(obj, allocatedSize, allocatedTime);
    }

    private void evictSample(Object[] head)
    {
        queue.poll();
        list.remove(head);
        SampleArray.setReference(null, head);
        SampleArray.setSpan(0L, head);
        SampleArray.setAllocationTime(0L, head);
        SampleArray.setThreadId(0L, head);
        SampleArray.setStackTraceId(0L, head);
        SampleArray.setUsedAtGC(0L, head);
        SampleArray.setArrayLength(0, head);
    }

    private void storeSample(WeakReference<?> obj, long allocatedSize, long allocatedTime)
    {
        final int index = queue.getCount();
        final Object[] sample = samples.getSample(index);
        SampleArray.setReference(obj, sample);
        SampleArray.setSpan(allocatedSize, sample);
        SampleArray.setAllocationTime(allocatedTime, sample);
        SampleArray.setThreadId(0L, sample);
        SampleArray.setStackTraceId(0L, sample);
        SampleArray.setUsedAtGC(0L, sample);
        SampleArray.setArrayLength(0, sample);
        queue.push(sample);
        list.prepend(sample);
    }

    void remove(Object[] sample)
    {
        final Object[] prev = SampleArray.getPrevious(sample);
        if (prev != null)
        {
            queue.remove(prev);
            SampleArray.setSpan(SampleArray.getSpan(sample) + SampleArray.getSpan(prev), prev);
            queue.push(prev);
        }
        queue.remove(sample);
        list.remove(sample);
    }
}
