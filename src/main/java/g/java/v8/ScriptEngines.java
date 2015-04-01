package g.java.v8;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;

// https://leanpub.com/whatsnewinjava8/read
public class ScriptEngines {
   public static void main(String... args) throws Exception {
      ScriptEngineManager engineManager = new ScriptEngineManager();
      ScriptEngine engine = engineManager.getEngineByName("nashorn");
      engine.eval("function p(s) { print(s) }");
      engine.eval("p('Hello Nashorn');");
      engine.eval(new FileReader("src/javag/date.js"));

      Invocable inv = (Invocable) engine;
      engine.eval("function p(s) { print(s) }");
      inv.invokeFunction("p", "hello");

      // later on...
      JPrinter printer = inv.getInterface(JPrinter.class);
      printer.p("Hello again!");
   }

   public static interface JPrinter {
      void p(String s);
   }
}
