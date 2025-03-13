package org.sample.service.loader.module_b;

import org.sample.service.loader.module_a.Point;
import org.sample.service.loader.module_a.PointFactory;

public class Point3dFactory implements PointFactory
{
    @Override
    public Point newPoint()
    {
        return new Point3d();
    }

    @Override
    public int priority()
    {
        return 20;
    }
}
