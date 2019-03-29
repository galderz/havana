package j.offheap.unsafe;

public class HighestPowerOf2 {

   public static void main(String[] args) {
      int highest = findNextHighestPowerOfTwo(ProcessorInfo.availableProcessors() << 1);
      System.out.println(highest);
      highest = findNextHighestPowerOfTwo(highest);
      System.out.println(highest);
   }

   public static int findNextHighestPowerOfTwo(int num) {
      if (num <= 1) {
         return 1;
      } else if (num >= 0x40000000) {
         return 0x40000000;
      }
      int highestBit = Integer.highestOneBit(num);
      return num <= highestBit ? highestBit : highestBit << 1;
   }

}
