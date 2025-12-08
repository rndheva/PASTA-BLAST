package com.tugasbesar.models.actors;

import com.tugasbesar.core.GamePanel;
import com.tugasbesar.core.KeyHandler;
import com.tugasbesar.models.abstracts.Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Chef extends Entity {

    GamePanel gp;
    KeyHandler keyH;

    // --- VARIABEL BARU ---
    public String direction = "down"; // Arah hadap (up, down, left, right)
    public Entity heldItem = null;    // Barang yang dipegang

    public Chef(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4; // Kecepatan Normal
        
        // Hitbox lebih kecil dari gambar asli (biar enak manuvernya)
        solidArea = new Rectangle(8, 16, 32, 32); 
    }

    @Override
    public void update() {
        
        // --- LOGIC DASH (LARI CEPAT) ---
        // Kalau tombol SHIFT ditekan, speed jadi 8 (Ngebut)
        if (keyH.dashPressed) {
            speed = 8;
        } else {
            speed = 4; // Normal
        }

        // --- LOGIC GERAK (WASD) ---
        // Kita simpan arah ("direction") setiap kali tombol ditekan
        if (keyH.upPressed) {
            direction = "up";
            y -= speed;
        }
        if (keyH.downPressed) {
            direction = "down";
            y += speed;
        }
        if (keyH.leftPressed) {
            direction = "left";
            x -= speed;
        }
        if (keyH.rightPressed) {
            direction = "right";
            x += speed;
        }

        // --- CEK TABRAKAN (MENTOK LAYAR) ---
        gp.cChecker.checkWindowBoundary(this);

        // --- LOGIC INTERAKSI (SPASI) ---
        if (keyH.interactPressed) {
            interact();
            // Matikan tombol manual biar gak spamming (sekali tekan = 1 aksi)
            keyH.interactPressed = false; 
        }
    }

    // --- LOGIC SENSOR DEPAN (RAYCASTING LITE) ---
    public void interact() {
        // 1. Hitung titik tengah Chef
        int centerX = x + (gp.tileSize / 2);
        int centerY = y + (gp.tileSize / 2);

        // 2. Hitung Chef ada di kolom & baris berapa sekarang
        int currentCol = centerX / gp.tileSize;
        int currentRow = centerY / gp.tileSize;

        // 3. Tentukan koordinat target (1 kotak di depan muka Chef)
        int targetCol = currentCol;
        int targetRow = currentRow;

        switch (direction) {
            case "up":    targetRow--; break; // Depan = Atas
            case "down":  targetRow++; break; // Depan = Bawah
            case "left":  targetCol--; break; // Depan = Kiri
            case "right": targetCol++; break; // Depan = Kanan
        }

        // TEST OUTPUT (Cek di Console saat tekan Spasi)
        System.out.println("⚡ ACTION: Chef (" + direction + ") interaksi di Col: " + targetCol + ", Row: " + targetRow);
    }

    @Override
    public void draw(Graphics2D g2) {
        // Gambar Chef (Badan Putih)
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
        
        // --- VISUALISASI ARAH HADAP (MATA HITAM) ---
        // Biar kita tau Chef lagi madep mana
        g2.setColor(Color.BLACK);
        switch(direction) {
            case "up":    g2.fillRect(x + 20, y + 5, 8, 8); break;  // Mata di atas
            case "down":  g2.fillRect(x + 20, y + 35, 8, 8); break; // Mata di bawah
            case "left":  g2.fillRect(x + 5, y + 20, 8, 8); break;  // Mata di kiri
            case "right": g2.fillRect(x + 35, y + 20, 8, 8); break; // Mata di kanan
        }

        // --- GAMBAR BARANG YANG DIPEGANG ---
        if (heldItem != null) {
            g2.setColor(Color.RED); // Barang warna merah
            g2.fillRect(x + 12, y - 10, 24, 24); // Melayang di atas kepala
        }
    }
}