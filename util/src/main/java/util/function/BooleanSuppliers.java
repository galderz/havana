package util.function;

import util.Asserts;

import java.util.function.BooleanSupplier;

public class BooleanSuppliers
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();

        BooleanSupplier alwaysTrue = () -> true;
        BooleanSupplier alwaysFalse = () -> false;

        assert alwaysTrue.getAsBoolean();
        assert !alwaysFalse.getAsBoolean();

        assert and(alwaysTrue, alwaysTrue).getAsBoolean();
        assert !and(alwaysTrue, alwaysFalse).getAsBoolean();
        assert !and(alwaysFalse, alwaysTrue).getAsBoolean();
        assert !and(alwaysFalse, alwaysFalse).getAsBoolean();

        assert or(alwaysTrue, alwaysTrue).getAsBoolean();
        assert or(alwaysTrue, alwaysFalse).getAsBoolean();
        assert or(alwaysFalse, alwaysTrue).getAsBoolean();
        assert !or(alwaysFalse, alwaysFalse).getAsBoolean();

        System.out.println("Success");
    }

    static BooleanSupplier and(BooleanSupplier cond1, BooleanSupplier cond2)
    {
        return () -> cond1.getAsBoolean() && cond2.getAsBoolean();
    }

    static BooleanSupplier or(BooleanSupplier cond1, BooleanSupplier cond2)
    {
        return () -> cond1.getAsBoolean() || cond2.getAsBoolean();
    }
}
