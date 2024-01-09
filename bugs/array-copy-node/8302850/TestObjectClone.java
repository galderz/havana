class TestObjectClone
{
    public static void main(String[] args)
    {
        for (int i = 0; i < 1_000; i++)
        {
            MyObject result = test(new MyObject());
            System.out.println(result);
            blackhole(result);
        }
    }

    static MyObject test(MyObject obj)
    {
        return obj.clone();
    }

    static void blackhole(Object obj)
    {
        if (obj.hashCode() == System.nanoTime())
        {
            System.out.println(obj);
        }
    }

    static class MyObject implements Cloneable {
        @Override
        public MyObject clone()
        {
            try
            {
                return (MyObject) super.clone();
            }
            catch (CloneNotSupportedException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}