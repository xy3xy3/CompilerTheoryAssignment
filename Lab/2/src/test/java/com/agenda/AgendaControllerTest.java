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

class AgendaControllerTest {

    @Mock
    private AgendaService service;

    @Mock
    private AgendaView view;

    private AgendaController controller;

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

    @Test
    void testHandleDelete_Success() {
        // 准备测试数据
        String[] command = {"delete", "testUser", "password123", "测试会议"};
        when(service.deleteMeeting("testUser", "password123", "测试会议")).thenReturn(true);

        // 执行测试
        controller.handleDelete(command);

        // 验证结果
        verify(view).showSuccess("会议删除成功");
    }

    @Test
    void testHandleDelete_Failure() {
        // 准备测试数据
        String[] command = {"delete", "testUser", "password123", "测试会议"};
        when(service.deleteMeeting("testUser", "password123", "测试会议")).thenReturn(false);

        // 执行测试
        controller.handleDelete(command);

        // 验证结果
        verify(view).showError("会议删除失败，请检查用户名、密码或会议标题");
    }

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