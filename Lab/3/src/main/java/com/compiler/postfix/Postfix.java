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
        // 如果出错，则输出有效部分并追加 " (error)"
        if (parser.hasError()) {
            System.out.println(parser.getPostfixOutput() + " (error)");
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
        String input = infixExpression + "\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Parser parser = new Parser(inputStream);
        parser.expr();

        if (parser.hasError()) {
            return parser.getPostfixOutput() + " (error)";
        } else {
            return parser.getPostfixOutput();
        }
    }
}
