package com.tax;

import java.util.Scanner;

// 主程序类，提供命令行交互
public class PersonalTaxApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 初始化税率计算器，默认起征点为 1600 元
        TaxCalculator calculator = new TaxCalculator(1600);
        // 添加默认的 5 个税率级别
        calculator.addBracket(new TaxBracket(0, 500, 0.05));
        calculator.addBracket(new TaxBracket(500, 2000, 0.10));
        calculator.addBracket(new TaxBracket(2000, 5000, 0.15));
        calculator.addBracket(new TaxBracket(5000, 20000, 0.20));
        calculator.addBracket(new TaxBracket(20000, Double.MAX_VALUE, 0.25)); // 上限无限制

        boolean exit = false;
        while (!exit) {
            System.out.println("\n========== 个人所得税计算器 ==========");
            System.out.println("1. 输入工资并计算税额");
            System.out.println("2. 设置起征点");
            System.out.println("3. 修改税率表");
            System.out.println("4. 显示当前税率表");
            System.out.println("5. 退出");
            System.out.print("请选择操作（1-5）：");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    calculateTaxForSalary(scanner, calculator);
                    break;
                case "2":
                    setNewThreshold(scanner, calculator);
                    break;
                case "3":
                    modifyTaxBracket(scanner, calculator);
                    break;
                case "4":
                    calculator.displayTaxBrackets();
                    break;
                case "5":
                    exit = true;
                    System.out.println("程序退出，再见！");
                    break;
                default:
                    System.out.println("无效的选项，请重新选择。");
                    break;
            }
        }
        scanner.close();
    }

    // 计算工资税额
    private static void calculateTaxForSalary(Scanner scanner, TaxCalculator calculator) {
        try {
            System.out.print("请输入月工资薪金总额：");
            double salary = Double.parseDouble(scanner.nextLine());
            double tax = calculator.calculateTax(salary);
            System.out.printf("应缴纳的个人所得税为：%.2f 元\n", tax);
            System.out.printf("税后实际收入：%.2f 元\n", salary - tax);
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误，请输入正确的数字。");
        }
    }

    // 设置新的起征点
    private static void setNewThreshold(Scanner scanner, TaxCalculator calculator) {
        try {
            System.out.print("请输入新的起征点：");
            double newThreshold = Double.parseDouble(scanner.nextLine());
            calculator.setThreshold(newThreshold);
            System.out.println("起征点设置成功！");
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误，请输入正确的数字。");
        }
    }

    // 修改税率表
    private static void modifyTaxBracket(Scanner scanner, TaxCalculator calculator) {
        calculator.displayTaxBrackets();
        System.out.print("请输入要修改的税率级别（数字）：");
        try {
            int level = Integer.parseInt(scanner.nextLine());
            if (level < 1 || level > calculator.getBrackets().size()) {
                System.out.println("无效的级别。");
                return;
            }
            TaxBracket bracket = calculator.getBrackets().get(level - 1);
            System.out.println("当前级别信息：" + bracket);

            // 获取相邻级别的信息，用于验证
            double lowerLevelUpperBound = 0;
            double higherLevelLowerBound = Double.MAX_VALUE;

            if (level > 1) {
                lowerLevelUpperBound = calculator.getBrackets().get(level - 2).getUpperBound();
            }

            if (level < calculator.getBrackets().size()) {
                higherLevelLowerBound = calculator.getBrackets().get(level).getLowerBound();
            }

            // 输入并验证新的下限
            double newLower = getValidLowerBound(scanner, bracket, lowerLevelUpperBound, higherLevelLowerBound);

            // 输入并验证新的上限
            double newUpper = getValidUpperBound(scanner, bracket, newLower, level, higherLevelLowerBound, calculator);

            // 如果修改了上限，需要同时修改下一级别的下限（保持一致性）
            updateNextBracketIfNeeded(calculator, level, bracket, newUpper);

            // 设置新的税率
            double newRate = getNewTaxRate(scanner, bracket);

            // 更新当前税率级别
            bracket.setLowerBound(newLower);
            bracket.setUpperBound(newUpper);
            bracket.setRate(newRate);
            System.out.println("税率级别修改成功！");
        } catch (NumberFormatException e) {
            System.out.println("输入格式错误，请输入正确的数字。");
        }
    }

    // 获取有效的下限值
    private static double getValidLowerBound(Scanner scanner, TaxBracket bracket,
                                           double lowerLevelUpperBound, double higherLevelLowerBound) {
        double newLower;
        while (true) {
            System.out.print("请输入新的下限（原值 " + bracket.getLowerBound() + "）：");
            newLower = Double.parseDouble(scanner.nextLine());

            // 验证：下限必须严格大于低级别的上限
            if (newLower <= lowerLevelUpperBound) {
                System.out.println("错误：下限必须严格大于低级别的上限 (" + lowerLevelUpperBound + ")");
                continue;
            }

            // 验证：下限必须严格小于高级别的下限
            if (newLower >= higherLevelLowerBound) {
                System.out.println("错误：下限必须严格小于高级别的下限 (" + higherLevelLowerBound + ")");
                continue;
            }

            break;
        }
        return newLower;
    }

    // 获取有效的上限值
    private static double getValidUpperBound(Scanner scanner, TaxBracket bracket,
                                           double newLower, int level, double higherLevelLowerBound, TaxCalculator calculator) {
        double newUpper;
        while (true) {
            System.out.print("请输入新的上限（输入 -1 表示无限制，原值 " +
                           (bracket.getUpperBound() == Double.MAX_VALUE ? "无上限" : bracket.getUpperBound()) + "）：");
            newUpper = Double.parseDouble(scanner.nextLine());
            if (newUpper == -1) {
                newUpper = Double.MAX_VALUE;
            }

            // 验证：上限必须严格大于自己的下限
            if (newUpper <= newLower) {
                System.out.println("错误：上限必须严格大于下限 (" + newLower + ")");
                continue;
            }

            // 验证：上限必须等于高级别的下限（确保无缝衔接）
            if (level < calculator.getBrackets().size() && newUpper != higherLevelLowerBound) {
                System.out.println("错误：上限必须等于高级别的下限 (" + higherLevelLowerBound + ")，以确保税率级别无缝衔接");
                continue;
            }

            break;
        }
        return newUpper;
    }

    // 更新下一级别的下限（如果需要）
    private static void updateNextBracketIfNeeded(TaxCalculator calculator, int level,
                                                TaxBracket bracket, double newUpper) {
        if (level < calculator.getBrackets().size() && newUpper != bracket.getUpperBound()) {
            TaxBracket nextBracket = calculator.getBrackets().get(level);
            nextBracket.setLowerBound(newUpper);
            System.out.println("已自动更新下一级别的下限为 " + newUpper + " 以保持一致性");
        }
    }

    // 获取新的税率
    private static double getNewTaxRate(Scanner scanner, TaxBracket bracket) {
        System.out.print("请输入新的税率（例如 0.05 表示 5%，原值 " + bracket.getRate() + "）：");
        return Double.parseDouble(scanner.nextLine());
    }
}