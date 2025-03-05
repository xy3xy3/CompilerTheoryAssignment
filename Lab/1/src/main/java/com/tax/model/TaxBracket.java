package com.tax.model;

/**
 * 税率级别类，表示一个税率区间
 *
 * <p>每个税率级别包含收入下限、收入上限以及适用的税率，
 * 通过该类可以描述不同收入区间对应的税率信息。</p>
 *
 * @author xy3
 * @version 1.0
 */
public class TaxBracket {
    private double lowerBound; // 下限
    private double upperBound; // 上限
    private double rate; // 税率

    /**
     * 构造一个新的税率级别
     *
     * @param lowerBound 收入下限
     * @param upperBound 收入上限
     * @param rate       适用税率（例如 0.05 表示 5%）
     */
    public TaxBracket(double lowerBound, double upperBound, double rate) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.rate = rate;
    }

    /**
     * 获取收入下限
     *
     * @return 收入下限值
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * 设置收入下限
     *
     * @param lowerBound 新的收入下限
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * 获取收入上限
     *
     * @return 收入上限值
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * 设置收入上限
     *
     * @param upperBound 新的收入上限
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * 获取适用税率
     *
     * @return 税率（例如 0.05 表示 5%）
     */
    public double getRate() {
        return rate;
    }

    /**
     * 设置适用税率
     *
     * @param rate 新的税率
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * 重写 toString 方法，返回格式化后的税率级别信息
     *
     * @return 税率级别字符串描述
     */
    @Override
    public String toString() {
        String upperBoundStr = upperBound == Double.MAX_VALUE ? "无上限" : String.format("%.2f", upperBound);
        return String.format("区间 [%.2f, %s], 税率 %.1f%%", lowerBound, upperBoundStr, rate * 100);
    }
}
