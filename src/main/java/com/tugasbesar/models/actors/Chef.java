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

    public Chef(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDefaultValues();
    }

    public void setDefaultValues() {
        // Posisi awal Chef (Tengah layar agak kiri)
        x = 100;
        y = 100;
        speed = 4; // Kecepatan jalan

        // HITBOX (Area Solid) - Biar tabrakannya akurat
        // Kita bikin kotak hitbox sedikit lebih kecil dari gambar aslinya
        solidArea = new Rectangle(8, 16, 32, 32); 
    }

    @Override
    public void update() {
        // LOGIKA GERAK (WASD)
        if (keyH.upPressed) {
            y -= speed;
        }
        if (keyH.downPressed) {
            y += speed;
        }
        if (keyH.leftPressed) {
            x -= speed;
        }
        if (keyH.rightPressed) {
            x += speed;
        }

        // --- CEK TABRAKAN (Panggil Polisi yang tadi dibuat) ---
        gp.cChecker.checkWindowBoundary(this);
    }

    @Override
    public void draw(Graphics2D g2) {
        // GAMBAR CHEF (Sementara Kotak Putih Dulu)
        // Nanti Person 4 (Frontend) tugasnya ganti ini jadi g2.drawImage(...)
        
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, gp.tileSize, gp.tileSize);
    }
}