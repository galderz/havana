package org.example;

import sun.jvm.hotspot.gc.parallel.ParallelScavengeHeap;
import sun.jvm.hotspot.gc.shared.CollectedHeap;
import sun.jvm.hotspot.gc.shared.GenCollectedHeap;
import sun.jvm.hotspot.memory.MemRegion;
import sun.jvm.hotspot.oops.ObjectHistogram;
import sun.jvm.hotspot.oops.Oop;
import sun.jvm.hotspot.runtime.VM;
import sun.jvm.hotspot.tools.Tool;

/**
 * Hello world!
 */
public class OldGen extends Tool
{
    public static void main(String[] args)
    {
        new OldGen().execute(args);
    }

    @Override
    public void run()
    {
        MemRegion oldRegion = getOldRegion(VM.getVM().getUniverse().heap());

        ObjectHistogram histogram = new ObjectHistogram()
        {
            @Override
            public boolean doObj(Oop obj)
            {
                return oldRegion.contains(obj.getHandle()) && super.doObj(obj);
            }
        };

        VM.getVM().getObjectHeap().iterate(histogram);
        histogram.print();
    }

    private MemRegion getOldRegion(CollectedHeap heap)
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
}
