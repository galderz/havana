package org.acme.restclient;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.Set;

@Path("/v2")
@RegisterRestClient
public interface CountriesService {

   // TODO: While @Consumes and @Produces are optional as auto-negotiation is supported, it is heavily recommended to annotate your endpoints with them to define precisely the expected content-types.
   // TODO: It will allow to narrow down the number of JAX-RS providers (which can be seen as converters) included in the native executable.

   @GET
   @Path("/name/{name}")
   @Produces("application/json")
   Set<Country> getByName(@PathParam("name") String name);

}
