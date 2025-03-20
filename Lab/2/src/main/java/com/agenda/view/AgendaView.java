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
        DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 构造函数，初始化扫描器
     */
    public AgendaView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * 设置扫描器
     *
     * @param scanner 扫描器对象
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * 显示提示符
     */
    public void showPrompt() {
        System.out.print("$ ");
    }

    /**
     * 读取用户输入的命令
     *
     * @return 包含命令和参数的字符串数组
     */
    public String[] readCommand() {
        String line = scanner.nextLine().trim();
        return line.split("\\s+");
    }

    /**
     * 显示欢迎信息
     */
    public void showWelcome() {
        System.out.println("欢迎使用议程管理系统！");
    }

    /**
     * 显示错误信息
     *
     * @param message 错误信息
     */
    public void showError(String message) {
        System.out.println("错误: " + message);
    }

    /**
     * 显示成功信息
     *
     * @param message 成功信息
     */
    public void showSuccess(String message) {
        System.out.println("成功: " + message);
    }

    /**
     * 显示会议列表
     *
     * @param meetings 会议列表
     */
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

    /**
     * 解析日期时间字符串
     *
     * @param dateTimeStr 日期时间字符串
     * @return 解析后的 LocalDateTime 对象
     */
    public LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    /**
     * 显示帮助信息
     */
    public void showHelp() {
        System.out.println("可用命令：");
        System.out.println("register <username> <password> - 注册新用户");
        System.out.println("add <username> <password> <participant> <start> <end> <title> - 添加会议");
        System.out.println("query <username> <password> <start> <end> - 查询会议");
        System.out.println("delete <username> <password> <title> - 删除会议");
        System.out.println("clear <username> <password> - 清除所有会议");
        System.out.println("quit - 退出系统");
        System.out.println("\n时间格式：yyyy-MM-dd HH:mm");
    }
}