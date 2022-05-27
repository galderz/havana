package util;

public class ParameterArray
{
    final ParameterValue[] elements;

    public ParameterArray(int size)
    {
        this.elements = new ParameterValue[size];
    }

    boolean addIfAbsent(ParameterValue elem)
    {
        if (this.elements[elem.index] != null)
        {
            return false;
        }

        this.elements[elem.index] = elem;
        return true;
    }

    ParameterValue get(int index)
    {
        return elements[index];
    }

    public static void main(String[] args)
    {
        Assert.check();

        testZeroArray();
        testNonZeroArray();
    }

    private static void testZeroArray()
    {
        final ParameterArray arr = new ParameterArray(0);
        assert arr.addIfAbsent(new ParameterValue(0));
    }

    private static void testNonZeroArray()
    {
        final ParameterArray arr = new ParameterArray(3);
        assert null == arr.get(0);
        assert null == arr.get(1);
        assert null == arr.get(2);

        final ParameterValue p1 = new ParameterValue(1);
        assert arr.addIfAbsent(p1);
        assert null == arr.get(0);
        assert null != arr.get(1);
        assert null == arr.get(2);
        assert !arr.addIfAbsent(p1);
    }
}

class ParameterValue
{
    final int index;

    ParameterValue(int index)
    {
        this.index = index;
    }
}