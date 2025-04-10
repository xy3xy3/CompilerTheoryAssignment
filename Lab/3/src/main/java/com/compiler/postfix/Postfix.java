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
 * 语法分析器类
 * 实现中缀表达式到后缀表达式的转换
 */
class Parser {
    // 当前读取的字符
    private int lookahead;
    // 用于累积后缀表达式的输出
    private StringBuilder postfixOutput;
    // 是否发生错误
    private boolean hasError;
    // 输入流
    private InputStream inputStream;

    /**
     * 构造函数，初始化语法分析器
     *
     * @throws IOException 如果输入输出出现异常
     */
    public Parser() throws IOException {
        this(System.in);
    }

    /**
     * 用于测试的构造函数，接受一个输入流
     *
     * @param inputStream 输入流
     * @throws IOException 如果输入输出出现异常
     */
    public Parser(InputStream inputStream) throws IOException {
        postfixOutput = new StringBuilder();
        hasError = false;
        this.inputStream = inputStream;
        // 读取第一个字符
        lookahead = inputStream.read();
    }

    /**
     * 分析表达式
     *
     * @throws IOException 如果输入输出出现异常
     */
    void expr() throws IOException {
        try {
            term();
            rest();
            // 如果还有未处理的字符，则说明输入格式错误
            if (lookahead != '\n' && lookahead != '\r' && lookahead != -1) {
                throw new Error("多余的字符");
            }
        } catch (Error e) {
            // 出错时不再打印详细信息，由主函数统一输出
            hasError = true;
        }
    }

    /**
     * 分析表达式的剩余部分
     * 使用循环代替尾递归
     *
     * @throws IOException 如果输入输出出现异常
     */
    void rest() throws IOException {
        while (true) {
            if (lookahead == '+') {
                match('+');
                term();
                postfixOutput.append('+');
            } else if (lookahead == '-') {
                match('-');
                term();
                postfixOutput.append('-');
            } else if (lookahead == ' ' || lookahead == '\t') {
                // 忽略空格和制表符
                match(lookahead);
                continue;
            } else if (lookahead == '\n' || lookahead == '\r' || lookahead == -1) {
                // 到达输入结束
                break;
            } else {
                break;
            }
        }
    }

    /**
     * 分析项（单个数字）
     *
     * @throws IOException 如果输入输出出现异常
     */
    void term() throws IOException {
        if (Character.isDigit((char)lookahead)) {
            char digit = (char)lookahead;
            postfixOutput.append(digit);
            match(lookahead);
            // 检查是否出现连续数字（缺少运算符）
            if (Character.isDigit((char)lookahead)) {
                throw new Error("连续数字");
            }
        } else if (lookahead == ' ' || lookahead == '\t') {
            match(lookahead);
            term();
        } else {
            throw new Error("期望一个数字");
        }
    }

    /**
     * 匹配当前字符
     *
     * @param t 期望匹配的字符
     * @throws IOException 如果输入输出出现异常
     */
    void match(int t) throws IOException {
        if (lookahead == t) {
            lookahead = inputStream.read();
        } else {
            throw new Error("匹配错误");
        }
    }

    /**
     * 获取转换后的后缀表达式
     *
     * @return 后缀表达式字符串
     */
    public String getPostfixOutput() {
        return postfixOutput.toString();
    }

    /**
     * 检查是否有错误发生
     *
     * @return 是否有错误
     */
    public boolean hasError() {
        return hasError;
    }
}

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
