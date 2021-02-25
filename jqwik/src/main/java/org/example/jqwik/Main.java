package org.example.jqwik;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Shrinkable;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args)
    {
        {
            final var l1 = Arbitraries.longs().sampleStream().limit(1000);
            System.out.println(l1.count());
        }

        {
            final var maps = Arbitraries.maps(Arbitraries.longs(), Arbitraries.longs());
            final var values = maps.sampleStream().limit(1000);
            System.out.println(values.findFirst().get().size());
        }

        {
            final var entries = Arbitraries.entries(Arbitraries.longs(), Arbitraries.longs());
            System.out.println(entries.sampleStream().limit(1000).count());
        }

        {
            System.out.println(
                Arbitraries.doubles().sampleStream().limit(1000).filter(d -> Double.MIN_VALUE == d).findFirst()
            );

            System.out.println(
                Arbitraries.doubles()
                    .edgeCases(cfg -> cfg.add(Double.MIN_VALUE))
                    .sampleStream().limit(1000)
                    .filter(d -> Double.MIN_VALUE == d).findFirst()
            );

            // Only works in 1.3.10
            System.out.println(
                Arbitraries.doubles().edgeCases(cfg -> cfg.add(Double.MIN_VALUE))
                    .edgeCases().suppliers().stream().map(Supplier::get).map(Shrinkable::value)
                    //.collect(Collectors.toList())
                    .filter(d -> d == Double.MIN_VALUE).findFirst()
            );

            final var combinator = Combinators.combine(
                Arbitraries.doubles().edgeCases(cfg -> cfg.add(Double.MIN_VALUE))
                , Arbitraries.doubles().edgeCases(cfg -> cfg.add(Double.MIN_VALUE))
            );

            System.out.println(
                combinator.as(Map::entry)
                    .edgeCases().suppliers().stream().map(Supplier::get).map(Shrinkable::value)
                    .collect(Collectors.toList())
            );
        }
    }
}
