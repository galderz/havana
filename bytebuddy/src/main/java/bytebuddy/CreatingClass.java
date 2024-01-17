package bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;

import java.util.Arrays;

public class CreatingClass
{
    public static void main(String[] args)
    {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
            .subclass(Object.class)
            .name("example.Type")
            .make();

        System.out.println(Arrays.toString(dynamicType.getBytes()));
    }
}
