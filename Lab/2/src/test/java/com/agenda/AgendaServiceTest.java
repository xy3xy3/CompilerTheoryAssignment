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
 * AgendaService单元测试类。
 *
 * <p>本测试类用于验证{@link AgendaService}类的各项功能，包括：
 * <ul>
 *   <li>用户注册</li>
 *   <li>会议添加</li>
 *   <li>会议查询</li>
 *   <li>会议删除</li>
 *   <li>清除所有会议</li>
 * </ul>
 *
 * <p>每个测试方法都独立执行，通过{@link BeforeEach}注解确保每次测试前都有一个新的AgendaService实例。
 *
 * @see AgendaService
 * @see Meeting
 */
class AgendaServiceTest {
    /** 测试用的AgendaService实例 */
    private AgendaService agendaService;

    /**
     * 在每个测试方法执行前设置测试环境。
     *
     * <p>创建一个新的AgendaService实例，确保每个测试方法都从干净的状态开始。
     */
    @BeforeEach
    public void setUp() {
        // 每个测试方法执行前都会初始化一个新的 AgendaService 实例
        agendaService = new AgendaService();
    }

    /**
     * 测试用户注册功能。
     *
     * <p>测试场景：
     * <ol>
     *   <li>注册新用户，预期结果：成功</li>
     *   <li>使用已存在的用户名再次注册，预期结果：失败</li>
     * </ol>
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
     * 测试成功添加会议的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>注册两个用户</li>
     *   <li>添加一个会议，由user1邀请user2</li>
     *   <li>分别查询两位用户的会议列表</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>会议添加成功</li>
     *   <li>两位用户的会议列表中都能查到这个会议</li>
     * </ul>
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
     * 测试添加会议时的时间冲突情况。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>注册两个用户</li>
     *   <li>成功添加第一个会议</li>
     *   <li>尝试添加与第一个会议时间重叠的第二个会议</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>第一个会议添加成功</li>
     *   <li>第二个会议因时间冲突而添加失败</li>
     * </ul>
     *
     * <p>时间冲突定义：当两个会议的时间范围有任何重叠时，视为冲突。
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
     * 测试删除指定会议的功能。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>注册两个用户</li>
     *   <li>添加一个会议</li>
     *   <li>查询会议并获取会议ID</li>
     *   <li>删除指定ID的会议</li>
     *   <li>验证两个用户的会议列表中都不再有该会议</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>删除会议操作成功</li>
     *   <li>两个用户的会议列表均为空</li>
     * </ul>
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
     * 测试清除用户所有会议的功能。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>注册两个用户</li>
     *   <li>添加两个不同时间段的会议</li>
     *   <li>清除user1的所有会议</li>
     *   <li>分别查询两个用户在相关时间段的会议</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>清除会议操作成功</li>
     *   <li>两个用户的会议列表均为空</li>
     * </ul>
     *
     * <p>注意：当发起者清除其会议时，会议的所有参与者对应的会议记录也应被删除。
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
