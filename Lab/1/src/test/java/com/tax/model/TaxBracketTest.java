package com.tax.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaxBracketTest {

    @Test
    public void testToString_NormalUpperBound() {
        TaxBracket bracket = new TaxBracket(500, 2000, 0.10);
        String expected = "区间 [500.00, 2000.00], 税率 10.0%";
        assertEquals(expected, bracket.toString());
    }

    @Test
    public void testToString_InfiniteUpperBound() {
        TaxBracket bracket = new TaxBracket(20000, Double.MAX_VALUE, 0.25);
        String expected = "区间 [20000.00, 无上限], 税率 25.0%";
        assertEquals(expected, bracket.toString());
    }
}
