package p.json;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

public class ReadJsonWithNashorn
{
    public static void main(String[] args) throws ScriptException
    {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine e = m.getEngineByName("nashorn");

        Object obj1 = e.eval(
            "JSON.parse('{ \"y\": \"hello\", \"x\": 343, \"z\": [2,4,5] }');");
        System.out.println(obj1.getClass());
        Map<String, Object> map1 = (Map<String, Object>) obj1;
        System.out.println(map1.get("x"));
        System.out.println(map1.get("y"));
        System.out.println(map1.get("z"));
        Map<Object, Object> array1 = (Map<Object, Object>) map1.get("z");
        array1.forEach((a, b) -> System.out.println("z[" + a + "] = " + b));
    }
}
