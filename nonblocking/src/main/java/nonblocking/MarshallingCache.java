package nonblocking;

final class MarshallingCache implements Cache {

   @Override
   public boolean putIfAbsent(Object key, Object value) {
      return false;  // TODO: Customise this generated block
   }

}
