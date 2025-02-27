# 个人所得税计算器

这是一个基于Maven的Java项目，用于计算个人所得税。

## 功能特性

- 基于级进税率计算个人所得税
- 可自定义税率级别和起征点
- 交互式命令行界面
- 实时计算税后收入

## 技术栈

- Java 23.0.2
- Maven
- JUnit 5（单元测试）

## 如何运行

1. 确保安装了JDK 23.0.2以及Maven
2. 克隆或下载项目代码
3. 进入项目根目录
4. 执行以下命令编译和运行项目：

```bash
mvn clean package
java -jar target/personal-tax-calculator-1.0-SNAPSHOT.jar
```

## 如何运行测试

项目包含了单元测试，可以通过以下命令运行：

```bash
mvn test
```

查看测试报告:

```bash
mvn surefire-report:report
```

测试报告将生成在 `./target/reports/surefire.html`。

## 测试覆盖的功能

- 验证低于起征点的工资不缴税
- 各个税率级别的税款计算
- 跨越多个税率级别的情况
- 高收入的税款计算
- 修改起征点后的税款计算
- 税率级别的基本功能

## 项目结构

```
personal-tax-calculator/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── tax/
│                   ├── PersonalTaxApp.java    # 主应用程序
│                   ├── TaxCalculator.java     # 税率计算器
│                   └── TaxBracket.java        # 税率级别
├── pom.xml                                    # Maven配置文件
└── README.md                                  # 项目说明
```