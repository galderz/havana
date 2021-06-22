package lang.system;

// Run with:
// -Xbatch -XX:-TieredCompilation -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
public class PrintAssembly
{
    public static void main(String[] args)
    {
        System.out.println("hello " + (int)(float) (Integer.MAX_VALUE - 1));
    }
}
