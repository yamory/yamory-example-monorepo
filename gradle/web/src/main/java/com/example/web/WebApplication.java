package com.example.web;

import com.example.core.Calculator;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 計算機Webアプリケーションのメインクラス
 */
public class WebApplication {
    private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        logger.info("WebアプリケーションをポートOUT {}で起動中...", PORT);

        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        // 静的コンテンツ用のサーブレット
        context.addServlet(new ServletHolder(new IndexServlet()), "/");
        context.addServlet(new ServletHolder(new CalculatorServlet()), "/api/calculate");

        server.setHandler(context);

        try {
            server.start();
            logger.info("Webアプリケーションが正常に開始されました: http://localhost:{}", PORT);
            server.join();
        } catch (Exception e) {
            logger.error("サーバーの開始に失敗しました", e);
            throw e;
        }
    }

    /**
     * インデックスページを提供するサーブレット
     */
    public static class IndexServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            resp.setContentType("text/html; charset=UTF-8");
            PrintWriter out = resp.getWriter();

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>計算機アプリケーション</title>
                        <meta charset="UTF-8">
                        <style>
                            body {
                                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                margin: 50px;
                                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                min-height: 100vh;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                            }
                            .calculator {
                                max-width: 400px;
                                margin: 0 auto;
                                background: white;
                                border-radius: 15px;
                                box-shadow: 0 10px 30px rgba(0,0,0,0.3);
                                padding: 30px;
                            }
                            h1 {
                                color: #333;
                                text-align: center;
                                margin-bottom: 30px;
                                font-size: 2em;
                            }
                            .input-group {
                                margin-bottom: 15px;
                            }
                            input, select, button {
                                padding: 12px;
                                margin: 5px;
                                font-size: 16px;
                                border: 2px solid #ddd;
                                border-radius: 8px;
                                width: calc(100% - 10px);
                            }
                            button {
                                background: linear-gradient(45deg, #4CAF50, #45a049);
                                color: white;
                                border: none;
                                cursor: pointer;
                                font-weight: bold;
                                transition: transform 0.2s;
                            }
                            button:hover {
                                transform: translateY(-2px);
                                box-shadow: 0 5px 15px rgba(0,0,0,0.2);
                            }
                            #result {
                                margin-top: 20px;
                                padding: 15px;
                                background: #f8f9fa;
                                border-radius: 8px;
                                border-left: 4px solid #4CAF50;
                                font-size: 18px;
                                font-weight: bold;
                            }
                            .operator-grid {
                                display: grid;
                                grid-template-columns: 1fr 1fr;
                                gap: 10px;
                            }
                        </style>
                    </head>
                    <body>
                        <div class="calculator">
                            <h1>🧮 計算機アプリケーション</h1>
                            <div class="input-group">
                                <input type="number" id="num1" placeholder="最初の数値" step="any">
                            </div>
                            <div class="input-group">
                                <select id="operator">
                                    <option value="+">➕ 加算 (+)</option>
                                    <option value="-">➖ 減算 (-)</option>
                                    <option value="*">✖️ 乗算 (×)</option>
                                    <option value="/">➗ 除算 (÷)</option>
                                    <option value="^">🔢 べき乗 (^)</option>
                                </select>
                            </div>
                            <div class="input-group">
                                <input type="number" id="num2" placeholder="二つ目の数値" step="any">
                            </div>
                            <button onclick="calculate()">🚀 計算実行</button>
                            <div id="result"></div>
                        </div>
                        <script>
                            async function calculate() {
                                const num1 = parseFloat(document.getElementById('num1').value);
                                const num2 = parseFloat(document.getElementById('num2').value);
                                const operator = document.getElementById('operator').value;

                                if (isNaN(num1) || isNaN(num2)) {
                                    document.getElementById('result').innerHTML = '❌ エラー: 有効な数値を入力してください';
                                    return;
                                }

                                try {
                                    const response = await fetch('/api/calculate', {
                                        method: 'POST',
                                        headers: { 'Content-Type': 'application/json' },
                                        body: JSON.stringify({ num1, num2, operator })
                                    });

                                    const data = await response.json();

                                    if (data.error) {
                                        document.getElementById('result').innerHTML = '❌ エラー: ' + data.error;
                                    } else {
                                        document.getElementById('result').innerHTML = '✅ 結果: ' + data.result;
                                    }
                                } catch (error) {
                                    document.getElementById('result').innerHTML = '❌ エラー: ' + error.message;
                                }
                            }
                        </script>
                    </body>
                    </html>
                    """;

            out.print(html);
        }
    }

    /**
     * 計算API用のサーブレット
     */
    public static class CalculatorServlet extends HttpServlet {
        private final Calculator calculator = new Calculator();
        private final Gson gson = new Gson();

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            resp.setContentType("application/json; charset=UTF-8");
            PrintWriter out = resp.getWriter();

            try {
                CalculationRequest request = gson.fromJson(req.getReader(), CalculationRequest.class);
                double result = performCalculation(request.num1(), request.operator(), request.num2());

                CalculationResponse response = CalculationResponse.success(result);

                out.print(gson.toJson(response));

            } catch (Exception e) {
                logger.error("計算エラー", e);

                CalculationResponse response = CalculationResponse.error(e.getMessage());

                out.print(gson.toJson(response));
            }
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

        record CalculationRequest(double num1, double num2, String operator) {
        }

        record CalculationResponse(Double result, String error) {
            static CalculationResponse success(double result) {
                return new CalculationResponse(result, null);
            }

            static CalculationResponse error(String error) {
                return new CalculationResponse(null, error);
            }
        }
    }
}