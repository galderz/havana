package nonblocking;

final class MarshallingCache implements Cache
{
    @Override
    public boolean putIfAbsent(Object key, Object value)
    {
        return false;  // TODO: Customise this generated block
    }

    @Override
    public Object getOrNull(Object key)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public boolean put(Object key, Object value)
    {
        return false;  // TODO: Customise this generated block
    }

    @Override
    public void invalidateAll()
    {
        // TODO: Customise this generated block
    }

    @Override
    public void invalidate(Object key)
    {
        // TODO: Customise this generated block
    }

    @Override
    public long count()
    {
        return 0;  // TODO: Customise this generated block
    }
}
