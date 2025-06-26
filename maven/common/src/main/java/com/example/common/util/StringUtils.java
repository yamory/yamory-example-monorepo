package com.example.common.util;

import java.util.Objects;

/**
 * 文字列操作のユーティリティクラス
 */
public class StringUtils {

    private StringUtils() {
        // ユーティリティクラスのため、インスタンス化を防ぐ
    }

    /**
     * 文字列がnullまたは空文字かどうかをチェック
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 文字列がnullまたは空文字または空白のみかどうかをチェック
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 文字列を指定された文字で左埋め
     */
    public static String leftPad(String str, int length, char padChar) {
        if (str == null) {
            str = "";
        }

        if (str.length() >= length) {
            return str;
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length - str.length(); i++) {
            sb.append(padChar);
        }
        sb.append(str);

        return sb.toString();
    }

    /**
     * 文字列をキャメルケースに変換
     */
    public static String toCamelCase(String str) {
        if (isEmpty(str)) {
            return str == null ? "" : str;
        }

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : str.toCharArray()) {
            if (c == '_' || c == '-' || c == ' ') {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }

    /**
     * 文字列の最初の文字を大文字に変換
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str == null ? "" : str;
        }

        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}