package com.tugasbesar.models.abstracts;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {
    public int x, y;
    public int speed;
    
    // Hitbox untuk tabrakan
    public Rectangle solidArea;
    public boolean collisionOn = false;

    // Setiap benda wajib punya method ini
    public abstract void update();
    public abstract void draw(Graphics2D g2);
}