/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.a_super_duper_company.nmgui;

/**
 *
 * @author mark
 */
public class App {
    public static void main(String[] args) {
        MainScreen mainScreen = MainScreen.getInstance();
        InputHandler keyHandler = InputHandler.getInstance();
        keyHandler.start();
    }
}
