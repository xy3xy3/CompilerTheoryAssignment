package com.agenda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户模型类
 */
public class User {
    private String username;
    private String password;
    private List<Meeting> meetings;

    /**
     * 构造函数，初始化用户对象
     *
     * @param username 用户名
     * @param password 密码
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meetings = new ArrayList<>();
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 获取用户的会议列表
     *
     * @return 会议列表
     */
    public List<Meeting> getMeetings() {
        return meetings;
    }

    /**
     * 添加会议到用户的会议列表
     *
     * @param meeting 会议对象
     */
    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    /**
     * 从用户的会议列表中移除会议
     *
     * @param meeting 会议对象
     */
    public void removeMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    /**
     * 清空用户的会议列表
     */
    public void clearMeetings() {
        meetings.clear();
    }
}