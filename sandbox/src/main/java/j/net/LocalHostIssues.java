package j.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHostIssues {

   public static void main(String[] args) {
      try {
         long start = System.currentTimeMillis();
         InetAddress address = InetAddress.getLocalHost();
         //String computerName = address.getHostName();
         System.out.println(address);
         long end = System.currentTimeMillis();
         System.out.println("Milliseconds run: " + (end - start));
//         if (computerName.indexOf(".") > -1)
//            computerName = computerName.substring(0,
//               computerName.indexOf(".")).toUpperCase();
      } catch (UnknownHostException e) {
         e.printStackTrace();
      }
   }

}
