package records;

public class Records
{
    record A(int a) {}

    record B(int b)
    {
        static B of(int b)
        {
            return new B(b);
        }
    }

    public static void main(String[] args)
    {
        final var a = new A(1);
        System.out.println(a.a);

        final var b = new B(1);
        System.out.println(b.b);

        final var c = new C(1);
        // System.out.println(c.c); // fails to compile cos `c` field is private
        System.out.println(c.c());
    }
}

record C(int c) {}
