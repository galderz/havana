package org.example.byteman.qbicc;

public class New extends AbstractValue
{
    private final ClassObjectType type;

    public New(ClassObjectType type)
    {
        this.type = type;
    }

    @Override
    String getNodeName()
    {
        return "New";
    }

    @Override
    public StringBuilder toString(StringBuilder b)
    {
        super.toString(b);
        b.append('(');
        type.toString(b);
        b.append(')');
        return b;
    }
}
