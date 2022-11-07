package util;

import java.util.Objects;

public class JfrOldObjectSamplePriorityQueue
{
    private final Object[][] items;
    private int count;
    private long total;

    public JfrOldObjectSamplePriorityQueue(int size)
    {
        this.items = new Object[size][];
        for (int i = 0; i < this.items.length; i++)
        {
            this.items[i] = new Object[2];
        }
    }

    /**
     * Inserts the specified Sample into this queue.
     *
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    public void push(long span)
    {
        assert span(items[count]) == null;

        setSpan(span, items[count]);
        count++;
        moveUp(count - 1);
        total += span;
    }

    /**
     * Retrieves and removes the head of the queue.
     * The head of the queue is the sample with the smallest span.
     *
     * @return a Sample or null if empty
     */
    public Object[] poll()
    {
        if (count == 0)
        {
            return null;
        }

        final Object[] head = items[0];
        assert head != null;
        swap(0, count - 1);
        count--;
        assert head == items[count];
        items[count] = null;
        moveDown(0);
        total -= span(head);
        return head;
    }

    public boolean isFull()
    {
        return count == items.length;
    }

    private void moveDown(int i)
    {
        do
        {
            int j = -1;
            int r = right(i);
            if (r < count && span(items[r]) < span(items[i]))
            {
                int l = left(i);
                if (span(items[l]) < span(items[r]))
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
                if (l < count && span(items[l]) < span(items[i]))
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
        while (i > 0 && span(items[i]) < span(items[parent]))
        {
            swap(i, parent);
            i = parent;
            parent = parent(i);
        }
    }

    private void swap(int i, int j)
    {
        final Object[] tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
        // items[i].index = i;
        // items[j].index = j;
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    private static void setSpan(long span, Object[] sample)
    {
        sample[0] = span;
    }

    static Long span(Object[] sample)
    {
        return (Long) sample[0];
    }
}
