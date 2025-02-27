# 个人所得税计算器

这是一个基于Maven的Java项目，用于计算个人所得税。

## 功能特性

- 基于级进税率计算个人所得税
- 可自定义税率级别和起征点
- 交互式命令行界面
- 实时计算税后收入

## 技术栈

- Java 11
- Maven

## 如何运行

1. 确保安装了JDK 11或更高版本以及Maven
2. 克隆或下载项目代码
3. 进入项目根目录
4. 执行以下命令编译和运行项目：

```bash
mvn clean package
java -jar target/personal-tax-calculator-1.0-SNAPSHOT.jar
```

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