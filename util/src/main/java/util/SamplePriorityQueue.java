package util;

final class SamplePriorityQueue
{
    private static final int OBJECT_INDEX = 0;
    private static final int SPAN_INDEX = 1;
    private static final int ALLOCATION_TIME_INDEX = 2;
    private static final int THREAD_ID_INDEX = 3;
    private static final int STACKTRACE_ID_INDEX = 4;
    private static final int USED_AT_GC_INDEX = 5;
    private static final int PREVIOUS = 6;

    private final Object[][] items;
    private final SampleList list;
    public int count;
    private long total;

    SamplePriorityQueue(int size)
    {
        this.items = new Object[size][];
        for (int i = 0; i < this.items.length; i++)
        {
            this.items[i] = new Object[7];
        }
        list = new SampleList();
    }

    /**
     * Inserts the specified Sample into this queue.
     *
     * This method does not check if the queue has enough capacity.
     * It's up to the caller decide how to deal with a full queue.
     */
    void push(Object obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC)
    {
        set(obj, span, allocationTime, threadId, stackTraceId, usedAtLastGC, items[count]);
        list.prepend(items[count]);
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
        list.remove(items[count]);
        clearItem(items[count]);
        moveDown(0);
        total -= span(head);
    }

    private void clearItem(Object[] item)
    {
        set(null, 0, 0, 0, 0, 0, item);
        item[PREVIOUS] = null;
    }

    boolean isFull()
    {
        return count == items.length;
    }

    long peekSpan()
    {
        return count == 0 ? -1 : span(items[0]);
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

    private static void set(Object obj, long span, long allocationTime, long threadId, long stackTraceId, long usedAtLastGC, Object[] sample)
    {
        sample[OBJECT_INDEX] = obj;
        sample[SPAN_INDEX] = span;
        sample[ALLOCATION_TIME_INDEX] = allocationTime;
        sample[THREAD_ID_INDEX] = threadId;
        sample[STACKTRACE_ID_INDEX] = stackTraceId;
        sample[USED_AT_GC_INDEX] = usedAtLastGC;
    }

    static Long span(Object[] sample)
    {
        return (Long) sample[SPAN_INDEX];
    }

    SampleList asList()
    {
        return list;
    }

    final class SampleList
    {
        Object[] head;
        Object[] tail;

        private SampleList() {
        }

        private void prepend(Object[] sample)
        {
            if (head == null)
            {
                head = sample;
                tail = sample;
                return;
            }

            Object[] tmp = head;
            head = sample;
            tmp[PREVIOUS] = sample;
        }

        public void remove(Object[] item)
        {
            if (tail == item)
            {
                // If item is tail, update tail to be item's prev
                tail = (Object[]) item[PREVIOUS];
                return;
            }

            // Else, find an element (e.g. next) whose previous is item.
            // Note: Iterate to locate index of next.
            //       Avoids the need the keep index in sample.
            Object[] next = null;
            for (int i = 0; i < items.length; i++)
            {
                if (items[i][PREVIOUS] == item) {
                    next = items[i];
                }
            }

            assert next != null;

            // Then set that next's previous to item's previous
            next[PREVIOUS] = item[PREVIOUS];
        }

        int firstIndex()
        {
            // Note: Iterate to locate index of tail.
            //       Avoids the need the keep index in sample.
            for (int i = 0; i < items.length; i++)
            {
                if (tail == items[i]) {
                    return i;
                }
            }

            return -1;
        }

        int prevIndex(int index)
        {
            final Object[] entry = items[index];
            if (entry == null) {
                return -1;
            }

            final Object prev = entry[PREVIOUS];
            // Note: Iterate to locate index of prev.
            //       Avoids the need the keep index in sample.
            for (int i = 0; i < items.length; i++)
            {
                if (prev == items[i])
                    return i;
            }

            return -1;
        }

        long allocationTimeAt(int index)
        {
            return longAt(index, ALLOCATION_TIME_INDEX);
        }

        Object objectAt(int index) {
            return items[index][OBJECT_INDEX];
        }

        long threadIdAt(int index) {
            return longAt(index, THREAD_ID_INDEX);
        }

        long stackTraceIdAt(int index) {
            return longAt(index, STACKTRACE_ID_INDEX);
        }


        long usedAtLastGCAt(int index) {
            return longAt(index, USED_AT_GC_INDEX);
        }

        private long longAt(int index, int fieldIndex) {
            final Object[] entry = items[index];
            return entry == null ? -1 : (long) entry[fieldIndex];
        }
    }
}
