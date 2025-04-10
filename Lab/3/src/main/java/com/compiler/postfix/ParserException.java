package com.compiler.postfix;

/**
 * 语法分析器异常类
 * 用于表示在语法分析过程中发生的各种错误
 */
public class ParserException extends Exception {
    /**
     * 错误类型枚举
     */
    public enum ErrorType {
        /**
         * 词法错误：输入中包含非法字符
         */
        LEXICAL_ERROR("词法错误"),
        
        /**
         * 语法错误：输入不符合语法规则
         */
        SYNTAX_ERROR("语法错误");
        
        private final String description;
        
        ErrorType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * 错误类型
     */
    private final ErrorType errorType;
    
    /**
     * 错误位置（字符索引）
     */
    private final int position;
    
    /**
     * 详细错误消息
     */
    private final String detailMessage;
    
    /**
     * 构造函数
     *
     * @param errorType 错误类型
     * @param position 错误位置
     * @param detailMessage 详细错误消息
     */
    public ParserException(ErrorType errorType, int position, String detailMessage) {
        super(errorType.getDescription() + " 在位置 " + position + ": " + detailMessage);
        this.errorType = errorType;
        this.position = position;
        this.detailMessage = detailMessage;
    }
    
    /**
     * 获取错误类型
     *
     * @return 错误类型
     */
    public ErrorType getErrorType() {
        return errorType;
    }
    
    /**
     * 获取错误位置
     *
     * @return 错误位置
     */
    public int getPosition() {
        return position;
    }
    
    /**
     * 获取详细错误消息
     *
     * @return 详细错误消息
     */
    public String getDetailMessage() {
        return detailMessage;
    }
}
