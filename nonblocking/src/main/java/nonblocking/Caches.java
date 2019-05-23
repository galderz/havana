package nonblocking;

import nonblocking.aeron.AeronCache;
import nonblocking.aeron.AeronSystem;
import nonblocking.aeron.CacheStage;

public class Caches implements AutoCloseable
{
    private final CacheStage cacheStage = new CacheStage();

    public BinaryCache cache()
    {
        return new AeronCache();
    }

    @Override
    public void close()
    {
        try
        {
            AeronSystem.AERON.close();
            cacheStage.close();
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

}
