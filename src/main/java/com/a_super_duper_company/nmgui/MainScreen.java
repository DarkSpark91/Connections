/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.a_super_duper_company.nmgui;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 *
 * @author mark
 */
public class MainScreen {
    int position;
    String[] connections;
    Terminal term = null;
    private static MainScreen instance = null;
    TextGraphics textGraphics;
    boolean shutdown;
    
    private MainScreen() {
        position = 0;
        connections = scan();
        init_screen();
        shutdown = false;
    }
    
    public static MainScreen getInstance() {
        if(instance == null) {
            instance = new MainScreen();
        }
        return instance;
    }
    
    final void init_screen() {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        try {
            term = defaultTerminalFactory.createTerminal();
            term.enterPrivateMode();
            term.clearScreen();
            term.setCursorVisible(false);
            textGraphics = term.newTextGraphics();
            draw();
            term.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    int getPosition() {
        return position;
    }
    
    String[] scan() {
        String[] cmd = {"nmcli", "d", "wifi", "list"};
        return runCommand(cmd);
    }
    
    void rescan() {
        String[] cmd = {"nmcli", "d", "wifi", "rescan"};
        runCommand(cmd);
        connections = scan();
        redraw();
        System.out.println("hello");
    }

    String[] runCommand(String[] cmd) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdOut = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            printArray(stdError.lines().toArray(size -> new String[size]));
            return stdOut.lines().toArray(size -> new String[size]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
    }
    
    void up() {
        position = (position == 0 ? 0 : position -1 );
        redraw();
    }
    
    void down() {
        position = (position + 2 == connections.length ? position : position + 1);
        redraw();
    }
    
    void redraw() {
        try {
            term.clearScreen();
            draw();
            term.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    void draw() {
        if (connections.length <= 1) {
            textGraphics.putString(0, 0, "no availible networks.");
        }
        for(int i = 0; i < connections.length; i++) {
            if(i == position+1) {
                textGraphics.putString(0, i, connections[i], SGR.REVERSE);
            } else {
                textGraphics.putString(0, i, connections[i]);
            }
        }
    }
    
    void quit() {
        close();
        shutdown = true;
    }
    
    String read() {
        try {
            KeyStroke input = term.readInput();
            if(input.getKeyType() == KeyType.Character) {
                return "" + input.getCharacter();
            }
            return input.getKeyType().toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    void close() {
        if(term != null) {
            try {
                term.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    boolean isClosed() {
        return shutdown;
    }
    
    void connect() {
        if(connections.length > 1) {
            String networkName = connections[position+1].substring(1, connections[0].indexOf("MODE")).trim();
            System.out.println(networkName);
            String[] cmd = {"nmcli", "connection", "up", "id", networkName};
            printArray(runCommand(cmd));
        }
    }
    
    void printArray(String[] s) {
        for(String var: s) {
            System.out.println(var);
        }
    }
}