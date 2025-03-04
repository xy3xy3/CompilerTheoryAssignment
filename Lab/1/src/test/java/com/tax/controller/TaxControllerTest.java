package com.tax.controller;

import com.tax.model.TaxCalculator;
import com.tax.model.TaxBracket;
import com.tax.view.TaxView;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class TaxControllerTest {

    @Test
    public void testCalculateTaxForSalary() {
        // 创建模拟的 TaxView
        TaxView mockView = mock(TaxView.class);
        // 设置模拟行为：先提示信息后返回一个工资字符串
        when(mockView.getInput()).thenReturn("4000");

        // 创建 TaxCalculator，并添加相应税率级别
        TaxCalculator calculator = new TaxCalculator(1600);
        calculator.addBracket(new TaxBracket(0, 500, 0.05));
        calculator.addBracket(new TaxBracket(500, 2000, 0.10));
        calculator.addBracket(new TaxBracket(2000, 5000, 0.15));
        calculator.addBracket(new TaxBracket(5000, 20000, 0.20));
        calculator.addBracket(new TaxBracket(20000, Double.MAX_VALUE, 0.25));

        // 构造 TaxController 时若支持依赖注入，则可以传入模拟对象
        TaxController controller = new TaxController(calculator, mockView);
        // 执行计算逻辑
        controller.calculateTaxForSalary();

        // 验证是否调用了显示税额的输出方法
        verify(mockView, atLeastOnce()).displayTaxCalculation(anyDouble(), eq(4000.0));
    }
}
