



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

## 自动测试

```shell
mvn test
```

测试结果如图，具体测试内容参加`design`文件。

![](./img/auto_test.png)