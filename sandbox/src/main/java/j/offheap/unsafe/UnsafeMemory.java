package j.offheap.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ConcurrentHashMap;

class UnsafeMemory {

   private static final Unsafe UNSAFE = UnsafeHolder.UNSAFE;
   private static final UnsafeMemory INSTANCE = new UnsafeMemory();
   private static final int BYTE_ARRAY_BASE_OFFSET = Unsafe.ARRAY_BYTE_BASE_OFFSET;
   private static final ConcurrentHashMap<Long, Long> ALLOCATED_BLOCKS = new ConcurrentHashMap<>();
   private static final boolean TRACE = true;

   private UnsafeMemory() {
   }

   byte getByte(long srcAddress, long offset) {
      checkAddress(srcAddress, offset + 4);
      byte value = UNSAFE.getByte(srcAddress + offset);
      if (TRACE) {
         System.out.printf("Read byte value 0x%02x from address 0x%016x+%d%n", value, srcAddress, offset);
      }
      return value;
   }

   void putByte(long destAddress, long offset, byte value) {
      checkAddress(destAddress, offset + 4);
      if (TRACE) {
         System.out.printf("Wrote byte value 0x%02x to address 0x%016x+%d%n", value, destAddress, offset);
      }
      UNSAFE.putByte(destAddress + offset, value);
   }

   int getInt(long srcAddress, long offset) {
      checkAddress(srcAddress, offset + 4);
      int value = UNSAFE.getInt(srcAddress + offset);
      if (TRACE) {
         System.out.printf("Read int value 0x%08x from address 0x%016x+%d%n", value, srcAddress, offset);
      }
      return value;
   }

   void putInt(long destAddress, long offset, int value) {
      checkAddress(destAddress, offset + 4);
      if (TRACE) {
         System.out.printf("Wrote int value 0x%08x to address 0x%016x+%d%n", value, destAddress, offset);
      }
      UNSAFE.putInt(destAddress + offset, value);
   }

   long getLong(long srcAddress, long offset) {
      return getLong(srcAddress, offset, true);
   }

   long getLongNoTraceIfAbsent(long srcAddress, long offset) {
      return getLong(srcAddress, offset, false);
   }

   private long getLong(long srcAddress, long offset, boolean alwaysTrace) {
      checkAddress(srcAddress, offset + 8);
      long value = UNSAFE.getLong(srcAddress + offset);
      if (TRACE && (alwaysTrace || value != 0)) {
         System.out.printf("Read long value 0x%016x from address 0x%016x+%d%n", value, srcAddress, offset);
      }
      return value;
   }

   void putLong(long destAddress, long offset, long value) {
      checkAddress(destAddress, offset + 8);
      if (TRACE) {
         System.out.printf("Wrote long value 0x%016x to address 0x%016x+%d%n", value, destAddress, offset);
      }
      UNSAFE.putLong(destAddress + offset, value);
   }

   void getBytes(long srcAddress, long srcOffset, byte[] destArray, long destOffset, long length) {
      checkAddress(srcAddress, srcOffset + length);
      if (TRACE) {
         System.out.printf("Read %d bytes from address 0x%016x+%d into array %s+%d%n", length, srcAddress, srcOffset, destArray, destOffset);
      }
      UNSAFE.copyMemory(null, srcAddress + srcOffset, destArray, BYTE_ARRAY_BASE_OFFSET + destOffset, length);
   }

   void putBytes(byte[] srcArray, long srcOffset, long destAddress, long destOffset, long length) {
      checkAddress(destAddress, destOffset + length);
      if (TRACE) {
         System.out.printf("Wrote %d bytes from array %s+%d to address 0x%016x+%d%n", length, srcArray, srcOffset, destAddress, destOffset);
      }
      UNSAFE.copyMemory(srcArray, BYTE_ARRAY_BASE_OFFSET + srcOffset, null, destAddress + destOffset, length);
   }

   /**
    * Only use for debugging
    */
   private static byte[] getBytes(long srcAddress, long srcOffset, int length) {
      checkAddress(srcAddress, srcOffset + length);
      byte[] bytes = new byte[length];
      UNSAFE.copyMemory(null, srcAddress + srcOffset, bytes, BYTE_ARRAY_BASE_OFFSET, length);
      return bytes;
   }

   private static void checkAddress(long address, long offset) {
      if (!TRACE)
         return;

      Long blockSize = ALLOCATED_BLOCKS.get(address);
      if (blockSize == null || blockSize < offset) {
         throw new IllegalArgumentException(String.format("Trying to access address 0x%016x+%d, but blockSize was %d",
            address, offset, blockSize));
      }
   }

   private long allocate(long size) {
      long address = UNSAFE.allocateMemory(size);
      if (TRACE) {
         Long prev = ALLOCATED_BLOCKS.put(address, size);
         if (prev != null) {
            throw new IllegalArgumentException();
         }
      }
      return address;
   }

   void free(long address) {
      Long prev = ALLOCATED_BLOCKS.remove(address);
      if (TRACE) {
         if (prev == null) {
            throw new IllegalArgumentException();
         }
      }
      UNSAFE.freeMemory(address);
   }

   private static class UnsafeHolder {
      static Unsafe UNSAFE = UnsafeHolder.getUnsafe();

      @SuppressWarnings("restriction")
      private static Unsafe getUnsafe() {
         // attempt to access field Unsafe#theUnsafe
         final Object maybeUnsafe = AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
               final Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
               unsafeField.setAccessible(true);
               // the unsafe instance
               return unsafeField.get(null);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
               return e;
            }
         });
         if (maybeUnsafe instanceof Exception) {
            throw new RuntimeException((Exception) maybeUnsafe);
         } else {
            return (Unsafe) maybeUnsafe;
         }
      }
   }

   public static void main(String[] args) {
      // Use unsafe to create an oversized array, which is not allowed on heap.
      long arrayNumElements = Integer.MAX_VALUE + 1L;
      long intSizeInBytes = 4;
      long arrayMemorySize = arrayNumElements * intSizeInBytes;

      // Allocate space for big size
      long startIndex = INSTANCE.allocate(arrayMemorySize);
      // Zero the array
      INSTANCE.UNSAFE.setMemory(startIndex, arrayMemorySize, (byte) 0);

      // Set value in array position 0
      long offset0 = 0 * intSizeInBytes;
      INSTANCE.putInt(startIndex, offset0, 10);

      // Set value in array in maximum position
      long offsetMax = (arrayNumElements - 1) * intSizeInBytes;
      INSTANCE.putInt(startIndex, offsetMax, 20);

      System.out.println(INSTANCE.getInt(startIndex, offset0));
      System.out.println(INSTANCE.getInt(startIndex, offsetMax));
   }

}
