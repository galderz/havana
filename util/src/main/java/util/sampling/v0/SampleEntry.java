package util.sampling.v0;

public class SampleEntry {
     Object object;
     long span;
     long allocationTime;
     long threadId;
     long stackTraceId;
     long usedAtGC;
     SampleEntry previous;

     public Object getObject()
     {
          return object;
     }

     public void setObject(Object object)
     {
          this.object = object;
     }

     public long getSpan()
     {
          return span;
     }

     public void setSpan(long span)
     {
          this.span = span;
     }

     public long getAllocationTime()
     {
          return allocationTime;
     }

     public void setAllocationTime(long allocationTime)
     {
          this.allocationTime = allocationTime;
     }

     public long getThreadId()
     {
          return threadId;
     }

     public void setThreadId(long threadId)
     {
          this.threadId = threadId;
     }

     public long getStackTraceId()
     {
          return stackTraceId;
     }

     public void setStackTraceId(long stackTraceId)
     {
          this.stackTraceId = stackTraceId;
     }

     public long getUsedAtGC()
     {
          return usedAtGC;
     }

     public void setUsedAtGC(long usedAtGC)
     {
          this.usedAtGC = usedAtGC;
     }

     public <T extends SampleEntry> T getPrevious()
     {
          return (T) previous;
     }

     public void setPrevious(SampleEntry previous)
     {
          this.previous = previous;
     }
}
