package net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class LocalPortClash {

   static final String HOST = "127.0.0.1";
   static final int PORT = 4444;

   public static void main(String[] args) throws Exception {
      server();
      Thread.sleep(2000L);
      client();
   }

   private static void client() throws IOException {
      try (
         Socket s = new Socket(HOST, PORT, InetAddress.getByName("127.0.0.1"), PORT);
         PrintWriter out =
            new PrintWriter(s.getOutputStream(), true);
         BufferedReader in =
            new BufferedReader(
               new InputStreamReader(s.getInputStream()));
         BufferedReader stdIn =
            new BufferedReader(
               new InputStreamReader(System.in))
      ) {
         String userInput;
         while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("echo: " + in.readLine());
         }
      } catch (Exception e) {
         throw e;
      }
   }

   private static Thread server() {
      Runnable r = () -> {
         try (
            ServerSocket serverSocket =
                  new ServerSocket(PORT);
            Socket clientSocket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                  new InputStreamReader(clientSocket.getInputStream()));
         ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
               System.out.println("[Server] " + inputLine);
            }
         } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                  + PORT + " or listening for a connection");
            System.out.println(e.getMessage());
         }
      };
      Thread t = new Thread(r);
      t.start();
      return t;
   }

}
