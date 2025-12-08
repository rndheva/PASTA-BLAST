package com.tugasbesar.core;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import com.tugasbesar.models.actors.Chef;

public class GamePanel extends JPanel implements Runnable {

    // --- Pengaturan Layar ---
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48x48 pixel
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixel
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixel

    // --- System Utama ---
    int FPS = 60;
    Thread gameThread;
    KeyHandler keyH = new KeyHandler();

    
   // --- SYSTEM V2.0 (Baru) ---
    public CollisionChecker cChecker = new CollisionChecker(this); // Polisi Tabrakan
    public int gameTime = 180; // Waktu 3 Menit (180 detik)
    public boolean isGameRunning = true;

    // --- ENTITY ---
    public Chef player = new Chef(this, keyH);


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // Jalankan Game Loop
        
        startGameTimer(); // Jalankan Waktu Mundur
    }
    
    // --- THREAD KHUSUS WAKTU (Agar tidak ganggu gerakan) ---
    public void startGameTimer() {
        Thread timerThread = new Thread(() -> {
            while (isGameRunning && gameTime > 0) {
                try {
                    Thread.sleep(1000); // Tunggu 1 detik
                    gameTime--;
                    // System.out.println("Sisa Waktu: " + gameTime); // Cek console kalau mau
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (gameTime <= 0) {
                System.out.println("⏳ WAKTU HABIS! GAME OVER");
                isGameRunning = false;
            }
        });
        timerThread.start();
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
        if (isGameRunning) {
            player.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Gambar Player
        player.draw(g2);
        
        // --- HUD (Tampilan Waktu) ---
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(30F)); 
        g2.drawString("Time: " + gameTime, 20, 40); // Tampil di pojok kiri atas

        g2.dispose();
    }
}