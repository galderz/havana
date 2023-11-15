package json;

import java.util.function.Predicate;

@FunctionalInterface
public interface JsonTransform<T>
{
    void accept(Json.JsonBuilder<T> builder, JsonReader.JsonValue element);

    static <T> JsonTransform<T> dropping(Predicate<JsonReader.JsonValue> filter)
    {
        return (builder, element) ->
        {
            if (!filter.test(element))
                builder.with(element);
        };
    }
}
