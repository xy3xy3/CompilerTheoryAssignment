package com.agenda;

import com.agenda.model.Meeting;
import com.agenda.service.AgendaService;
import com.agenda.view.AgendaView;
import com.agenda.controller.AgendaController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AgendaController 单元测试类。
 *
 * <p>本测试类使用 Mockito 模拟 {@link AgendaService} 和 {@link AgendaView} 组件，
 * 用于验证 {@link AgendaController} 类的各项功能，包括：
 * <ul>
 *   <li>用户注册命令处理</li>
 *   <li>会议添加命令处理</li>
 *   <li>会议查询命令处理</li>
 *   <li>会议删除命令处理</li>
 *   <li>清除会议命令处理</li>
 *   <li>无效命令处理</li>
 * </ul>
 *
 * <p>每个测试方法都独立执行，通过 {@link BeforeEach} 注解确保每次测试前都重新初始化相关模拟对象。
 *
 * @see AgendaController
 * @see AgendaService
 * @see AgendaView
 */
class AgendaControllerTest {

    /** 模拟的 AgendaService 对象 */
    @Mock
    private AgendaService service;

    /** 模拟的 AgendaView 对象 */
    @Mock
    private AgendaView view;

    /** 待测试的 AgendaController 实例 */
    private AgendaController controller;

    /**
     * 在每个测试方法执行前设置测试环境。
     *
     * <p>初始化所有模拟对象，创建 AgendaController 实例，
     * 并通过反射方式将模拟的 service 和 view 对象注入到 controller 中。
     *
     * @throws Exception 如果反射操作失败
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new AgendaController();
        // 使用反射设置mock对象
        try {
            java.lang.reflect.Field serviceField = AgendaController.class.getDeclaredField("service");
            java.lang.reflect.Field viewField = AgendaController.class.getDeclaredField("view");
            serviceField.setAccessible(true);
            viewField.setAccessible(true);
            serviceField.set(controller, service);
            viewField.set(controller, view);
        } catch (Exception e) {
            fail("Failed to set up mock objects: " + e.getMessage());
        }
    }

    /**
     * 测试成功处理用户注册命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备注册命令参数</li>
     *   <li>模拟 service.registerUser 返回成功</li>
     *   <li>调用 controller.handleRegister 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showSuccess 方法应被调用，显示注册成功信息</li>
     * </ul>
     */
    @Test
    void testHandleRegister_Success() {
        // 准备测试数据
        String[] command = {"register", "testUser", "password123"};
        when(service.registerUser("testUser", "password123")).thenReturn(true);

        // 执行测试
        controller.handleRegister(command);

        // 验证结果
        verify(view).showSuccess("用户注册成功");
    }

    /**
     * 测试用户注册失败时的命令处理场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备注册命令参数</li>
     *   <li>模拟 service.registerUser 返回失败</li>
     *   <li>调用 controller.handleRegister 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showError 方法应被调用，显示用户名已存在的错误信息</li>
     * </ul>
     */
    @Test
    void testHandleRegister_Failure() {
        // 准备测试数据
        String[] command = {"register", "testUser", "password123"};
        when(service.registerUser("testUser", "password123")).thenReturn(false);

        // 执行测试
        controller.handleRegister(command);

        // 验证结果
        verify(view).showError("用户名已存在");
    }

    /**
     * 测试成功处理会议添加命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备添加会议命令参数</li>
     *   <li>模拟 view.parseDateTime 方法成功解析日期时间</li>
     *   <li>模拟 service.addMeeting 方法返回成功</li>
     *   <li>调用 controller.handleAdd 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showSuccess 方法应被调用，显示会议添加成功信息</li>
     * </ul>
     */
    @Test
    void testHandleAdd_Success() {
        // 准备测试数据
        String[] command = {
            "add", "testUser", "password123", "participant",
            "2024-03-20 10:00", "2024-03-20 11:00", "测试会议"
        };
        LocalDateTime startDateTime = LocalDateTime.of(2024, 3, 20, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 20, 11, 0);

        when(view.parseDateTime("2024-03-20 10:00")).thenReturn(startDateTime);
        when(view.parseDateTime("2024-03-20 11:00")).thenReturn(endDateTime);
        when(service.addMeeting("testUser", "password123", "participant", "测试会议",
            startDateTime, endDateTime)).thenReturn(true);

        // 执行测试
        controller.handleAdd(command);

        // 验证结果
        verify(view).showSuccess("会议添加成功");
    }

    /**
     * 测试会议添加命令中日期时间格式错误的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备含有无效日期格式的添加会议命令参数</li>
     *   <li>模拟 view.parseDateTime 方法抛出异常</li>
     *   <li>调用 controller.handleAdd 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showError 方法应被调用，显示时间格式错误信息</li>
     * </ul>
     */
    @Test
    void testHandleAdd_InvalidDateTime() {
        // 准备测试数据
        String[] command = {
            "add", "testUser", "password123", "participant",
            "invalid-date 10:00", "2024-03-20 11:00", "测试会议"
        };

        when(view.parseDateTime("invalid-date 10:00")).thenThrow(new IllegalArgumentException());

        // 执行测试
        controller.handleAdd(command);

        // 验证结果
        verify(view).showError("时间格式错误，请使用正确的格式：yyyy-MM-dd HH:mm");
    }

    /**
     * 测试成功处理会议查询命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备查询会议命令参数</li>
     *   <li>模拟 view.parseDateTime 方法成功解析日期时间</li>
     *   <li>模拟 service.queryMeetings 方法返回预期的会议列表</li>
     *   <li>调用 controller.handleQuery 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showMeetings 方法应被调用，显示查询到的会议列表</li>
     * </ul>
     */
    @Test
    void testHandleQuery_Success() {
        // 准备测试数据
        String[] command = {
            "query", "testUser", "password123",
            "2024-03-20 10:00", "2024-03-20 11:00"
        };
        LocalDateTime startDateTime = LocalDateTime.of(2024, 3, 20, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 20, 11, 0);
        List<Meeting> expectedMeetings = Arrays.asList(
            new Meeting("测试会议", "testUser", "participant", startDateTime, endDateTime)
        );

        when(view.parseDateTime("2024-03-20 10:00")).thenReturn(startDateTime);
        when(view.parseDateTime("2024-03-20 11:00")).thenReturn(endDateTime);
        when(service.queryMeetings("testUser", "password123", startDateTime, endDateTime))
            .thenReturn(expectedMeetings);

        // 执行测试
        controller.handleQuery(command);

        // 验证结果
        verify(view).showMeetings(expectedMeetings);
    }

    /**
     * 测试成功处理会议删除命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>创建测试用会议对象</li>
     *   <li>模拟查询会议并获取会议 ID</li>
     *   <li>模拟 service.deleteMeeting 方法返回成功</li>
     *   <li>调用 controller.handleDelete 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showSuccess 方法应被调用，显示会议删除成功信息</li>
     * </ul>
     */
    @Test
    void testHandleDelete_Success() {
        // 准备测试数据
        LocalDateTime startDateTime = LocalDateTime.of(2024, 3, 20, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 20, 11, 0);
        Meeting testMeeting = new Meeting("测试会议", "testUser", "participant", startDateTime, endDateTime);
        String meetingId = testMeeting.getId();

        // 模拟查询会议返回结果
        List<Meeting> meetings = Arrays.asList(testMeeting);
        when(service.queryMeetings("testUser", "password123", startDateTime.minusHours(1), endDateTime.plusHours(1)))
            .thenReturn(meetings);

        // 模拟删除会议成功
        when(service.deleteMeeting("testUser", "password123", meetingId)).thenReturn(true);

        // 先执行查询命令获取会议ID
        String[] queryCommand = {
            "query", "testUser", "password123",
            "2024-03-20 09:00", "2024-03-20 12:00"
        };
        when(view.parseDateTime("2024-03-20 09:00")).thenReturn(startDateTime.minusHours(1));
        when(view.parseDateTime("2024-03-20 12:00")).thenReturn(endDateTime.plusHours(1));
        controller.handleQuery(queryCommand);

        // 验证显示会议列表
        verify(view).showMeetings(meetings);

        // 然后执行删除命令
        String[] deleteCommand = {"delete", "testUser", "password123", meetingId};
        controller.handleDelete(deleteCommand);

        // 验证结果
        verify(view).showSuccess("会议删除成功");
    }

    /**
     * 测试处理会议删除命令失败的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>创建测试用会议对象</li>
     *   <li>模拟查询会议并获取会议 ID</li>
     *   <li>模拟 service.deleteMeeting 方法返回失败</li>
     *   <li>调用 controller.handleDelete 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showError 方法应被调用，显示会议删除失败信息</li>
     * </ul>
     */
    @Test
    void testHandleDelete_Failure() {
        // 准备测试数据
        LocalDateTime startDateTime = LocalDateTime.of(2024, 3, 20, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 3, 20, 11, 0);
        Meeting testMeeting = new Meeting("测试会议", "testUser", "participant", startDateTime, endDateTime);
        String meetingId = testMeeting.getId();

        // 模拟查询会议返回结果
        List<Meeting> meetings = Arrays.asList(testMeeting);
        when(service.queryMeetings("testUser", "password123", startDateTime.minusHours(1), endDateTime.plusHours(1)))
            .thenReturn(meetings);

        // 模拟删除会议失败
        when(service.deleteMeeting("testUser", "password123", meetingId)).thenReturn(false);

        // 先执行查询命令获取会议ID
        String[] queryCommand = {
            "query", "testUser", "password123",
            "2024-03-20 09:00", "2024-03-20 12:00"
        };
        when(view.parseDateTime("2024-03-20 09:00")).thenReturn(startDateTime.minusHours(1));
        when(view.parseDateTime("2024-03-20 12:00")).thenReturn(endDateTime.plusHours(1));
        controller.handleQuery(queryCommand);

        // 验证显示会议列表
        verify(view).showMeetings(meetings);

        // 然后执行删除命令（但删除会失败）
        String[] deleteCommand = {"delete", "testUser", "password123", meetingId};
        controller.handleDelete(deleteCommand);

        // 验证结果
        verify(view).showError("会议删除失败，请检查用户名、密码或会议ID");
    }

    /**
     * 测试成功处理清除所有会议命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备清除会议命令参数</li>
     *   <li>模拟 service.clearMeetings 方法返回成功</li>
     *   <li>调用 controller.handleClear 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showSuccess 方法应被调用，显示所有会议清除成功信息</li>
     * </ul>
     */
    @Test
    void testHandleClear_Success() {
        // 准备测试数据
        String[] command = {"clear", "testUser", "password123"};
        when(service.clearMeetings("testUser", "password123")).thenReturn(true);

        // 执行测试
        controller.handleClear(command);

        // 验证结果
        verify(view).showSuccess("所有会议清除成功");
    }

    /**
     * 测试处理清除所有会议命令失败的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备清除会议命令参数</li>
     *   <li>模拟 service.clearMeetings 方法返回失败</li>
     *   <li>调用 controller.handleClear 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showError 方法应被调用，显示清除会议失败信息</li>
     * </ul>
     */
    @Test
    void testHandleClear_Failure() {
        // 准备测试数据
        String[] command = {"clear", "testUser", "password123"};
        when(service.clearMeetings("testUser", "password123")).thenReturn(false);

        // 执行测试
        controller.handleClear(command);

        // 验证结果
        verify(view).showError("清除会议失败，请检查用户名和密码");
    }

    /**
     * 测试处理无效命令的场景。
     *
     * <p>测试步骤：
     * <ol>
     *   <li>准备无效命令参数</li>
     *   <li>调用 controller.handleInvalidCommand 方法</li>
     * </ol>
     *
     * <p>预期结果：
     * <ul>
     *   <li>view.showHelp 方法应被调用，显示帮助信息</li>
     * </ul>
     */
    @Test
    void testInvalidCommand() {
        // 准备测试数据
        String[] command = {"invalid", "testUser", "password123"};

        // 执行测试
        controller.handleInvalidCommand(command);

        // 验证结果
        verify(view).showHelp();
    }
}