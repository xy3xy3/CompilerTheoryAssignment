package com.compiler.postfix;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

/**
 * Postfix类的单元测试
 */
public class PostfixTest {

    @Test
    @DisplayName("测试用例1: 简单的中缀表达式 9-5+2")
    public void testCase001() throws IOException {
        String infixExpression = "9-5+2";
        String expectedPostfix = "95-2+";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "测试用例1失败");
    }
    
    @Test
    @DisplayName("测试用例2: 包含空格的中缀表达式 1 + 2 + 3 + 4 + 5")
    public void testCase002() throws IOException {
        String infixExpression = "1 + 2 + 3 + 4 + 5";
        String expectedPostfix = "12+3+4+5+";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "测试用例2失败");
    }
    
    @Test
    @DisplayName("测试用例3: 错误的中缀表达式 1+")
    public void testCase003() throws IOException {
        String infixExpression = "1+";
        String expectedPostfix = "1 (error)";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "测试用例3失败");
    }
    
    @Test
    @DisplayName("测试用例4: 错误的中缀表达式 1+2+")
    public void testCase004() throws IOException {
        String infixExpression = "1+2+";
        String expectedPostfix = "12+ (error)";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "测试用例4失败");
    }
    
    @Test
    @DisplayName("测试连续数字错误")
    public void testConsecutiveDigitsError() throws IOException {
        String infixExpression = "12+3";
        String expectedPostfix = "1 (error)";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "连续数字错误测试失败");
    }
    
    @Test
    @DisplayName("测试非数字输入错误")
    public void testNonDigitError() throws IOException {
        String infixExpression = "a+b";
        String expectedPostfix = " (error)";
        
        String actualPostfix = Postfix.convertToPostfix(infixExpression);
        
        assertEquals(expectedPostfix, actualPostfix, "非数字输入错误测试失败");
    }
}
