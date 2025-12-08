package com.tugasbesar.models.stations;

import com.tugasbesar.models.actors.Chef;
import com.tugasbesar.models.interfaces.Preparable;
import com.tugasbesar.models.enums.IngredientState;

public class CuttingStation extends Station {
    
    private int cutProgress = 0;
    private final int CUT_SPEED = 25; 

    public CuttingStation(int x, int y) {
        super(x, y, "Cutting Station", "C");
    }

    @Override
    public void interact(Chef chef) {
        // --- 1. AMBIL HASIL (CHOPPED) ---
        if (itemOnStation instanceof Preparable) {
            Preparable item = (Preparable) itemOnStation;
            
            // Chef mengambil barang yang sudah jadi
            if (item.getState() == IngredientState.CHOPPED && !chef.hasItem()) {
                chef.setHeldItem(takeItem());
                cutProgress = 0;
                
                // LOG: Tampilkan siapa yang mengambil
                System.out.println("[Cutting] " + chef.getName() + " mengambil " + chef.getHeldItem().getName() + " (CHOPPED)");
                return;
            }
        }

        // --- 2. TARUH BAHAN (RAW) ---
        if (isEmpty() && chef.hasItem()) {
            if (chef.getHeldItem() instanceof Preparable) {
                Preparable item = (Preparable) chef.getHeldItem();
                
                if (item.canBeChopped()) {
                    placeItem(item);
                    chef.setHeldItem(null);
                    
                    // LOG: Tampilkan siapa yang menaruh
                    System.out.println("[Cutting] " + chef.getName() + " menaruh " + itemOnStation.getName());
                } else {
                    System.out.println(">>> [TOLAK] " + chef.getName() + ", item ini tidak bisa dipotong!");
                }
            }
            return;
        }

        // --- 3. AMBIL BALIK / SWAP ---
        // Kalau Chef mau ambil balik Tomat Mentah yang belum selesai dipotong
        if (!isEmpty() && !chef.hasItem()) {
             chef.setHeldItem(takeItem());
             cutProgress = 0; // Reset progress karena barang diambil
             System.out.println("[Cutting] " + chef.getName() + " membatalkan pemotongan & mengambil barang balik.");
             return;
        }
    }

    @Override
    public void update() {
        // SYARAT: Harus ada Chef yang berdiri di sini (chefAtStation tidak null)
        if (chefAtStation == null) return;

        // --- LOGIC CUTTING ---
        if (itemOnStation instanceof Preparable) {
            Preparable item = (Preparable) itemOnStation;

            if (item.canBeChopped()) {
                cutProgress += CUT_SPEED;
                
                // --- VISUALISASI DENGAN NAMA CHEF ---
                // Kita pakai 'chefAtStation' karena dia yang sedang berdiri menunggu proses
                String workingChef = chefAtStation.getName();
                
                System.out.println("   [" + workingChef + "] " + getProgressBar(20) + 
                                   " Memotong " + item.getName());

                // Cek Selesai
                if (cutProgress >= 100) {
                    item.chop();       
                    cutProgress = 100; 
                    System.out.println(">>> [SELESAI] " + workingChef + " berhasil memotong " + item.getName() + "!");
                }
            }
        }
    }

    // Helper Progress Bar
    public String getProgressBar(int width) {
        int percent = Math.min(100, cutProgress);
        int filled = (int) Math.round((percent / 100.0) * width);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < width; i++) sb.append(i < filled ? '#' : '-');
        sb.append("] ").append(percent).append('%');
        return sb.toString();
    }
}