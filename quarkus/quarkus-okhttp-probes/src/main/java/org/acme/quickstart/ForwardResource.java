package org.acme.quickstart;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/forward")
public class ForwardResource {

   @GET
   @Produces(MediaType.TEXT_PLAIN)
   public String forward() throws IOException {
      OkHttpClient client = new OkHttpClient();
      return run("https://raw.github.com/square/okhttp/master/README.md", client);
   }

   String run(String url, OkHttpClient client) throws IOException {
      Request request = new Request.Builder()
         .url(url)
         .build();

      try (Response response = client.newCall(request).execute()) {
         return response.body().string();
      }
   }

}
