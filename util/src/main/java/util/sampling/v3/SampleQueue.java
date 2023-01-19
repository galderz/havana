package util.sampling.v3;

public class SampleQueue
{
    private final SampleArray samples;
    private int count;
    private long total;

    // @Platforms(Platform.HOSTED_ONLY.class)
    SampleQueue(SampleArray samples)
    {
        this.samples = samples;
    }

    boolean isFull()
    {
        return count == samples.getCapacity();
    }

    int getCount()
    {
        return count;
    }

    /**
     * Inserts the specified Sample into this queue.
     * <p>
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    void push(Object[] sample)
    {
        count++;
        moveUp(count - 1);
        total += SampleArray.getSpan(sample);
    }

    Object[] peek()
    {
        return count == 0 ? SampleArray.EMPTY : samples.getSample(0);
    }

    /**
     * Removes the head of the queue.
     * The head of the queue is the sample with the smallest span.
     */
    void poll()
    {
        if (count == 0)
        {
            return;
        }

        final Object[] head = peek();
        samples.swap(0, count - 1);
        count--;
        // clearItem(items[count]);
        moveDown(0);
        total -= SampleArray.getSpan(head);
    }

    private void moveUp(int i)
    {
        int parent = parent(i);
        while (i > 0 && samples.getSpan(i) < samples.getSpan(parent))
        {
            samples.swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    private void moveDown(int i)
    {
        do
        {
            int j = -1;
            int r = right(i);
            if (r < count && samples.getSpan(r) < samples.getSpan(i))
            {
                int l = left(i);
                if (samples.getSpan(l) < samples.getSpan(r))
                {
                    j = l;
                }
                else
                {
                    j = r;
                }
            }
            else
            {
                int l = left(i);
                if (l < count && samples.getSpan(l) < samples.getSpan(i))
                {
                    j = l;
                }
            }

            if (j >= 0)
            {
                samples.swap(i, j);
            }
            i = j;
        } while (i >= 0);
    }

    private static int left(int i)
    {
        return 2 * i + 1;
    }

    private static int right(int i)
    {
        return 2 * i + 2;
    }

    void remove(Object[] sample)
    {
        final long span = SampleArray.getSpan(sample);
        SampleArray.setSpan(0L, sample);
        moveUp(samples.getIndexOf(sample));
        SampleArray.setSpan(span, sample);
        poll();
    }
}
