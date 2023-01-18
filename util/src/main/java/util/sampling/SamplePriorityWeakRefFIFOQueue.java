package util.sampling;

import java.lang.ref.WeakReference;

public class SamplePriorityWeakRefFIFOQueue
{
    private static final int REF_SLOT = 0;
    private static final int SPAN_SLOT = 1;
    private static final int ALLOCATION_TIME_SLOT = 2;
    private static final int THREAD_ID_SLOT = 3;
    private static final int STACKTRACE_ID_SLOT = 4;
    private static final int USED_AT_GC_SLOT = 5;
    private static final int ARRAY_LENGTH_SLOT = 6;
    private static final int PREVIOUS_SLOT = 7;

    private final Object[][] items;
    private final SampleList list;
    public int count;
    private long total;

    SamplePriorityWeakRefFIFOQueue(int capacity)
    {
        this.items = new Object[capacity][];
        for (int i = 0; i < this.items.length; i++)
        {
            this.items[i] = new Object[PREVIOUS_SLOT + 1];
        }
        this.list = new SampleList();
    }

    /**
     * Inserts the specified Sample into this queue.
     * <p>
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    void push(WeakReference<?> obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC, int length)
    {
        set(obj, span, allocationTime, threadId, stackTraceId, usedAtLastGC, length, items[count]);
        list.prepend(items[count]);
        count++;
        moveUp(count - 1);
        total += span;
    }

    private void push(Object[] sample)
    {
        final WeakReference<?> ref = getReference(sample);
        final long span = getSpan(sample);
        final long allocationTime = getAllocationTime(sample);
        final long threadId = getThreadId(sample);
        final long stacktraceId = getStackTraceId(sample);
        final long usedAtGC = getUsedAtGC(sample);
        final int arrayLength = getArrayLength(sample);
        push(ref, span, allocationTime, threadId, stacktraceId, usedAtGC, arrayLength);
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

        final Object[] head = items[0];
        swap(0, count - 1);
        count--;
        list.remove(items[count]);
        clearItem(items[count]);
        moveDown(0);
        total -= getSpan(head);
    }

    SampleList list()
    {
        return list;
    }

    private void clearItem(Object[] item)
    {
        set(null, 0L, 0, 0, 0, 0, 0, item);
        item[PREVIOUS_SLOT] = null;
    }

    boolean isFull()
    {
        return count == items.length;
    }

    long peekSpan()
    {
        return count == 0 ? -1 : getSpan(items[0]);
    }

    WeakReference<?> peekObject()
    {
        return count == 0 ? null : (WeakReference<?>) items[0][REF_SLOT];
    }

    private void moveDown(int i)
    {
        do
        {
            int j = -1;
            int r = right(i);
            if (r < count && getSpan(items[r]) < getSpan(items[i]))
            {
                int l = left(i);
                if (getSpan(items[l]) < getSpan(items[r]))
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
                if (l < count && getSpan(items[l]) < getSpan(items[i]))
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
        while (i > 0 && getSpan(items[i]) < getSpan(items[parent]))
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
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    private static void set(WeakReference<?> obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC, int length, Object[] sample)
    {
        sample[REF_SLOT] = obj;
        sample[SPAN_SLOT] = span;
        sample[ALLOCATION_TIME_SLOT] = allocationTime;
        sample[THREAD_ID_SLOT] = threadId;
        sample[STACKTRACE_ID_SLOT] = stackTraceId;
        sample[USED_AT_GC_SLOT] = usedAtLastGC;
        sample[ARRAY_LENGTH_SLOT] = length;
    }

    WeakReference<?> getReference(Object[] sample)
    {
        return (WeakReference<?>) sample[REF_SLOT];
    }

    long getSpan(Object[] sample)
    {
        return (long) sample[SPAN_SLOT];
    }

    long getAllocationTime(Object[] sample)
    {
        return (long) sample[ALLOCATION_TIME_SLOT];
    }

    long getThreadId(Object[] sample)
    {
        return (long) sample[THREAD_ID_SLOT];
    }

    long getStackTraceId(Object[] sample)
    {
        return (long) sample[STACKTRACE_ID_SLOT];
    }

    long getUsedAtGC(Object[] sample)
    {
        return (long) sample[USED_AT_GC_SLOT];
    }

    int getArrayLength(Object[] sample)
    {
        return (int) sample[ARRAY_LENGTH_SLOT];
    }

    Object[] getPrevious(Object[] entry)
    {
        return (Object[]) entry[PREVIOUS_SLOT];
    }

    void remove(Object[] sample)
    {
        final Object[] prev = getPrevious(sample);
        if (prev != null)
        {
            pop(prev);
            prev[SPAN_SLOT] = getSpan(prev) + getSpan(sample);
            push(prev);
        }
        pop(sample);
        list.remove(sample);
    }

    private void pop(Object[] sample)
    {
        final long span = getSpan(sample);
        sample[SPAN_SLOT] = 0L;
        moveUp(getIndexOf(sample));
        sample[SPAN_SLOT] = span;
        poll();
    }

    private int getIndexOf(Object[] target)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (target == items[i])
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * A singly linked list view of the queue.
     * An item's previous is the item that was added after to the queue after the item itself.
     * The list is iterated in FIFO order, starting with the element added first
     * and following previous links to find elements added after.
     * The traversal can also be used to discover entries that need removing,
     * e.g. if references have been GC'd.
     */
    class SampleList
    {
        // Points to the oldest entry added to the list.
        // This would be the first FIFO iterated element.
        // Only gets updated when an entry is removed.
        Object[] head;

        // Points to the youngest entry added to the list.
        // This would be last FIFO iterated element.
        // Prepending merely updates this pointer.
        Object[] tail;

        private SampleList()
        {
        }

        private void prepend(Object[] sample)
        {
            if (tail == null)
            {
                tail = sample;
                head = sample;
                return;
            }

            Object[] tmp = tail;
            tail = sample;
            tmp[PREVIOUS_SLOT] = sample;
        }

        private void remove(Object[] item)
        {
            if (head == item)
            {
                // If item is head, update head to be item's prev
                head = getPrevious(item);
                return;
            }

            // Else, find an element whose previous is item; iow, find item's next element.
            Object[] next = findNext(item);

            assert next != null;

            // Then set that next's previous to item's previous
            next[PREVIOUS_SLOT] = item[PREVIOUS_SLOT];

            // If the element removed is tail, update it to item's next.
            if (tail == item)
            {
                tail = next;
            }
        }

        Object[] head()
        {
            return head;
        }

        Object[] next(Object[] current)
        {
            return getPrevious(current);
        }

        private Object[] findNext(Object[] target)
        {
            Object[] current = head;
            while (current != null)
            {
                if (current[PREVIOUS_SLOT] == target)
                {
                    return current;
                }

                current = getPrevious(current);
            }

            return null;
        }
    }
}
