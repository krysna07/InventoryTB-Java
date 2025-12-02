package DAO;

import DAOInterface.IDAOUser;
import Helper.KoneksiDB;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DAOUser implements IDAOUser {
    
    Connection con;
    
    // ... (Query-query Insert, Update, Delete, dll. lainnya di sini) ...
    
    // Query untuk proses Login
    // KOREKSI: Mengganti nama tabel dari 'login' menjadi 'users'
    // Perhatikan: Gunakan MD5(?) jika Anda menyimpan password ter-hash MD5 di database.
    String strLogin = "SELECT * FROM users WHERE username=? AND password=MD5(?)"; 
    
    // JIKA TIDAK MENGGUNAKAN MD5, gunakan:
    // String strLogin = "SELECT * FROM users WHERE username=? AND password=?";
    
    public DAOUser()
    {
        con = KoneksiDB.getConnection();
    }

    // ... (Implementasi metode insert, update, delete, getAll, getAllByName di sini) ...

    
    // --- Metode Tambahan (Login) ---
    public User login(String username, String password) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            ps = con.prepareStatement(strLogin);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery(); // Baris yang menyebabkan error sebelumnya
            
            if (rs.next()) {
                // Login Berhasil
                user = new User();
                user.setIdUser(rs.getInt("id_user"));
                user.setUsername(rs.getString("username"));
                user.setNamaLengkap(rs.getString("nama_lengkap"));
                user.setLevel(rs.getString("level"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error SQL saat Login: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException ex) {
                // Ignore close exception
            }
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean insert(User u) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(User u) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(int ID_User) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<User> getAllByName(String Password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}