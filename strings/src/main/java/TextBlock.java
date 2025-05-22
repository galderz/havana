public class TextBlock
{
    public static void main(String[] args)
    {

        float a = 1.0f;
        float b = 2.0f;
        boolean isFloatCheckWithRawBits = true;
        System.err.printf("""
            ERROR: Verify.checkEQ failed: value mismatch. check raw: %b
              Values: %.1f vs %.1f
              Raw:    %d vs %d
            """, isFloatCheckWithRawBits, a, b, Float.floatToRawIntBits(a), Float.floatToRawIntBits(b));

        System.err.println("ERROR: Verify.checkEQ failed: value mismatch. check raw: " + isFloatCheckWithRawBits);
        System.err.println("  Values: " + a + " vs " + b);
        System.err.println("  Raw:    " + Float.floatToRawIntBits(a) + " vs " + Float.floatToRawIntBits(b));

        String text = """
            Hello World
            """;
        System.out.println(text);
    }
}
