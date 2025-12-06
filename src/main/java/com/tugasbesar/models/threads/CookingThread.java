package com.tugasbesar.models.threads;

public class CookingThread extends Thread {
    
    private int cookingTimeSeconds;
    private Runnable onFinishAction; // Aksi yang dijalankan pas matang
    public boolean isRunning = false;

    // Constructor: Terima berapa lama masak & apa yang dilakukan pas selesai
    public CookingThread(int seconds, Runnable onFinishAction) {
        this.cookingTimeSeconds = seconds;
        this.onFinishAction = onFinishAction;
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            System.out.println("🔥 Mulai memasak selama " + cookingTimeSeconds + " detik...");
            
            // Thread Tidur (Proses Masak) - Game Loop Tetap Jalan!
            Thread.sleep(cookingTimeSeconds * 1000L);
            
            System.out.println("✅ Masakan Matang!");
            
            // Jalankan aksi selesai (misal: ubah status makanan)
            if (onFinishAction != null) {
                onFinishAction.run();
            }
            
        } catch (InterruptedException e) {
            System.out.println("⚠️ Proses masak diganggu!");
        } finally {
            isRunning = false; // Tandai kompor mati
        }
    }
}