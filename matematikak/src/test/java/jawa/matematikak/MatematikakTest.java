package jawa.matematikak;

import org.junit.jupiter.api.Test;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.*;

public class MatematikakTest
{
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
