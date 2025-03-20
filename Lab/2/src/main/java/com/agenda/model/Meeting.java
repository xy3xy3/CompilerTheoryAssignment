package com.agenda.model;

import java.time.LocalDateTime;

/**
 * 会议模型类
 */
public class Meeting {
    private String title;
    private String organizer;
    private String participant;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Meeting(String title, String organizer, String participant,
                  LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.organizer = organizer;
        this.participant = participant;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getParticipant() {
        return participant;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean overlaps(Meeting other) {
        return (this.startTime.isBefore(other.endTime) &&
                this.endTime.isAfter(other.startTime));
    }
}