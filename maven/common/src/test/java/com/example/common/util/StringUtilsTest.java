package com.example.common.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * StringUtilsクラスのテスト
 */
class StringUtilsTest {

    @Test
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" "));
        assertFalse(StringUtils.isEmpty("hello"));
    }

    @Test
    void testIsBlank() {
        assertTrue(StringUtils.isBlank(null));
        assertTrue(StringUtils.isBlank(""));
        assertTrue(StringUtils.isBlank(" "));
        assertTrue(StringUtils.isBlank("  \t  "));
        assertFalse(StringUtils.isBlank("hello"));
        assertFalse(StringUtils.isBlank(" hello "));
    }

    @Test
    void testLeftPad() {
        assertEquals("00123", StringUtils.leftPad("123", 5, '0'));
        assertEquals("123", StringUtils.leftPad("123", 3, '0'));
        assertEquals("123", StringUtils.leftPad("123", 2, '0'));
        assertEquals("00000", StringUtils.leftPad(null, 5, '0'));
        assertEquals("  123", StringUtils.leftPad("123", 5, ' '));
    }

    @Test
    void testToCamelCase() {
        assertEquals("helloWorld", StringUtils.toCamelCase("hello_world"));
        assertEquals("helloWorld", StringUtils.toCamelCase("hello-world"));
        assertEquals("helloWorld", StringUtils.toCamelCase("hello world"));
        assertEquals("", StringUtils.toCamelCase(""));
        assertEquals("", StringUtils.toCamelCase(null));
        assertEquals("hello", StringUtils.toCamelCase("HELLO"));
    }

    @Test
    void testCapitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"));
        assertEquals("Hello", StringUtils.capitalize("Hello"));
        assertEquals("HELLO", StringUtils.capitalize("hELLO"));
        assertEquals("", StringUtils.capitalize(""));
        assertEquals("", StringUtils.capitalize(null));
        assertEquals("H", StringUtils.capitalize("h"));
    }
}