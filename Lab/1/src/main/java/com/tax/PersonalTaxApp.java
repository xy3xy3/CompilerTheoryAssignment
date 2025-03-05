package com.tax;

import com.tax.controller.TaxController;

/**
 * 个人所得税计算器主程序
 *
 * <p>这是个人所得税计算器的入口类，负责初始化控制器并启动应用程序。</p>
 *
 * @author xy3
 * @version 1.0
 */
public class PersonalTaxApp {

    /**
     * 默认构造函数
     *
     * <p>该构造函数被设为私有，防止外部实例化，因为本类只包含静态入口方法。</p>
     */
    private PersonalTaxApp() {
        // 禁止实例化
    }

    /**
     * 应用程序入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        TaxController controller = new TaxController();
        controller.run();
    }
}
