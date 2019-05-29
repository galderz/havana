package nonblocking;

interface Cache {

   boolean putIfAbsent(Object key, Object value);

   Object getOrNull(Object key);

   boolean put(Object key, Object value);

   void invalidateAll();

   void invalidate(Object key);

   long count();

}
