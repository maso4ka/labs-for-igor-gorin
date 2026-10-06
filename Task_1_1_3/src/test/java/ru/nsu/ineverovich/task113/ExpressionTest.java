package ru.nsu.ineverovich.task113;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for mathematical expressions.
 */
class ExpressionTest {

    @Test
    void numberTest() {
        Expression expression = new Number(42);

        assertEquals("42", expression.toString());
        assertEquals(42, expression.eval(""));
        assertEquals("0", expression.derivative("x").toString());
    }

    @Test
    void variableTest() {
        Expression expression = new Variable("variableName");

        assertEquals("variableName", expression.toString());
        assertEquals(17, expression.eval("variableName = 17"));
        assertEquals("1", expression.derivative("variableName").toString());
        assertEquals("0", expression.derivative("x").toString());
    }

    @Test
    void arithmeticTest() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10; y = 13"));
    }

    @Test
    void derivativeTest() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals("(0+((0*x)+(2*1)))",
                expression.derivative("x").toString());

        assertEquals(2, expression.derivative("x").eval("x = 10"));
    }

    @Test
    void subtractionDerivativeTest() {
        Expression expression = new Sub(
                new Variable("x"),
                new Number(5)
        );

        assertEquals("(1-0)", expression.derivative("x").toString());
        assertEquals(1, expression.derivative("x").eval("x = 20"));
    }

    @Test
    void divisionTest() {
        Expression expression = new Div(
                new Variable("x"),
                new Number(2)
        );

        assertEquals(5, expression.eval("x = 10"));
        assertEquals("((1*2)-(x*0))/(2*2)", expression.derivative("x").toString()
                .replaceFirst("^\\((.*)\\)$", "$1"));
    }

    @Test
    void parserTest() {
        Expression expression = Expression.parse("(3+(2*x))");

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10; y = 13"));
    }

    @Test
    void parserWithLongVariableTest() {
        Expression expression = Expression.parse(
                "(longVariable+(2*anotherVariable))");

        assertEquals(16, expression.eval(
                "longVariable = 4; anotherVariable = 6"));
    }

    @Test
    void parserWithNegativeNumberTest() {
        Expression expression = Expression.parse("(-5+(-2*x))");

        assertEquals(-11, expression.eval("x = 3"));
    }

    @Test
    void invalidExpressionTest() {
        assertThrows(IllegalArgumentException.class,
                () -> Expression.parse("(3%2)"));
    }

    @Test
    void missingVariableTest() {
        Expression expression = new Variable("x");

        assertThrows(IllegalArgumentException.class,
                () -> expression.eval(""));
    }

    @Test
    void invalidAssignmentTest() {
        assertThrows(IllegalArgumentException.class,
                () -> Expression.parse("(x+y)").eval("x=1;invalid"));
    }
}
