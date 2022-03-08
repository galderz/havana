package lang.string;

public class ValidClassNames
{
    public static void main(String[] args)
    {
        String name = "org/example/ea/samples.EASample_01_Basic.sample1(I)I";
        System.out.println(name.replaceAll("[./()]", "_"));
    }
}
