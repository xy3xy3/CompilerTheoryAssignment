# 议程管理系统设计文档

**作者**：胡瑞康

**学号**：2020123456

---

## 项目概述

议程管理系统是一个基于MVC架构的Java命令行应用程序，提供用户注册、会议管理（添加、查询、删除、清空）等功能，确保会议时间无冲突且数据安全可靠。系统采用分层设计，实现高内聚低耦合，便于未来功能扩展和维护。

---

## 文件结构

```bash
src/
├── main/
│   └── java/
│       └── com/agenda/
│           ├── controller/  # 控制器层
│           ├── model/       # 模型层
│           ├── service/     # 服务层
│           ├── view/        # 视图层
│           └── App.java     # 应用入口
└── test/
    └── java/
        └── com/agenda/
            ├── AgendaControllerTest.java  # 控制器测试
            └── AgendaServiceTest.java     # 服务测试
```

---

## 系统架构（MVC模式）

### 分层说明

| 层级       | 组件                  | 职责                        |
|------------|-----------------------|-----------------------------|
| **视图层** | `AgendaView`          | 用户交互界面，输入输出处理  |
| **控制层** | `AgendaController`    | 业务逻辑处理与请求分发      |
| **服务层** | `AgendaService`       | 核心业务逻辑与数据管理      |
| **模型层** | `User`, `Meeting`     | 数据实体表示                |


---

### UML类图

![](img/uml.svg)

---

### 模型层（Model）

#### User 类

**职责**：表示用户数据，包含用户名、密码和用户的会议列表。

**代码示例**：
```java
public class User {
    private String username;
    private String password;
    private List<Meeting> meetings;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meetings = new ArrayList<>();
    }

    // 核心方法
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public List<Meeting> getMeetings() { return meetings; }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    public void clearMeetings() {
        meetings.clear();
    }
}
```

**关键特性**：
- 封装用户基本信息（用户名、密码）
- 管理用户的会议集合
- 提供会议增删改查的基本操作

#### Meeting 类

**职责**：表示会议数据，包含会议ID、标题、组织者、参与者、开始时间与结束时间，并提供时间重叠检查功能。

**代码示例**：
```java
public class Meeting {
    private String id;
    private String title;
    private String organizer;
    private String participant;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Meeting(String title, String organizer, String participant,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.id = UUID.randomUUID().toString(); // 生成唯一标识符
        this.title = title;
        this.organizer = organizer;
        this.participant = participant;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // getter 方法...

    /**
     * 判断会议是否与另一个会议时间重叠
     */
    public boolean overlaps(Meeting other) {
        return (this.startTime.isBefore(other.endTime) &&
                this.endTime.isAfter(other.startTime));
    }
}
```

**关键特性**：
- 使用UUID生成唯一会议标识符
- 存储会议的完整信息（标题、组织者、参与者、时间）
- 提供时间冲突检测算法

---

### 服务层（Service）

#### AgendaService 类

**职责**：处理用户注册、会议管理逻辑，确保数据一致性与时间无冲突。

**代码示例**：
```java
public class AgendaService {
    private Map<String, User> users;

    public AgendaService() {
        this.users = new HashMap<>();
    }

    // 用户注册
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

    // 添加会议
    public boolean addMeeting(String organizer, String password, String participant,
                             String title, LocalDateTime startTime, LocalDateTime endTime) {
        // 验证用户存在与密码正确
        User organizerUser = users.get(organizer);
        User participantUser = users.get(participant);

        if (organizerUser == null || participantUser == null ||
            !organizerUser.getPassword().equals(password)) {
            return false;
        }

        Meeting meeting = new Meeting(title, organizer, participant, startTime, endTime);

        // 检查时间冲突
        for (Meeting existingMeeting : organizerUser.getMeetings()) {
            if (meeting.overlaps(existingMeeting)) {
                return false;
            }
        }
        for (Meeting existingMeeting : participantUser.getMeetings()) {
            if (meeting.overlaps(existingMeeting)) {
                return false;
            }
        }

        // 添加会议
        organizerUser.addMeeting(meeting);
        participantUser.addMeeting(meeting);
        return true;
    }

    // 查询、删除、清空会议等方法...
}
```

**核心业务逻辑**：

1. **用户管理**：
   - 注册用户，确保用户名唯一
   - 用户认证（密码验证）

2. **会议管理**：
   - 创建会议时检查时间冲突
   - 为组织者和参与者双方同时添加会议
   - 删除会议时从双方的会议列表中移除
   - 根据时间段查询会议

---

### 视图层（View）

#### AgendaView 类

**职责**：处理用户输入输出，通过命令行交互展示系统状态。

**代码示例**：
```java
public class AgendaView {
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm");

    public AgendaView() {
        this.scanner = new Scanner(System.in);
    }

    // 交互方法
    public void showPrompt() {
        System.out.print("$ ");
    }

    public String[] readCommand() {
        String line = scanner.nextLine().trim();
        return line.split("\\s+");
    }

    // 显示方法
    public void showWelcome() {
        System.out.println("欢迎使用议程管理系统！");
    }

    public void showError(String message) {
        System.out.println("错误: " + message);
    }

    public void showSuccess(String message) {
        System.out.println("成功: " + message);
    }

    // 格式化显示会议列表
    public void showMeetings(List<Meeting> meetings) {
        if (meetings.isEmpty()) {
            System.out.println("没有找到符合条件的会议。");
            return;
        }

        System.out.println("找到以下会议：");
        for (Meeting meeting : meetings) {
            System.out.printf("ID: %s\n", meeting.getId());
            System.out.printf("标题: %s\n", meeting.getTitle());
            System.out.printf("组织者: %s\n", meeting.getOrganizer());
            System.out.printf("参与者: %s\n", meeting.getParticipant());
            System.out.printf("开始时间: %s\n",
                meeting.getStartTime().format(DATE_FORMATTER));
            System.out.printf("结束时间: %s\n",
                meeting.getEndTime().format(DATE_FORMATTER));
            System.out.println("-------------------");
        }
    }

    // 辅助方法 - 解析时间
    public LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
```

**主要功能**：

1. **用户输入处理**：
   - 提供命令行提示符
   - 读取并解析用户命令

2. **展示系统结果**：
   - 格式化显示会议列表
   - 提供成功/失败反馈

3. **格式转换**：
   - 日期时间解析
   - 标准化输出格式

---

### 控制层（Controller）

#### AgendaController 类

**职责**：接收视图层输入，调用服务层执行具体业务逻辑。

**代码示例**：
```java
public class AgendaController {
    private AgendaService service;
    private AgendaView view;

    public AgendaController() {
        this.service = new AgendaService();
        this.view = new AgendaView();
    }

    // 主循环
    public void start() {
        view.showWelcome();
        while (true) {
            view.showPrompt();
            String[] command = view.readCommand();

            if (command.length == 0) {
                continue;
            }

            switch (command[0].toLowerCase()) {
                case "register":
                    handleRegister(command);
                    break;
                case "add":
                    handleAdd(command);
                    break;
                case "query":
                    handleQuery(command);
                    break;
                case "delete":
                    handleDelete(command);
                    break;
                case "clear":
                    handleClear(command);
                    break;
                case "quit":
                    return;
                default:
                    handleInvalidCommand(command);
            }
        }
    }

    // 命令处理方法示例
    public void handleAdd(String[] command) {
        if (command.length != 7 && command.length != 9) {
            view.showError("添加会议命令格式错误");
            return;
        }

        try {
            String username = command[1];
            String password = command[2];
            String participant = command[3];
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;
            String title;

            // 解析不同格式的命令
            if (command.length == 9) {
                // 处理9个参数格式
                String startDate = command[4];
                String startTime = command[5];
                String endDate = command[6];
                String endTime = command[7];
                title = command[8];

                startDateTime = view.parseDateTime(startDate + " " + startTime);
                endDateTime = view.parseDateTime(endDate + " " + endTime);
            } else {
                // 处理7个参数格式
                startDateTime = view.parseDateTime(command[4]);
                endDateTime = view.parseDateTime(command[5]);
                title = command[6];
            }

            // 调用服务层并显示结果
            if (service.addMeeting(username, password, participant,
                                  title, startDateTime, endDateTime)) {
                view.showSuccess("会议添加成功");
            } else {
                view.showError("会议添加失败，请检查用户名、密码或时间冲突");
            }
        } catch (Exception e) {
            view.showError("时间格式错误，请使用格式：yyyy-MM-dd HH:mm");
        }
    }

    // 其他命令处理方法...
}
```

**核心功能**：

1. **命令路由**：
   - 解析用户输入，分发到对应的处理方法

2. **参数验证**：
   - 检查命令格式正确性
   - 捕获并处理异常

3. **视图-服务协调**：
   - 从视图获取用户输入
   - 调用服务层执行业务逻辑
   - 通过视图显示操作结果

---

## 主程序入口

**职责**：作为程序的入口，负责启动控制层。

**示例代码**：
```java
public class App {
    public static void main(String[] args) {
        AgendaController controller = new AgendaController();
        controller.start();
    }
}
```

---

## 测试模块

### AgendaServiceTest 类

**职责**：测试服务层逻辑，包括用户注册、会议添加、查询、删除和清空等功能。

#### 测试用例与代码示例：

- **测试用户注册功能**
```java
@Test
public void testRegisterUser() {
    boolean result = agendaService.registerUser("user1", "pass1");
    assertTrue(result, "第一次注册应成功");

    // 重复注册相同用户名应失败
    result = agendaService.registerUser("user1", "pass2");
    assertFalse(result, "重复注册同一用户名应失败");
}
```

- **测试会议添加成功**
```java
@Test
public void testAddMeetingSuccess() {
    agendaService.registerUser("user1", "pass1");
    agendaService.registerUser("user2", "pass2");

    LocalDateTime startTime = LocalDateTime.of(2025, 3, 21, 10, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 3, 21, 11, 0);
    boolean addResult = agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime, endTime);
    assertTrue(addResult, "会议添加应成功");
}
```

- **测试会议时间冲突**
```java
@Test
public void testAddMeetingTimeConflict() {
    agendaService.registerUser("user1", "pass1");
    agendaService.registerUser("user2", "pass2");

    LocalDateTime startTime1 = LocalDateTime.of(2025, 3, 21, 10, 0);
    LocalDateTime endTime1 = LocalDateTime.of(2025, 3, 21, 11, 0);
    agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime1, endTime1);

    LocalDateTime startTime2 = LocalDateTime.of(2025, 3, 21, 10, 30);
    LocalDateTime endTime2 = LocalDateTime.of(2025, 3, 21, 11, 30);
    boolean conflictResult = agendaService.addMeeting("user1", "pass1", "user2", "Meeting2", startTime2, endTime2);
    assertFalse(conflictResult, "有时间冲突的会议应添加失败");
}
```

- **测试会议删除**
```java
@Test
public void testDeleteMeeting() {
    agendaService.registerUser("user1", "pass1");
    agendaService.registerUser("user2", "pass2");

    LocalDateTime startTime = LocalDateTime.of(2025, 3, 22, 9, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 3, 22, 10, 0);
    agendaService.addMeeting("user1", "pass1", "user2", "MeetingToDelete", startTime, endTime);

    List<Meeting> meetings = agendaService.queryMeetings("user1", "pass1", startTime.minusHours(1), endTime.plusHours(1));
    String meetingId = meetings.get(0).getId();

    boolean deleteResult = agendaService.deleteMeeting("user1", "pass1", meetingId);
    assertTrue(deleteResult, "删除会议应成功");
}
```

- **测试清空会议**
```java
@Test
public void testClearMeetings() {
    agendaService.registerUser("user1", "pass1");
    agendaService.registerUser("user2", "pass2");

    LocalDateTime startTime1 = LocalDateTime.of(2025, 3, 23, 14, 0);
    LocalDateTime endTime1 = LocalDateTime.of(2025, 3, 23, 15, 0);
    agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime1, endTime1);

    agendaService.clearMeetings("user1", "pass1");

    List<Meeting> meetings = agendaService.queryMeetings("user1", "pass1", startTime1.minusDays(1), endTime1.plusDays(1));
    assertTrue(meetings.isEmpty(), "所有会议应被清空");
}
```


### AgendaControllerTest 类

**职责**：测试控制层命令解析与分发，使用Mockito模拟service和view交互。

#### 主要测试内容与代码示例：

- **测试用户注册命令**
```java
@Test
void testHandleRegister_Success() {
    String[] command = {"register", "testUser", "password123"};
    when(service.registerUser("testUser", "password123")).thenReturn(true);
    controller.handleRegister(command);
    verify(view).showSuccess("用户注册成功");
}
```

- **测试会议添加成功**
```java
@Test
void testHandleAdd_Success() {
    String[] command = {"add", "testUser", "password123", "participant", "2024-03-20 10:00", "2024-03-20 11:00", "会议标题"};
    LocalDateTime start = LocalDateTime.of(2024, 3, 20, 10, 0);
    LocalDateTime end = LocalDateTime.of(2024, 3, 20, 11, 0);

    when(view.parseDateTime("2024-03-20 10:00")).thenReturn(start);
    when(view.parseDateTime("2024-03-20 11:00")).thenReturn(end);
    when(service.addMeeting("testUser", "password123", "participant", "会议标题", start, end)).thenReturn(true);

    controller.handleAdd(command);
    verify(view).showSuccess("会议添加成功");
}
```

- **测试时间格式错误处理**
```java
@Test
void testHandleAdd_InvalidDateTime() {
    String[] command = {"add", "testUser", "password123", "participant", "invalid", "2024-03-20 11:00", "会议标题"};
    when(view.parseDateTime("invalid")).thenThrow(new IllegalArgumentException());
    controller.handleAdd(command);
    verify(view).showError("时间格式错误，请使用正确的格式：yyyy-MM-dd HH:mm");
}
```

- **测试会议查询与删除**
```java
@Test
void testHandleQuery_Success() {
    String[] command = {"query", "testUser", "password123", "2024-03-20 10:00", "2024-03-20 11:00"};
    LocalDateTime start = LocalDateTime.of(2024, 3, 20, 10, 0);
    LocalDateTime end = LocalDateTime.of(2024, 3, 20, 11, 0);
    Meeting meeting = new Meeting("会议", "testUser", "participant", start, end);
    when(view.parseDateTime("2024-03-20 10:00")).thenReturn(start);
    when(view.parseDateTime("2024-03-20 11:00")).thenReturn(end);
    when(service.queryMeetings("testUser", "password123", start, end)).thenReturn(Arrays.asList(meeting));

    controller.handleQuery(command);
    verify(view).showMeetings(Arrays.asList(meeting));
}
```