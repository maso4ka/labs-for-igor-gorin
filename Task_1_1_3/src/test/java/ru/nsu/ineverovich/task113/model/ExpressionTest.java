package ru.nsu.ineverovich.task113.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тесты иерархии математических выражений.
 */
class ExpressionTest {
    @Test
    void printsExpression() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals("(3+(2*x))", expression.toString());
    }

    @Test
    void evaluatesExpression() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals(23, expression.eval("x = 10; y = 13"));
    }

    @Test
    void differentiatesByVariable() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals("(0+((0*x)+(2*1)))", expression.derivative("x").toString());
    }

    @Test
    void differentiatesByOtherVariable() {
        Expression expression = new Add(
                new Number(3),
                new Mul(new Number(2), new Variable("x"))
        );

        assertEquals("(0+((0*x)+(2*0)))", expression.derivative("y").toString());
    }

    @Test
    void parsesExpression() {
        Expression expression = Expression.parse("(3+(2*x))");

        assertEquals("(3+(2*x))", expression.toString());
        assertEquals(23, expression.eval("x = 10"));
    }

    @Test
    void parsesNegativeNumber() {
        Expression expression = Expression.parse("-5");
        assertEquals("-5", expression.toString());
    }

    @Test
    void supportsAllOperations() {
        assertEquals(7, Expression.parse("(10-3)").eval(""));
        assertEquals(30, Expression.parse("(10*3)").eval(""));
        assertEquals(3, Expression.parse("(10/3)").eval(""));
    }

    @Test
    void supportsLongVariableNames() {
        Expression expression = Expression.parse("(student+(2*score))");

        assertEquals(17, expression.eval("student = 3; score = 7"));
    }

    @Test
    void throwsWhenVariableIsNotAssigned() {
        Expression expression = new Variable("x");

        assertThrows(IllegalArgumentException.class, () -> expression.eval("y = 10"));
    }

    @Test
    void returnsCorrectOperations() {
        assertEquals(Operation.ADD, new Add(new Number(1), new Number(2)).getOperation());
        assertEquals(Operation.SUB, new Sub(new Number(1), new Number(2)).getOperation());
        assertEquals(Operation.MUL, new Mul(new Number(1), new Number(2)).getOperation());
        assertEquals(Operation.DIV, new Div(new Number(1), new Number(2)).getOperation());
    }
}
