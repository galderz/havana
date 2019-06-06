package org.acme.quarkus.sample;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/developer")
public class DeveloperResource
{
    @Inject
    DeveloperRepository developerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Developer> developers(@QueryParam("spanish") boolean spanish)
    {
        if (spanish)
        {
            return developerRepository.findSpanish();
        }

        return Developer.listAll();
    }

    @Transactional
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Developer newDeveloper(Developer developer)
    {
        developer.id = null;
        developer.persist();
        return developer;
    }
}
