package com.tax.controller;

import com.tax.model.TaxCalculator;
import com.tax.model.TaxBracket;
import com.tax.view.TaxView;
import java.util.List;

/**
 * 控制器类，协调视图和模型之间的交互
 */
public class TaxController {
    private TaxCalculator calculator;
    private TaxView view;

    public TaxCalculator getCalculator() {
        return calculator;
    }

    public TaxView getView() {
        return view;
    }

    // 依赖注入构造函数，便于单元测试注入模拟对象
    public TaxController(TaxCalculator calculator, TaxView view) {
        this.calculator = calculator;
        this.view = view;
    }

    public TaxController() {
        // 初始化税率计算器，默认起征点为 1600 元
        calculator = new TaxCalculator(1600);
        // 添加默认的 5 个税率级别
        calculator.addBracket(new TaxBracket(0, 500, 0.05));
        calculator.addBracket(new TaxBracket(500, 2000, 0.10));
        calculator.addBracket(new TaxBracket(2000, 5000, 0.15));
        calculator.addBracket(new TaxBracket(5000, 20000, 0.20));
        calculator.addBracket(new TaxBracket(20000, Double.MAX_VALUE, 0.25));

        view = new TaxView();
    }

    /**
     * 主运行方法，包含菜单交互循环
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            view.displayMenu();
            String choice = view.getInput();
            switch (choice) {
                case "1":
                    calculateTaxForSalary();
                    break;
                case "2":
                    setNewThreshold();
                    break;
                case "3":
                    modifyTaxBracket();
                    break;
                case "4":
                    view.displayTaxBrackets(calculator.getThreshold(), calculator.getBrackets());
                    break;
                case "5":
                    exit = true;
                    view.displayMessage("程序退出，再见！");
                    break;
                default:
                    view.displayMessage("无效的选项，请重新选择。");
                    break;
            }
        }
        view.close();
    }

    // 计算工资税额
    protected void calculateTaxForSalary() {
        try {
            view.displayMessage("请输入月工资薪金总额：");
            double salary = Double.parseDouble(view.getInput());
            double tax = calculator.calculateTax(salary);
            view.displayTaxCalculation(tax, salary);
        } catch (NumberFormatException e) {
            view.displayMessage("输入格式错误，请输入正确的数字。");
        }
    }

    // 设置新的起征点
    protected void setNewThreshold() {
        try {
            view.displayMessage("请输入新的起征点：");
            double newThreshold = Double.parseDouble(view.getInput());
            calculator.setThreshold(newThreshold);
            view.displayMessage("起征点设置成功！");
        } catch (NumberFormatException e) {
            view.displayMessage("输入格式错误，请输入正确的数字。");
        }
    }

    // 修改税率表
    protected void modifyTaxBracket() {
        view.displayTaxBrackets(calculator.getThreshold(), calculator.getBrackets());
        view.displayMessage("请输入要修改的税率级别（数字）：");
        try {
            int level = Integer.parseInt(view.getInput());
            List<TaxBracket> brackets = calculator.getBrackets();
            if (level < 1 || level > brackets.size()) {
                view.displayMessage("无效的级别。");
                return;
            }
            TaxBracket bracket = brackets.get(level - 1);
            view.displayMessage("当前级别信息：" + bracket.toString());

            // 获取相邻级别的信息，用于验证
            double lowerLevelUpperBound = 0;
            double higherLevelLowerBound = Double.MAX_VALUE;
            if (level > 1) {
                lowerLevelUpperBound = brackets.get(level - 2).getUpperBound();
            }
            if (level < brackets.size()) {
                higherLevelLowerBound = brackets.get(level).getLowerBound();
            }

            // 输入并验证新的下限
            double newLower = getValidLowerBound(bracket, lowerLevelUpperBound, higherLevelLowerBound);

            // 输入并验证新的上限
            double newUpper = getValidUpperBound(bracket, newLower, level, higherLevelLowerBound);

            // 如果修改了上限，需要同时修改下一级别的下限（保持一致性）
            updateNextBracketIfNeeded(level, bracket, newUpper);

            // 设置新的税率
            double newRate = getNewTaxRate(bracket);

            // 更新当前税率级别
            bracket.setLowerBound(newLower);
            bracket.setUpperBound(newUpper);
            bracket.setRate(newRate);
            view.displayMessage("税率级别修改成功！");
        } catch (NumberFormatException e) {
            view.displayMessage("输入格式错误，请输入正确的数字。");
        }
    }

    // 获取有效的下限值
    protected double getValidLowerBound(TaxBracket bracket, double lowerLevelUpperBound, double higherLevelLowerBound) {
        double newLower;
        while (true) {
            view.displayMessage("请输入新的下限（原值 " + bracket.getLowerBound() + "）：");
            newLower = Double.parseDouble(view.getInput());
            if (newLower <= lowerLevelUpperBound) {
                view.displayMessage("错误：下限必须严格大于低级别的上限 (" + lowerLevelUpperBound + ")");
                continue;
            }
            if (newLower >= higherLevelLowerBound) {
                view.displayMessage("错误：下限必须严格小于高级别的下限 (" + higherLevelLowerBound + ")");
                continue;
            }
            break;
        }
        return newLower;
    }

    // 获取有效的上限值
    protected double getValidUpperBound(TaxBracket bracket, double newLower, int level, double higherLevelLowerBound) {
        double newUpper;
        while (true) {
            String currentUpper = bracket.getUpperBound() == Double.MAX_VALUE ? "无上限"
                    : String.valueOf(bracket.getUpperBound());
            view.displayMessage("请输入新的上限（输入 -1 表示无限制，原值 " + currentUpper + "）：");
            newUpper = Double.parseDouble(view.getInput());
            if (newUpper == -1) {
                newUpper = Double.MAX_VALUE;
            }
            if (newUpper <= newLower) {
                view.displayMessage("错误：上限必须严格大于下限 (" + newLower + ")");
                continue;
            }
            if (level < calculator.getBrackets().size() && newUpper != higherLevelLowerBound) {
                view.displayMessage("错误：上限必须等于高级别的下限 (" + higherLevelLowerBound + ")，以确保税率级别无缝衔接");
                continue;
            }
            break;
        }
        return newUpper;
    }

    // 更新下一级别的下限（如果需要）
    protected void updateNextBracketIfNeeded(int level, TaxBracket bracket, double newUpper) {
        List<TaxBracket> brackets = calculator.getBrackets();
        if (level < brackets.size() && newUpper != bracket.getUpperBound()) {
            TaxBracket nextBracket = brackets.get(level);
            nextBracket.setLowerBound(newUpper);
            view.displayMessage("已自动更新下一级别的下限为 " + newUpper + " 以保持一致性");
        }
    }

    // 获取新的税率
    protected double getNewTaxRate(TaxBracket bracket) {
        view.displayMessage("请输入新的税率（例如 0.05 表示 5%，原值 " + bracket.getRate() + "）：");
        return Double.parseDouble(view.getInput());
    }
}
