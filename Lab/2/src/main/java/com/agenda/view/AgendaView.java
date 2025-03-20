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
        System.out.println("register [用户名] [密码] - 注册新用户");
        System.out.println("add [用户名] [密码] [参与者] [开始日期] [开始时间] [结束日期] [结束时间] [标题] - 添加会议");
        System.out.println("  日期格式：yyyy-MM-dd");
        System.out.println("  时间格式：HH:mm");
        System.out.println("query [用户名] [密码] [开始时间] [结束时间] - 查询会议");
        System.out.println("delete [用户名] [密码] [会议标题] - 删除会议");
        System.out.println("clear [用户名] [密码] - 清除所有会议");
        System.out.println("quit - 退出系统");
    }
}