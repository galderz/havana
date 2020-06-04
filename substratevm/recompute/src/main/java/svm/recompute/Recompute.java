package svm.recompute;

public class Recompute
{
    final RecomputeReset reset = new RecomputeReset();
    final RecomputeAlias alias = new RecomputeAlias();

    public static void main(String[] args)
    {
        final var recompute = new Recompute();
        System.out.println("reset.buffer=" + recompute.reset.buffer());
        System.out.println("alias.buffer=" + recompute.alias.buffer());
    }
}
