package com.tugasbesar.core;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 pixel
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixel
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixel

    int FPS = 60;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    // Posisi Pemain (Test)
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        if (keyH.upPressed) playerY -= playerSpeed;
        if (keyH.downPressed) playerY += playerSpeed;
        if (keyH.leftPressed) playerX -= playerSpeed;
        if (keyH.rightPressed) playerX += playerSpeed;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(playerX, playerY, tileSize, tileSize);
        g2.dispose();
    }
}