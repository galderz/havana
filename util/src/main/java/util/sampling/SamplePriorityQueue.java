package util.sampling;

final class SamplePriorityQueue
{
    private final SampleEntry[] items;
    private int count;
    private long total;

    SamplePriorityQueue(int size)
    {
        this.items = new SampleEntry[size];
    }

    /**
     * Inserts the specified Sample into this queue.
     * <p>
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    void push(Object obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC)
    {
        final SampleEntry entry = new SampleEntry();
        entry.setObject(obj);
        entry.setSpan(span);
        entry.setAllocationTime(allocationTime);
        entry.setThreadId(threadId);
        entry.setStackTraceId(stackTraceId);
        entry.setUsedAtGC(usedAtLastGC);
        items[count] = entry;
        count++;
        moveUp(count - 1);
        total += span;
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

        final SampleEntry head = items[0];
        swap(0, count - 1);
        count--;
        clearItem(items[count]);
        moveDown(0);
        total -= span(head);
    }

    private void clearItem(SampleEntry entry)
    {
        entry.setObject(null);
        entry.setSpan(0);
        entry.setAllocationTime(0);
        entry.setThreadId(0);
        entry.setStackTraceId(0);
        entry.setUsedAtGC(0);
    }

    boolean isFull()
    {
        return count == items.length;
    }

    long peekSpan()
    {
        return count == 0 ? -1 : span(items[0]);
    }

    Object peekObject()
    {
        return count == 0 ? null : items[0].getObject();
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

    private static int left(int i)
    {
        return 2 * i + 1;
    }

    private static int right(int i)
    {
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
        final SampleEntry tmp = items[i];
        items[i] = items[j];
        items[j] = tmp;
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    static Long span(SampleEntry entry)
    {
        return entry.getSpan();
    }

    SampleEntry[] getArray()
    {
        return items;
    }
}
