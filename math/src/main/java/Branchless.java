public class Branchless
{
    public static void main(String[] args)
    {
        Asserts.needEnabledAsserts();
        LoadToIndexAndType.test();
        LoadToIndex.test();
        LoadToType.test();
    }

    /**
     * Map each bytecode such that,
     * you get a mapping of bytecode to index,
     * and a mapping of bytecode to type.
     *
     * ILOAD_0 -> 0, ILOAD_1 -> 1...
     * LLOAD_0 -> 0, LLOAD_1 -> 1...etc
     *
     * And:
     * ILOAD_0 -> 0, ILOAD_1 -> 0...
     * LLOAD_0 -> 1, LLOAD_1 -> 1...etc
     */
    private static final class LoadToIndexAndType
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

        static int[] map(int bytecode)
        {
            final int base = bytecode - ILOAD_0;
            return new int[]{
                base & 3 // index
                , base >>> 2 // type
            };
        }

        static void test()
        {
            testIndex();
            testType();
        }

        static void testIndex()
        {
            assert map(ILOAD_0)[0] == 0;
            assert map(ILOAD_1)[0] == 1;
            assert map(ILOAD_2)[0] == 2;
            assert map(ILOAD_3)[0] == 3;
            assert map(LLOAD_0)[0] == 0;
            assert map(LLOAD_1)[0] == 1;
            assert map(LLOAD_2)[0] == 2;
            assert map(LLOAD_3)[0] == 3;
            assert map(FLOAD_0)[0] == 0;
            assert map(FLOAD_1)[0] == 1;
            assert map(FLOAD_2)[0] == 2;
            assert map(FLOAD_3)[0] == 3;
            assert map(DLOAD_0)[0] == 0;
            assert map(DLOAD_1)[0] == 1;
            assert map(DLOAD_2)[0] == 2;
            assert map(DLOAD_3)[0] == 3;
            assert map(ALOAD_0)[0] == 0;
            assert map(ALOAD_1)[0] == 1;
            assert map(ALOAD_2)[0] == 2;
            assert map(ALOAD_3)[0] == 3;
        }

        static void testType()
        {
            assert map(ILOAD_0)[1] == 0;
            assert map(ILOAD_1)[1] == 0;
            assert map(ILOAD_2)[1] == 0;
            assert map(ILOAD_3)[1] == 0;
            assert map(LLOAD_0)[1] == 1;
            assert map(LLOAD_1)[1] == 1;
            assert map(LLOAD_2)[1] == 1;
            assert map(LLOAD_3)[1] == 1;
            assert map(FLOAD_0)[1] == 2;
            assert map(FLOAD_1)[1] == 2;
            assert map(FLOAD_2)[1] == 2;
            assert map(FLOAD_3)[1] == 2;
            assert map(DLOAD_0)[1] == 3;
            assert map(DLOAD_1)[1] == 3;
            assert map(DLOAD_2)[1] == 3;
            assert map(DLOAD_3)[1] == 3;
            assert map(ALOAD_0)[1] == 4;
            assert map(ALOAD_1)[1] == 4;
            assert map(ALOAD_2)[1] == 4;
            assert map(ALOAD_3)[1] == 4;
        }
    }

    /**
     * Map each bytecode to its index.
     * E.g. ILOAD_* -> 0, LLOAD_* -> 1...etc
     */
    private static final class LoadToIndex
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
            return (bytecode - ILOAD_0) >>> 2;
        }

        static void test()
        {
            assert map(ILOAD_0) == 0;
            assert map(ILOAD_1) == 0;
            assert map(ILOAD_2) == 0;
            assert map(ILOAD_3) == 0;

            assert map(LLOAD_0) == 1;
            assert map(LLOAD_1) == 1;
            assert map(LLOAD_2) == 1;
            assert map(LLOAD_3) == 1;

            assert map(FLOAD_0) == 2;
            assert map(FLOAD_1) == 2;
            assert map(FLOAD_2) == 2;
            assert map(FLOAD_3) == 2;

            assert map(DLOAD_0) == 3;
            assert map(DLOAD_1) == 3;
            assert map(DLOAD_2) == 3;
            assert map(DLOAD_3) == 3;

            assert map(ALOAD_0) == 4;
            assert map(ALOAD_1) == 4;
            assert map(ALOAD_2) == 4;
            assert map(ALOAD_3) == 4;
        }
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
