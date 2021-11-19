package org.example.byteman;

import org.example.byteman.qbicc.ClassObjectType;
import org.example.byteman.qbicc.New;
import org.example.byteman.qbicc.ReferenceHandle;

public class Main
{
    public static void main(String[] args)
    {
        // Invoked.main(args);

        final New aNew = new New(new ClassObjectType("java/lang/StringBuilder"));
        System.out.println(aNew);

        final ReferenceHandle aRef = new ReferenceHandle(new New(new ClassObjectType("java/lang/StringBuilder")));
        System.out.println(aRef);
    }
}
