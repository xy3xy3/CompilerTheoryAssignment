package com.tax.view;

import com.tax.model.TaxBracket;
import java.util.List;
import java.util.Scanner;

/**
 * 视图类，负责与用户进行交互（输入/输出）
 */
public class TaxView {
    private Scanner scanner;

    public TaxView() {
        scanner = new Scanner(System.in);
    }

    /**
     * 显示主菜单
     */
    public void displayMenu() {
        System.out.println("\n========== 个人所得税计算器 ==========");
        System.out.println("1. 输入工资并计算税额");
        System.out.println("2. 设置起征点");
        System.out.println("3. 修改税率表");
        System.out.println("4. 显示当前税率表");
        System.out.println("5. 退出");
        System.out.print("请选择操作（1-5）：");
    }

    /**
     * 获取用户输入
     * @return 用户输入的字符串
     */
    public String getInput() {
        return scanner.nextLine();
    }

    /**
     * 显示提示信息
     * @param message 提示信息
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * 显示税率表
     * @param threshold 起征点
     * @param brackets 税率级别列表
     */
    public void displayTaxBrackets(double threshold, List<TaxBracket> brackets) {
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
     * 显示税额计算结果
     * @param tax 应缴税款
     * @param salary 工资收入
     */
    public void displayTaxCalculation(double tax, double salary) {
        System.out.printf("应缴纳的个人所得税为：%.2f 元\n", tax);
        System.out.printf("税后实际收入：%.2f 元\n", salary - tax);
    }

    public void close() {
        scanner.close();
    }
}
