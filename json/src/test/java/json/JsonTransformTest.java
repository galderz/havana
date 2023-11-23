package json;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class JsonTransformTest
{
    @Test
    public void testDiscardingPatterns() throws IOException
    {
        String original = JsonSamples.resourcesJsonContent();
        final Json.JsonObjectBuilder objBuilder = Json.object(false, true);
        final Predicate<JsonReader.JsonValue> discardValues = v ->
        {
            if (v instanceof JsonReader.JsonMember)
            {
                final JsonReader.JsonMember member = (JsonReader.JsonMember) v;
                if ("pattern".equals(member.attribute().value()))
                {
                    final JsonReader.JsonString value = (JsonReader.JsonString) member.value();
                    return value.value().contains("io.quarkus") || value.value().contains("io.smallrye");
                }
            }

            return false;
        };
        objBuilder.transform(JsonReader.of(original).read(), JsonTransform.dropping(discardValues));

        final JsonReader reader = JsonReader.of(objBuilder.build());
        final JsonReader.JsonObject obj = reader.read();
        final JsonReader.JsonArray includes = obj
            .<JsonReader.JsonObject>get("resources")
            .get("includes");

        final List<String> patterns = includes.<JsonReader.JsonObject>stream()
            .<JsonReader.JsonString>map(include -> include.get("pattern"))
            .map(JsonReader.JsonString::value)
            .collect(Collectors.toList());

        assertThat(patterns.size(), is(18));
        assertThat(patterns.get(0), equalTo("\\\\QMETA-INF/microprofile-config.properties\\\\E"));
    }

    @Test
    public void testKeepAll() throws IOException
    {
        String original = JsonSamples.resourcesJsonContent();

        final Json.JsonObjectBuilder objBuilder = Json.object(false, true);
        objBuilder.transform(JsonReader.of(original).read(), JsonTransform.dropping(v -> false));

        final JsonReader reader = JsonReader.of(objBuilder.build());
        final JsonReader.JsonObject obj = reader.read();
        final JsonReader.JsonArray includes = obj
            .<JsonReader.JsonObject>get("resources")
            .get("includes");

        final List<String> patterns = includes.<JsonReader.JsonObject>stream()
            .<JsonReader.JsonString>map(include -> include.get("pattern"))
            .map(JsonReader.JsonString::value)
            .collect(Collectors.toList());

        assertThat(patterns.size(), is(20));
        assertThat(patterns.get(0), equalTo("\\\\QMETA-INF/microprofile-config.properties\\\\E"));
    }
}