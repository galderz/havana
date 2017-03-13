package g.java.maps;

import org.infinispan.pojos.SerializablePojo;
import org.infinispan.test.SerializableTestPojo;

import java.io.Serializable;
import java.time.Instant;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeSet;

public class ExternallyMarshallableViaMap {

   private static final TreeSet<String> whiteListClasses =
         new TreeSet<>(new ContainsComparator());

   static {
      whiteListClasses.add("Exception");
      whiteListClasses.add("$$Lambda$");
      whiteListClasses.add("java.lang.Class");
      whiteListClasses.add("java.time.Instant"); // prod
      whiteListClasses.add("org.hibernate.cache"); // prod
      whiteListClasses.add("org.hibernate.search.query.engine.impl.LuceneHSQuery"); // prod
      whiteListClasses.add("org.infinispan.distexec.RunnableAdapter"); // prod
      whiteListClasses.add("org.infinispan.jcache.annotation.DefaultCacheKey"); // prod
      whiteListClasses.add("org.infinispan.query.clustered.QueryResponse"); // prod
      whiteListClasses.add("org.infinispan.server.core.transport.NettyTransport$ConnectionAdderTask"); // prod
      whiteListClasses.add("org.infinispan.server.hotrod.CheckAddressTask"); // prod
      whiteListClasses.add("org.infinispan.server.infinispan.task.DistributedServerTask"); // prod
      whiteListClasses.add("org.infinispan.scripting.impl.DataType"); // prod
      whiteListClasses.add("org.infinispan.scripting.impl.DistributedScript");
      whiteListClasses.add("org.infinispan.stats.impl.ClusterCacheStatsImpl$DistributedCacheStatsCallable"); // prod
      whiteListClasses.add("org.infinispan.xsite.BackupSender$TakeSiteOfflineResponse"); // prod
      whiteListClasses.add("org.infinispan.xsite.BackupSender$BringSiteOnlineResponse"); // prod
      whiteListClasses.add("org.infinispan.xsite.XSiteAdminCommand$Status"); // prod
      whiteListClasses.add("org.infinispan.util.logging.events.EventLogLevel"); // prod
      whiteListClasses.add("org.infinispan.util.logging.events.EventLogCategory"); // prod
      whiteListClasses.add("java.util.Date"); // test
      whiteListClasses.add("java.lang.Byte"); // test
      whiteListClasses.add("java.lang.Integer"); // test
      whiteListClasses.add("java.lang.Double"); // test
      whiteListClasses.add("java.lang.Short"); // test
      whiteListClasses.add("java.lang.Long"); // test
      whiteListClasses.add("org.infinispan.test"); // test
      whiteListClasses.add("org.infinispan.server.test"); // test
      whiteListClasses.add("org.infinispan.it"); // test
      whiteListClasses.add("org.infinispan.all"); // test
      whiteListClasses.add("org.jboss.as.quickstarts.datagrid"); // quickstarts testing
   }

   private ExternallyMarshallableViaMap() {
      // Static class
   }

   public static boolean isAllowed(Object obj) {
      Class<?> clazz = obj.getClass();
      return isAllowed(clazz);
   }

   public static boolean isAllowed(Class<?> clazz) {
      Package pkg = clazz.getPackage();
      if (pkg == null) {
         if (clazz.isArray()) {
            return true;
         } else {
            throw new IllegalStateException("No package info for " + clazz + ", runtime-generated class?");
         }
      }
      String pkgName = pkg.getName();
      boolean isBlackList =
            Serializable.class.isAssignableFrom(clazz)
                  && isMarshallablePackage(pkgName)
                  && !isWhiteList(clazz.getName());
      return !isBlackList;
   }

   private static boolean isMarshallablePackage(String pkg) {
      return pkg.startsWith("java.")
            || pkg.startsWith("org.infinispan.")
            || pkg.startsWith("org.jgroups.")
            || pkg.startsWith("org.hibernate")
            || pkg.startsWith("org.apache")
            || pkg.startsWith("org.jboss")
            || pkg.startsWith("com.arjuna")
            ;
   }

   private static boolean isWhiteList(String className) {
      return whiteListClasses.contains(className);
//      return className.endsWith("Exception")
//            || className.contains("$$Lambda$")
//            || className.equals("java.lang.Class")
//            || className.equals("java.time.Instant") // prod
//            || className.startsWith("org.hibernate.cache") // prod
//            || className.equals("org.hibernate.search.query.engine.impl.LuceneHSQuery") // prod
//            || className.equals("org.infinispan.distexec.RunnableAdapter") // prod
//            || className.equals("org.infinispan.jcache.annotation.DefaultCacheKey") // prod
//            || className.equals("org.infinispan.query.clustered.QueryResponse") // prod
//            || className.equals("org.infinispan.server.core.transport.NettyTransport$ConnectionAdderTask") // prod
//            || className.equals("org.infinispan.server.hotrod.CheckAddressTask") // prod
//            || className.equals("org.infinispan.server.infinispan.task.DistributedServerTask") // prod
//            || className.equals("org.infinispan.scripting.impl.DataType") // prod
//            || className.equals("org.infinispan.scripting.impl.DistributedScript")
//            || className.equals("org.infinispan.stats.impl.ClusterCacheStatsImpl$DistributedCacheStatsCallable") // prod
//            || className.equals("org.infinispan.xsite.BackupSender$TakeSiteOfflineResponse") // prod
//            || className.equals("org.infinispan.xsite.BackupSender$BringSiteOnlineResponse") // prod
//            || className.equals("org.infinispan.xsite.XSiteAdminCommand$Status") // prod
//            || className.equals("org.infinispan.util.logging.events.EventLogLevel") // prod
//            || className.equals("org.infinispan.util.logging.events.EventLogCategory") // prod
//            || className.equals("java.util.Date") // test
//            || className.equals("java.lang.Byte") // test
//            || className.equals("java.lang.Integer") // test
//            || className.equals("java.lang.Double") // test
//            || className.equals("java.lang.Short") // test
//            || className.equals("java.lang.Long") // test
//            || className.startsWith("org.infinispan.test") // test
//            || className.startsWith("org.infinispan.server.test") // test
//            || className.startsWith("org.infinispan.it") // test
//            || className.startsWith("org.infinispan.all") // test
//            || className.contains("org.jboss.as.quickstarts.datagrid") // quickstarts testing
//            ;
   }

   static final class ContainsComparator implements Comparator<String> {

      @Override
      public int compare(String o1, String o2) {
         if (o1.contains(o2))
            return 0;
         else
            return o1.compareTo(o2);
      }

   }

   public static void main(String[] args) {
      System.out.println(ExternallyMarshallableViaMap.isAllowed(Instant.now()));
      System.out.println(ExternallyMarshallableViaMap.isAllowed(new SerializablePojo()));
      System.out.println(ExternallyMarshallableViaMap.isAllowed(new SerializableTestPojo()));
   }

}
