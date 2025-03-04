package com.tax;

import com.tax.controller.TaxController;

public class PersonalTaxApp {
    public static void main(String[] args) {
        TaxController controller = new TaxController();
        controller.run();
    }
}
