package j.aeron.eventsystem;

final class EventSystem {

   private static final CacheStage CACHE_STAGE = new CacheStage();

   static CacheStage cacheStage() {
      return CACHE_STAGE;
   }

}
