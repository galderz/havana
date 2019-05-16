package j.aeron.eventsystem;

final class EventSystem {

   private static final CacheStage CACHE_STAGE = new CacheStage();
   private static final BridgeStage CACHE_BRIDGE = new BridgeStage();

   static CacheStage cacheStage() {
      return CACHE_STAGE;
   }
   
   static BridgeStage cacheBridge() {
      return CACHE_BRIDGE;
   }

   static void stop() {
      try {
         Aeron.AERON.stop();
         CACHE_BRIDGE.stop();
         CACHE_STAGE.stop();
      } catch (InterruptedException e) {
         Thread.currentThread().interrupt();
      }
   }

}
