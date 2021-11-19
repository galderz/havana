package org.example.byteman.qbicc;

abstract class AbstractNode implements Node
{
    public final String toString()
    {
        return toString(new StringBuilder()).toString();
    }

    abstract String getNodeName();

    @Override
    public StringBuilder toString(StringBuilder b)
    {
        b.append(getNodeName());
//        if (hasValueHandleDependency())
//        {
//            b.append('[');
//            getValueHandle().toString(b);
//            b.append(']');
//        }
        return b;
    }

}
