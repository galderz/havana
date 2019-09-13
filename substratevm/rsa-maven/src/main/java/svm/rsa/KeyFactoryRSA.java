package svm.rsa;

import java.security.KeyFactory;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class KeyFactoryRSA
{
    public static void main(String[] args) throws Exception
    {
        final String providerName = "SunRsaSign";
        List<String> providerClasses = registerProvider(providerName);
        System.out.printf("Provider=%s classes: %s%n", providerName, providerClasses);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        System.out.printf("Loaded RSA.KeyFactory: %s%n", keyFactory);
    }

    /**
     * Determine the classes that make up the provider and its services
     *
     * @param providerName - JCA provider name
     * @return class names that make up the provider and its services
     */
    private static List<String> registerProvider(String providerName)
    {
        ArrayList<String> providerClasses = new ArrayList<>();
        Provider provider = Security.getProvider(providerName);
        providerClasses.add(provider.getClass().getName());
        Set<Provider.Service> services = provider.getServices();
        for (Provider.Service service : services)
        {
            String serviceClass = service.getClassName();
            providerClasses.add(serviceClass);
            // Need to pull in the key classes
            String supportedKeyClasses = service.getAttribute("SupportedKeyClasses");
            if (supportedKeyClasses != null)
            {
                String[] keyClasses = supportedKeyClasses.split("\\|");
                providerClasses.addAll(Arrays.asList(keyClasses));
            }
        }
        return providerClasses;
    }

}
