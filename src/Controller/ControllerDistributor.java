package Controller;

import DAO.DAODistributor;
import java.util.List;
import DAOInterface.IDAODistributor;
import Model.Distributor;
import Model.TabelModelDistributor;
import View.FormDistributor;
import javax.swing.JOptionPane;

/**
 * Controller untuk Form Distributor
 * @author kumil
 */
public class ControllerDistributor {
    
    private FormDistributor frmDistributor;
    private IDAODistributor iDistributor;
    private List<Distributor> lstDis;
    
    public ControllerDistributor(FormDistributor frmDistributor) {
        this.frmDistributor = frmDistributor;
        this.iDistributor = new DAODistributor();
        
        System.out.println("\n=== [CONTROLLER DISTRIBUTOR] INITIALIZATION ===");
    }
    
    /**
     * Load semua data distributor dari database ke tabel
     */
    public void isiTable() {
        // ✅ FIXED: Selalu ambil data fresh dari database
        lstDis = iDistributor.getAll();
        
        TabelModelDistributor tabelDis = new TabelModelDistributor(lstDis);
        frmDistributor.getTabelData().setModel(tabelDis);
        
        System.out.println("[CONTROLLER] Tabel diisi dengan " + lstDis.size() + " data");
    }
    
    /**
     * Insert distributor baru ke database
     */
    public void Insert() {
        try {
            // Validasi input kosong
            if (frmDistributor.gettxtID_Distributor().getText().trim().isEmpty() ||
                frmDistributor.gettxtNama_Distributor().getText().trim().isEmpty() ||
                frmDistributor.gettxtNo_Telepon().getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(frmDistributor, 
                    "Semua field harus diisi!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Distributor b = new Distributor();
            b.setID_Distributor(Integer.parseInt(frmDistributor.gettxtID_Distributor().getText().trim()));
            b.setNama_Distributor(frmDistributor.gettxtNama_Distributor().getText().trim());
            b.setAlamat(frmDistributor.gettxtAlamat().getText().trim());
            
            // ✅ FIXED: Set sebagai String (bukan parse ke Integer)
            b.setNo_Telepon(frmDistributor.gettxtNo_Telepon().getText().trim());
            
            System.out.println("\n=== [CONTROLLER] INSERT DISTRIBUTOR ===");
            System.out.printf("Data:%n  ID: %d%n  Nama: %s%n  Telepon: %s%n",
                b.getID_Distributor(), b.getNama_Distributor(), b.getNo_Telepon());
            
            boolean res = iDistributor.insert(b);
            
            if (res) {
                JOptionPane.showMessageDialog(frmDistributor, 
                    "✅ Input berhasil!\n\n" +
                    "Distributor: " + b.getNama_Distributor() + "\n" +
                    "Telepon: " + b.getNo_Telepon(),
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // ✅ CRITICAL FIX: Refresh tabel setelah insert!
                isiTable();
                reset();
                
            } else {
                JOptionPane.showMessageDialog(frmDistributor, 
                    "❌ Gagal menyimpan data!\n" +
                    "Kemungkinan ID sudah digunakan atau terjadi error database.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Format ID salah!\nID harus berupa angka.",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("[ERROR] NumberFormatException: " + e.getMessage());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Reset semua field form
     */
    public void reset() {
        if (!frmDistributor.gettxtID_Distributor().isEnabled()) {
            frmDistributor.gettxtID_Distributor().setEnabled(true);
        }
        
        frmDistributor.gettxtID_Distributor().setText("");
        frmDistributor.gettxtNama_Distributor().setText("");
        frmDistributor.gettxtAlamat().setText("");
        frmDistributor.gettxtNo_Telepon().setText("");
        
        System.out.println("[CONTROLLER] Form reset");
    }
    
    /**
     * Isi field form dengan data dari row tabel yang diklik
     */
    public void isiField(int row) {
        if (row >= 0 && row < lstDis.size()) {
            Distributor dist = lstDis.get(row);
            
            frmDistributor.gettxtID_Distributor().setEnabled(false);
            frmDistributor.gettxtID_Distributor().setText(dist.getID_Distributor().toString());
            frmDistributor.gettxtNama_Distributor().setText(dist.getNama_Distributor());
            frmDistributor.gettxtAlamat().setText(dist.getAlamat());
            
            // ✅ FIXED: Langsung set sebagai String (bukan toString() dari Integer)
            frmDistributor.gettxtNo_Telepon().setText(dist.getNo_Telepon());
            
            System.out.printf("[CONTROLLER] Field diisi: [%d] %s | Tel: %s%n",
                dist.getID_Distributor(), dist.getNama_Distributor(), dist.getNo_Telepon());
        }
    }
    
    /**
     * Update data distributor yang sudah ada
     */
    public void update() {
        try {
            // Validasi input
            if (frmDistributor.gettxtNama_Distributor().getText().trim().isEmpty() ||
                frmDistributor.gettxtNo_Telepon().getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(frmDistributor, 
                    "Nama dan Telepon harus diisi!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            Distributor b = new Distributor();
            b.setID_Distributor(Integer.parseInt(frmDistributor.gettxtID_Distributor().getText().trim()));
            b.setNama_Distributor(frmDistributor.gettxtNama_Distributor().getText().trim());
            b.setAlamat(frmDistributor.gettxtAlamat().getText().trim());
            
            // ✅ FIXED: Set sebagai String (bukan parse ke Integer)
            b.setNo_Telepon(frmDistributor.gettxtNo_Telepon().getText().trim());
            
            System.out.println("\n=== [CONTROLLER] UPDATE DISTRIBUTOR ===");
            System.out.printf("ID: %d | Nama: %s%n", b.getID_Distributor(), b.getNama_Distributor());
            
            iDistributor.update(b);
            
            JOptionPane.showMessageDialog(frmDistributor, 
                "✅ Update berhasil!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE);
            
            // ✅ CRITICAL FIX: Refresh tabel setelah update!
            isiTable();
            reset();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Format ID salah!",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            System.err.println("[ERROR] NumberFormatException: " + e.getMessage());
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Hapus distributor dari database
     */
    public void delete() {
        try {
            int idDist = Integer.parseInt(frmDistributor.gettxtID_Distributor().getText().trim());
            String namaDist = frmDistributor.gettxtNama_Distributor().getText();
            
            // Konfirmasi sebelum delete
            int confirm = JOptionPane.showConfirmDialog(
                frmDistributor,
                "Hapus distributor?\n\n" +
                "ID: " + idDist + "\n" +
                "Nama: " + namaDist + "\n\n" +
                "Data yang sudah dihapus tidak dapat dikembalikan!",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("\n=== [CONTROLLER] DELETE DISTRIBUTOR ===");
                System.out.printf("Menghapus ID: %d | Nama: %s%n", idDist, namaDist);
                
                iDistributor.delete(idDist);
                
                JOptionPane.showMessageDialog(frmDistributor, 
                    "✅ Delete berhasil!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // ✅ CRITICAL FIX: Refresh tabel setelah delete!
                isiTable();
                reset();
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Pilih data yang akan dihapus terlebih dahulu!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frmDistributor, 
                "Error: " + e.getMessage() + "\n\n" +
                "Kemungkinan distributor ini masih terhubung dengan data barang.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Search distributor berdasarkan nama
     */
    public void search() {
        String keyword = frmDistributor.gettxtCariNama().getText().trim();
        
        if (keyword.isEmpty()) {
            // ✅ FIXED: Tampilkan semua jika keyword kosong
            isiTable();
            System.out.println("[CONTROLLER] Search kosong, tampilkan semua data");
        } else {
            lstDis = iDistributor.getAllByName(keyword);
            TabelModelDistributor tabelDis = new TabelModelDistributor(lstDis);
            frmDistributor.getTabelData().setModel(tabelDis);
            
            System.out.printf("[CONTROLLER] Search '%s' -> %d hasil%n", keyword, lstDis.size());
            
            if (lstDis.isEmpty()) {
                JOptionPane.showMessageDialog(frmDistributor, 
                    "Tidak ada hasil untuk: " + keyword,
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}