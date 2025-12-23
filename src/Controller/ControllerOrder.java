package Controller;

import DAO.DAOOrder;
import Model.Order;
import Model.TabelModelOrder;
import View.FormOrder;
import java.util.List;
import DAOInterface.IDAOOrder;
import Model.Barang;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ControllerOrder {
    private FormOrder frmOrder;
    private IDAOOrder iOrder;
    private List<Order> lstOrd;

    public ControllerOrder(FormOrder frmOrder) {
        this.frmOrder = frmOrder;
        this.iOrder = new DAOOrder();
        
        System.out.println("\n=== [CONTROLLER] INITIALIZATION ===");
        
        // Debug: Cek apakah barang memiliki harga
        List<Barang> testBarang = iOrder.getAllByNama_Barang();
        System.out.println("Jumlah barang di database: " + testBarang.size());
        
        if (!testBarang.isEmpty()) {
            Barang contoh = testBarang.get(0);
            System.out.println("Contoh barang pertama:");
            System.out.println("  Nama: '" + contoh.getNama_Barang() + "'");
            System.out.println("  Harga: Rp " + contoh.getHarga());
            
            // Test method getHargaByNama
            System.out.println("\nTest getHargaByNama():");
            int hargaTest = iOrder.getHargaByNama(contoh.getNama_Barang());
            System.out.println("  Result: Rp " + hargaTest);
        }
    }

    public void isiTable() {
        lstOrd = iOrder.getAll();
        TabelModelOrder tabelOrd = new TabelModelOrder(lstOrd);
        frmOrder.getTabelData().setModel(tabelOrd);
        
        System.out.println("[CONTROLLER] Tabel diisi dengan " + lstOrd.size() + " data");
    }

    public void Insert() {
        try {
            Order o = new Order();
            o.setID_Order(Integer.parseInt(frmOrder.gettxtID_Order().getText()));
            
            // Ambil nama barang dari combobox
            String selectedText = frmOrder.gettxtNama_Barang().getSelectedItem().toString();
            System.out.println("\n=== [CONTROLLER] INSERT ORDER ===");
            System.out.println("Selected from combobox: " + selectedText);
            
            // Ekstrak nama barang bersih (hilangkan format harga jika ada)
            String namaBarang = extractNamaBarang(selectedText);
            System.out.println("Nama Barang (clean): " + namaBarang);
            
            o.setNama_Barang(namaBarang);
            o.setJumlah(Integer.parseInt(frmOrder.gettxtJumlah().getText()));
            
            // ===== AMBIL HARGA DARI DATABASE =====
            System.out.println("\n[1] Mencari harga dari database...");
            int harga = iOrder.getHargaByNama(namaBarang);
            
            if (harga > 0) {
                o.setHarga(harga);
                System.out.println("✅ Harga berhasil diambil dari DB: Rp " + harga);
            } else {
                System.out.println("[WARNING] getHargaByNama() return 0, coba method lain...");
                
                // Coba method getBarangDenganHarga
                Barang barang = iOrder.getBarangDenganHarga(namaBarang);
                if (barang != null && barang.getHarga() > 0) {
                    o.setHarga(barang.getHarga());
                    System.out.println("✅ Harga ditemukan via getBarangDenganHarga: Rp " + barang.getHarga());
                } else {
                    System.out.println("[WARNING] Kedua method gagal!");
                    
                    // Tampilkan semua barang untuk debugging
                    System.out.println("=== DEBUG: SEMUA BARANG DI DATABASE ===");
                    List<Barang> semuaBarang = iOrder.getAllByNama_Barang();
                    boolean found = false;
                    
                    for (Barang b : semuaBarang) {
                        System.out.println("  - '" + b.getNama_Barang() + "' = Rp " + b.getHarga());
                        if (b.getNama_Barang().equalsIgnoreCase(namaBarang)) {
                            found = true;
                            o.setHarga(b.getHarga());
                            System.out.println("✅ Ditemukan di list semua barang! Harga: Rp " + b.getHarga());
                        }
                    }
                    
                    if (!found || o.getHarga() == 0) {
                        // Minta input manual HANYA JIKA BENAR-BENAR TIDAK ADA
                        String inputHarga = JOptionPane.showInputDialog(
                            frmOrder,
                            "Harga untuk '" + namaBarang + "' tidak ditemukan!\n" +
                            "Masukkan harga satuan:",
                            "Input Harga Manual",
                            JOptionPane.WARNING_MESSAGE
                        );
                        
                        if (inputHarga != null && !inputHarga.trim().isEmpty()) {
                            o.setHarga(Integer.parseInt(inputHarga.trim()));
                            System.out.println("✅ Harga manual: Rp " + o.getHarga());
                        } else {
                            JOptionPane.showMessageDialog(frmOrder, "Insert dibatalkan!");
                            return;
                        }
                    }
                }
            }
            
            // Simpan ke database
            boolean res = iOrder.insert(o);
            
            if (res) {
                JOptionPane.showMessageDialog(frmOrder, "✅ Input berhasil!");
                
                // Kurangi stok
                iOrder.kurangiStok(o.getNama_Barang(), o.getJumlah());
                
                // Tanya cetak struk
                int pilihan = JOptionPane.showConfirmDialog(
                    frmOrder,
                    "Total: Rp " + (o.getJumlah() * o.getHarga()) + "\n" +
                    "Cetak struk untuk order ini?",
                    "Cetak Struk",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (pilihan == JOptionPane.YES_OPTION) {
                    List<Order> singleOrder = new ArrayList<>();
                    singleOrder.add(o);
                    cetakStruk(singleOrder);
                }
                
                isiTable();
                reset();
                
            } else {
                JOptionPane.showMessageDialog(frmOrder, "❌ Gagal menyimpan data!");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmOrder, "Format angka salah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmOrder, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String extractNamaBarang(String comboText) {
        // Jika format: "Nama Barang (Rp Harga)"
        if (comboText.contains("(Rp")) {
            return comboText.substring(0, comboText.indexOf("(Rp")).trim();
        }
        return comboText.trim();
    }

    public void cetakStruk(List<Order> orders) {
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(frmOrder, "Tidak ada data untuk dicetak!");
            return;
        }
        
        try {
            System.out.println("\n=== [CONTROLLER] CETAK STRUK ===");
            System.out.println("Jumlah item: " + orders.size());
            
            // Ambil harga untuk setiap item dari database
            List<Order> ordersWithPrice = new ArrayList<>();
            int totalAll = 0;
            
            for (Order order : orders) {
                System.out.println("\nProcessing: " + order.getNama_Barang());
                
                // Jika order belum punya harga atau harga = 0
                if (order.getHarga() == 0) {
                    int harga = iOrder.getHargaByNama(order.getNama_Barang());
                    
                    if (harga > 0) {
                        order.setHarga(harga);
                        System.out.println("  ✅ Harga dari DB: Rp " + harga);
                    } else {
                        System.out.println("  ❌ Harga tidak ditemukan di DB!");
                        
                        // Tanya input manual
                        String inputHarga = JOptionPane.showInputDialog(
                            frmOrder,
                            "Harga untuk '" + order.getNama_Barang() + "' tidak ditemukan di database!\n" +
                            "Masukkan harga satuan:",
                            "Input Harga Manual",
                            JOptionPane.WARNING_MESSAGE
                        );
                        
                        if (inputHarga != null && !inputHarga.trim().isEmpty()) {
                            order.setHarga(Integer.parseInt(inputHarga.trim()));
                            System.out.println("  ✅ Harga manual: Rp " + order.getHarga());
                        } else {
                            System.out.println("  ⚠️ Item dilewati (user cancel)");
                            continue; // Skip item ini
                        }
                    }
                } else {
                    System.out.println("  ✅ Harga sudah ada: Rp " + order.getHarga());
                }
                
                int subtotal = order.getJumlah() * order.getHarga();
                totalAll += subtotal;
                ordersWithPrice.add(order);
                
                System.out.printf("  📋 %-30s x%3d = Rp %,10d%n",
                    order.getNama_Barang(), order.getJumlah(), subtotal);
            }
            
            if (ordersWithPrice.isEmpty()) {
                JOptionPane.showMessageDialog(frmOrder, "Tidak ada item valid untuk dicetak!");
                return;
            }
            
            // Konfirmasi sebelum cetak
            String confirmMsg = String.format(
                "📋 DETAIL PEMBELIAN:\n" +
                "====================\n" +
                "Total Item  : %d\n" +
                "Total Bayar : Rp %,d\n\n" +
                "Cetak struk PDF?",
                ordersWithPrice.size(), totalAll
            );
            
            int confirm = JOptionPane.showConfirmDialog(
                frmOrder,
                confirmMsg,
                "Konfirmasi Cetak",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Generate ID struk
                String idStruk = "STR-" + 
                    new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                
                // Panggil ReportStruk
                Helper.ReportStruk.generateStruk(ordersWithPrice, idStruk);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                frmOrder,
                "❌ Gagal mencetak struk:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    public void reset() {
        frmOrder.gettxtID_Order().setEnabled(true);
        frmOrder.gettxtID_Order().setText("");
        frmOrder.gettxtNama_Barang().setSelectedIndex(0);
        frmOrder.gettxtJumlah().setText("");
        System.out.println("[CONTROLLER] Form reset");
    }

    public void isiField(int row) {
        if (row >= 0 && row < lstOrd.size()) {
            Order order = lstOrd.get(row);
            
            frmOrder.gettxtID_Order().setEnabled(false);
            frmOrder.gettxtID_Order().setText(String.valueOf(order.getID_Order()));
            frmOrder.gettxtNama_Barang().setSelectedItem(order.getNama_Barang());
            frmOrder.gettxtJumlah().setText(String.valueOf(order.getJumlah()));
            
            System.out.println("[CONTROLLER] Field diisi: " + order.getNama_Barang() + 
                             " | Harga: Rp " + order.getHarga());
        }
    }

    public void update() {
        try {
            Order o = new Order();
            o.setJumlah(Integer.parseInt(frmOrder.gettxtJumlah().getText()));
            o.setNama_Barang(frmOrder.gettxtNama_Barang().getSelectedItem().toString());
            o.setID_Order(Integer.parseInt(frmOrder.gettxtID_Order().getText()));
            
            iOrder.update(o);
            JOptionPane.showMessageDialog(frmOrder, "✅ Update berhasil");
            
            isiTable();
            reset();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmOrder, "Format angka salah!");
        }
    }

    public void delete() {
        try {
            int idOrder = Integer.parseInt(frmOrder.gettxtID_Order().getText());
            
            int confirm = JOptionPane.showConfirmDialog(
                frmOrder,
                "Hapus order ID: " + idOrder + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                iOrder.delete(idOrder);
                JOptionPane.showMessageDialog(frmOrder, "✅ Delete berhasil");
                isiTable();
                reset();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmOrder, "ID Order tidak valid!");
        }
    }

    public void search() {
        String keyword = frmOrder.gettxtCariNama().getText().trim();
        
        if (keyword.isEmpty()) {
            isiTable(); // Tampilkan semua jika kosong
        } else {
            lstOrd = iOrder.getAllByName(keyword);
            TabelModelOrder tabelOrd = new TabelModelOrder(lstOrd);
            frmOrder.getTabelData().setModel(tabelOrd);
            
            System.out.println("[CONTROLLER] Search: '" + keyword + "' -> " + lstOrd.size() + " results");
        }
    }

    public void JCOMBONama_BRG() {
        List<Barang> lstBrg = iOrder.getAllByNama_Barang();
        
        System.out.println("\n=== [CONTROLLER] MENGISI COMBOBOX BARANG ===");
        System.out.println("Jumlah barang: " + lstBrg.size());
        
        frmOrder.gettxtNama_Barang().removeAllItems();
        
        // Tambahkan item kosong pertama
        frmOrder.gettxtNama_Barang().addItem("-- Pilih Barang --");
        
        for (Barang barang : lstBrg) {
            // Format: Nama Barang (Rp Harga)
            String displayText = String.format("%s (Rp %,d)", 
                barang.getNama_Barang(), barang.getHarga());
            
            frmOrder.gettxtNama_Barang().addItem(displayText);
            System.out.println("  Added to combobox: " + displayText);
        }
    }
}