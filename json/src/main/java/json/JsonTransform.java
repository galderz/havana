package json;

public class JsonTransform
{

    public static final class JsonString
    {
        public void toObject(JsonString value, Json.JsonObjectBuilder builder)
        {
            builder.put(value, value);
        }

        public void toArray(Json.JsonArrayBuilder builder)
        {
            builder.add(value);
        }
    }
}
