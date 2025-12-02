package Helper;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.SQLException;
import java.sql.*;

public class KoneksiDB {
    static Connection con;
    
    public static Connection getConnection()
    {
        if(con == null)
        {
            MysqlDataSource data = new MysqlDataSource();
            
            // ⚠️ BARIS YANG DIKOREKSI SESUAI NAMA DATABASE ASLI
            data.setDatabaseName("inventory_tb"); 
            
            data.setUser("root");
            data.setPassword("");
            data.setServerName("localhost"); // Tambahkan setServerName jika belum ada
            
            try
            {
                con = data.getConnection();
                System.out.println("Koneksi Berhasil");
            }catch(SQLException e)
            {
                System.out.println("Koneksi Gagal");
                // Tampilkan pesan error detail agar tahu penyebabnya
                System.err.println("Detail Error: " + e.getMessage()); 
            }
        }
        return con;
    }
}