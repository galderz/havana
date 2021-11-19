package org.example.byteman.qbicc;

public class ClassObjectType
{
    private final String internalName;

    public ClassObjectType(String internalName)
    {
        this.internalName = internalName;
    }

    public final String toString() {
        return toString(new StringBuilder()).toString();
    }

    public StringBuilder toString(final StringBuilder b)
    {
        return b.append("class").append('(').append(internalName).append(')');
    }
}
