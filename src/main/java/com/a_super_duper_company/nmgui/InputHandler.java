/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.a_super_duper_company.nmgui;

import java.util.HashMap;

/**
 *
 * @author mark
 */
public class InputHandler {
    HashMap<String, Runnable> options = new HashMap<>();
    MainScreen mainScreen;
    static InputHandler instance = null;
    
    private InputHandler() {
        mainScreen = MainScreen.getInstance();
        fillMap();
    }
    
    public static InputHandler getInstance() {
        if(instance == null) {
            instance = new InputHandler();
        }
        return instance;
    }
    private void fillMap() {
        options.put("q", () -> mainScreen.quit());
        options.put("arrowup", () -> mainScreen.up());
        options.put("l", () -> mainScreen.up());
        options.put("arrowdown", () -> mainScreen.down());
        options.put("k", () -> mainScreen.down());
        options.put("enter", () -> mainScreen.connect());
        options.put("r", () -> mainScreen.rescan());
    }
    
    public void start() {
        // eof hack to handle window 
        String tmp = "";
        while(!mainScreen.isClosed() && !tmp.equals("eof")) {
            tmp = mainScreen.read().toLowerCase();
            if(options.containsKey(tmp))
                options.get(tmp).run();
            else {
                System.out.println(tmp);
            }
        }
    }
}
