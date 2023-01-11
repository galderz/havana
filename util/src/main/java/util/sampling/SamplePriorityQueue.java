package util.sampling;

final class SamplePriorityQueue
{
    private final SampleEntry[] items;
    private final SampleList list;
    private int count;
    private long total;

    SamplePriorityQueue(int size)
    {
        this.items = new SampleEntry[size];
        this.list = new SampleList();
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
        list.prepend(entry);
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
        list.remove(items[count]);
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
        entry.setPrevious(null);
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

    SampleList asList()
    {
        return list;
    }

    // The list is linked via previous entry.
    // An item's previous is the item that was added after to the queue after the item itself.
    // This makes it easy to follow the queue in FIFO order.
    // Starting with the element added first and following previous links to find elements added after.
    final class SampleList
    {
        // Points to the youngest entry added to the list.
        // This would be last FIFO iterated element.
        // Prepending merely updates this pointer.
        SampleEntry head;

        // Points to the oldest entry added to the list.
        // This would be the first FIFO iterated element.
        // Only gets updated when an entry is removed.
        SampleEntry tail;

        private SampleList()
        {
        }

        private void prepend(SampleEntry entry)
        {
            if (head == null)
            {
                head = entry;
                tail = entry;
                return;
            }

            SampleEntry tmp = head;
            head = entry;
            tmp.setPrevious(entry);
        }

        public void remove(SampleEntry item)
        {
            if (tail == item)
            {
                // If item is tail, update tail to be item's prev
                tail = item.getPrevious();
                return;
            }

            // Else, find an element whose previous is item; iow, find item's next element.
            // Note: Iterate to locate index of next.
            //       Avoids the need the keep index in sample.
            SampleEntry next = findNext(item);
            assert next != null;

            // Then set that next's previous to item's previous
            next.setPrevious(item.getPrevious());

            // If the element removed is head, update it to item's next.
            if (head == item)
            {
                head = next;
            }
        }

        int firstIndex()
        {
            // Note: Iterate to locate index of tail.
            //       Avoids the need the keep index in sample.
            for (int i = 0; i < items.length; i++)
            {
                if (tail == items[i])
                {
                    return i;
                }
            }

            return -1;
        }

        int prevIndex(int index)
        {
            final SampleEntry entry = items[index];
            if (entry == null)
            {
                return -1;
            }

            final SampleEntry prev = entry.getPrevious();
            if (prev == null)
            {
                return -1;
            }

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
            final SampleEntry entry = items[index];
            return entry == null ? -1 : entry.getAllocationTime();
        }

        Object objectAt(int index)
        {
            return items[index].getObject();
        }

        long threadIdAt(int index)
        {
            final SampleEntry entry = items[index];
            return entry == null ? -1 : entry.getThreadId();
        }

        long stackTraceIdAt(int index)
        {
            final SampleEntry entry = items[index];
            return entry == null ? -1 : entry.getStackTraceId();
        }

        long usedAtLastGCAt(int index)
        {
            final SampleEntry entry = items[index];
            return entry == null ? -1 : entry.getUsedAtGC();
        }

        private SampleEntry findNext(SampleEntry entry)
        {
            SampleEntry current = tail;
            while (current != null)
            {
                if (current.getPrevious() == entry)
                {
                    return current;
                }

                current = current.getPrevious();
            }

            return null;
        }
    }
}
