package poet;

import java.util.ArrayList;
import java.util.List;

public class CodeBlock
{
    final List<String> parts = new ArrayList<>();
    final String indent = "    ";

    public CodeBlock beginControlFlow(String controlFlow, Object... args) {
        add(controlFlow + " {\n", args);
        indent();
        return this;
    }

    public CodeBlock addStatement(String format, Object... args) {
        add(String.format(format + ";\n", args));
        return this;
    }

    public CodeBlock endControlFlow() {
        unindent();
        add("}\n");
        return this;
    }

    private CodeBlock add(String code, Object... args) {
        add(String.format(code, args));
        return this;
    }

    private CodeBlock indent() {
        this.parts.add("$>");
        return this;
    }

    private CodeBlock unindent() {
        this.parts.add("$<");
        return this;
    }

    private CodeBlock add(String part) {
        parts.add(part);
        return this;
    }

    @Override
    public String toString()
    {
        final StringBuilder out = new StringBuilder();
        emit(this, out);
        return out.toString();
    }

    private static void emit(CodeBlock codeBlock, StringBuilder out)
    {
        int indentLevel = 0;
        for (String part : codeBlock.parts) {
            switch (part) {
                case "$>" ->
                    indentLevel++;
                case "$<" ->
                    indentLevel--;
                default ->
                {
                    for (int j = 0; j < indentLevel; j++)
                    {
                        out.append(codeBlock.indent);
                    }
                    out.append(part);
                }
            }
        }
    }

    public static void main(String[] args)
    {
        System.out.println(reassoc(1));
        System.out.println(reassoc(2));
    }

    static String reassoc(int size)
    {
        var codeBlock = new CodeBlock();
        codeBlock
            .beginControlFlow(
                "for (int i = 0; i < array.length; i += %d)"
                , size
            );

        int index = 0;
        for (int i = 0; i < size; i++)
        {
            codeBlock.addStatement(
                "var v%d = array[i + %d]"
                , index
                , index
            );
            index++;
        }

        codeBlock.addStatement("var t0 = Math.max(v1, v0)");

        index = 1;
        for (int i = index; i < size - 1; i++)
        {
            codeBlock.addStatement(
                "var t%d = Math.max(v%d, t%d)"
                , index
                , index + 1
                , index - 1
            );
            index++;
        }

        return codeBlock
            .addStatement("result = Math.max(result, t%d)", index - 1)
            .endControlFlow()
            .toString()
        ;
    }
}
