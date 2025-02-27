package com.tax;

/**
 * 税率级别类，表示一个税率区间
 */
public class TaxBracket {
    private double lowerBound; // 下限
    private double upperBound; // 上限
    private double rate;       // 税率

    /**
     * 构造一个新的税率级别
     * @param lowerBound 收入下限
     * @param upperBound 收入上限
     * @param rate 适用税率（例如0.05表示5%）
     */
    public TaxBracket(double lowerBound, double upperBound, double rate) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.rate = rate;
    }

    /**
     * 获取收入下限
     * @return 下限值
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * 设置收入下限
     * @param lowerBound 新的下限值
     */
    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    /**
     * 获取收入上限
     * @return
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * 设置收入上限
     * @param upperBound 新的上限值
     */
    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    /**
     * 获取税率
     * @return 税率值（例如0.05表示5%）
     */
    public double getRate() {
        return rate;
    }

    /**
     * 设置税率
     * @param rate 新的税率值
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        String upperBoundStr = upperBound == Double.MAX_VALUE ? "无上限" : String.valueOf(upperBound);
        return String.format("区间 [%.2f, %s], 税率 %.1f%%",
                          lowerBound,
                          upperBoundStr,
                          rate * 100);
    }
}