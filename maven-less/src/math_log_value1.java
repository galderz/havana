public class math_log_value1
{
    public static void main(String[] args)
    {
        final var value = 3.4529686207034E-311;
        System.out.println("Test value: " + value);

        final var result = Math.log(value);
        System.out.println(result);
    }
}
