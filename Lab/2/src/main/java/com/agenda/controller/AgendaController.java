package com.agenda.controller;

import com.agenda.model.Meeting;
import com.agenda.service.AgendaService;
import com.agenda.view.AgendaView;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 议程管理系统的控制器类
 */
public class AgendaController {
    private AgendaService service;
    private AgendaView view;

    /**
     * 构造函数，初始化服务和视图
     */
    public AgendaController() {
        this.service = new AgendaService();
        this.view = new AgendaView();
    }

    /**
     * 启动议程管理系统
     */
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
                case "batch":
                    handleBatch(command);
                    break;
                case "quit":
                    return;
                default:
                    handleInvalidCommand(command);
            }
        }
    }

    /**
     * 处理用户注册命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleRegister(String[] command) {
        if (command.length != 3) {
            view.showError("注册命令格式错误");
            return;
        }

        String username = command[1];
        String password = command[2];

        if (service.registerUser(username, password)) {
            view.showSuccess("用户注册成功");
        } else {
            view.showError("用户名已存在");
        }
    }

    /**
     * 处理添加会议命令
     *
     * @param command 包含命令和参数的字符串数组
     */
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

            if (command.length == 9) {
                // 处理9个参数的格式：add username password participant startDate startTime endDate endTime title
                String startDate = command[4];
                String startTime = command[5];
                String endDate = command[6];
                String endTime = command[7];
                title = command[8];

                startDateTime = view.parseDateTime(startDate + " " + startTime);
                endDateTime = view.parseDateTime(endDate + " " + endTime);
            } else {
                // 处理7个参数的格式：add username password participant startDateTime endDateTime title
                String startDateTimeStr = command[4];
                String endDateTimeStr = command[5];
                title = command[6];

                startDateTime = view.parseDateTime(startDateTimeStr);
                endDateTime = view.parseDateTime(endDateTimeStr);
            }

            if (service.addMeeting(username, password, participant, title, startDateTime, endDateTime)) {
                view.showSuccess("会议添加成功");
            } else {
                view.showError("会议添加失败，请检查用户名、密码或时间冲突");
            }
        } catch (Exception e) {
            view.showError("时间格式错误，请使用正确的格式：yyyy-MM-dd HH:mm");
        }
    }

    /**
     * 处理查询会议命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleQuery(String[] command) {
        if (command.length != 5 && command.length != 7) {
            view.showError("查询会议命令格式错误");
            return;
        }

        try {
            String username = command[1];
            String password = command[2];
            LocalDateTime startDateTime;
            LocalDateTime endDateTime;

            if (command.length == 7) {
                // 处理7个参数的格式：query username password startDate startTime endDate endTime
                String startDate = command[3];
                String startTime = command[4];
                String endDate = command[5];
                String endTime = command[6];

                startDateTime = view.parseDateTime(startDate + " " + startTime);
                endDateTime = view.parseDateTime(endDate + " " + endTime);
            } else {
                // 处理5个参数的格式：query username password startDateTime endDateTime
                String startDateTimeStr = command[3];
                String endDateTimeStr = command[4];

                startDateTime = view.parseDateTime(startDateTimeStr);
                endDateTime = view.parseDateTime(endDateTimeStr);
            }

            List<Meeting> meetings = service.queryMeetings(username, password, startDateTime, endDateTime);
            view.showMeetings(meetings);
        } catch (Exception e) {
            view.showError("时间格式错误，请使用正确的格式：yyyy-MM-dd HH:mm");
        }
    }

    /**
     * 处理删除会议命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleDelete(String[] command) {
        if (command.length != 4) {
            view.showError("删除会议命令格式错误");
            return;
        }

        String username = command[1];
        String password = command[2];
        String meetingId = command[3];

        if (service.deleteMeeting(username, password, meetingId)) {
            view.showSuccess("会议删除成功");
        } else {
            view.showError("会议删除失败，请检查用户名、密码或会议ID");
        }
    }

    /**
     * 处理清除会议命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleClear(String[] command) {
        if (command.length != 3) {
            view.showError("清除会议命令格式错误");
            return;
        }

        String username = command[1];
        String password = command[2];

        if (service.clearMeetings(username, password)) {
            view.showSuccess("所有会议清除成功");
        } else {
            view.showError("清除会议失败，请检查用户名和密码");
        }
    }

    /**
     * 处理无效命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleInvalidCommand(String[] command) {
        view.showHelp();
    }

    /**
     * 处理批处理命令
     *
     * @param command 包含命令和参数的字符串数组
     */
    public void handleBatch(String[] command) {
        if (command.length != 2) {
            view.showError("批处理命令格式错误");
            return;
        }

        String fileName = command[1];
        List<String> lines = readFile(fileName);

        if (lines == null) {
            return;
        }

        view.showSuccess("开始执行批处理命令文件: " + fileName);

        for (String line : lines) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                continue;
            }

            // 分割命令行
            String[] batchCommand = line.trim().split("\\s+");

            // 显示正在执行的命令
            System.out.println("执行命令: " + line);

            // 处理批处理中的命令，但忽略quit和batch命令
            switch (batchCommand[0].toLowerCase()) {
                case "register":
                    handleRegister(batchCommand);
                    break;
                case "add":
                    handleAdd(batchCommand);
                    break;
                case "query":
                    handleQuery(batchCommand);
                    break;
                case "delete":
                    handleDelete(batchCommand);
                    break;
                case "clear":
                    handleClear(batchCommand);
                    break;
                default:
                    view.showError("不支持的批处理命令: " + batchCommand[0]);
            }
        }

        view.showSuccess("批处理命令文件执行完成: " + fileName);
    }

    /**
     * 读取文件内容
     *
     * @param fileName 文件名
     * @return 文件内容的行列表，如果文件不存在或无法读取则返回null
     */
    private List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            view.showError("无法读取文件: " + fileName);
            return null;
        }
    }
}