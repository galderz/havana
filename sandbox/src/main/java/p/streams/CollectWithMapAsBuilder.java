package p.streams;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectWithMapAsBuilder
{
    public static void main(String[] args)
    {
        Stream
            .of(
                "name=a,age=20"
                , "name=b,age=30"
            )
            .map(CollectWithMapAsBuilder::toFields)
            .map(CollectWithMapAsBuilder::toPerson)
            .forEach(System.out::println);
    }

    static Map<String, Object> toFields(String person)
    {
        final var fields = Arrays.asList(person.split(","));
        return fields.stream()
            .map(fs -> fs.split("="))
            .collect(Collectors.toMap(fs -> fs[0], fs -> fs[1]));
    }

    static Person toPerson(Map<String, Object> fields)
    {
        System.out.println(fields);
        final var name = (String) fields.get("name");
        final var age = Integer.parseInt((String) fields.get("age"));
        return new Person(name, age);
    }

    static final class Person
    {
        final String name;
        final int age;

        Person(String name, int age)
        {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString()
        {
            return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
        }
    }

}
