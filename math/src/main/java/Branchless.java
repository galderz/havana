public class Branchless
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        LoadToType.test();
    }

    /**
     * Map each bytecode to its type.
     * E.g. ILOAD_* -> ILOAD_0, LLOAD_* -> LLOAD_0...etc
     */
    private static final class LoadToType
    {
        static final int ILOAD_0 =  26;
        static final int ILOAD_1 =  27;
        static final int ILOAD_2 =  28;
        static final int ILOAD_3 =  29;
        static final int LLOAD_0 =  30;
        static final int LLOAD_1 =  31;
        static final int LLOAD_2 =  32;
        static final int LLOAD_3 =  33;
        static final int FLOAD_0 =  34;
        static final int FLOAD_1 =  35;
        static final int FLOAD_2 =  36;
        static final int FLOAD_3 =  37;
        static final int DLOAD_0 =  38;
        static final int DLOAD_1 =  39;
        static final int DLOAD_2 =  40;
        static final int DLOAD_3 =  41;
        static final int ALOAD_0 =  42;
        static final int ALOAD_1 =  43;
        static final int ALOAD_2 =  44;
        static final int ALOAD_3 =  45;

        static int map(int bytecode)
        {
            return (((bytecode - ILOAD_0) >>> 2) << 2) + ILOAD_0;
        }

        static void test()
        {
            assert map(ILOAD_0) == ILOAD_0;
            assert map(ILOAD_1) == ILOAD_0;
            assert map(ILOAD_2) == ILOAD_0;
            assert map(ILOAD_3) == ILOAD_0;

            assert map(LLOAD_0) == LLOAD_0;
            assert map(LLOAD_1) == LLOAD_0;
            assert map(LLOAD_2) == LLOAD_0;
            assert map(LLOAD_3) == LLOAD_0;

            assert map(FLOAD_0) == FLOAD_0;
            assert map(FLOAD_1) == FLOAD_0;
            assert map(FLOAD_2) == FLOAD_0;
            assert map(FLOAD_3) == FLOAD_0;

            assert map(DLOAD_0) == DLOAD_0;
            assert map(DLOAD_1) == DLOAD_0;
            assert map(DLOAD_2) == DLOAD_0;
            assert map(DLOAD_3) == DLOAD_0;

            assert map(ALOAD_0) == ALOAD_0;
            assert map(ALOAD_1) == ALOAD_0;
            assert map(ALOAD_2) == ALOAD_0;
            assert map(ALOAD_3) == ALOAD_0;
        }
    }
}
