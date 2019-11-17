package j.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class ResolveName
{
    public static void main(String[] args) throws UnknownHostException
    {
        String host = "a550943defb2811e9a7d40a398a4a2fa-739354478.us-east-2.elb.amazonaws.com";
        final InetAddress[] addresses = InetAddress.getAllByName(host);
        System.out.println(Arrays.toString(addresses));
    }

}
