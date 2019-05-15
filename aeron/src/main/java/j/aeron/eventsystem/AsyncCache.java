package j.aeron.eventsystem;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

class AsyncCache {

   private AtomicInteger msgId = new AtomicInteger();
   private ConcurrentMap<Integer, CompletionStage> completions = new ConcurrentHashMap<>();

   CompletionStage putIfAbsent(byte[] key, byte[] value) {
      final CompletableFuture future = new CompletableFuture();
      completions.put(msgId.getAndIncrement(), future);
      EventSystem.cacheStage().sink().putIfAbsent(key, value);
      return future;
   }

}
