package json;

import org.junit.jupiter.api.Test;

public class JsonTransformTest
{
    @Test
    public void testTransformResourceJson()
    {
        String text =
            "{\n" +
                "  \"resources\": {\n" +
                "    \"includes\": [\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/microprofile-config.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/io.quarkus.runtime.test.TestHttpEndpointProvider\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/io.smallrye.config.ConfigSourceFactory\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/java.lang.System$LoggerFinder\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.apache.maven.surefire.spi.MasterProcessChannelProcessorFactory\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.codehaus.groovy.runtime.ExtensionModule\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.eclipse.microprofile.config.spi.ConfigProviderResolver\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.jboss.logging.LoggerProvider\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.junit.jupiter.api.extension.Extension\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\QMETA-INF/services/org.slf4j.spi.SLF4JServiceProvider\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qapplication.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qio/quarkus/bootstrap/resolver/maven/options/BootstrapMavenOptions.class\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qjava/io/Serializable.class\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qjunit-platform.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qlogging.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qorg/acme/GreetingResourceTest.class\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qorg/jboss/logmanager/configuration/logging.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qorg/osgi/annotation/bundle/Requirements.class\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"\\\\Qvertx-default-jul-logging.properties\\\\E\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"pattern\": \"java.base:\\\\Qjava/io/Serializable.class\\\\E\"\n" +
                "      },\n" +
                "    ]\n" +
                "  },\n" +
                "  \"bundles\": []\n" +
                "}";

        final JsonReader reader = JsonReader.of(text);
        final JsonReader.JsonObject obj = reader.read();
        reader.transform(obj, JsonTransform.dropping(v -> false));
    }
}
