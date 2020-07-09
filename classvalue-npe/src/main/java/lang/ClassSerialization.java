package lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

public class ClassSerialization
{
    static final Path PATH = Path.of(System.getProperty("java.io.tmpdir"), "class.serialization");

    static class Serialize
    {
        public static void main(String[] args) throws Exception
        {
            final Field field = getField();

            final var klass = int[].class;
            System.out.println(field.get(klass));

            // Fill up class value map
            MethodHandles.arrayElementGetter(int[].class);
            System.out.println(field.get(klass));

            final var bytes = serialize(klass);
            Files.write(PATH, bytes);
        }
    }

    static class Deserialize
    {
        public static void main(String[] args) throws Exception
        {
            final Field field = getField();

            final var bytes = Files.readAllBytes(PATH);
            final var newKlass = deserialize(bytes);

            System.out.println(field.get(newKlass));
        }
    }

    private static Field getField() throws Exception
    {
        final var field = Class.class.getDeclaredField("classValueMap");
        field.setAccessible(true);
        return field;
    }

    static byte[] serialize(Object obj)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutput oo = new ObjectOutputStream(baos))
        {
            oo.writeObject(obj);
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    static Object deserialize(byte[] bytes)
    {
        if (bytes == null) return null;

        try (ObjectInput oi =
                 new ObjectInputStream(new ByteArrayInputStream(bytes)))
        {
            return oi.readObject();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

}
