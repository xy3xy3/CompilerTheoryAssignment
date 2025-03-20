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

    /**
     * 构造函数，初始化会议对象
     *
     * @param title 会议标题
     * @param organizer 会议组织者
     * @param participant 会议参与者
     * @param startTime 会议开始时间
     * @param endTime 会议结束时间
     */
    public Meeting(String title, String organizer, String participant,
                   LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.organizer = organizer;
        this.participant = participant;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * 获取会议标题
     *
     * @return 会议标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取会议组织者
     *
     * @return 会议组织者
     */
    public String getOrganizer() {
        return organizer;
    }

    /**
     * 获取会议参与者
     *
     * @return 会议参与者
     */
    public String getParticipant() {
        return participant;
    }

    /**
     * 获取会议开始时间
     *
     * @return 会议开始时间
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 获取会议结束时间
     *
     * @return 会议结束时间
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 判断会议是否与另一个会议时间重叠
     *
     * @param other 另一个会议对象
     * @return 如果时间重叠返回 true，否则返回 false
     */
    public boolean overlaps(Meeting other) {
        return (this.startTime.isBefore(other.endTime) &&
                this.endTime.isAfter(other.startTime));
    }
}