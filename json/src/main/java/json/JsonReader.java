package json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A json format reader.
 * It follows the <a href="https://www.json.org/json-en.html>ECMA-404 The JSON Data Interchange Standard.</a>.
 */
public class JsonReader
{
    private final String text;
    private final int length;
    private int position;

    private JsonReader(String text)
    {
        this.text = text;
        this.length = text.length();
    }

    public static JsonReader of(String source)
    {
        return new JsonReader(source);
    }

    public <T extends JsonValue> T read()
    {
        return cast(readElement());
    }

    /**
     * element
     *     ws value ws
     */
    private JsonValue readElement()
    {
        ignoreWhitespace();
        JsonValue result = readValue();
        ignoreWhitespace();
        return result;
    }

    /**
     * value
     *     object
     *     array
     *     string
     *     number
     *     "true"
     *     "false"
     *     "null"
     */
    private JsonValue readValue()
    {
        final int ch = peekChar();
        if (ch < 0)
        {
            throw new IllegalArgumentException("Unable to fully read json value");
        }

        switch (ch)
        {
            case '{':
                return readObject();
            case '[':
                return readArray();
            case '"':
                return readString();
            case 't':
                return readConstant("true", JsonBoolean.TRUE);
            case 'f':
                return readConstant("false", JsonBoolean.FALSE);
            case 'n':
                return readConstant("null", JsonNull.INSTANCE);
            default:
                if (Character.isDigit(ch) || '-' == ch)
                {
                    return readNumber(position);
                }
                throw new IllegalArgumentException("Unknown start character for json value: " + ch);
        }
    }

    /**
     * object
     *     '{' ws '}'
     *     '{' members '}'
     * members
     *     member
     *     member ',' members
     * member
     *     ws string ws ':' element
     */
    private JsonValue readObject()
    {
        position++;

        Map<String, JsonValue> result = new HashMap<>();

        while (position < length)
        {
            ignoreWhitespace();
            switch (peekChar())
            {
                case '}':
                    position++;
                    return new JsonObject(result);
                case ',':
                    position++;
                    break;
                case '"':
                    final String attribute = readString().value;
                    ignoreWhitespace();
                    final int colon = nextChar();
                    if (':' != colon)
                    {
                        throw new IllegalArgumentException("Expected : after attribute");
                    }
                    final JsonValue element = readElement();
                    result.put(attribute, element);
                    break;
            }
        }

        throw new IllegalArgumentException("Json object ended without }");
    }

    /**
     * array
     *     '[' ws ']'
     *     '[' elements ']'
     * elements
     *     element
     *     element ',' elements
     */
    private JsonValue readArray()
    {
        position++;

        final List<JsonValue> result = new ArrayList<>();

        while (position < length)
        {
            ignoreWhitespace();
            switch (peekChar())
            {
                case ']':
                    position++;
                    return new JsonArray(result);
                case ',':
                    position++;
                    break;
                default:
                    result.add(readElement());
                    break;
            }
        }

        throw new IllegalArgumentException("Json array ended without ]");
    }

    /**
     * string
     *     '"' characters '"'
     */
    private JsonString readString()
    {
        position++;

        int start = position;
        StringBuilder unicodeString = null;

        while (position < length)
        {
            final int ch = nextChar();

            if (Character.isISOControl(ch))
            {
                throw new IllegalArgumentException("Control characters not allowed in json string");
            }

            if ('"' == ch)
            {
                final String chunk = text.substring(start, position - 1);
                final String result = unicodeString != null
                    ? unicodeString.append(chunk).toString()
                    : chunk;

                // End of string
                return new JsonString(result);
            }

            if ('\\' == ch)
            {
                switch (nextChar())
                {
                    case '"':     // quotation mark
                    case '\\':    // reverse solidus
                    case '/':     // solidus
                    case 'b':     // backspace
                    case 'f':     // formfeed
                    case 'n':     // linefeed
                    case 'r':     // carriage return
                    case 't':     // horizontal tab
                        break;
                    case 'u':     // unicode
                        if (unicodeString == null) {
                            unicodeString = new StringBuilder(position - start);
                        }
                        unicodeString.append(text, start, position - 1);
                        unicodeString.append(readUnicode());
                        start = position;
                }
            }
        }

        throw new IllegalArgumentException("String not closed");
    }

    private char readUnicode()
    {
        final char digit1 = Character.forDigit(nextChar(), 16);
        final char digit2 = Character.forDigit(nextChar(), 16);
        final char digit3 = Character.forDigit(nextChar(), 16);
        final char digit4 = Character.forDigit(nextChar(), 16);
        return (char) (digit1 << 12 | digit2 << 8 | digit3 << 4 | digit4);
    }

    /**
     * number
     *     integer fraction exponent
     */
    private JsonValue readNumber(int numStartIndex)
    {
        final boolean isFraction = skipToEndOfNumber();
        final String number = text.substring(numStartIndex, position);
        return isFraction
            ? new JsonDouble(Double.parseDouble(number))
            : new JsonInteger(Long.parseLong(number));
    }

    private boolean skipToEndOfNumber()
    {
        // Find the end of a number then parse with library methods
        int ch = nextChar();
        if ('-' == ch)
        {
            ch = nextChar();
        }

        if (Character.isDigit(ch) && '0' != ch)
        {
            ignoreDigits();
        }

        boolean isFraction = false;
        ch = peekChar();
        if ('.' == ch)
        {
            isFraction = true;
            position++;
            ignoreDigits();
        }

        ch = peekChar();
        switch (ch)
        {
            case 'e':
            case 'E':
                position++;
                ch = nextChar();
                switch (ch)
                {
                    case '-':
                    case '+':
                        position++;
                }
                ignoreDigits();
        }

        return isFraction;
    }

    private void ignoreDigits()
    {
        while (position < length)
        {
            final int ch = peekChar();
            if (!Character.isDigit(ch))
            {
                break;
            }
            position++;
        }
    }

    private JsonValue readConstant(String expected, JsonValue result)
    {
        if (text.regionMatches(position, expected, 0, expected.length()))
        {
            position += expected.length();
            return result;
        }
        throw new IllegalArgumentException("Unable to read json constant for: " + expected);
    }

    /**
     * ws
     *     ""
     *     '0020' ws
     *     '000A' ws
     *     '000D' ws
     *     '0009' ws
     */
    private void ignoreWhitespace()
    {
        while (position < length)
        {
            final int ch = peekChar();
            switch (ch)
            {
                case ' ':   // '0020' SPACE
                case '\n':  // '000A' LINE FEED
                case '\r':  // '000D' CARRIAGE RETURN
                case '\t':  // '0009' CHARACTER TABULATION
                    position++;
                    break;
                default:
                    return;
            }
        }
    }

    private int peekChar() {
        return position < length
            ? text.charAt(position)
            : -1;
    }

    private int nextChar() {
        final int ch = peekChar();
        position++;
        return ch;
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj)
    {
        return (T) obj;
    }

    public interface JsonValue {}

    public static final class JsonObject implements JsonValue
    {
        private final Map<String, JsonValue> value;

        public JsonObject(Map<String, JsonValue> value)
        {
            this.value = value;
        }

        <T extends JsonValue> T get(String attribute)
        {
            return cast(value.get(attribute));
        }

//        Map<String, JsonValue> value()
//        {
//            return value;
//        }
    }

    public static final class JsonArray implements JsonValue
    {
        private final List<JsonValue> value;

        public JsonArray(List<JsonValue> value)
        {
            this.value = value;
        }

        public List<JsonValue> value()
        {
            return value;
        }

        public <T extends JsonValue> Stream<T> stream()
        {
            return cast(value.stream());
        }
    }

    public static final class JsonString implements JsonValue
    {
        private final String value;

        public JsonString(String value)
        {
            this.value = value;
        }

        public String value()
        {
            return value;
        }
    }

    public interface JsonNumber extends JsonValue {}

    public static final class JsonInteger implements JsonNumber
    {
        private final long value;

        public JsonInteger(long value)
        {
            this.value = value;
        }

        public long longValue()
        {
            return value;
        }

        public int intValue()
        {
            return (int) value;
        }
    }

    public static final class JsonDouble implements JsonNumber
    {
        private final double value;

        public JsonDouble(double value)
        {
            this.value = value;
        }

        public double value()
        {
            return value;
        }
    }

    public enum JsonBoolean implements JsonValue
    {
        TRUE(true),
        FALSE(false);

        private final boolean value;

        JsonBoolean(boolean value)
        {
            this.value = value;
        }

        public boolean value()
        {
            return value;
        }
    }

    public enum JsonNull implements JsonValue
    {
        INSTANCE;
    }
}
