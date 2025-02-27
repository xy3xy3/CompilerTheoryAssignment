package com.tax;

import java.util.ArrayList;
import java.util.List;

/**
 * 税率计算器类，管理税率级别并计算应缴税款
 */
public class TaxCalculator {
    private double threshold; // 起征点
    private List<TaxBracket> brackets; // 税率级别列表

    /**
     * 构造一个新的税率计算器
     * @param threshold 税收起征点
     */
    public TaxCalculator(double threshold) {
        this.threshold = threshold;
        this.brackets = new ArrayList<>();
    }

    /**
     * 添加一个税率级别
     * @param bracket 税率级别
     */
    public void addBracket(TaxBracket bracket) {
        brackets.add(bracket);
    }

    /**
     * 计算应缴税款
     * @param salary 工资收入
     * @return 应缴税款
     */
    public double calculateTax(double salary) {
        // 如果工资低于起征点，不缴税
        if (salary <= threshold) {
            return 0;
        }

        double taxableIncome = salary - threshold; // 应税收入
        double tax = 0;

        // 按照级别累进计算税款
        for (TaxBracket bracket : brackets) {
            double lower = bracket.getLowerBound();
            double upper = bracket.getUpperBound();
            double rate = bracket.getRate();

            if (taxableIncome > lower) {
                double taxableAmountInBracket = Math.min(taxableIncome - lower, upper - lower);
                tax += taxableAmountInBracket * rate;
            }
        }

        return tax;
    }

    /**
     * 显示当前的税率级别
     */
    public void displayTaxBrackets() {
        System.out.println("\n当前个人所得税税率表（起征点：" + threshold + " 元）:");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-10s\n", "级别", "下限", "上限", "税率");
        System.out.println("-------------------------------------------------");

        for (int i = 0; i < brackets.size(); i++) {
            TaxBracket bracket = brackets.get(i);
            String upperBoundStr = bracket.getUpperBound() == Double.MAX_VALUE ? "无上限" : String.valueOf(bracket.getUpperBound());
            System.out.printf("%-5d %-15.2f %-15s %-10.1f%%\n",
                            i + 1,
                            bracket.getLowerBound(),
                            upperBoundStr,
                            bracket.getRate() * 100);
        }
        System.out.println("-------------------------------------------------");
    }

    /**
     * 获取所有税率级别
     * @return 税率级别列表
     */
    public List<TaxBracket> getBrackets() {
        return brackets;
    }

    /**
     * 设置新的起征点
     * @param threshold 新的起征点
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    /**
     * 获取当前起征点
     * @return 当前起征点
     */
    public double getThreshold() {
        return threshold;
    }
}