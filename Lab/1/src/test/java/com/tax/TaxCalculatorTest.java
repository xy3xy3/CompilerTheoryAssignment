package com.tax;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TaxCalculator类的单元测试
 */
public class TaxCalculatorTest {

    private TaxCalculator calculator;

    @BeforeEach
    public void setUp() {
        // 在每个测试方法前，初始化一个标准税率计算器
        calculator = new TaxCalculator(1600);
        calculator.addBracket(new TaxBracket(0, 500, 0.05));
        calculator.addBracket(new TaxBracket(500, 2000, 0.10));
        calculator.addBracket(new TaxBracket(2000, 5000, 0.15));
        calculator.addBracket(new TaxBracket(5000, 20000, 0.20));
        calculator.addBracket(new TaxBracket(20000, Double.MAX_VALUE, 0.25));
    }

    @Test
    public void testNoTaxBelowThreshold() {
        // 测试低于起征点的工资不缴税
        assertEquals(0, calculator.calculateTax(1500), "工资低于起征点不应该缴税");
        assertEquals(0, calculator.calculateTax(1600), "工资等于起征点不应该缴税");
    }

    @Test
    public void testFirstBracketTax() {
        // 测试工资在第一个税率级别的情况
        // 应纳税所得额: 1700 - 1600 = 100，税率5%
        assertEquals(5, calculator.calculateTax(1700), 0.01, "第一级税率计算有误");
    }

    @Test
    public void testMultipleBracketsTax() {
        // 测试跨越多个税率级别的情况
        // 应纳税所得额: 4600 - 1600 = 3000
        // 第一级: 500 * 0.05 = 25
        // 第二级: 1500 * 0.10 = 150
        // 第三级: 1000 * 0.15 = 150
        // 总税额: 25 + 150 + 150 = 325
        assertEquals(325, calculator.calculateTax(4600), 0.01, "跨级别税率计算有误");
    }

    @Test
    public void testHighIncomeTax() {
        // 测试高收入的情况
        // 应纳税所得额: 30000 - 1600 = 28400
        // 第一级: 500 * 0.05 = 25
        // 第二级: 1500 * 0.10 = 150
        // 第三级: 3000 * 0.15 = 450
        // 第四级: 15000 * 0.20 = 3000
        // 第五级: 8400 * 0.25 = 2100
        // 总税额: 25 + 150 + 450 + 3000 + 2100 = 5725
        assertEquals(5725, calculator.calculateTax(30000), 0.01, "高收入税率计算有误");
    }

    @Test
    public void testChangeThreshold() {
        // 测试修改起征点后的税款计算
        calculator.setThreshold(2000);
        // 应纳税所得额: 4600 - 2000 = 2600
        // 第一级: 500 * 0.05 = 25
        // 第二级: 1500 * 0.10 = 150
        // 第三级: 600 * 0.15 = 90
        // 总税额: 25 + 150 + 90 = 265
        assertEquals(265, calculator.calculateTax(4600), 0.01, "修改起征点后税率计算有误");
    }

    @Test
    public void testAddBracket() {
        // 测试添加新的税率级别
        calculator = new TaxCalculator(1600);  // 重新初始化
        calculator.addBracket(new TaxBracket(0, 1000, 0.03));
        calculator.addBracket(new TaxBracket(1000, Double.MAX_VALUE, 0.05));

        // 应纳税所得额: 4600 - 1600 = 3000
        // 第一级: 1000 * 0.03 = 30
        // 第二级: 2000 * 0.05 = 100
        // 总税额: 30 + 100 = 130
        assertEquals(130, calculator.calculateTax(4600), 0.01, "添加新的税率级别后计算有误");
    }
}