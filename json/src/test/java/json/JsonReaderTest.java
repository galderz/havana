package json;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class JsonReaderTest
{
    @Test
    public void testEmptyParameterTypes()
    {
        String text = JsonSamples.emptyParameterTypeJsonContent();

        final JsonReader reader = JsonReader.of(text);
        final JsonReader.JsonArray array = reader.read();

        final Optional<JsonReader.JsonArray> parameterTypes = array.<JsonReader.JsonObject>stream()
            .<JsonReader.JsonArray>map(obj -> obj.get("methods"))
            .<JsonReader.JsonObject>flatMap(JsonReader.JsonArray::stream)
            .<JsonReader.JsonArray>map(method -> method.get("parameterTypes"))
            .findFirst();

        assertThat(parameterTypes.isPresent(), is(true));
        assertThat(parameterTypes.get(), notNullValue());
    }

    @Test
    public void testResourceJson()
    {
        String text = JsonSamples.resourcesJsonContent();

        final JsonReader reader = JsonReader.of(text);
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
