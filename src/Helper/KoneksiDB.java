package Helper;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.JOptionPane;

public class KoneksiDB {
    static Connection con;
    
    public static Connection getConnection() {
        if(con == null) {
            try {
                // Buat database jika belum ada
                createDatabaseIfNotExists();
                
                // Koneksi ke database
                MysqlDataSource data = new MysqlDataSource();
                data.setDatabaseName("inventory_tb");
                data.setUser("root");
                data.setPassword("");
                data.setServerName("localhost");
                data.setPort(3306);
                data.setUseSSL(false);
                data.setAllowPublicKeyRetrieval(true);
                
                con = data.getConnection();
                System.out.println("Koneksi Berhasil ke database inventory_tb");
                
                // Buat semua tabel jika belum ada
                createTablesIfNotExists();
                
            } catch(SQLException e) {
                System.out.println("Koneksi Gagal");
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
                e.printStackTrace();
                
                JOptionPane.showMessageDialog(null, 
                    "Gagal terhubung ke database:\n" + e.getMessage() +
                    "\n\nPastikan MySQL server berjalan!",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        return con;
    }
    
    private static void createDatabaseIfNotExists() {
        Connection tempConn = null;
        Statement stmt = null;
        try {
            // Koneksi tanpa database spesifik
            MysqlDataSource data = new MysqlDataSource();
            data.setUser("root");
            data.setPassword("");
            data.setServerName("localhost");
            data.setPort(3306);
            data.setUseSSL(false);
            
            tempConn = data.getConnection();
            stmt = tempConn.createStatement();
            
            // Buat database jika belum ada
            String createDB = "CREATE DATABASE IF NOT EXISTS inventory_tb CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci";
            stmt.executeUpdate(createDB);
            System.out.println("Database inventory_tb sudah siap atau berhasil dibuat");
            
        } catch(SQLException e) {
            System.err.println("Gagal membuat database: " + e.getMessage());
        } finally {
            try {
                if(stmt != null) stmt.close();
                if(tempConn != null) tempConn.close();
            } catch(SQLException e) {
                // Ignore
            }
        }
    }
    
    private static void createTablesIfNotExists() {
        Connection conn = con;
        Statement stmt = null;
        
        try {
            stmt = conn.createStatement();
            
            // 1. Tabel distributor
            String createDistributor = "CREATE TABLE IF NOT EXISTS distributor (" +
                "ID_Distributor INT PRIMARY KEY," +
                "Nama_Distributor VARCHAR(100) NOT NULL," +
                "Alamat TEXT," +
                "No_Telepon VARCHAR(15)" +
                ")";
            stmt.executeUpdate(createDistributor);
            System.out.println("Tabel distributor sudah siap");
            
            // 2. Tabel barang
            String createBarang = "CREATE TABLE IF NOT EXISTS barang (" +
                "ID_Barang INT PRIMARY KEY," +
                "ID_Distributor INT," +
                "Nama_Barang VARCHAR(100) NOT NULL," +
                "Satuan VARCHAR(20)," +
                "Harga INT," +
                "Stok INT," +
                "FOREIGN KEY (ID_Distributor) REFERENCES distributor(ID_Distributor)" +
                ")";
            stmt.executeUpdate(createBarang);
            System.out.println("Tabel barang sudah siap");
            
            // 3. Tabel pesan/order
            String createPesan = "CREATE TABLE IF NOT EXISTS pesan (" +
                "ID_Order INT PRIMARY KEY," +
                "Nama_Barang VARCHAR(100)," +
                "Jumlah INT" +
                ")";
            stmt.executeUpdate(createPesan);
            System.out.println("Tabel pesan sudah siap");
            
            // 4. Tabel users untuk login
            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                "id_user INT PRIMARY KEY AUTO_INCREMENT," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "nama_lengkap VARCHAR(100)," +
                "level VARCHAR(20) DEFAULT 'user'" +
                ")";
            stmt.executeUpdate(createUsers);
            System.out.println("Tabel users sudah siap");
            
            // 5. Insert data user default jika tabel kosong
            String checkUsers = "SELECT COUNT(*) as count FROM users";
            ResultSet rs = stmt.executeQuery(checkUsers);
            rs.next();
            int userCount = rs.getInt("count");
            
            if(userCount == 0) {
                // Insert admin dan user default
                String insertUsers = "INSERT INTO users (username, password, nama_lengkap, level) VALUES " +
                    "('admin', MD5('admin123'), 'Administrator', 'admin'), " +
                    "('user', MD5('user123'), 'User Biasa', 'user')";
                stmt.executeUpdate(insertUsers);
                System.out.println("Data user default berhasil ditambahkan");
            }
            
            //Insert sample data distributor jika kosong
            String checkDistributor = "SELECT COUNT(*) as count FROM distributor";
            rs = stmt.executeQuery(checkDistributor);
            rs.next();
            int distCount = rs.getInt("count");
            
            if(distCount == 0) {
                String insertDistributor = "INSERT INTO distributor (ID_Distributor, Nama_Distributor, Alamat, No_Telepon) VALUES " +
                    "(1, 'Distributor Elektronik Maju', 'Jl. Merdeka No. 123', '081234567890'), " +
                    "(2, 'Distributor Komputer Sentosa', 'Jl. Sudirman No. 45', '082345678901'), " +
                    "(3, 'Distributor IT Solution', 'Jl. Gatot Subroto No. 67', '083456789012')";
                stmt.executeUpdate(insertDistributor);
                System.out.println("Data distributor sample berhasil ditambahkan");
            }
            
            //Insert sample data barang jika kosong
            String checkBarang = "SELECT COUNT(*) as count FROM barang";
            rs = stmt.executeQuery(checkBarang);
            rs.next();
            int barangCount = rs.getInt("count");
            
            if(barangCount == 0) {
                String insertBarang = "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(1, 1, 'Laptop Dell Inspiron 15', 'Unit', 12000000, 15), " +
                    "(2, 1, 'Mouse Wireless Logitech', 'Pcs', 250000, 50), " +
                    "(3, 2, 'Keyboard Mechanical RGB', 'Pcs', 850000, 30), " +
                    "(4, 2, 'Monitor 24 inch LG', 'Unit', 3500000, 20), " +
                    "(5, 3, 'SSD 512GB Samsung', 'Pcs', 1200000, 40), " +
                    "(6, 3, 'RAM DDR4 16GB', 'Pcs', 800000, 35)";
                stmt.executeUpdate(insertBarang);
                System.out.println("Data barang sample berhasil ditambahkan");
            }
            
            System.out.println("Semua tabel dan data sample sudah siap!");
            
        } catch(SQLException e) {
            System.err.println("Gagal membuat tabel: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if(stmt != null) stmt.close();
            } catch(SQLException e) {
                // Ignore
            }
        }
    }
    
    // Method untuk menutup koneksi
    public static void closeConnection() {
        try {
            if(con != null && !con.isClosed()) {
                con.close();
                System.out.println("Koneksi ditutup");
                con = null;
            }
        } catch(SQLException e) {
            System.err.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}