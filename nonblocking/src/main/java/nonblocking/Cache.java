package nonblocking;

interface Cache {

   boolean putIfAbsent(Object key, Object value);

   Object getOrNull(Object key);

}
