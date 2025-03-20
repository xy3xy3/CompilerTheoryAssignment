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

    public AgendaService() {
        this.users = new HashMap<>();
    }

    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password));
        return true;
    }

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
            user.removeMeeting(meetingToDelete);
            User participant = users.get(meetingToDelete.getParticipant());
            if (participant != null) {
                participant.removeMeeting(meetingToDelete);
            }
            return true;
        }
        return false;
    }

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