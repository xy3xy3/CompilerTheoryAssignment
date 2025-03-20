package com.agenda.controller;

import com.agenda.model.Meeting;
import com.agenda.service.AgendaService;
import com.agenda.view.AgendaView;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 议程管理系统的控制器类
 */
public class AgendaController {
    private AgendaService service;
    private AgendaView view;

    public AgendaController() {
        this.service = new AgendaService();
        this.view = new AgendaView();
    }

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

    public void handleAdd(String[] command) {
        if (command.length != 9) {
            view.showError("添加会议命令格式错误");
            return;
        }

        try {
            String username = command[1];
            String password = command[2];
            String participant = command[3];
            String startDate = command[4];
            String startTime = command[5];
            String endDate = command[6];
            String endTime = command[7];
            String title = command[8];

            LocalDateTime startDateTime = view.parseDateTime(startDate + " " + startTime);
            LocalDateTime endDateTime = view.parseDateTime(endDate + " " + endTime);

            if (service.addMeeting(username, password, participant, title, startDateTime, endDateTime)) {
                view.showSuccess("会议添加成功");
            } else {
                view.showError("会议添加失败，请检查用户名、密码或时间冲突");
            }
        } catch (Exception e) {
            view.showError("日期格式错误，请使用yyyy-MM-dd HH:mm格式");
        }
    }

    public void handleQuery(String[] command) {
        if (command.length != 7) {
            view.showError("查询会议命令格式错误");
            return;
        }

        try {
            String username = command[1];
            String password = command[2];
            String startDate = command[3];
            String startTime = command[4];
            String endDate = command[5];
            String endTime = command[6];

            LocalDateTime startDateTime = view.parseDateTime(startDate + " " + startTime);
            LocalDateTime endDateTime = view.parseDateTime(endDate + " " + endTime);

            List<Meeting> meetings = service.queryMeetings(username, password, startDateTime, endDateTime);
            view.showMeetings(meetings);
        } catch (Exception e) {
            view.showError("日期格式错误，请使用yyyy-MM-dd HH:mm格式");
        }
    }

    public void handleDelete(String[] command) {
        if (command.length != 4) {
            view.showError("删除会议命令格式错误");
            return;
        }

        String username = command[1];
        String password = command[2];
        String title = command[3];

        if (service.deleteMeeting(username, password, title)) {
            view.showSuccess("会议删除成功");
        } else {
            view.showError("会议删除失败，请检查用户名、密码或会议标题");
        }
    }

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

    public void handleInvalidCommand(String[] command) {
        view.showHelp();
    }
}