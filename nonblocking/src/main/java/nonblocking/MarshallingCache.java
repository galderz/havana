package nonblocking;

final class MarshallingCache implements Cache {

   @Override
   public boolean putIfAbsent(Object key, Object value) {
      return false;  // TODO: Customise this generated block
   }

   @Override
   public Object getOrNull(Object key) {
      return null;  // TODO: Customise this generated block
   }

}
