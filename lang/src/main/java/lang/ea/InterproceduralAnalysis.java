package lang.ea;

public class InterproceduralAnalysis
{
    int data;
    InterproceduralAnalysis a;
    InterproceduralAnalysis b;
    static InterproceduralAnalysis staticA;
    static InterproceduralAnalysis staticB;

    static int first()
    {
        staticA = new InterproceduralAnalysis();
        staticB = staticA;
        return second(staticA, staticB);
    }

    static int second(InterproceduralAnalysis f1, InterproceduralAnalysis f2)
    {
        f1.a = new InterproceduralAnalysis();
        f2.b = new InterproceduralAnalysis();
        f1.data = 10;
        f2.data = f2.data + 20;
        f2 = new InterproceduralAnalysis();
        return f1.data;
    }

    public static void main(String[] args)
    {
        System.out.println(first() == 30 ? '.' : 'F');
    }
}
