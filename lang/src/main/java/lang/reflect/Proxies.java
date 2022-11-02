package lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class Proxies
{
    public static void main(String[] args)
    {
        basicProxy();
        mapAndMarkerProxy();
    }

    private static void basicProxy()
    {
        Map proxyInstance = (Map) Proxy.newProxyInstance(
            Proxies.class.getClassLoader()
            , new Class[] { Map.class }
            , new DynamicInvocationHandler()
        );
        proxyInstance.put("hello", "world");
        System.out.println(proxyInstance.getClass());
    }

    private static void mapAndMarkerProxy()
    {
        Map proxyInstance = (Map) Proxy.newProxyInstance(
            Proxies.class.getClassLoader()
            , new Class[] { Map.class, Marker.class }
            , new DynamicInvocationHandler()
        );
        proxyInstance.put("hello", "world");
        System.out.println(proxyInstance.getClass());
    }

    static class DynamicInvocationHandler implements InvocationHandler
    {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
        {
            System.out.printf("Invoked method: %s%n", method.getName());
            return 42;
        }
    }
}
