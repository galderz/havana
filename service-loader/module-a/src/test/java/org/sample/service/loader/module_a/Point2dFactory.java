package org.sample.service.loader.module_a;

public class Point2dFactory implements PointFactory
{
    @Override
    public Point newPoint()
    {
        return new Point();
    }

    @Override
    public int priority()
    {
        return 10;
    }
}
