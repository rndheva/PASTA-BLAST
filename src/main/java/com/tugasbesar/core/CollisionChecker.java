package com.tugasbesar.core;

import com.tugasbesar.models.abstracts.Entity;

public class CollisionChecker {
    
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // Method untuk cek batas layar (biar gak keluar window)
    public void checkWindowBoundary(Entity entity) {
        // Mentok Kiri
        if (entity.x < 0) {
            entity.x = 0;
        }
        // Mentok Atas
        if (entity.y < 0) {
            entity.y = 0;
        }
        // Mentok Kanan (Lebar Layar - Lebar Entity)
        if (entity.x > gp.screenWidth - gp.tileSize) {
            entity.x = gp.screenWidth - gp.tileSize;
        }
        // Mentok Bawah (Tinggi Layar - Tinggi Entity)
        if (entity.y > gp.screenHeight - gp.tileSize) {
            entity.y = gp.screenHeight - gp.tileSize;
        }
    }
}