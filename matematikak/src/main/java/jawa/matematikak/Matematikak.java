package jawa.matematikak;

public class Matematikak
{
    public static void main(String[] args)
    {
        System.out.printf("ln2_hi=%a%n", 6.93147180369123816490e-01);
        System.out.printf("ln2_lo=%a%n", 1.90821492927058770002e-10);
        System.out.printf("two54=%a%n", 1.80143985094819840000e+16);
        System.out.printf("Lg1=%a%n", 6.666666666666735130e-01);
        System.out.printf("Lg2=%a%n", 3.999999999940941908e-01);
        System.out.printf("Lg3=%a%n", 2.857142874366239149e-01);
        System.out.printf("Lg4=%a%n", 2.222219843214978396e-01);
        System.out.printf("Lg5=%a%n", 1.818357216161805012e-01);
        System.out.printf("Lg6=%a%n", 1.531383769920937332e-01);
        System.out.printf("Lg7=%a%n", 1.479819860511658591e-01);
    }

    public static class Log
    {
        private static final double ln2_hi = 0x1.62e42feep-1;       // 6.93147180369123816490e-01
        private static final double ln2_lo = 0x1.a39ef35793c76p-33; // 1.90821492927058770002e-10
        private static final double two54 = 0x1.0p54;               // 1.80143985094819840000e+16
        private static final double Lg1 = 0x1.5555555555593p-1; // 6.666666666666735130e-01
        private static final double Lg2 = 0x1.999999997fa04p-2; // 3.999999999940941908e-01
        private static final double Lg3 = 0x1.2492494229359p-2; // 2.857142874366239149e-01
        private static final double Lg4 = 0x1.c71c51d8e78afp-3; // 2.222219843214978396e-01
        private static final double Lg5 = 0x1.7466496cb03dep-3; // 1.818357216161805012e-01
        private static final double Lg6 = 0x1.39a09d078c69fp-3; // 1.531383769920937332e-01
        private static final double Lg7 = 0x1.2f112df3e5244p-3; // 1.479819860511658591e-01
        private static final double zero = 0.0;

        public static strictfp double compute(double x)
        {
            double hfsq;
            double f;
            double s;
            double z;
            double R;
            double w;
            double t1;
            double t2;
            double dk;
            int k;
            int hx;
            int i;
            int j;
            /*unsigned*/ int lx;

            hx = __HI(x);           /* high word of x */
            lx = __LO(x);           /* low  word of x */

            k = 0;
            if (hx < 0x00100000)
            {                  /* x < 2**-1022  */
                if (((hx & 0x7fffffff) | lx) == 0)
                    return -two54 / zero;             /* log(+-0)=-inf */
                if (hx < 0)
                    return (x - x) / zero;        /* log(-#) = NaN */
                k -= 54;    /* subnormal number, scale up x */
                x *= two54; /* subnormal number, scale up x */
                hx = __HI(x);               /* high word of x */
            }
            if (hx >= 0x7ff00000)
                return x + x;
            k += (hx >> 20) - 1023;
            hx &= 0x000fffff;
            i = (hx + 0x95f64) & 0x100000;
            x = __HI(x, hx | (i ^ 0x3ff00000));    /* normalize x or x/2 */
            k += (i >> 20);
            f = x - 1.0;
            if ((0x000fffff & (2 + hx)) < 3)
            {     /* |f| < 2**-20 */
                if (f == zero)
                {
                    if (k == 0) return zero;
                    else
                    {
                        dk = (double) k;
                        return dk * ln2_hi + dk * ln2_lo;
                    }
                }
                R = f * f * (0.5 - 0.33333333333333333 * f);
                if (k == 0)
                    return f - R;
                else
                {
                    dk = (double) k;
                    return dk * ln2_hi - ((R - dk * ln2_lo) - f);
                }
            }
            s = f / (2.0 + f);
            dk = (double) k;
            z = s * s;
            i = hx - 0x6147a;
            w = z * z;
            j = 0x6b851 - hx;
            t1 = w * (Lg2 + w * (Lg4 + w * Lg6));
            t2 = z * (Lg1 + w * (Lg3 + w * (Lg5 + w * Lg7)));
            i |= j;
            R = t2 + t1;

            if (i > 0)
            {
                hfsq = 0.5 * f * f;
                if (k == 0)
                    return f - (hfsq - s * (hfsq + R));
                else
                    return dk * ln2_hi - ((hfsq - (s * (hfsq + R) + dk * ln2_lo)) - f);
            }
            else
            {
                if (k == 0)
                    return f - s * (f - R);
                else
                    return dk * ln2_hi - ((s * (f - R) - dk * ln2_lo) - f);
            }
        }

        private Log()
        {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Return the low-order 32 bits of the double argument as an int.
     */
    private static int __LO(double x)
    {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) transducer;
    }

    /**
     * Return the high-order 32 bits of the double argument as an int.
     */
    private static int __HI(double x)
    {
        long transducer = Double.doubleToRawLongBits(x);
        return (int) (transducer >> 32);
    }

    /**
     * Return a double with its high-order bits of the second argument
     * and the low-order bits of the first argument..
     */
    private static double __HI(double x, int high)
    {
        long transX = Double.doubleToRawLongBits(x);
        return Double.longBitsToDouble((transX & 0x0000_0000_FFFF_FFFFL) |
            (((long) high)) << 32);
    }
}
