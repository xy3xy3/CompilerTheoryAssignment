package com.agenda;

import com.agenda.controller.AgendaController;

/**
 * 议程管理系统主类
 */
public class App {
    /**
     * 主方法，启动议程管理系统
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        AgendaController controller = new AgendaController();
        controller.start();
    }
}