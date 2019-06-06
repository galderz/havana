package org.acme.quarkus.sample;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DeveloperRepository implements PanacheRepository<Developer>
{
    public List<Developer> findSpanish()
    {
        // `name` is assumed to be a property of Developer.
        // That's why the query can be defined in a more compact way.
        return list("name like concat('%', ?1, '%')", "Galder");
    }
}
