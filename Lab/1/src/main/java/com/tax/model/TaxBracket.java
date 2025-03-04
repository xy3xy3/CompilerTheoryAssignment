package com.tax.model;

/**
 * 税率级别类，表示一个税率区间
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

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        String upperBoundStr = upperBound == Double.MAX_VALUE ? "无上限" : String.format("%.2f", upperBound);
        return String.format("区间 [%.2f, %s], 税率 %.1f%%", lowerBound, upperBoundStr, rate * 100);
    }

}
