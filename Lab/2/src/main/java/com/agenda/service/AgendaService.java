package com.agenda.service;

import com.agenda.model.Meeting;
import com.agenda.model.User;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 议程管理系统的核心服务类
 */
public class AgendaService {
    private Map<String, User> users;

    /**
     * 构造函数，初始化用户映射
     */
    public AgendaService() {
        this.users = new HashMap<>();
    }

    /**
     * 注册新用户
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果注册成功返回 true，否则返回 false
     */
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

    /**
     * 添加会议
     *
     * @param organizer 会议组织者
     * @param password 组织者密码
     * @param participant 会议参与者
     * @param title 会议标题
     * @param startTime 会议开始时间
     * @param endTime 会议结束时间
     * @return 如果添加成功返回 true，否则返回 false
     */
    public boolean addMeeting(String organizer, String password, String participant,
                              String title, LocalDateTime startTime, LocalDateTime endTime) {
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

        organizerUser.addMeeting(meeting);
        participantUser.addMeeting(meeting);
        return true;
    }

    /**
     * 查询会议
     *
     * @param username 用户名
     * @param password 密码
     * @param startTime 查询开始时间
     * @param endTime 查询结束时间
     * @return 会议列表
     */
    public List<Meeting> queryMeetings(String username, String password,
                                       LocalDateTime startTime, LocalDateTime endTime) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Collections.emptyList();
        }

        List<Meeting> result = new ArrayList<>();
        for (Meeting meeting : user.getMeetings()) {
            if ((!meeting.getStartTime().isAfter(endTime)) &&
                (!meeting.getEndTime().isBefore(startTime))) {
                result.add(meeting);
            }
        }
        return result;
    }

    /**
     * 删除会议
     *
     * @param username 用户名
     * @param password 密码
     * @param meetingTitle 会议标题
     * @return 如果删除成功返回 true，否则返回 false
     */
    public boolean deleteMeeting(String username, String password, String meetingTitle) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }

        Meeting meetingToDelete = null;
        for (Meeting meeting : user.getMeetings()) {
            if (meeting.getTitle().equals(meetingTitle)) {
                meetingToDelete = meeting;
                break;
            }
        }

        if (meetingToDelete != null) {
            // 检查用户是否是会议的组织者或参与者
            if (meetingToDelete.getOrganizer().equals(username) ||
                meetingToDelete.getParticipant().equals(username)) {
                user.removeMeeting(meetingToDelete);
                User otherUser = users.get(meetingToDelete.getOrganizer().equals(username) ?
                    meetingToDelete.getParticipant() : meetingToDelete.getOrganizer());
                if (otherUser != null) {
                    otherUser.removeMeeting(meetingToDelete);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 清除用户的所有会议
     *
     * @param username 用户名
     * @param password 密码
     * @return 如果清除成功返回 true，否则返回 false
     */
    public boolean clearMeetings(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }

        for (Meeting meeting : user.getMeetings()) {
            User participant = users.get(meeting.getParticipant());
            if (participant != null) {
                participant.removeMeeting(meeting);
            }
        }
        user.clearMeetings();
        return true;
    }
}