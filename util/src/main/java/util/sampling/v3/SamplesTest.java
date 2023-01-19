package util.sampling.v3;

import util.Asserts;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SamplesTest
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        testQueueOfferThenPoll();
        testQueueIsFull();
        testQueuePeek();
        testIterateAllocationTimesFIFO();
        testIterateAllocationTimesFIFOSizeMinusOne();
        testIterateAllocationTimesFIFOSize();
        testIterateAllocationTimesFIFOSizePlusOnePopOldest();
        testIterateAllocationTimesFIFOSizePlusOnePopMiddle();
        testIterateAllocationTimesFIFOSizePlusOnePopYoungest();
        testPushAndIterateMany();
        testIterateRemoteAll();
    }

    private static void testQueueOfferThenPoll()
    {
        Sampler sampler = new Sampler(10);
        sampler.sample(new WeakReference<>("200"), 200, 1);
        sampler.sample(new WeakReference<>("400"), 400, 2);
        sampler.sample(new WeakReference<>("300"), 300, 3);
        sampler.sample(new WeakReference<>("500"), 500, 4);
        sampler.sample(new WeakReference<>("100"), 100, 5);

        assert 100 == SampleArray.getSpan(sampler.queue.peek());
        assert "100" == SampleArray.getReference(sampler.queue.peek()).get();
        sampler.queue.poll();
        assert 200 == SampleArray.getSpan(sampler.queue.peek());
        assert "200" == SampleArray.getReference(sampler.queue.peek()).get();
        sampler.queue.poll();
        assert 300 == SampleArray.getSpan(sampler.queue.peek());
        assert "300" == SampleArray.getReference(sampler.queue.peek()).get();
        sampler.queue.poll();
        assert 400 == SampleArray.getSpan(sampler.queue.peek());
        assert "400" == SampleArray.getReference(sampler.queue.peek()).get();
        sampler.queue.poll();
        assert 500 == SampleArray.getSpan(sampler.queue.peek());
        assert "500" == SampleArray.getReference(sampler.queue.peek()).get();
        sampler.queue.poll();
        assert null == sampler.queue.peek();
    }

    private static void testQueueIsFull()
    {
        Sampler sampler = new Sampler(3);
        sampler.sample(new WeakReference<>(new Object()), 300, 1);
        assert !sampler.queue.isFull();
        sampler.sample(new WeakReference<>(new Object()), 200, 2);
        assert !sampler.queue.isFull();
        sampler.sample(new WeakReference<>(new Object()), 100, 3);
        assert sampler.queue.isFull();
    }

    private static void testQueuePeek()
    {
        Sampler sampler = new Sampler(3);
        assert null == sampler.queue.peek();
        sampler.sample(new WeakReference<>("300"), 300, 1);
        assert 300 == SampleArray.getSpan(sampler.queue.peek());
        assert "300" == SampleArray.getReference(sampler.queue.peek()).get();
    }

    private static void testIterateAllocationTimesFIFO()
    {
        Sampler sampler = new Sampler(10);
        sampler.sample(new WeakReference<>("200"), 200, 1);
        sampler.sample(new WeakReference<>("400"), 400, 2);
        sampler.sample(new WeakReference<>("300"), 300, 3);
        sampler.sample(new WeakReference<>("500"), 500, 4);
        sampler.sample(new WeakReference<>("100"), 100, 5);

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L)) : allocationTimes;
        assert objects.equals(List.of("200", "400", "300", "500", "100")) : allocationTimes;
    }

    private static void iterate(List<Long> allocationTimes, List<String> objects, Sampler sampler)
    {
        Object[] current = sampler.list.head();
        while (current != null)
        {
            allocationTimes.add(SampleArray.getAllocationTime(current));
            objects.add((String) SampleArray.getReference(current).get());
            current = sampler.list.next(current);
        }
    }

    private static void testIterateAllocationTimesFIFOSizeMinusOne()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size - 1; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i * 100, i);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6")) : objects;
    }

    private static void testIterateAllocationTimesFIFOSize()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i * 100, i);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 7L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "7")) : objects;
    }

    /**
     * Pop oldest because that's the one with the lowest span
     */
    private static void testIterateAllocationTimesFIFOSizePlusOnePopOldest()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size + 1; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i * 100, i);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)) : allocationTimes;
        assert objects.equals(List.of("1", "2", "3", "4", "5", "6", "7", "8")) : objects;
    }

    /**
     * Pop middle because that is the one with the lowest span
     */
    private static void testIterateAllocationTimesFIFOSizePlusOnePopMiddle()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size + 1; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i == (size / 2) ? 100 : 200, i);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 5L, 6L, 7L, 8L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "5", "6", "7", "8")) : objects;
    }

    private static void testIterateAllocationTimesFIFOSizePlusOnePopYoungest()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size + 1; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i == (size - 1) ? 100 : 200, i);
        }

        List<Long> allocationTimes = new ArrayList<>();
        List<String> objects = new ArrayList<>();
        iterate(allocationTimes, objects, sampler);
        assert allocationTimes.equals(List.of(0L, 1L, 2L, 3L, 4L, 5L, 6L, 8L)) : allocationTimes;
        assert objects.equals(List.of("0", "1", "2", "3", "4", "5", "6", "8")) : objects;
    }

    private static void testPushAndIterateMany()
    {
        Sampler sampler = new Sampler(256);

        // Attempt to push a large number of entries
        for (int i = 0; i < 1_000_000; i++)
        {
            sampler.sample(new WeakReference<>(new Object()), i, 0);
        }

        // Attempt to iterate over the contents of the queue
        int count = 0;
        Object[] current = sampler.list.head();
        while (current != null)
        {
            count++;
            current = sampler.list.next(current);
        }

        assert 256 == count;
    }

    private static void testIterateRemoteAll()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        Object[] current = sampler.list.head();
        int removed = 0;
        while (current != null)
        {
            Object[] next = sampler.list.next(current);
            final String value = (String) SampleArray.getReference(current).get();
            if (value != null)
            {
                sampler.remove(current);
                removed++;
            }

            current = next;
        }

        assert 4 == removed;
    }

    // todo test iterate and remove youngest
    // todo test iterate and remove oldest
    // todo test iterate and remove middle
}
