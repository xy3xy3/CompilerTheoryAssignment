package com.tax;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TaxBracket类的单元测试
 */
public class TaxBracketTest {

    @Test
    public void testConstructor() {
        TaxBracket bracket = new TaxBracket(1000, 5000, 0.1);
        assertEquals(1000, bracket.getLowerBound(), "下限值设置错误");
        assertEquals(5000, bracket.getUpperBound(), "上限值设置错误");
        assertEquals(0.1, bracket.getRate(), "税率值设置错误");
    }

    @Test
    public void testSetters() {
        TaxBracket bracket = new TaxBracket(0, 0, 0);

        bracket.setLowerBound(2000);
        assertEquals(2000, bracket.getLowerBound(), "设置下限值后获取错误");

        bracket.setUpperBound(8000);
        assertEquals(8000, bracket.getUpperBound(), "设置上限值后获取错误");

        bracket.setRate(0.15);
        assertEquals(0.15, bracket.getRate(), "设置税率值后获取错误");
    }

    @Test
    public void testToString() {
        TaxBracket bracket = new TaxBracket(3000, 10000, 0.2);
        String expected = String.format("区间 [%.2f, %s], 税率 %.1f%%", 3000.0, "10000.0", 20.0);
        assertEquals(expected, bracket.toString(), "toString方法输出不符合预期");

        // 测试无上限的情况
        TaxBracket unlimitedBracket = new TaxBracket(5000, Double.MAX_VALUE, 0.25);
        String expectedUnlimited = String.format("区间 [%.2f, %s], 税率 %.1f%%", 5000.0, "无上限", 25.0);
        assertEquals(expectedUnlimited, unlimitedBracket.toString(), "无上限情况下toString方法输出不符合预期");
    }
}