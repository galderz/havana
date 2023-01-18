package util.sampling.v1;

import java.lang.ref.WeakReference;

public class SamplePriorityWeakRefQueue
{
    private static final int REF_INDEX = 0;
    private static final int SPAN_INDEX = 1;
    private static final int ALLOCATION_TIME_INDEX = 2;
    private static final int THREAD_ID_INDEX = 3;
    private static final int STACKTRACE_ID_INDEX = 4;
    private static final int USED_AT_GC_INDEX = 5;
    private static final int ARRAY_LENGTH_INDEX = 6;

    private final Object[][] items;
    public int count;
    private long total;

    SamplePriorityWeakRefQueue(int capacity)
    {
        this.items = new Object[capacity][];
        for (int i = 0; i < this.items.length; i++)
        {
            this.items[i] = new Object[ARRAY_LENGTH_INDEX + 1];
        }
    }

    /**
     * Inserts the specified Sample into this queue.
     *
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    void push(WeakReference<?> obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC, int length)
    {
        set(obj, span, allocationTime, threadId, stackTraceId, usedAtLastGC, length, items[count]);
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

        final Object[] head = items[0];
        swap(0, count - 1);
        count--;
        clearItem(items[count]);
        moveDown(0);
        total -= span(head);
    }

    private void clearItem(Object[] item)
    {
        set(null, 0L, 0, 0, 0, 0, 0, item);
    }

    boolean isFull()
    {
        return count == items.length;
    }

    long peekSpan()
    {
        return count == 0 ? -1 : span(items[0]);
    }

    WeakReference<?> peekObject()
    {
        return count == 0 ? null : (WeakReference<?>) items[0][REF_INDEX];
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
    }

    public void removeAt(int index) {
        final Object[] sample = items[index];
        final long span = span(sample);
        sample[SPAN_INDEX] = 0L;
        moveUp(index);
        sample[SPAN_INDEX] = span;
        poll();
    }

    private static int parent(int i)
    {
        return (i - 1) / 2;
    }

    private static void set(WeakReference<?> obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC, int length, Object[] sample)
    {
        sample[REF_INDEX] = obj;
        sample[SPAN_INDEX] = span;
        sample[ALLOCATION_TIME_INDEX] = allocationTime;
        sample[THREAD_ID_INDEX] = threadId;
        sample[STACKTRACE_ID_INDEX] = stackTraceId;
        sample[USED_AT_GC_INDEX] = usedAtLastGC;
        sample[ARRAY_LENGTH_INDEX] = length;
    }

    int getCapacity() {
        return items.length;
    }

    WeakReference<?> getReferenceAt(int index) {
        return (WeakReference<?>) items[index][REF_INDEX];
    }

    long getAllocationTimeAt(int index)
    {
        return longAt(index, ALLOCATION_TIME_INDEX);
    }

    long getThreadIdAt(int index) {
        return longAt(index, THREAD_ID_INDEX);
    }

    long getStackTraceIdAt(int index) {
        return longAt(index, STACKTRACE_ID_INDEX);
    }

    long getUsedAtLastGCAt(int index) {
        return longAt(index, USED_AT_GC_INDEX);
    }

    int getArrayLength(int index) {
        final Object[] entry = items[index];
        return entry == null ? -1 : (int) entry[ARRAY_LENGTH_INDEX];
    }

    private static long span(Object[] sample) {
        // todo can sample be null?
        return sample == null ? - 1 : (long) sample[SPAN_INDEX];
    }

    private long longAt(int index, int fieldIndex) {
        final Object[] entry = items[index];
        // todo can entry be null?
        return entry == null ? -1 : (long) entry[fieldIndex];
    }
}
