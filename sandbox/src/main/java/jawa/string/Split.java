package jawa.string;

import java.util.Arrays;

public class Split
{
    public static void main(String[] args)
    {
        {
            String s = "    # ------------- Libraries -------------";
            System.out.println(Arrays.toString(s.split("#")));
        }

        {
            String s = "    \"JACOCOCORE\" : {  # deprecated, to be removed in a future version";
            System.out.println(Arrays.toString(s.split("#")));
        }
    }
}
