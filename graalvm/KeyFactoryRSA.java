import java.security.KeyFactory;

public class KeyFactoryRSA
{
    public static void main(String[] args) throws Exception
    {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        System.out.printf("Loaded RSA.KeyFactory: %s%n", keyFactory);
    }
}
