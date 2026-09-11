package org.example.jackson;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;

public class Main
{
    public static void main(String[] args)
    {
        final MyValue v = new MyValue();
        v.name = "my-name";
        v.age = 42;

        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
        mapper.writeValue(new File("target/result.json"), v);
    }

    public static class MyValue {
        public String name;
        public int age;
        // NOTE: if using getters/setters, can keep fields `protected` or `private`
    }
}
