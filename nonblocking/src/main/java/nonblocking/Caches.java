package nonblocking;

import nonblocking.aeron.AeronCache;
import nonblocking.aeron.AeronSystem;
import nonblocking.aeron.CacheStage;

public class Caches implements AutoCloseable
{
    private final CacheStage cacheStage = new CacheStage();
    private AeronCache aeronCache;

    public BinaryCache cache()
    {
        aeronCache = new AeronCache();
        return aeronCache;
    }

    @Override
    public void close()
    {
        try
        {
            AeronSystem.AERON.close();
            cacheStage.close();
            aeronCache.close();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

}
