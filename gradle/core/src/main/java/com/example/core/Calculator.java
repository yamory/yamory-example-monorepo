package com.example.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基本的な計算機能を提供するクラス
 */
public class Calculator {
    private static final Logger logger = LoggerFactory.getLogger(Calculator.class);

    /**
     * 加算を実行します
     */
    public double add(double a, double b) {
        double result = a + b;
        logger.info("加算: {} + {} = {}", a, b, result);
        return result;
    }

    /**
     * 減算を実行します
     */
    public double subtract(double a, double b) {
        double result = a - b;
        logger.info("減算: {} - {} = {}", a, b, result);
        return result;
    }

    /**
     * 乗算を実行します
     */
    public double multiply(double a, double b) {
        double result = a * b;
        logger.info("乗算: {} × {} = {}", a, b, result);
        return result;
    }

    /**
     * 除算を実行します
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("ゼロで割ることはできません");
        }
        double result = a / b;
        logger.info("除算: {} ÷ {} = {}", a, b, result);
        return result;
    }

    /**
     * べき乗を実行します
     */
    public double power(double base, double exponent) {
        double result = Math.pow(base, exponent);
        logger.info("べき乗: {}^{} = {}", base, exponent, result);
        return result;
    }
}