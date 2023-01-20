package util.sampling.v3;

import static util.sampling.v3.SampleArray.getPrevious;
import static util.sampling.v3.SampleArray.setPrevious;

/**
 * A singly linked list view of the queue.
 * An item's previous is the item that was added after to the queue after the item itself.
 * The list is iterated in FIFO order, starting with the element added first
 * and following previous links to find elements added after.
 * The traversal can also be used to discover entries that need removing,
 * e.g. if references have been garbage collected.
 */
public class SampleList
{
    private final SampleArray samples;

    // Points to the oldest entry added to the list.
    // This would be the first FIFO iterated element.
    // Only gets updated when an entry is removed.
    Object[] head;

    // Points to the youngest entry added to the list.
    // This would be last FIFO iterated element.
    // Prepending merely updates this pointer.
    Object[] tail;

    // @Platforms(Platform.HOSTED_ONLY.class)
    SampleList(SampleArray samples)
    {
        this.samples = samples;
    }

    Object[] head()
    {
        return head;
    }

    Object[] next(Object[] current)
    {
        return getPrevious(current);
    }

    void prepend(Object[] sample)
    {
        if (tail == null)
        {
            tail = sample;
            head = sample;
            return;
        }

        Object[] tmp = tail;
        tail = sample;
        setPrevious(sample, tmp);
    }

    void remove(Object[] sample)
    {
        if (head == sample)
        {
            // If item is head, update head to be item's prev
            head = getPrevious(sample);
            return;
        }

        // Else, find an element whose previous is item; iow, find item's next element.
        Object[] next = findNext(sample);

        assert next != null;

        // Then set that next's previous to item's previous
        setPrevious(getPrevious(sample), next);

        // If the element removed is tail, update it to item's next.
        if (tail == sample)
        {
            tail = next;
        }
    }

    private Object[] findNext(Object[] target)
    {
        Object[] current = head;
        while (current != null)
        {
            if (getPrevious(current) == target)
            {
                return current;
            }

            current = getPrevious(current);
        }

        return null;
    }
}
