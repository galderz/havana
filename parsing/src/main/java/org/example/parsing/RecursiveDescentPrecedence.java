package org.example.parsing;

import java.util.List;

import static java.lang.System.out;

public class RecursiveDescentPrecedence
{
    public static final List<Character> OPERATORS = List.of('-', '*');

    public static void main(String[] args)
    {
        // Problem is that it is incorrectly parsing operators as right associative when they should be left associative.
        // Fixing it is not easy, because implementing left associativity in a recursive descendent parser causes an infinite loop.
        // It's possible to fix it but it's quite involved.
        // See: https://web.archive.org/web/20191231231734/www.engr.mun.ca/~theo/Misc/exp_parsing.htm
        out.println(parse("2-3-4")); // error, prints (2-(3-4)) instead of ((2-3)-4))

        out.println(parse("2*3-4"));
        out.println(parse("2-3*4"));
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
        Accumulator left = parseFactor(expr, position);
        if (left == null)
        {
            return null;
        }

        if (left.position < expr.length() && isSubstract(expr.charAt(left.position)))
        {
            Accumulator right = parse(expr, left.position + 1);
            if (right == null)
            {
                return null;
            }
            return new Accumulator(new Substract(left.expr, right.expr), right.position);
        }

        return left;
    }

    private static Accumulator parseFactor(String expr, int position)
    {
        Accumulator left = parseTerm(expr, position);
        if (left == null)
        {
            return null;
        }

        if (left.position < expr.length() && isMultiply(expr.charAt(left.position)))
        {
            Accumulator right = parseFactor(expr, left.position + 1);
            if (right == null)
            {
                return null;
            }
            return new Accumulator(new Multiply(left.expr, right.expr), right.position);
        }

        return left;
    }

    private static Accumulator parseTerm(String expr, int position)
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

        return new Accumulator(new Num(num), limit);
    }

    private static boolean isSubstract(char elem)
    {
        return elem == OPERATORS.get(0);
    }

    private static boolean isMultiply(char elem)
    {
        return elem == OPERATORS.get(1);
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
