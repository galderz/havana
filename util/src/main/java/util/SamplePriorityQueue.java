package util;

import java.util.Objects;

public class SamplePriorityQueue
{
    private final Sample[] items;
    private int count;
    private long total;

    public SamplePriorityQueue(int size)
    {
        this.items = new Sample[size];
    }

    /**
     * Inserts the specified Sample into this queue.
     *
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    public void push(Sample sample)
    {
        assert sample != null;
        assert items[count] == null;

        items[count] = sample;
        count++;
        moveUp(count - 1);
        total += sample.span;
    }

    /**
     * Retrieves and removes the head of the queue.
     * The head of the queue is the sample with the smallest span.
     *
     * @return a Sample or null if empty
     */
    public Sample poll()
    {
        if (count == 0)
        {
            return null;
        }

        final Sample head = items[0];
        assert head != null;
        swap(0, count - 1);
        count--;
        assert head == items[count];
        items[count] = null;
        moveDown(0);
        total -= head.span;
        return head;
    }

    private void moveDown(int i)
    {
        do
        {
            int j = -1;
            int r = right(i);
            if (r < count && items[r].span < items[i].span)
            {
                int l = left(i);
                if (items[l].span < items[r].span)
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
                if (l < count && items[l].span < items[i].span)
                {
                    j = l;
                }
            }

            if (j >= 0)
            {
                swap(i, j);
            }
            i = j;
        } while (i >= 0);
    }

    private static int left(int i) {
        return 2 * i + 1;
    }

    private static int right(int i) {
        return 2 * i + 2;
    }

    private void moveUp(int i)
    {
        int parent = parent(i);
        while (i > 0 && items[i].span < items[parent].span)
        {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private void swap(int i, int j)
    {
        final Sample tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
        items[i].index = i;
        items[j].index = j;
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testOfferThenPoll();
        // todo test offering beyond the limit size
    }

    private static void testOfferThenPoll()
    {
        SamplePriorityQueue queue = new SamplePriorityQueue(10);
        queue.push(new Sample().setSpan(200));
        queue.push(new Sample().setSpan(400));
        queue.push(new Sample().setSpan(300));
        queue.push(new Sample().setSpan(500));
        queue.push(new Sample().setSpan(100));

        assert 100 == queue.poll().span;
        assert 200 == queue.poll().span;
        assert 300 == queue.poll().span;
        assert 400 == queue.poll().span;
        assert 500 == queue.poll().span;
        assert Objects.isNull(queue.poll());
    }

    private static final class Sample
    {
        int index;
        long span;

        public Sample setSpan(long span)
        {
            this.span = span;
            return this;
        }
    }
}

