import java.util.List;

public class Split
{
    public static void main(String[] args)
    {
        {
            final List<String> test = List.of("cycles:p", "branches");

            final List<String> withoutEventModifiers = test.stream()
                .map(s -> s.split(":"))
                .filter(s -> s.length > 0)
                .map(s -> s[0])
                .toList();

            System.out.println(withoutEventModifiers);
        }

        System.out.println("Hello");
    }
}
