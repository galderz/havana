package org.example;

import sun.jvm.hotspot.gc.parallel.ParallelScavengeHeap;
import sun.jvm.hotspot.gc.shared.CollectedHeap;
import sun.jvm.hotspot.gc.shared.GenCollectedHeap;
import sun.jvm.hotspot.memory.MemRegion;
import sun.jvm.hotspot.oops.ObjectHistogram;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

import java.util.Arrays;

/**
 * Hello world!
 */
public class Histograms extends Tool
{
    private final MemoryRegion region;

    public Histograms(MemoryRegion region)
    {
        this.region = region;
    }

    public static void main(String[] args)
    {
        final String memoryRegion = args[0];
        System.out.printf("=== %s Gen Histogram ===%n", memoryRegion);
        new Histograms(MemoryRegion.valueOf(memoryRegion.toUpperCase())).execute(tail(args));
    }

    public static <T> T[] tail(T[] array) {
        if (array.length == 0)
            throw new IllegalArgumentException("Array cannot be empty");

        return Arrays.copyOfRange(array, 1, array.length);
    }

    @Override
    public void run()
    {
        final CollectedHeap heap = VM.getVM().getUniverse().heap();
        MemRegion region = switch (this.region) {
            case OLD -> getOldRegion(heap);
            case YOUNG -> getYoungRegion(heap);
        };

        ObjectHistogram histogram = new ObjectHistogram()
        {
            @Override
            public boolean doObj(Oop obj)
            {
                return region.contains(obj.getHandle()) && super.doObj(obj);
            }
        };

        VM.getVM().getObjectHeap().iterate(histogram);
        histogram.print();
    }

    private static MemRegion getOldRegion(CollectedHeap heap)
    {
        if (heap instanceof ParallelScavengeHeap)
        {
            return ((ParallelScavengeHeap) heap).oldGen().objectSpace().usedRegion();
        }
        else if (heap instanceof GenCollectedHeap)
        {
            return ((GenCollectedHeap) heap).getGen(1).usedRegion();
        }
        else
        {
            throw new UnsupportedOperationException(heap.kind() + " is not supported");
        }
    }

    private static MemRegion getYoungRegion(CollectedHeap heap)
    {
        if (heap instanceof ParallelScavengeHeap)
        {
            // TODO add toSpace and fromSpace aside edenSpace
            return ((ParallelScavengeHeap) heap).youngGen().edenSpace().usedRegion();
        }
        else if (heap instanceof GenCollectedHeap)
        {
            return ((GenCollectedHeap) heap).getGen(0).usedRegion();
        }
        else
        {
            throw new UnsupportedOperationException(heap.kind() + " is not supported");
        }
    }

    enum MemoryRegion
    {
        YOUNG,
        OLD
    }
}
