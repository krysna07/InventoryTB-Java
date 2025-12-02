package Model;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * TabelModel untuk JTable yang menampilkan data dari objek List<User>.
 */
public class TabelModelUser extends AbstractTableModel {
    
    // Variabel list untuk menyimpan data pengguna
    private List<User> lstUser;
    
    // Header kolom yang akan ditampilkan
    private final String[] HEADER = {"ID User", "Username", "Level"};
    
    // --- Constructor ---
    public TabelModelUser(List<User> lstUser) {
        this.lstUser = lstUser;
    }
    
    // --- Implementasi Wajib ---

    @Override
    public int getRowCount() {
        // Mengembalikan jumlah baris data
        if (this.lstUser == null) {
            return 0;
        }
        return this.lstUser.size();
    }

    @Override
    public int getColumnCount() {
        // Mengembalikan jumlah kolom
        return HEADER.length; 
    }
    
    @Override
    public String getColumnName(int column) {
        // Menentukan nama header kolom
        return HEADER[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Mengisi nilai sel tabel berdasarkan baris dan kolom
        switch (columnIndex) {
            case 0:
                // Mengambil ID User (Pastikan method ini ada di User.java)
                return lstUser.get(rowIndex).getIdUser(); 
            case 1:
                // Mengambil Username (Pastikan method ini ada di User.java)
                return lstUser.get(rowIndex).getUsername();
            case 2:
                // Mengambil Level (Pastikan method ini ada di User.java)
                return lstUser.get(rowIndex).getLevel();
            default:
                return null;
        }
    }
}