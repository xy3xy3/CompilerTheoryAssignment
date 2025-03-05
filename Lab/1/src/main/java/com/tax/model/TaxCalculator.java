package com.tax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 税率计算器类，管理税率级别并计算应缴税款
 *
 * <p>该类用于存储起征点和多个税率级别，提供根据工资收入计算应缴税款的方法。</p>
 *
 * @author xy3
 * @version 1.0
 */
public class TaxCalculator {
    private double threshold; // 起征点
    private List<TaxBracket> brackets; // 税率级别列表

    /**
     * 构造函数
     *
     * @param threshold 起征点，当收入低于该值时不计税
     */
    public TaxCalculator(double threshold) {
        this.threshold = threshold;
        this.brackets = new ArrayList<>();
    }

    /**
     * 添加一个税率级别
     *
     * @param bracket 要添加的税率级别
     */
    public void addBracket(TaxBracket bracket) {
        brackets.add(bracket);
    }

    /**
     * 根据工资收入计算应缴税款
     *
     * <p>计算规则：若工资低于起征点，则不计税；否则按照各个税率级别累进计算应缴税款。</p>
     *
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

    /**
     * 获取所有税率级别列表
     *
     * @return 税率级别列表
     */
    public List<TaxBracket> getBrackets() {
        return brackets;
    }

    /**
     * 设置新的起征点
     *
     * @param threshold 新的起征点值
     */
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    /**
     * 获取当前起征点
     *
     * @return 起征点值
     */
    public double getThreshold() {
        return threshold;
    }
}
