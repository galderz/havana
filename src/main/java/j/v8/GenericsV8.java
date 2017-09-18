package j.v8;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GenericsV8 {

   public static void main(String[] args) {
      ExtendedMap<Integer, String> map = new ExtendedMapImpl<>();

      int range = 10;
      IntStream.range(0, range).boxed().forEach(i -> map.put(i, i + "-value"));
      assertEquals(range, map.size());

      ExtendedSet<Map.Entry<Integer, String>> entrySet = map.entrySet();

      // List<Map.Entry<Integer, String>> list = jdkCollect(entrySet);
      // List<Map.Entry<Integer, String>> list = helperCollect(entrySet);
      List<Map.Entry<Integer, String>> list = supplierCollectMethodRef(entrySet);


      assertEquals(map.size(), list.size());
   }

   private static List<Map.Entry<Integer, String>> supplierCollectLambda(ExtendedSet<Map.Entry<Integer, String>> entrySet) {
      return entrySet.stream().sorted(
         (e1, e2) -> Integer.compare(e1.getKey(), e2.getKey())).collect(
         () -> Collectors.toList());
   }

   private static List<Map.Entry<Integer, String>> supplierCollectMethodRef(ExtendedSet<Map.Entry<Integer, String>> entrySet) {
      return entrySet.stream().sorted(
         (e1, e2) -> Integer.compare(e1.getKey(), e2.getKey())).collect(
         Collectors::toList);
   }

   private static List<Map.Entry<Integer, String>> helperCollect(ExtendedSet<Map.Entry<Integer, String>> entrySet) {
      return entrySet.stream().sorted(
         (e1, e2) -> Integer.compare(e1.getKey(), e2.getKey())).collect(
         Helper.serialCollector(Collectors::toList));
   }

   private static List<Map.Entry<Integer, String>> jdkCollect(ExtendedSet<Map.Entry<Integer, String>> entrySet) {
      return entrySet.stream().sorted(
         (e1, e2) -> Integer.compare(e1.getKey(), e2.getKey())).collect(
         Collectors.toList());
   }

   static <T> void assertEquals(Object expected, Object actual) {
      System.out.printf("expected:<%s>, actual:<%s>%n", expected, actual);
      assert expected == actual;
   }

   static final class Helper {
      public static <T, R> Collector<T, ?, R> serialCollector(SerializableSupplier<Collector<T, ?, R>> s) {
         return s.get();
      }
   }

   interface SerializableSupplier<T> extends Serializable, Supplier<T> {
   }

   interface ExtendedStream<R> extends Stream<R> {

      @Override
      ExtendedStream<R> sorted(Comparator<? super R> comparator);

      <R1> R1 collect(SerializableSupplier<Collector<? super R, ?, R1>> supplier);

   }

   interface ExtendedSet<E> extends Set<E> {

      @Override
      ExtendedStream<E> stream();

   }

   interface ExtendedMap<K, V> extends ConcurrentMap<K, V> {

      @Override
      ExtendedSet<Entry<K, V>> entrySet();

   }

   public final static class ExtendedMapImpl<K, V> implements ExtendedMap<K, V> {

      private final ConcurrentMap<K, V> map = new ConcurrentHashMap<>();

      @Override
      public V put(K key, V value) {
         return map.put(key, value);
      }

      @Override
      public int size() {
         return map.size();
      }

      @Override
      public ExtendedSet<Entry<K, V>> entrySet() {
         return new ExtendedSetImpl<>(map.entrySet());
      }

      @Override
      public boolean isEmpty() {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean containsKey(Object key) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean containsValue(Object value) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public V get(Object key) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public V remove(Object key) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public void putAll(Map<? extends K, ? extends V> m) {
         // TODO: Customise this generated block
      }

      @Override
      public void clear() {
         // TODO: Customise this generated block
      }

      @Override
      public Set<K> keySet() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Collection<V> values() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public V putIfAbsent(K key, V value) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public boolean remove(Object key, Object value) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean replace(K key, V oldValue, V newValue) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public V replace(K key, V value) {
         return null;  // TODO: Customise this generated block
      }

   }

   public static final class ExtendedSetImpl<E> implements ExtendedSet<E> {

      private final Set<E> set;

      public ExtendedSetImpl(Set<E> set) {
         this.set = set;
      }

      @Override
      public ExtendedStream<E> stream() {
         return new ExtendedStreamImpl<>(StreamSupport.stream(spliterator(), false));
      }

      @Override
      public Iterator<E> iterator() {
         return set.iterator();
      }

      @Override
      public int size() {
         return set.size();
      }

      @Override
      public boolean isEmpty() {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean contains(Object o) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public Object[] toArray() {
         return new Object[0];  // TODO: Customise this generated block
      }

      @Override
      public <T> T[] toArray(T[] a) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public boolean add(E e) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean remove(Object o) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean containsAll(Collection<?> c) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean addAll(Collection<? extends E> c) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean retainAll(Collection<?> c) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean removeAll(Collection<?> c) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public void clear() {
         // TODO: Customise this generated block
      }
   }

   public static final class ExtendedStreamImpl<T> implements ExtendedStream<T> {

      private final Stream<T> stream;

      public ExtendedStreamImpl(Stream<T> stream) {
         this.stream = stream;
      }

      @Override
      public ExtendedStream<T> sorted(Comparator<? super T> comparator) {
         return new ExtendedStreamImpl<>(stream.sorted(comparator));
      }

      @Override
      public <R, A> R collect(Collector<? super T, A, R> collector) {
         return stream.collect(collector);
      }

      @Override
      public <R1> R1 collect(SerializableSupplier<Collector<? super T, ?, R1>> supplier) {
         return stream.collect(supplier.get());
      }

      @Override
      public Iterator<T> iterator() {
         return stream.iterator();
      }

      @Override
      public Stream<T> filter(Predicate<? super T> predicate) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public <R> Stream<R> map(Function<? super T, ? extends R> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public IntStream mapToInt(ToIntFunction<? super T> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public LongStream mapToLong(ToLongFunction<? super T> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> distinct() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> sorted() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> peek(Consumer<? super T> action) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> limit(long maxSize) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> skip(long n) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public void forEach(Consumer<? super T> action) {
         // TODO: Customise this generated block
      }

      @Override
      public void forEachOrdered(Consumer<? super T> action) {
         // TODO: Customise this generated block
      }

      @Override
      public Object[] toArray() {
         return new Object[0];  // TODO: Customise this generated block
      }

      @Override
      public <A> A[] toArray(IntFunction<A[]> generator) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public T reduce(T identity, BinaryOperator<T> accumulator) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Optional<T> reduce(BinaryOperator<T> accumulator) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator, BiConsumer<R, R> combiner) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Optional<T> min(Comparator<? super T> comparator) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Optional<T> max(Comparator<? super T> comparator) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public long count() {
         return 0;  // TODO: Customise this generated block
      }

      @Override
      public boolean anyMatch(Predicate<? super T> predicate) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean allMatch(Predicate<? super T> predicate) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public boolean noneMatch(Predicate<? super T> predicate) {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public Optional<T> findFirst() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Optional<T> findAny() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Spliterator<T> spliterator() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public boolean isParallel() {
         return false;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> sequential() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> parallel() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> unordered() {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public Stream<T> onClose(Runnable closeHandler) {
         return null;  // TODO: Customise this generated block
      }

      @Override
      public void close() {
         // TODO: Customise this generated block
      }
   }

}
