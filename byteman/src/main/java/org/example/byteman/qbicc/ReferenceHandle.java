package org.example.byteman.qbicc;

public class ReferenceHandle extends AbstractNode
{
    private final Value referenceValue;

    public ReferenceHandle(Value referenceValue)
    {
        this.referenceValue = referenceValue;
    }

    @Override
    String getNodeName()
    {
        return "Reference";
    }

    @Override
    public StringBuilder toString(StringBuilder b) {
        super.toString(b);
        b.append('(');
        referenceValue.toString(b);
        b.append(')');
        return b;
    }
}
