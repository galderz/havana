package org.sample.service.loader.module_a;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.ServiceLoader;

public class PointLookup
{
    public static Point lookup()
    {
        final ServiceLoader<PointFactory> loader = ServiceLoader.load(PointFactory.class);
        final ArrayList<PointFactory> factories = new ArrayList<>();
        loader.forEach(factories::add);

        System.out.println(factories.size());

        // Order by priorities with highest priority first
        factories.sort(Comparator.comparing(PointFactory::priority).reversed());

        return factories.getFirst().newPoint();
    }
}
