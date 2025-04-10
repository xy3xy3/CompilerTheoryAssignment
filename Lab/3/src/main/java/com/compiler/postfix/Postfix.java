package com.compiler.postfix;

import java.io.*;

/**
 * 后缀表达式转换程序
 * 将中缀表达式转换为后缀表达式
 *
 * @author xy3
 * @version 1.0
 */

/**
 * 主类，程序入口
 */
public class Postfix {
    /**
     * 主方法
     *
     * @param args 命令行参数
     * @throws IOException 如果输入输出出现异常
     */
    public static void main(String[] args) throws IOException {
        System.out.println("输入一个中缀表达式，程序将输出其后缀表示法：");
        Parser parser = new Parser();
        parser.expr();
        // 如果出错，则输出有效部分并追加错误信息
        if (parser.hasError()) {
            System.out.println(parser.getPostfixOutput() + " (error)");
            // 输出详细错误信息
            if (parser.getErrorType() != null) {
                System.out.println("错误类型: " + parser.getErrorType().getDescription());
                System.out.println("错误位置: " + parser.getErrorPosition());
                System.out.println("错误信息: " + parser.getErrorMessage());
            }
        } else {
            System.out.println(parser.getPostfixOutput());
            System.out.println("程序结束。");
        }
    }

    /**
     * 将中缀表达式转换为后缀表达式
     *
     * @param infixExpression 中缀表达式
     * @return 后缀表达式
     * @throws IOException 如果输入输出出现异常
     */
    public static String convertToPostfix(String infixExpression) throws IOException {
        // 添加换行符以确保表达式结束被正确处理
        // 保留原始输入中的换行符，如果没有才添加
        String input = infixExpression;
        if (!infixExpression.endsWith("\n") && !infixExpression.endsWith("\r")) {
            input = infixExpression + "\n";
        }
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Parser parser = new Parser(inputStream);
        parser.expr();

        if (parser.hasError()) {
            return parser.getPostfixOutput() + " (error)";
        } else {
            return parser.getPostfixOutput();
        }
    }

    /**
     * 将中缀表达式转换为后缀表达式，并返回详细错误信息
     *
     * @param infixExpression 中缀表达式
     * @return 包含后缀表达式和错误信息的对象数组
     * @throws IOException 如果输入输出出现异常
     */
    public static Object[] convertToPostfixWithErrorInfo(String infixExpression) throws IOException {
        // 添加换行符以确保表达式结束被正确处理
        // 保留原始输入中的换行符，如果没有才添加
        String input = infixExpression;
        if (!infixExpression.endsWith("\n") && !infixExpression.endsWith("\r")) {
            input = infixExpression + "\n";
        }
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Parser parser = new Parser(inputStream);
        parser.expr();

        if (parser.hasError()) {
            return new Object[] {
                parser.getPostfixOutput() + " (error)",
                parser.getErrorType() != null ? parser.getErrorType().getDescription() : "未知错误",
                parser.getErrorPosition(),
                parser.getErrorMessage()
            };
        } else {
            return new Object[] {
                parser.getPostfixOutput(),
                null,
                -1,
                null
            };
        }
    }
}
