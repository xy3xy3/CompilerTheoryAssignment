# 议程管理系统说明文档

- **姓名**：胡瑞康
- **学号**：22336087
- **邮箱**：hurk3@mail2.sysu.edu.cn
- **电话**: 13265249840

# 项目简介

议程管理系统是一个基于MVC架构的Java命令行应用程序，提供用户注册、会议管理（添加、查询、删除、清空）等功能，确保会议时间无冲突且数据安全可靠。系统采用分层设计，实现高内聚低耦合，便于未来功能扩展和维护。

# 技术栈

- **Java**: 版本 23.0.2
- **Maven**: 项目构建与依赖管理工具
- **JUnit 5**: 单元测试框架

# 运行方法

使用封装好的 `run.bat`文件进行编译和运行

```shell
./run.bat
```

如果您未安装maven，请直接运行下面的打包好的jar包

```shell
java -jar target/agenda-system-1.0-SNAPSHOT.jar
```

# 测试方法

## 人为简单测试

```shell
register user1 1
register user2 1
add user1 1 user2 2025-03-20 10:00 2025-03-20 11:00 Meeting1
query user1 1 2025-03-20 10:00 2025-03-20 11:00
delete user1 1 实际ID
query user1 1 2025-03-20 10:00 2025-03-20 11:00
```

测试结果如图，符合预期，最开始可以查询到，删除会议后查询不到。

![](./img/human_test.png)

## 执行脚本测试

```shell
batch batch.sh
```

成功识别对应脚本并逐行执行

![](./img/batch.png)

## 自动测试

```shell
mvn test
```

测试结果如图，具体测试内容可参考 `design`文件，主要针对Controller和Service层进行测试。

![](./img/auto_test.png)

# 文档生成

本次实验代码使用了 `javadoc`生成文档，具体命令如下：

```shell
javadoc -d docs -sourcepath src/main/java -subpackages com.agenda
```

执行后会在 `docs`目录下生成文档，可使用VsCode的 `Live Server`插件打开，具体效果如图

![](./img/docs.png)

# 收获体会

在开发过程中，我遇到了一些关键问题，并提出了相应的解决方案，主要包括：

**时间格式处理问题**
   - **问题描述**：会议时间需要精确到分钟，用户输入容易出现格式错误，导致解析失败。
   - **解决方案**：使用`DateTimeFormatter`对输入的时间字符串进行严格校验，并通过异常捕获机制反馈错误提示，要求用户按照`yyyy-MM-dd HH:mm`格式输入。

**会议时间冲突检测**
   - **问题描述**：在添加会议时，需要确保新会议的时间与当前用户以及参与者已存在的会议不冲突。
   - **解决方案**：在`Meeting`类中设计`overlaps`方法，通过比较会议开始与结束时间，判断时间是否重叠，避免冲突情况发生。
   - **改进思路**：未来可扩展支持多时区、会议优先级以及动态调整会议时间的功能，以适应更复杂的业务需求。

**数据同步与一致性**
   - **问题描述**：会议涉及到多个用户（如组织者和参与者），在删除或修改会议时需保证双方数据的一致性。
   - **解决方案**：在操作时同时更新涉及到的所有用户的会议列表，并通过单元测试验证数据同步效果，确保不会出现数据孤岛。

**命令解析的鲁棒性**
   - **问题描述**：用户输入命令格式多样，参数数量和顺序错误容易导致命令解析失败。
   - **解决方案**：在控制层中对输入命令进行严格的参数校验，并提供详细的错误提示，指导用户正确输入，提升系统健壮性。


在本次的开发过程中，我也获得了如下体会和收获：

- **分层架构优势明显**
  采用MVC模式将视图、控制和服务逻辑分离，不仅使代码结构清晰，还提高了系统的可维护性和扩展性，使未来功能迭代更为方便。

- **异常处理和用户反馈的重要性**
  通过严格的输入校验和异常捕获机制，有效提高了系统对错误输入的容错能力，保证了用户体验。系统能及时反馈错误信息，指导用户进行正确的操作。

- **单元测试保障系统质量**
  充分编写了针对各个功能模块的单元测试，不仅覆盖了正常流程，也考虑了边界条件和异常场景。测试用例的完善为后续改进和扩展提供了可靠保障。
