package org.example.byteman.logging;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class LogDump
{
    public static void main(String[] args) throws IOException
    {
        final String path = args[0];
        final String dump = dump(Path.of(path));
        System.out.println(dump);
    }

    static String dump(Path path) throws IOException
    {
        final StringBuilder builder = new StringBuilder();

        final byte[] bytes = Files.readAllBytes(path);
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);

        while (buffer.hasRemaining())
        {
            builder
                .append(buffer.getLong())
                .append(':')
                .append(Logger.Context.values()[buffer.getShort()].name())
                .append(':')
                .append(buffer.getInt())
                .append('\n');
        }

        return builder.toString();
    }
}
