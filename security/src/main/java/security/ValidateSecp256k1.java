package security;

import java.math.BigInteger;

public class ValidateSecp256k1
{
    public static void main(String[] args)
    {
        // String pubKey = "02929c915fab405ae6748e0478285a816a85147d2b2d5d64f3323dd0c463ad5441";
        String pubKey = "zz";
        // final long num = Long.parseLong(pubKey, 16);
        final BigInteger bigInt = new BigInteger(pubKey, 16);
        System.out.println(bigInt);
    }
}
