package com.agenda;

import com.agenda.controller.AgendaController;

/**
 * 议程管理系统主类
 */
public class App {
    public static void main(String[] args) {
        AgendaController controller = new AgendaController();
        controller.start();
    }
}