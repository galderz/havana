package json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * A simple JSON string generator.
 */
public final class Json
{

    private static final String OBJECT_START = "{";
    private static final String OBJECT_END = "}";
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String NAME_VAL_SEPARATOR = ":";
    private static final String ENTRY_SEPARATOR = ",";

    private static final int CONTROL_CHAR_START = 0;
    private static final int CONTROL_CHAR_END = 0x1f;
    private static final char CHAR_QUOTATION_MARK = '"';

    private static final Map<Character, String> REPLACEMENTS;

    static {
        REPLACEMENTS = new HashMap<>();
        // control characters
        for (int i = CONTROL_CHAR_START; i <= CONTROL_CHAR_END; i++) {
            REPLACEMENTS.put((char) i, String.format("\\u%04x", i));
        }
        // quotation mark
        REPLACEMENTS.put('"', "\\\"");
        // reverse solidus
        REPLACEMENTS.put('\\', "\\\\");
    }

    private Json() {
    }

    /**
     * @return the new JSON array builder, empty builders are not ignored
     */
    public static JsonArrayBuilder array() {
        return new JsonArrayBuilder(false, false);
    }

    /**
     * @param ignoreEmptyBuilders
     * @return the new JSON array builder
     * @see JsonBuilder#ignoreEmptyBuilders
     */
    static JsonArrayBuilder array(boolean ignoreEmptyBuilders, boolean skipEscape) {
        return new JsonArrayBuilder(ignoreEmptyBuilders, skipEscape);
    }

    /**
     * @return the new JSON object builder, empty builders are not ignored
     */
    public static JsonObjectBuilder object() {
        return new JsonObjectBuilder(false, false);
    }

    /**
     * @param ignoreEmptyBuilders
     * @param skipEscape
     * @return the new JSON object builder
     * @see JsonBuilder#ignoreEmptyBuilders
     */
    static JsonObjectBuilder object(boolean ignoreEmptyBuilders, boolean skipEscape) {
        return new JsonObjectBuilder(ignoreEmptyBuilders, skipEscape);
    }

    public abstract static class JsonBuilder<T> {

        protected boolean ignoreEmptyBuilders = false;
        protected final boolean skipEscape;
        protected JsonTransform transform;

        /**
         * @param ignoreEmptyBuilders If set to true all empty builders added to this builder will be ignored during
         *        {@link #build()}
         */
        JsonBuilder(boolean ignoreEmptyBuilders, boolean skipEscape) {
            this.ignoreEmptyBuilders = ignoreEmptyBuilders;
            this.skipEscape = skipEscape;
        }

        /**
         * @return <code>true</code> if there are no elements/properties, <code>false</code> otherwise
         */
        abstract boolean isEmpty();

        /**
         * @return a string representation
         * @throws IOException
         */
        abstract String build() throws IOException;

        abstract void appendTo(Appendable appendable) throws IOException;

        /**
         * @param value
         * @return <code>true</code> if the value is null or an empty builder and {@link #ignoreEmptyBuilders} is set to
         *         <code>true</code>, <code>false</code>
         *         otherwise
         */
        protected boolean isIgnored(Object value) {
            return value == null || (ignoreEmptyBuilders && value instanceof JsonBuilder && ((JsonBuilder<?>) value).isEmpty());
        }

        protected boolean isValuesEmpty(Collection<Object> values) {
            if (values.isEmpty()) {
                return true;
            }
            for (Object object : values) {
                if (object instanceof JsonBuilder) {
                    if (!((JsonBuilder<?>) object).isEmpty()) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;

        }

        protected abstract T self();

        abstract void with(JsonReader.JsonValue element);

        public void setTransform(JsonTransform transform)
        {
            this.transform = transform;
        }
    }

    /**
     * JSON array builder.
     */
    public static class JsonArrayBuilder extends JsonBuilder<JsonArrayBuilder> {

        private final List<Object> values;

        private JsonArrayBuilder(boolean ignoreEmptyBuilders, boolean skipEscape) {
            super(ignoreEmptyBuilders, skipEscape);
            this.values = new ArrayList<Object>();
        }

        JsonArrayBuilder add(JsonArrayBuilder value) {
            addInternal(value);
            return this;
        }

        public JsonArrayBuilder add(JsonObjectBuilder value) {
            addInternal(value);
            return this;
        }

        public JsonArrayBuilder add(String value) {
            addInternal(value);
            return this;
        }

        JsonArrayBuilder add(boolean value) {
            addInternal(value);
            return this;
        }

        JsonArrayBuilder add(int value) {
            addInternal(value);
            return this;
        }

        JsonArrayBuilder add(long value) {
            addInternal(value);
            return this;
        }

        JsonArrayBuilder addAll(List<JsonObjectBuilder> value) {
            if (value != null && !value.isEmpty()) {
                values.addAll(value);
            }
            return this;
        }

        void addInternal(Object value) {
            if (value != null) {
                values.add(value);
            }
        }

        public boolean isEmpty() {
            return isValuesEmpty(values);
        }

        String build() throws IOException {
            StringBuilder builder = new StringBuilder();
            appendTo(builder);
            return builder.toString();
        }

        @Override
        public void appendTo(Appendable appendable) throws IOException {
            appendable.append(ARRAY_START);
            int idx = 0;
            for (ListIterator<Object> iterator = values.listIterator(); iterator.hasNext();) {
                Object value = iterator.next();
                if (isIgnored(value)) {
                    continue;
                }
                if (++idx > 1) {
                    appendable.append(ENTRY_SEPARATOR);
                }
                appendValue(appendable, value, skipEscape);
            }
            appendable.append(ARRAY_END);
        }

        @Override
        protected JsonArrayBuilder self() {
            return this;
        }

        @Override
        void with(JsonReader.JsonValue element) {
            if (element instanceof JsonReader.JsonString) {
                add(((JsonReader.JsonString) element).value());
            } else if (element instanceof JsonReader.JsonInteger) {
                final long longValue = ((JsonReader.JsonInteger) element).longValue();
                final int intValue = (int) longValue;
                if (longValue == intValue) {
                    add(intValue);
                } else {
                    add(longValue);
                }
            } else if (element instanceof JsonReader.JsonBoolean) {
                add(((JsonReader.JsonBoolean) element).value());
            } else if (element instanceof JsonReader.JsonArray) {
                final JsonArrayBuilder arrayBuilder = Json.array(ignoreEmptyBuilders, skipEscape);
                arrayBuilder.transform((JsonReader.JsonArray) element, transform);
                add(arrayBuilder);
            } else if (element instanceof JsonReader.JsonObject) {
                final JsonObjectBuilder objectBuilder = Json.object(ignoreEmptyBuilders, skipEscape);
                objectBuilder.transform((JsonReader.JsonObject) element, transform);
                if (!objectBuilder.isEmpty()) {
                    add(objectBuilder);
                }
            }
        }

        public void transform(JsonReader.JsonArray value, JsonTransform transform)
        {
            setTransform(transform);
            value.forEach(transform, this);
        }
    }

    /**
     * JSON object builder.
     */
    public static class JsonObjectBuilder extends JsonBuilder<JsonObjectBuilder> {

        private final Map<String, Object> properties;

        private JsonObjectBuilder(boolean ignoreEmptyBuilders, boolean skipEscape) {
            super(ignoreEmptyBuilders, skipEscape);
            this.properties = new HashMap<String, Object>();
        }

        public JsonObjectBuilder put(String name, String value) {
            putInternal(name, value);
            return this;
        }

        public JsonObjectBuilder put(String name, JsonObjectBuilder value) {
            putInternal(name, value);
            return this;
        }

        public JsonObjectBuilder put(String name, JsonArrayBuilder value) {
            putInternal(name, value);
            return this;
        }

        public JsonObjectBuilder put(String name, boolean value) {
            putInternal(name, value);
            return this;
        }

        JsonObjectBuilder put(String name, int value) {
            putInternal(name, value);
            return this;
        }

        JsonObjectBuilder put(String name, long value) {
            putInternal(name, value);
            return this;
        }

        boolean has(String name) {
            return properties.containsKey(name);
        }

        void putInternal(String name, Object value) {
            Objects.requireNonNull(name);
            if (value != null) {
                properties.put(name, value);
            }
        }

        public boolean isEmpty() {
            if (properties.isEmpty()) {
                return true;
            }
            return isValuesEmpty(properties.values());
        }

        String build() throws IOException {
            StringBuilder builder = new StringBuilder();
            appendTo(builder);
            return builder.toString();
        }

        @Override
        public void appendTo(Appendable appendable) throws IOException {
            appendable.append(OBJECT_START);
            int idx = 0;
            for (Iterator<Entry<String, Object>> iterator = properties.entrySet().iterator(); iterator.hasNext();) {
                Entry<String, Object> entry = iterator.next();
                if (isIgnored(entry.getValue())) {
                    continue;
                }
                if (++idx > 1) {
                    appendable.append(ENTRY_SEPARATOR);
                }
                appendStringValue(appendable, entry.getKey(), skipEscape);
                appendable.append(NAME_VAL_SEPARATOR);
                appendValue(appendable, entry.getValue(), skipEscape);
            }
            appendable.append(OBJECT_END);
        }

        @Override
        protected JsonObjectBuilder self() {
            return this;
        }

        @Override
        void with(JsonReader.JsonValue element) {
            if (element instanceof JsonReader.JsonMember) {
                final JsonReader.JsonMember member = (JsonReader.JsonMember) element;
                final String attribute = member.attribute().value();
                final JsonReader.JsonValue value = member.value();
                if (value instanceof JsonReader.JsonString) {
                    put(attribute, ((JsonReader.JsonString) value).value());
                } else if (value instanceof JsonReader.JsonInteger) {
                    final long longValue = ((JsonReader.JsonInteger) value).longValue();
                    final int intValue = (int) longValue;
                    if (longValue == intValue) {
                        put(attribute, intValue);
                    } else {
                        put(attribute, longValue);
                    }
                } else if (value instanceof JsonReader.JsonBoolean) {
                    final boolean booleanValue = ((JsonReader.JsonBoolean) value).value();
                    put(attribute, booleanValue);
                } else if (value instanceof JsonReader.JsonArray) {
                    final JsonArrayBuilder arrayBuilder = Json.array(ignoreEmptyBuilders, skipEscape);
                    arrayBuilder.transform((JsonReader.JsonArray) value, transform);
                    put(attribute, arrayBuilder);
                } else if (value instanceof JsonReader.JsonObject) {
                    final JsonObjectBuilder objectBuilder = Json.object(ignoreEmptyBuilders, skipEscape);
                    objectBuilder.transform((JsonReader.JsonObject) value, transform);
                    if (!objectBuilder.isEmpty()) {
                        put(attribute, objectBuilder);
                    }
                }
            }
        }

        public void transform(JsonReader.JsonObject value, JsonTransform transform) {
//            final ResolvedTransform resolved = new ResolvedTransform(this, transform);
//            value.forEach(resolved);
            setTransform(transform);
            value.forEach(transform, this);
        }
    }

    static void appendValue(Appendable appendable, Object value, boolean skipEscape) throws IOException {
        if (value instanceof JsonObjectBuilder) {
            appendable.append(((JsonObjectBuilder) value).build());
        } else if (value instanceof JsonArrayBuilder) {
            appendable.append(((JsonArrayBuilder) value).build());
        } else if (value instanceof String) {
            appendStringValue(appendable, value.toString(), skipEscape);
        } else if (value instanceof Boolean || value instanceof Integer || value instanceof Long) {
            appendable.append(value.toString());
        } else {
            throw new IllegalStateException("Unsupported value type: " + value);
        }
    }

    static void appendStringValue(Appendable appendable, String value, boolean skipEscape) throws IOException {
        appendable.append(CHAR_QUOTATION_MARK);
        if (skipEscape) {
            appendable.append(value);
        } else {
            appendable.append(escape(value));
        }
        appendable.append(CHAR_QUOTATION_MARK);
    }

    /**
     * Escape quotation mark, reverse solidus and control characters (U+0000 through U+001F).
     *
     * @param value
     * @return escaped value
     * @see <a href="https://www.ietf.org/rfc/rfc4627.txt">https://www.ietf.org/rfc/rfc4627.txt</a>
     */
    static String escape(String value) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            String replacement = REPLACEMENTS.get(c);
            if (replacement != null) {
                builder.append(replacement);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
