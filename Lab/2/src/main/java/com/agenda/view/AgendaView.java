package com.agenda.view;

import com.agenda.model.Meeting;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * 议程管理系统的视图类
 */
public class AgendaView {
    private Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public AgendaView() {
        this.scanner = new Scanner(System.in);
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void showPrompt() {
        System.out.print("$ ");
    }

    public String[] readCommand() {
        String line = scanner.nextLine().trim();
        return line.split("\\s+");
    }

    public void showWelcome() {
        System.out.println("欢迎使用议程管理系统！");
    }

    public void showError(String message) {
        System.out.println("错误: " + message);
    }

    public void showSuccess(String message) {
        System.out.println("成功: " + message);
    }

    public void showMeetings(List<Meeting> meetings) {
        if (meetings.isEmpty()) {
            System.out.println("没有找到符合条件的会议。");
            return;
        }

        System.out.println("找到以下会议：");
        for (Meeting meeting : meetings) {
            System.out.printf("标题: %s\n", meeting.getTitle());
            System.out.printf("组织者: %s\n", meeting.getOrganizer());
            System.out.printf("参与者: %s\n", meeting.getParticipant());
            System.out.printf("开始时间: %s\n", meeting.getStartTime().format(DATE_FORMATTER));
            System.out.printf("结束时间: %s\n", meeting.getEndTime().format(DATE_FORMATTER));
            System.out.println("-------------------");
        }
    }

    public LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_FORMATTER);
    }

    public void showHelp() {
        System.out.println("可用命令：");
        System.out.println("register <username> <password> - 注册新用户");
        System.out.println("add <username> <password> <participant> <startDate> <startTime> <endDate> <endTime> <title> - 添加会议");
        System.out.println("query <username> <password> <startDate> <startTime> <endDate> <endTime> - 查询会议");
        System.out.println("delete <username> <password> <title> - 删除会议");
        System.out.println("clear <username> <password> - 清除所有会议");
        System.out.println("quit - 退出系统");
    }
}