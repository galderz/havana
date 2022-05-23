package util;

public class ParameterArray
{
    ParameterValue[] elements;

    public ParameterArray()
    {
        initArray(1 << 2);
    }

    private void initArray(int initialSize)
    {
        this.elements = new ParameterValue[initialSize];
        for (int i = 0; i < initialSize; i++)
        {
            this.elements[i] = null;
        }
    }

    boolean addIfAbsent(ParameterValue elem)
    {
        if (elem.index >= this.elements.length)
        {
            ParameterValue[] tmp = new ParameterValue[this.elements.length << 1];
            System.arraycopy(this.elements, 0, tmp, 0, this.elements.length);
            this.elements = tmp;
        }

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

        final ParameterArray arr = new ParameterArray();
        assert null == arr.get(0);
        assert null == arr.get(1);
        assert null == arr.get(2);

        final ParameterValue p1 = new ParameterValue(1);
        assert arr.addIfAbsent(p1);
        assert null == arr.get(0);
        assert null != arr.get(1);
        assert null == arr.get(2);

        final ParameterValue p4 = new ParameterValue(4);
        assert arr.addIfAbsent(p4);
        assert null == arr.get(0);
        assert null != arr.get(1);
        assert null == arr.get(2);
        assert null == arr.get(2);
        assert null != arr.get(4);

        assert arr.addIfAbsent(new ParameterValue(8));
        assert null != arr.get(8);
        assert !arr.addIfAbsent(new ParameterValue(8));
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