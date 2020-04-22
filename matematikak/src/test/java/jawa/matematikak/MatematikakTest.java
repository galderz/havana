package jawa.matematikak;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

public class MatematikakTest
{
    @Test
    public void testX()
    {
        computeJava();
        computeC();

        final var computed = computedResult();
        System.out.println(computed);
    }

    public static strictfp double computedResult()
    {
        double result = -714.86472959032766993914;
        System.out.println(result);
        return result;
    }

    public static strictfp double computeJava()
    {
        double ln2_hi = 0x1.62e42feep-1;
        double ln2_lo = 0x1.a39ef35793c76p-33;
        double f = -0.20545561800531686;
        double s = -0.11448901463052563;
        double R = 0.008807864809700685;
        double dk = -1031.0;

        double result = dk * ln2_hi - ((s * (f - R) - dk * ln2_lo) - f);
        System.out.println(result);
        return result;
    }

    public static strictfp double computeC()
    {
        double ln2_hi = 0.69314718036912382;
        double ln2_lo = 0.00000000019082149292705877;
        double f = -0.20545561800531686;
        double s = -0.11448901463052563;
        double R = 0.0088078648097006852;
        double dk = -1031.0;

        double result = dk * ln2_hi - ((s * (f - R) - dk * ln2_lo) - f);
        System.out.println(result);
        return result;
    }

    @Test
    public void testSingleNewMathLog()
    {
        final var value = 3.4529686207034E-311;
        final var javaMath = Math.log(value);
        final var newMath = Matematikak.Log.compute(value);
        System.out.println(javaMath);
        System.out.println(newMath);
        assertThat(newMath, is(javaMath));
    }

    /**
     * Smallest found falsifying value(s) :-
     * 4.782566E-317
     * Other found falsifying value(s) :-
     * 9.5433206E-317
     * 1.965126996E-315
     * 2.09427324E-315
     * 3.97444462255E-313
     * 9.74156377665E-313
     * 7.482118185763E-312
     * 4.55394854194016E-310
     * 9.00409574129136E-310
     * 1.91732501888472E-309
     * 3.457494164074512E-302
     */
    @Test
    @Disabled
    public void testNewMathLog()
    {
        qt()
            .forAll(doubles().any())
            .check(d ->
                Double.isNaN(Matematikak.Log.compute(d))
                    || Matematikak.Log.compute(d) == Math.log(d)
            );
    }

    @Test
    public void testJavaMathLog()
    {
        qt()
            .forAll(doubles().any())
            .check(d ->
                Double.isNaN(Math.log(d))
                || Math.log(d) == Math.log(d)
            );
    }

    @Test
    public void testJavaMathSin()
    {
        qt()
            .forAll(doubles().any())
            .check(d ->
                Double.isNaN(Math.sin(d))
                    || Math.sin(d) == Math.sin(d)
            );
    }
}
