package com.example.app;

import com.example.core.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * 計算機アプリケーションのメインクラス
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final Calculator calculator;

    public Application() {
        this.calculator = new Calculator();
    }

    public static void main(String[] args) {
        logger.info("計算機アプリケーションを開始します");
        Application app = new Application();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== 簡単な計算機アプリケーション ===");
        System.out.println("利用可能な操作: +, -, *, /, ^, quit");

        while (true) {
            try {
                System.out.print("\n最初の数値を入力してください (quitで終了): ");
                String input = scanner.nextLine().trim();

                if ("quit".equalsIgnoreCase(input)) {
                    break;
                }

                double num1 = Double.parseDouble(input);

                System.out.print("演算子を入力してください (+, -, *, /, ^): ");
                String operator = scanner.nextLine().trim();

                System.out.print("二つ目の数値を入力してください: ");
                double num2 = Double.parseDouble(scanner.nextLine().trim());

                double result = performCalculation(num1, operator, num2);
                System.out.printf("結果: %.4f%n", result);

            } catch (NumberFormatException e) {
                System.out.println("エラー: 有効な数値を入力してください");
            } catch (IllegalArgumentException e) {
                System.out.println("エラー: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("予期しないエラーが発生しました: " + e.getMessage());
                logger.error("予期しないエラー", e);
            }
        }

        System.out.println("アプリケーションを終了します");
        scanner.close();
        logger.info("計算機アプリケーションを終了しました");
    }

    private double performCalculation(double num1, String operator, double num2) {
        return switch (operator) {
            case "+" -> calculator.add(num1, num2);
            case "-" -> calculator.subtract(num1, num2);
            case "*" -> calculator.multiply(num1, num2);
            case "/" -> calculator.divide(num1, num2);
            case "^" -> calculator.power(num1, num2);
            default -> throw new IllegalArgumentException("未対応の演算子: " + operator);
        };
    }
}