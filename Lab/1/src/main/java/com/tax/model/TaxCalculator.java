package com.tax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 税率计算器类，管理税率级别并计算应缴税款
 */
public class TaxCalculator {
    private double threshold; // 起征点
    private List<TaxBracket> brackets; // 税率级别列表

    public TaxCalculator(double threshold) {
        this.threshold = threshold;
        this.brackets = new ArrayList<>();
    }

    public void addBracket(TaxBracket bracket) {
        brackets.add(bracket);
    }

    /**
     * 计算应缴税款
     * @param salary 工资收入
     * @return 应缴税款
     */
    public double calculateTax(double salary) {
        if (salary <= threshold) {
            return 0;
        }
        double taxableIncome = salary - threshold;
        double tax = 0;
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

    public List<TaxBracket> getBrackets() {
        return brackets;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }
}
