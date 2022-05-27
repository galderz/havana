package util;

import java.util.HashMap;
import java.util.Map;

public class HashMaps
{
    public static void main(String[] args)
    {
        Map m = new HashMap();
        m.put("a", "b");

        System.out.println(m.get(null));
    }
}
