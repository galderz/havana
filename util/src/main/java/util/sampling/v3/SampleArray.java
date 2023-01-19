package util.sampling.v3;

import util.sampling.v2.SamplePriorityWeakRefFIFOQueue;

import java.lang.ref.WeakReference;

public class SampleArray
{
    private static final int REF_SLOT = 0;
    private static final int SPAN_SLOT = 1;
    private static final int ALLOCATION_TIME_SLOT = 2;
    private static final int THREAD_ID_SLOT = 3;
    private static final int STACKTRACE_ID_SLOT = 4;
    private static final int USED_AT_GC_SLOT = 5;
    private static final int ARRAY_LENGTH_SLOT = 6;
    private static final int PREVIOUS_SLOT = 7;

    static final Object[] EMPTY = new Object[PREVIOUS_SLOT + 1];

    private final Object[][] samples;

    // @Platforms(Platform.HOSTED_ONLY.class)
    SampleArray(int capacity)
    {
        this.samples = new Object[capacity][];
        for (int i = 0; i < this.samples.length; i++)
        {
            this.samples[i] = new Object[PREVIOUS_SLOT + 1];
        }
    }

    int getCapacity()
    {
        return samples.length;
    }

    void swap(int i, int j)
    {
        final Object[] tmp = samples[i];
        samples[i] = samples[j];
        samples[j] = tmp;
    }

    int getIndexOf(Object[] sample)
    {
        for (int i = 0; i < samples.length; i++)
        {
            if (sample == samples[i])
            {
                return i;
            }
        }

        return -1;
    }

    Object[] getSample(int index)
    {
        return samples[index];
    }

    static WeakReference<?> getReference(Object[] sample)
    {
        return (WeakReference<?>) sample[REF_SLOT];
    }

    static void setReference(WeakReference<?> value, Object[] sample)
    {
        sample[REF_SLOT] = value;
    }

    static long getSpan(Object[] sample)
    {
        return (long) sample[SPAN_SLOT];
    }

    static void setSpan(long value, Object[] sample)
    {
        sample[SPAN_SLOT] = value;
    }

    static long getAllocationTime(Object[] sample)
    {
        return (long) sample[ALLOCATION_TIME_SLOT];
    }

    static void setAllocationTime(long value, Object[] sample)
    {
        sample[ALLOCATION_TIME_SLOT] = value;
    }

    static long getThreadId(Object[] sample)
    {
        return (long) sample[THREAD_ID_SLOT];
    }

    static void setThreadId(long value, Object[] sample)
    {
        sample[THREAD_ID_SLOT] = value;
    }

    static long getStackTraceId(Object[] sample)
    {
        return (long) sample[STACKTRACE_ID_SLOT];
    }

    static void setStackTraceId(long value, Object[] sample)
    {
        sample[STACKTRACE_ID_SLOT] = value;
    }

    static long getUsedAtGC(Object[] sample)
    {
        return (long) sample[USED_AT_GC_SLOT];
    }

    static void setUsedAtGC(long value, Object[] sample)
    {
        sample[USED_AT_GC_SLOT] = value;
    }

    static int getArrayLength(Object[] sample)
    {
        return (int) sample[ARRAY_LENGTH_SLOT];
    }

    static void setArrayLength(long value, Object[] sample)
    {
        sample[ARRAY_LENGTH_SLOT] = value;
    }

    static Object[] getPrevious(Object[] entry)
    {
        return (Object[]) entry[PREVIOUS_SLOT];
    }

    static void setPrevious(Object[] value, Object[] sample)
    {
        sample[PREVIOUS_SLOT] = value;
    }

//    WeakReference<?> getReference(int index)
//    {
//        return (WeakReference<?>) samples[index][REF_SLOT];
//    }
//
//    void setReference(WeakReference<?> value, int index)
//    {
//        samples[index][REF_SLOT] = value;
//    }

    long getSpan(int index)
    {
        return (long) samples[index][SPAN_SLOT];
    }

    public void clear(Object[] sample)
    {
        SampleArray.setReference(null, sample);
        SampleArray.setSpan(0L, sample);
        SampleArray.setAllocationTime(0L, sample);
        SampleArray.setThreadId(0L, sample);
        SampleArray.setStackTraceId(0L, sample);
        SampleArray.setUsedAtGC(0L, sample);
        SampleArray.setArrayLength(0, sample);
    }

//    void setSpan(long value, int index)
//    {
//        samples[index][SPAN_SLOT] = value;
//    }
//
//    long getAllocationTime(int index)
//    {
//        return (long) samples[index][ALLOCATION_TIME_SLOT];
//    }
//
//    void setAllocationTime(long value, int index)
//    {
//        samples[index][ALLOCATION_TIME_SLOT] = value;
//    }
//
//    long getThreadId(int index)
//    {
//        return (long) samples[index][THREAD_ID_SLOT];
//    }
//
//    void setThreadId(long value, int index)
//    {
//        samples[index][THREAD_ID_SLOT] = value;
//    }
//
//    long getStackTraceId(int index)
//    {
//        return (long) samples[index][STACKTRACE_ID_SLOT];
//    }
//
//    void setStackTraceId(long value, int index)
//    {
//        samples[index][STACKTRACE_ID_SLOT] = value;
//    }
//
//    long getUsedAtGC(int index)
//    {
//        return (long) samples[index][USED_AT_GC_SLOT];
//    }
//
//    void setUsedAtGC(long value, int index)
//    {
//        samples[index][USED_AT_GC_SLOT] = value;
//    }
//
//    int getArrayLength(int index)
//    {
//        return (int) samples[index][ARRAY_LENGTH_SLOT];
//    }
//
//    void setArrayLength(long value, int index)
//    {
//        samples[index][ARRAY_LENGTH_SLOT] = value;
//    }
//
//    Object[] getPrevious(int index)
//    {
//        return (Object[]) samples[index][PREVIOUS_SLOT];
//    }
//
//    void setPrevious(Object[] value, int index)
//    {
//        samples[index][PREVIOUS_SLOT] = value;
//    }
}
