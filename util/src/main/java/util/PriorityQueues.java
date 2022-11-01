package util;

import java.util.PriorityQueue;

public class PriorityQueues
{
    public static void main(String[] args)
    {
        final PriorityQueue<Sample> queue = new PriorityQueue<>(10, Comparator.INSTANCE);
        queue.offer(new Sample().setSpan(200));
        queue.offer(new Sample().setSpan(300));
        queue.offer(new Sample().setSpan(100));

        System.out.println(queue.poll().span);
        System.out.println(queue.poll().span);
        System.out.println(queue.poll().span);
    }

    static final class Comparator implements java.util.Comparator<Sample> {
        static final Comparator INSTANCE = new Comparator();

        @Override
        public int compare(Sample o1, Sample o2) {
            return Long.compare(o1.span, o2.span);
        }
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

