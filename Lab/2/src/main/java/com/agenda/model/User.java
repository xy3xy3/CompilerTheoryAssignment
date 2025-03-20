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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meetings = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

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