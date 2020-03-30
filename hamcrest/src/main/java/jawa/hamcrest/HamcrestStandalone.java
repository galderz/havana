package jawa.hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HamcrestStandalone
{
    public static void main(String[] args)
    {
        String theBiscuit = "Ginger";
        String myBiscuit = "Ginger";
        assertThat(theBiscuit, equalTo(myBiscuit));
    }
}
