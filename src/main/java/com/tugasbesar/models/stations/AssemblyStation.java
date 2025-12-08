package com.tugasbesar.models.stations;

import com.tugasbesar.models.actors.Chef;
import com.tugasbesar.models.abstracts.Item;
import com.tugasbesar.models.interfaces.Preparable;
import com.tugasbesar.models.item.kitchen_utensil.Plate;

public class AssemblyStation extends Station {

    public AssemblyStation(int x, int y) {
        super(x, y, "Assembly Station", "A");
    }

    @Override
    public void interact(Chef chef) {
        Item hand = chef.getHeldItem();
        Item tableItem = itemOnStation;

        // --- 1. LOGIKA PLATING (GABUNG BAHAN) ---

        // Skenario A: Chef bawa Piring, di Meja ada Bahan Matang
        if (hand instanceof Plate && tableItem instanceof Preparable) {
            performPlating(chef, (Plate) hand, (Preparable) tableItem, true);
            return;
        }

        // Skenario B: Chef bawa Bahan Matang, di Meja ada Piring
        if (hand instanceof Preparable && tableItem instanceof Plate) {
            performPlating(chef, (Plate) tableItem, (Preparable) hand, false);
            return;
        }

        // --- 2. LOGIKA AMBIL / TARUH BIASA ---

        // Ambil item dari meja (Tangan kosong)
        if (hand == null && !isEmpty()) {
            chef.setHeldItem(takeItem());
            System.out.println("[Assembly] " + chef.getName() + " mengambil " + chef.getHeldItem().getName());
            return;
        }

        // Taruh item ke meja (Hanya boleh Piring)
        if (hand != null && isEmpty()) {
            if (hand instanceof Plate) {
                placeItem(hand);
                chef.setHeldItem(null);
                System.out.println("[Assembly] " + chef.getName() + " menaruh Piring.");
            } else {
                System.out.println("[!] Hanya Piring yang boleh ditaruh di sini.");
            }
        }
    }

    // Helper untuk validasi aturan Pasta
    private void performPlating(Chef chef, Plate plate, Preparable item, boolean isPlateInHand) {
        
        // 1. Cek Kesiapan Bahan (Harus Matang / Siap)
        if (!item.canBePlacedOnPlate()) {
            System.out.println("[!] Bahan belum siap (mentah/gosong)!");
            return;
        }

        // 2. Deteksi Nama (Pakai contains biar aman kalau ada suffix "Cooked")
        String itemName = item.getName().toLowerCase();
        boolean incomingIsPasta = itemName.contains("pasta");

        // Cek isi piring saat ini
        boolean plateHasPasta = plate.getContents().stream()
                                .anyMatch(p -> p.getName().toLowerCase().contains("pasta"));

        // RULE 1: Piring kosong WAJIB diisi Pasta dulu (Base Layer)
        if (plate.getContents().isEmpty() && !incomingIsPasta) {
            System.out.println("[!] Piring kosong harus diisi Pasta dulu sebagai dasar.");
            return;
        }

        // RULE 2: Jangan menumpuk Pasta di atas Pasta
        if (plateHasPasta && incomingIsPasta) {
            System.out.println("[!] Sudah ada Pasta di piring.");
            return;
        }

        // --- EKSEKUSI PLATING ---
        
        // Masukkan bahan ke piring
        plate.addIngredient(item); 
        System.out.println("[Plating] " + item.getName() + " ditata di atas Piring.");

        // Hapus item dari sumbernya
        if (isPlateInHand) {
            // Kalau Chef pegang piring, berarti bahan diambil dari meja -> Hapus dari meja
            this.itemOnStation = null; 
        } else {
            // Kalau Chef pegang bahan, berarti bahan diambil dari tangan -> Hapus dari tangan
            chef.setHeldItem(null);    
        }
    }
}