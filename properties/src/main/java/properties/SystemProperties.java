package properties;

public class SystemProperties
{
    public static void main(String[] args)
    {
        // With -Dblah
        //   System.getProperty(blah) =
        //   System.getProperty(blah) == null ? false
        //   System.getProperty(blah).isEmpty() ? true
        //   Boolean.getBoolean(blah) false

        final String blah = System.getProperty("blah");
        System.out.println("System.getProperty(blah) = " + blah);
        System.out.println("System.getProperty(blah) == null ? " + (blah == null));
        System.out.println("System.getProperty(blah).isEmpty() ? " + (blah != null ? blah.isEmpty() : "<NA>"));
        final boolean blahAsBoolean = Boolean.getBoolean("blah");
        System.out.println("Boolean.getBoolean(blah) " + blahAsBoolean);
    }
}
