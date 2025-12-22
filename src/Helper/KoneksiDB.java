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
            
            // 1. Tabel distributor (supplier material bangunan)
            String createDistributor = "CREATE TABLE IF NOT EXISTS distributor (" +
                "ID_Distributor INT PRIMARY KEY," +
                "Nama_Distributor VARCHAR(100) NOT NULL," +
                "Alamat TEXT," +
                "No_Telepon VARCHAR(15)" +
                ")";
            stmt.executeUpdate(createDistributor);
            System.out.println("Tabel distributor sudah siap");
            
            // 2. Tabel barang (material bangunan)
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
            
            // 3. Tabel pesan/order (transaksi pembelian)
            String createPesan = "CREATE TABLE IF NOT EXISTS pesan (" +
                "ID_Order INT PRIMARY KEY," +
                "Nama_Barang VARCHAR(100)," +
                "Jumlah INT," +
                "Tanggal_Pesan TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + // Tambahkan tanggal
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
            
            // 5. Tabel kategori barang (optional - untuk grouping)
            String createKategori = "CREATE TABLE IF NOT EXISTS kategori (" +
                "ID_Kategori INT PRIMARY KEY AUTO_INCREMENT," +
                "Nama_Kategori VARCHAR(50)" +
                ")";
            stmt.executeUpdate(createKategori);
            System.out.println("Tabel kategori sudah siap");
            
            // 6. Insert data user default jika tabel kosong
            String checkUsers = "SELECT COUNT(*) as count FROM users";
            ResultSet rs = stmt.executeQuery(checkUsers);
            rs.next();
            int userCount = rs.getInt("count");
            
            if(userCount == 0) {
                // Insert admin dan user default
                String insertUsers = "INSERT INTO users (username, password, nama_lengkap, level) VALUES " +
                    "('admin', MD5('admin123'), 'Pemilik Toko', 'admin'), " +
                    "('kasir', MD5('kasir123'), 'Kasir Toko', 'user'), " +
                    "('gudang', MD5('gudang123'), 'Staff Gudang', 'user')";
                stmt.executeUpdate(insertUsers);
                System.out.println("Data user default berhasil ditambahkan");
            }
            
            // Insert data kategori jika kosong
            String checkKategori = "SELECT COUNT(*) as count FROM kategori";
            rs = stmt.executeQuery(checkKategori);
            rs.next();
            int kategoriCount = rs.getInt("count");
            
            if(kategoriCount == 0) {
                String insertKategori = "INSERT INTO kategori (Nama_Kategori) VALUES " +
                    "('Semen & Perekat'), " +
                    "('Batu & Pasir'), " +
                    "('Besi & Baja'), " +
                    "('Kayu & Triplek'), " +
                    "('Cat & Thinner'), " +
                    "('Pipa & Sanitasi'), " +
                    "('Alat Tukang'), " +
                    "('Listrik & Lampu')";
                stmt.executeUpdate(insertKategori);
                System.out.println("Data kategori berhasil ditambahkan");
            }
            
            // Insert sample data distributor (supplier toko bangunan)
            String checkDistributor = "SELECT COUNT(*) as count FROM distributor";
            rs = stmt.executeQuery(checkDistributor);
            rs.next();
            int distCount = rs.getInt("count");
            
            if(distCount == 0) {
                String insertDistributor = "INSERT INTO distributor (ID_Distributor, Nama_Distributor, Alamat, No_Telepon) VALUES " +
                    "(1, 'PT. Semen Tiga Roda', 'Jl. Industri No. 1, Cibinong', '021-87654321'), " +
                    "(2, 'PT. Kayu Jati Mas', 'Jl. Hutan Jati No. 45, Jepara', '0291-123456'), " +
                    "(3, 'CV. Besi Baja Makmur', 'Jl. Baja No. 12, Cilegon', '0254-789012'), " +
                    "(4, 'UD. Cat Warna Indah', 'Jl. Kimia No. 8, Tangerang', '021-5551234'), " +
                    "(5, 'PT. Pipa Sejahtera', 'Jl. Pipa No. 33, Surabaya', '031-4567890')";
                stmt.executeUpdate(insertDistributor);
                System.out.println("Data distributor sample berhasil ditambahkan");
            }
            
            // Insert sample data barang (material bangunan)
            String checkBarang = "SELECT COUNT(*) as count FROM barang";
            rs = stmt.executeQuery(checkBarang);
            rs.next();
            int barangCount = rs.getInt("count");
            
            if(barangCount == 0) {
                String insertBarang = "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    // Semen & Perekat
                    "(101, 1, 'Semen Tiga Roda 40kg', 'Sak', 65000, 200), " +
                    "(102, 1, 'Semen Gresik 50kg', 'Sak', 58000, 150), " +
                    "(103, 1, 'Semen Putih 25kg', 'Sak', 95000, 80), " +
                    "(104, 1, 'Perekat Keramik 20kg', 'Sak', 75000, 120), " +
                    "(105, 1, 'Beton Instan', 'Sak', 45000, 300), " +
                    
                    // Batu & Pasir
                    "(201, 1, 'Pasir Beton', 'M3', 250000, 50), " +
                    "(202, 1, 'Pasir Pasang', 'M3', 180000, 70), " +
                    "(203, 1, 'Batu Split 1-2', 'M3', 300000, 40), " +
                    "(204, 1, 'Batu Kali', 'M3', 220000, 35), " +
                    "(205, 1, 'Sirtu', 'M3', 150000, 60), " +
                    
                    // Besi & Baja
                    "(301, 3, 'Beton 10mm per batang', 'Batang', 110000, 500), " +
                    "(302, 3, 'Beton 8mm per batang', 'Batang', 65000, 600), " +
                    "(303, 3, 'Wiremesh M8', 'Lembar', 250000, 100), " +
                    "(304, 3, 'Plat Besi 1.2mm', 'Lembar', 450000, 45), " +
                    "(305, 3, 'Baja Ringan 0.75mm', 'Batang', 85000, 300), " +
                    
                    // Kayu & Triplek
                    "(401, 2, 'Kayu Balok 6x12', 'Batang', 85000, 200), " +
                    "(402, 2, 'Kayu Usuk 5/7', 'Batang', 45000, 300), " +
                    "(403, 2, 'Triplek 9mm', 'Lembar', 120000, 150), " +
                    "(404, 2, 'Multiplek 12mm', 'Lembar', 185000, 120), " +
                    "(405, 2, 'Papan Mahoni', 'Lembar', 95000, 180), " +
                    
                    // Cat & Thinner
                    "(501, 4, 'Cat Dulux Exterior', 'Kaleng', 175000, 200), " +
                    "(502, 4, 'Cat Nippon Paint', 'Kaleng', 160000, 180), " +
                    "(503, 4, 'Cat Tembok Avian', 'Kaleng', 145000, 220), " +
                    "(504, 4, 'Thinner A', 'Kaleng', 45000, 300), " +
                    "(505, 4, 'Meni Besi', 'Kaleng', 65000, 150), " +
                    
                    // Pipa & Sanitasi
                    "(601, 5, 'Pipa PVC 3/4\"', 'Batang', 45000, 400), " +
                    "(602, 5, 'Pipa PVC 1\"', 'Batang', 65000, 350), " +
                    "(603, 5, 'Pipa Galvanis 1\"', 'Batang', 125000, 200), " +
                    "(604, 5, 'Kloset Duduk', 'Unit', 350000, 50), " +
                    "(605, 5, 'Wastafel Keramik', 'Unit', 285000, 40), " +
                    
                    // Alat Tukang
                    "(701, 3, 'Palu Besi', 'Buah', 45000, 200), " +
                    "(702, 3, 'Tang Kombinasi', 'Buah', 55000, 180), " +
                    "(703, 3, 'Gergaji Triplek', 'Buah', 35000, 250), " +
                    "(704, 3, 'Meteran 5m', 'Buah', 25000, 300), " +
                    "(705, 3, 'Sekop', 'Buah', 40000, 150), " +
                    
                    // Listrik & Lampu
                    "(801, 4, 'Kabel NYM 2.5mm', 'Roll', 185000, 100), " +
                    "(802, 4, 'Lampu LED 18W', 'Buah', 35000, 500), " +
                    "(803, 4, 'Saklar Ganda', 'Buah', 25000, 400), " +
                    "(804, 4, 'Stop Kontak', 'Buah', 20000, 450), " +
                    "(805, 4, 'MCB 6A', 'Buah', 45000, 200)";
                    
                stmt.executeUpdate(insertBarang);
                System.out.println("Data barang sample (toko bangunan) berhasil ditambahkan");
            }
            
            System.out.println("Semua tabel dan data sample untuk TOKO BANGUNAN sudah siap!");
            
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