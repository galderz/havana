package org.example.jqwik;

import net.jqwik.api.Arbitraries;

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
    }
}
