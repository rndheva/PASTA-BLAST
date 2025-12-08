package com.tugasbesar.core;

public class Main { 
    public static void main(String[] args) {
        System.out.println("Menyalakan Engine....");
        GamePanel gamePanel = new GamePanel();
        new GameWindow(gamePanel);
        gamePanel.startGameThread();
    }
}
