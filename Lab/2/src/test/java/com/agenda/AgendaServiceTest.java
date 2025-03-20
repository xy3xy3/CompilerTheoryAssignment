package com.agenda;

import com.agenda.model.Meeting;
import com.agenda.service.AgendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AgendaService 单元测试类
 */
class AgendaServiceTest {
    private AgendaService agendaService;

    @BeforeEach
    public void setUp() {
        // 每个测试方法执行前都会初始化一个新的 AgendaService 实例
        agendaService = new AgendaService();
    }

    /**
     * 测试用户注册功能：
     * - 注册新用户成功
     * - 注册相同用户名失败
     */
    @Test
    public void testRegisterUser() {
        boolean result = agendaService.registerUser("user1", "pass1");
        assertTrue(result, "第一次注册应成功");
        // 相同用户名再次注册应失败
        result = agendaService.registerUser("user1", "pass2");
        assertFalse(result, "重复注册同一用户名应失败");
    }

    /**
     * 测试正常添加会议：
     * - 两个用户均已注册后，添加会议成功
     * - 查询时，双方都能查询到该会议
     */
    @Test
    public void testAddMeetingSuccess() {
        agendaService.registerUser("user1", "pass1");
        agendaService.registerUser("user2", "pass2");

        LocalDateTime startTime = LocalDateTime.of(2025, 3, 21, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 21, 11, 0);
        boolean addResult = agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime, endTime);
        assertTrue(addResult, "添加会议应成功");

        // 分别查询两个用户在会议时间段内的会议
        List<Meeting> meetingsUser1 = agendaService.queryMeetings("user1", "pass1", startTime.minusHours(1), endTime.plusHours(1));
        assertEquals(1, meetingsUser1.size(), "user1 应该有 1 个会议");

        List<Meeting> meetingsUser2 = agendaService.queryMeetings("user2", "pass2", startTime.minusHours(1), endTime.plusHours(1));
        assertEquals(1, meetingsUser2.size(), "user2 应该有 1 个会议");
    }

    /**
     * 测试添加会议时时间冲突的情况：
     * - 当同一用户或其预约方已有重叠会议时，添加会议应失败
     */
    @Test
    public void testAddMeetingTimeConflict() {
        agendaService.registerUser("user1", "pass1");
        agendaService.registerUser("user2", "pass2");

        LocalDateTime startTime1 = LocalDateTime.of(2025, 3, 21, 10, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2025, 3, 21, 11, 0);
        boolean addResult1 = agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime1, endTime1);
        assertTrue(addResult1, "第一个会议添加应成功");

        // 新的会议与第一个会议存在时间重叠，应添加失败
        LocalDateTime startTime2 = LocalDateTime.of(2025, 3, 21, 10, 30);
        LocalDateTime endTime2 = LocalDateTime.of(2025, 3, 21, 11, 30);
        boolean addResult2 = agendaService.addMeeting("user1", "pass1", "user2", "Meeting2", startTime2, endTime2);
        assertFalse(addResult2, "重叠的会议添加应失败");
    }

    /**
     * 测试删除会议功能：
     * - 添加会议后，删除指定会议，双方的会议列表中均应删除该会议
     */
    @Test
    public void testDeleteMeeting() {
        agendaService.registerUser("user1", "pass1");
        agendaService.registerUser("user2", "pass2");

        LocalDateTime startTime = LocalDateTime.of(2025, 3, 22, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 3, 22, 10, 0);
        boolean addResult = agendaService.addMeeting("user1", "pass1", "user2", "MeetingToDelete", startTime, endTime);
        assertTrue(addResult, "会议添加应成功");

        // 获取会议ID
        List<Meeting> meetings = agendaService.queryMeetings("user1", "pass1", startTime.minusHours(1), endTime.plusHours(1));
        assertEquals(1, meetings.size(), "应该找到1个会议");
        String meetingId = meetings.get(0).getId();

        // 删除会议
        boolean deleteResult = agendaService.deleteMeeting("user1", "pass1", meetingId);
        assertTrue(deleteResult, "删除会议应成功");

        // 查询会议应为空
        List<Meeting> meetingsUser1 = agendaService.queryMeetings("user1", "pass1", startTime.minusHours(1), endTime.plusHours(1));
        assertEquals(0, meetingsUser1.size(), "user1 的会议应为空");
        List<Meeting> meetingsUser2 = agendaService.queryMeetings("user2", "pass2", startTime.minusHours(1), endTime.plusHours(1));
        assertEquals(0, meetingsUser2.size(), "user2 的会议应为空");
    }

    /**
     * 测试清除所有会议功能：
     * - 添加多个会议后，清除某个用户所有会议，同时对方的会议列表也应被清除
     */
    @Test
    public void testClearMeetings() {
        agendaService.registerUser("user1", "pass1");
        agendaService.registerUser("user2", "pass2");

        LocalDateTime startTime1 = LocalDateTime.of(2025, 3, 23, 14, 0);
        LocalDateTime endTime1 = LocalDateTime.of(2025, 3, 23, 15, 0);
        boolean addResult1 = agendaService.addMeeting("user1", "pass1", "user2", "Meeting1", startTime1, endTime1);
        assertTrue(addResult1, "第一个会议添加应成功");

        LocalDateTime startTime2 = LocalDateTime.of(2025, 3, 23, 16, 0);
        LocalDateTime endTime2 = LocalDateTime.of(2025, 3, 23, 17, 0);
        boolean addResult2 = agendaService.addMeeting("user1", "pass1", "user2", "Meeting2", startTime2, endTime2);
        assertTrue(addResult2, "第二个会议添加应成功");

        // 清除 user1 的所有会议
        boolean clearResult = agendaService.clearMeetings("user1", "pass1");
        assertTrue(clearResult, "清除会议应成功");

        // 查询 user1 的会议应为空
        List<Meeting> meetingsUser1 = agendaService.queryMeetings("user1", "pass1", startTime1.minusHours(1), endTime2.plusHours(1));
        assertEquals(0, meetingsUser1.size(), "user1 的会议列表应为空");

        // 对于 user2 而言，因为对应会议都被清除了，其会议列表也应为空
        List<Meeting> meetingsUser2 = agendaService.queryMeetings("user2", "pass2", startTime1.minusHours(1), endTime2.plusHours(1));
        assertEquals(0, meetingsUser2.size(), "user2 的会议列表应为空");
    }
}
