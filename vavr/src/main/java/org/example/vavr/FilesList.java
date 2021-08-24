package org.example.vavr;

import io.vavr.CheckedFunction1;
import io.vavr.Function1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FilesList
{
    public static void main(String[] args)
    {
        final Function1<Path, Stream<Path>> filesList = CheckedFunction1.of(Files::list).unchecked();
        final Stream<Path> contents = filesList.apply(Path.of("/tmp"));
        contents.forEach(System.out::println);
    }
}
