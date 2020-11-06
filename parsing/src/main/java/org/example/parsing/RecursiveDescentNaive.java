package org.example.parsing;

import javax.print.attribute.standard.RequestingUserName;
import java.util.List;

import static java.lang.System.out;

public class RecursiveDescentNaive
{
    public static final List<Character> OPERATORS = List.of('-', '*');

    public static void main(String[] args)
    {
        out.println(parse("2*3-4"));
        out.println(parse("2-3*4"));
        out.println(parse("2-3"));
    }

    static Expr parse(String expr)
    {
        final var result = parse(expr, 0);
        if (result == null || result.position < expr.length())
            throw new IllegalArgumentException("Syntax error");
        return result.expr;
    }

    record Accumulator(Expr expr, int position) {}

    static Accumulator parse(String expr, int position)
    {
        final var elem = expr.charAt(position);
        if (!isNumber(elem))
        {
            return null;
        }

        int limit = position + 1;
        int num = Character.getNumericValue(elem);
        while (limit < expr.length() && isNumber(expr.charAt(limit)))
        {
            num = num * 10 + Character.getNumericValue(expr.charAt(limit));
            limit++;
        }

        Num left = new Num(num);
        // out.println("Number: " + left);
        if (limit == expr.length() || !isOperator(expr.charAt(limit)))
        {
            return new Accumulator(left, limit + 1);
        }

        char operator = expr.charAt(limit);
        final var right = parse(expr, limit + 1);
        if (right == null)
        {
            return null;
        }

        if (operator == '-')
        {
            return new Accumulator(new Substract(left, right.expr), right.position);
        }
        else
        {
            return new Accumulator(new Multiply(left, right.expr), right.position);
        }
    }

    private static boolean isOperator(char elem)
    {
        return OPERATORS.contains(elem);
    }

    private static boolean isNumber(char elem)
    {
        return elem >= '0' && elem <= '9';
    }

    interface Expr {}

    record Num(int number) implements Expr
    {
        @Override
        public String toString()
        {
            return String.valueOf(number);
        }
    }

    record Multiply(Expr left, Expr right) implements Expr
    {
        @Override
        public String toString()
        {
            return String.format("(%s*%s)", left, right);
        }
    }

    record Substract(Expr left, Expr right) implements Expr
    {
        @Override
        public String toString()
        {
            return String.format("(%s-%s)", left, right);
        }
    }
}
