package j.aeron.eventsystem;

import io.aeron.CommonContext;

final class Constants {

   public static final String CHANNEL = CommonContext.IPC_CHANNEL;
   public static final int CACHE_IN_STREAM = 10;
   public static final int CACHE_OUT_STREAM = 20;

   static final int FRAGMENT_COUNT_LIMIT = 10;

}
