package util.sampling.v3;

import util.Asserts;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class SamplesTest
{
    public static void main(String[] args) throws Exception
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
        testIterateAndRemoveAll();
        testIterateAndRemoveOldest();
        testIterateAndRemoveYoungest();
        testIterateAndRemoveMiddle();
        testIterateAndRemoveAndEvict();
        testMultiThreaded();
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
        assert null == SampleArray.getReference(sampler.queue.peek());
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
        assert null == SampleArray.getReference(sampler.queue.peek());
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

        List<String> objects = iterate(sampler);
        assert objects.equals(List.of("200", "400", "300", "500", "100")) : objects;
    }

    private static List<String> iterate(Sampler sampler)
    {
        List<String> objects = new ArrayList<>();
        Object[] current = sampler.list.head();
        while (current != null)
        {
            objects.add((String) SampleArray.getReference(current).get());
            current = sampler.list.next(current);
        }
        return objects;
    }

    private static void testIterateAllocationTimesFIFOSizeMinusOne()
    {
        final int size = 8;
        Sampler sampler = new Sampler(size);

        for (int i = 0; i < size - 1; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), i * 100, i);
        }

        List<String> objects = iterate(sampler);
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

        List<String> objects = iterate(sampler);
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

        List<String> objects = iterate(sampler);
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

        List<String> objects = iterate(sampler);
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

        List<String> objects = iterate(sampler);
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

    private static void testIterateAndRemoveAll()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        List<String> objects = new ArrayList<>();
        int removed = iterateAndRemove(x -> true, objects, sampler);

        assert 4 == removed;
        assert null == sampler.list.head();
        assert objects.equals(List.of("0", "1", "2", "3")) : objects;
    }

    private static int iterateAndRemove(Predicate<String> shouldRemove, List<String> objects, Sampler sampler)
    {
        Object[] current = sampler.list.head();
        int removed = 0;
        while (current != null)
        {
            Object[] next = sampler.list.next(current);
            final String value = (String) SampleArray.getReference(current).get();
            if (shouldRemove.test(value))
            {
                objects.add(value);
                sampler.remove(current);
                removed++;
            }

            current = next;
        }

        return removed;
    }

    private static void testIterateAndRemoveOldest()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        List<String> removedObjects = new ArrayList<>();
        int removed = iterateAndRemove(v -> v.equals("0"), removedObjects, sampler);

        assert 1 == removed;
        assert null != sampler.list.head();
        assert removedObjects.equals(List.of("0")) : removedObjects;

        List<String> objects = iterate(sampler);
        assert objects.equals(List.of("1", "2", "3")) : objects;
    }

    private static void testIterateAndRemoveYoungest()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        List<String> removedObjects = new ArrayList<>();
        int removed = iterateAndRemove(v -> v.equals("3"), removedObjects, sampler);

        assert 1 == removed;
        assert null != sampler.list.head();
        assert removedObjects.equals(List.of("3")) : removedObjects;

        List<String> objects = iterate(sampler);
        assert objects.equals(List.of("0", "1", "2")) : objects;
    }

    private static void testIterateAndRemoveMiddle()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        List<String> removedObjects = new ArrayList<>();
        int removed = iterateAndRemove(v -> v.equals("1") || v.equals("2"), removedObjects, sampler);

        assert 2 == removed;
        assert null != sampler.list.head();
        assert removedObjects.equals(List.of("1", "2")) : removedObjects;

        List<String> objects = iterate(sampler);
        assert objects.equals(List.of("0", "3")) : objects;
    }

    private static void testIterateAndRemoveAndEvict()
    {
        final int size = 4;
        Sampler sampler = new Sampler(size);
        for (int i = 0; i < size; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }

        List<String> objects = new ArrayList<>();
        int removed = iterateAndRemove(x -> true, objects, sampler);

        assert 4 == removed;
        assert null == sampler.list.head();
        assert objects.equals(List.of("0", "1", "2", "3")) : objects;

        for (int i = 0; i < size * 2; i++)
        {
            sampler.sample(new WeakReference<>(String.valueOf(i)), 10 + i, i);
        }
    }

    private static void testMultiThreaded() throws InterruptedException
    {
        final AtomicBoolean running = new AtomicBoolean(true);

        final int size = 32;
        final Sampler sampler = new Sampler(size);

        final CountDownLatch latch = new CountDownLatch(1);

        final Scavenging scavenging = new Scavenging(sampler, running);
        scavenging.start();

        final Sampling sampling = new Sampling(sampler, latch);
        sampling.start();

        latch.countDown();

        sampling.join();
        running.set(false);
        scavenging.join();
    }

    static class Sampling extends Thread
    {
        final Sampler sampler;
        final CountDownLatch latch;
        int numAttempts = 1_000_000;
        int numSampled;

        Sampling(Sampler sampler, CountDownLatch latch)
        {
            this.sampler = sampler;
            this.latch = latch;
        }

        @Override
        public void run()
        {
            try
            {
                latch.await();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            for (int i = 1; i <= numAttempts; i++)
            {
                if (sampler.sample(new WeakReference<>(i), 10 + i, System.nanoTime()))
                {
                    numSampled++;
                }
            }
        }
    }

    static class Scavenging extends Thread
    {
        final Sampler sampler;
        final AtomicBoolean running;
        int numScavenged;

        Scavenging(Sampler sampler, AtomicBoolean running)
        {
            this.sampler = sampler;
            this.running = running;
        }

        @Override
        public void run()
        {
            while (running.get())
            {
                Object[] current = sampler.list.head();
                while (current != null)
                {
                    Object[] next = sampler.list.next(current);
                    sampler.remove(current);
                    numScavenged++;
                    current = next;
                }
            }
        }
    }
}
