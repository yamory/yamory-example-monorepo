package com.example.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Calculator テスト")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("加算のテスト")
    void testAdd() {
        assertEquals(5.0, calculator.add(2.0, 3.0));
        assertEquals(0.0, calculator.add(-1.0, 1.0));
        assertEquals(-5.0, calculator.add(-2.0, -3.0));
    }

    @Test
    @DisplayName("減算のテスト")
    void testSubtract() {
        assertEquals(1.0, calculator.subtract(4.0, 3.0));
        assertEquals(-2.0, calculator.subtract(-1.0, 1.0));
        assertEquals(1.0, calculator.subtract(-2.0, -3.0));
    }

    @Test
    @DisplayName("乗算のテスト")
    void testMultiply() {
        assertEquals(6.0, calculator.multiply(2.0, 3.0));
        assertEquals(-6.0, calculator.multiply(-2.0, 3.0));
        assertEquals(0.0, calculator.multiply(0.0, 5.0));
    }

    @Test
    @DisplayName("除算のテスト")
    void testDivide() {
        assertEquals(2.0, calculator.divide(6.0, 3.0));
        assertEquals(-2.0, calculator.divide(-6.0, 3.0));
        assertEquals(0.0, calculator.divide(0.0, 5.0));
    }

    @Test
    @DisplayName("ゼロ除算のテスト")
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(5.0, 0.0);
        });
    }

    @Test
    @DisplayName("べき乗のテスト")
    void testPower() {
        assertEquals(8.0, calculator.power(2.0, 3.0));
        assertEquals(1.0, calculator.power(5.0, 0.0));
        assertEquals(0.25, calculator.power(2.0, -2.0), 0.001);
    }
}