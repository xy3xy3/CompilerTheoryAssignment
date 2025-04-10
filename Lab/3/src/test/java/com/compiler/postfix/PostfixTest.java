package com.compiler.postfix;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

/**
 * Postfix类的单元测试
 */
public class PostfixTest {

    //==========================================================
    // 原始测试用例 (Original Test Cases)
    //==========================================================

    @Test
    @DisplayName("原始测试用例1: 中缀表达式 9-5+2")
    public void testOriginal001() throws IOException {
        String infixExpression = "9-5+2";
        String expectedPostfix = "95-2+";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "原始测试用例1失败");
    }

    @Test
    @DisplayName("原始测试用例2: 中缀表达式 1-2+3-4+5-6+7-8+9-0")
    public void testOriginal002() throws IOException {
        String infixExpression = "1-2+3-4+5-6+7-8+9-0";
        String expectedPostfix = "12-3+4-5+6-7+8-9+0-";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "原始测试用例2失败");
    }

    @Test
    @DisplayName("原始测试用例3: 错误的中缀表达式 95+2")
    public void testOriginal003() throws IOException {
        String infixExpression = "95+2";
        String expectedPostfix = "9 (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "原始测试用例3失败");
    }

    @Test
    @DisplayName("原始测试用例4: 错误的中缀表达式 9-5+-2")
    public void testOriginal004() throws IOException {
        String infixExpression = "9-5+-2";
        String expectedPostfix = "95- (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "原始测试用例4失败");
    }

    //==========================================================
    // 自定义测试用例 (Custom Test Cases)
    //==========================================================

    @Test
    @DisplayName("自定义测试1: 包含空格的中缀表达式 1 + 2 + 3 + 4 + 5")
    public void testCustom001() throws IOException {
        String infixExpression = "1 + 2 + 3 + 4 + 5";
        String expectedPostfix = "12+3+4+5+";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "自定义测试1失败");
    }

    @Test
    @DisplayName("自定义测试2: 错误的中缀表达式 1+")
    public void testCustom002() throws IOException {
        String infixExpression = "1+";
        String expectedPostfix = "1 (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "自定义测试2失败");
    }

    @Test
    @DisplayName("自定义测试3: 错误的中缀表达式 1+2+")
    public void testCustom003() throws IOException {
        String infixExpression = "1+2+";
        String expectedPostfix = "12+ (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "自定义测试3失败");
    }

    @Test
    @DisplayName("自定义测试4: 测试连续数字错误")
    public void testCustom004() throws IOException {
        String infixExpression = "12+3";
        String expectedPostfix = "1 (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "连续数字错误测试失败");
    }

    @Test
    @DisplayName("自定义测试5: 测试非数字输入错误")
    public void testCustom005() throws IOException {
        String infixExpression = "a+b";
        String expectedPostfix = " (error)";

        String actualPostfix = Postfix.convertToPostfix(infixExpression);

        assertEquals(expectedPostfix, actualPostfix, "非数字输入错误测试失败");
    }
}
