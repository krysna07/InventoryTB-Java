/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package InventoryTB;

import Helper.KoneksiDB;
import View.FormLogin;

/**
 *
 * @author kumil
 */
public class InventoryTB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        KoneksiDB.getConnection(); // Setup database
        
        // Langsung tampilkan login
        java.awt.EventQueue.invokeLater(() -> {
            new FormLogin().setVisible(true);
        });
        
    }
    
}
