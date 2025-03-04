package com.tax.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxCalculatorTest {

    @Test
    public void testCalculateTax_NoTaxWhenSalaryBelowThreshold() {
        // 创建计算器，设置起征点为 1600 元
        TaxCalculator calculator = new TaxCalculator(1600);
        // 添加税率级别（这里只添加一个即可，测试工资低于起征点的情况）
        calculator.addBracket(new TaxBracket(0, 500, 0.05));

        double salary = 1500; // 低于起征点
        double tax = calculator.calculateTax(salary);
        // 当工资低于起征点时，应缴税款为 0
        assertEquals(0, tax);
    }

    @Test
    public void testCalculateTax_WithTax() {
        TaxCalculator calculator = new TaxCalculator(1600);
        // 添加默认的税率级别
        calculator.addBracket(new TaxBracket(0, 500, 0.05));
        calculator.addBracket(new TaxBracket(500, 2000, 0.10));
        calculator.addBracket(new TaxBracket(2000, 5000, 0.15));
        calculator.addBracket(new TaxBracket(5000, 20000, 0.20));
        calculator.addBracket(new TaxBracket(20000, Double.MAX_VALUE, 0.25));

        double salary = 4000;
        // 计算过程说明：
        // 应纳税所得额 = 4000 - 1600 = 2400
        // 第一级：0~500，纳税 500 * 0.05 = 25
        // 第二级：500~2000，纳税 1500 * 0.10 = 150
        // 第三级：2000~2400，纳税 400 * 0.15 = 60
        // 总税款 = 25 + 150 + 60 = 235
        double tax = calculator.calculateTax(salary);
        assertEquals(235, tax, 0.001);
    }
}
