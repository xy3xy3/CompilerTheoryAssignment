package com.compiler.postfix;

import java.io.IOException;
import java.io.InputStream;

/**
 * 语法分析器类
 * 实现中缀表达式到后缀表达式的转换
 */
public class Parser {
    // 当前读取的字符
    private int lookahead;
    // 用于累积后缀表达式的输出
    private StringBuilder postfixOutput;
    // 是否发生错误
    private boolean hasError;
    // 输入流
    private InputStream inputStream;
    // 当前位置（字符索引）
    private int position;
    // 错误信息
    private String errorMessage;
    // 错误类型
    private ParserException.ErrorType errorType;

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
        this.position = 0;
        this.errorMessage = "";
        this.errorType = null;
        // 读取第一个字符
        lookahead = inputStream.read();
    }

    /**
     * 分析表达式
     *
     * @throws IOException 如果输入输出出现异常
     */
    public void expr() throws IOException {
        try {
            term();
            rest();
            // 如果还有未处理的字符，则说明输入格式错误
            if (lookahead != '\n' && lookahead != '\r' && lookahead != -1) {
                reportError(ParserException.ErrorType.SYNTAX_ERROR, "多余的字符");
            }
            // 检查表达式是否以运算符结尾（缺少右操作数）
            // 只在输入以运算符结尾时检查
            if (lookahead == '\n' || lookahead == '\r' || lookahead == -1) {
                if (postfixOutput.length() > 0) {
                    char lastChar = postfixOutput.charAt(postfixOutput.length() - 1);
                    if (lastChar == '+' || lastChar == '-') {
                        reportError(ParserException.ErrorType.SYNTAX_ERROR, "缺少右操作数");
                    }
                }
            }
        } catch (ParserException e) {
            // 记录错误信息
            hasError = true;
            errorMessage = e.getDetailMessage();
            errorType = e.getErrorType();
        } catch (Error e) {
            // 兼容原有的Error异常
            hasError = true;
            errorMessage = e.getMessage();
            errorType = ParserException.ErrorType.SYNTAX_ERROR;
        }
    }

    /**
     * 分析表达式的剩余部分
     * 使用循环代替尾递归
     *
     * @throws IOException 如果输入输出出现异常
     * @throws ParserException 如果输入不符合语法规则
     */
    public void rest() throws IOException, ParserException {
        while (true) {
            if (lookahead == '+') {
                match('+');
                try {
                    term();
                    postfixOutput.append('+');
                } catch (ParserException e) {
                    // 如果在term()中出错，尝试恢复并继续分析
                    if ((e.getErrorType() == ParserException.ErrorType.SYNTAX_ERROR ||
                         e.getErrorType() == ParserException.ErrorType.LEXICAL_ERROR) &&
                        (e.getDetailMessage().contains("期望一个数字") ||
                         e.getDetailMessage().contains("非法字符"))) {
                        // 缺少右操作数或非法字符，尝试跳过当前运算符
                        skipToNextOperator();
                        continue;
                    } else {
                        throw e; // 其他错误不尝试恢复
                    }
                }
            } else if (lookahead == '-') {
                match('-');
                try {
                    term();
                    postfixOutput.append('-');
                } catch (ParserException e) {
                    // 如果在term()中出错，尝试恢复并继续分析
                    if ((e.getErrorType() == ParserException.ErrorType.SYNTAX_ERROR ||
                         e.getErrorType() == ParserException.ErrorType.LEXICAL_ERROR) &&
                        (e.getDetailMessage().contains("期望一个数字") ||
                         e.getDetailMessage().contains("非法字符"))) {
                        // 缺少右操作数或非法字符，尝试跳过当前运算符
                        skipToNextOperator();
                        continue;
                    } else {
                        throw e; // 其他错误不尝试恢复
                    }
                }
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
     * 跳过到下一个运算符或表达式结束
     * 用于错误恢复
     *
     * @throws IOException 如果输入输出出现异常
     */
    private void skipToNextOperator() throws IOException {
        while (lookahead != '+' && lookahead != '-' &&
               lookahead != '\n' && lookahead != '\r' && lookahead != -1) {
            lookahead = inputStream.read();
            position++;
        }
    }

    /**
     * 分析项（单个数字）
     *
     * @throws IOException 如果输入输出出现异常
     * @throws ParserException 如果输入不符合语法规则
     */
    public void term() throws IOException, ParserException {
        if (Character.isDigit((char)lookahead)) {
            char digit = (char)lookahead;
            postfixOutput.append(digit);
            match(lookahead);
            // 检查是否出现连续数字（缺少运算符）
            if (Character.isDigit((char)lookahead)) {
                reportError(ParserException.ErrorType.SYNTAX_ERROR, "连续数字，缺少运算符");
            }
        } else if (lookahead == ' ' || lookahead == '\t') {
            match(lookahead);
            term();
        } else if (lookahead == '+' || lookahead == '-') {
            reportError(ParserException.ErrorType.SYNTAX_ERROR, "缺少左操作数");
        } else if (lookahead < 0 || lookahead > 127 || (!Character.isLetterOrDigit((char)lookahead) && lookahead != '\n' && lookahead != '\r')) {
            reportError(ParserException.ErrorType.LEXICAL_ERROR, "非法字符: '" + (char)lookahead + "'");
        } else {
            reportError(ParserException.ErrorType.SYNTAX_ERROR, "期望一个数字，得到: '" + (char)lookahead + "'");
        }
    }

    /**
     * 匹配当前字符
     *
     * @param t 期望匹配的字符
     * @throws IOException 如果输入输出出现异常
     * @throws ParserException 如果匹配失败
     */
    public void match(int t) throws IOException, ParserException {
        if (lookahead == t) {
            lookahead = inputStream.read();
            position++;
        } else {
            reportError(ParserException.ErrorType.SYNTAX_ERROR, "期望 '" + (char)t + "', 得到 '" + (char)lookahead + "'");
        }
    }

    /**
     * 报告错误
     *
     * @param errorType 错误类型
     * @param message 错误消息
     * @throws ParserException 包含错误信息的异常
     */
    private void reportError(ParserException.ErrorType errorType, String message) throws ParserException {
        throw new ParserException(errorType, position, message);
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

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * 获取错误类型
     *
     * @return 错误类型
     */
    public ParserException.ErrorType getErrorType() {
        return errorType;
    }

    /**
     * 获取错误位置
     *
     * @return 错误位置
     */
    public int getErrorPosition() {
        return position;
    }
}
