package org.acme.quickstart;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/management")
public class ManagementResource {

   private static final okhttp3.MediaType JSON
      = okhttp3.MediaType.get("application/json; charset=utf-8");

   @GET
   @Path("/health")
   @Produces(MediaType.APPLICATION_JSON)
   public String health() throws IOException {
      return post(
         "http://localhost:9990/management"
         , "{\"operation\":\"read-attribute\",\"name\":\"cluster-health\",\"address\":[\"subsystem\",\"datagrid-infinispan\",\"cache-container\",\"clustered\",\"health\",\"HEALTH\"]}"
      );
   }

   @GET
   @Path("/running")
   @Produces(MediaType.APPLICATION_JSON)
   public String running() throws IOException {
      return post(
          "http://localhost:9990/management"
          , "{\"operation\":\"read-attribute\",\"name\":\"server-state\"}"
      );
   }

   private String post(String url, String json) throws IOException {
      final Credentials credentials = new Credentials("admin123", "admin123");
      final DigestAuthenticator authenticator = new DigestAuthenticator(credentials);

      final Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();
      final OkHttpClient client = new OkHttpClient.Builder()
         .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
         .addInterceptor(new AuthenticationCacheInterceptor(authCache))
         .build();

      RequestBody body = RequestBody.create(JSON, json);
      Request request = new Request.Builder()
         .url(url)
         .post(body)
         .build();
      try (Response response = client.newCall(request).execute()) {
         return response.body().string();
      }
   }

}
