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
                element.to(builder);
                // element.to(builder);
                // to(element, builder);
        };
    }

//    static <T> void to(JsonReader.JsonValue element, Json.JsonBuilder<T> builder)
//    {
//
//    }
}
