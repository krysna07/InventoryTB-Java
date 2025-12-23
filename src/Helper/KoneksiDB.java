package Helper;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.SQLException;
import java.sql.*;
import javax.swing.JOptionPane;

public class KoneksiDB {

    static Connection con;

    public static Connection getConnection() {
        if (con == null) {
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
                System.out.println("✅ Koneksi berhasil ke database inventory_tb");

                // Buat semua tabel jika belum ada
                createTablesIfNotExists();

            } catch (SQLException e) {
                System.err.println("❌ Koneksi Gagal");
                System.err.println("SQL State: " + e.getSQLState());
                System.err.println("Error Code: " + e.getErrorCode());
                System.err.println("Message: " + e.getMessage());
                e.printStackTrace();

                JOptionPane.showMessageDialog(null,
                        "Gagal terhubung ke database:\n" + e.getMessage()
                        + "\n\nPastikan MySQL server berjalan!",
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
            System.out.println("✅ Database inventory_tb sudah siap");

        } catch (SQLException e) {
            System.err.println("❌ Gagal membuat database: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (tempConn != null) tempConn.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    private static void createTablesIfNotExists() {
        try (Statement stmt = con.createStatement()) {

            System.out.println("\n=== INISIALISASI DATABASE ===");

            // 1. Tabel distributor (HARUS PERTAMA - karena ada FK)
            String createDistributor
                    = "CREATE TABLE IF NOT EXISTS distributor ("
                    + "ID_Distributor INT NOT NULL,"
                    + "Nama_Distributor VARCHAR(100) NOT NULL,"
                    + "Alamat TEXT,"
                    + "No_Telepon VARCHAR(15),"
                    + "PRIMARY KEY (ID_Distributor)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createDistributor);
            System.out.println("✅ Tabel distributor");

            // 2. Tabel barang (KEDUA - depends on distributor)
            String createBarang
                    = "CREATE TABLE IF NOT EXISTS barang ("
                    + "ID_Barang INT NOT NULL,"
                    + "ID_Distributor INT,"
                    + "Nama_Barang VARCHAR(100) NOT NULL,"
                    + "Satuan VARCHAR(20),"
                    + "Harga INT DEFAULT 0,"
                    + "Stok INT DEFAULT 0,"
                    + "PRIMARY KEY (ID_Barang),"
                    + "FOREIGN KEY (ID_Distributor) REFERENCES distributor(ID_Distributor)"
                    + " ON UPDATE CASCADE ON DELETE RESTRICT"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createBarang);
            System.out.println("✅ Tabel barang");

            // 3. Tabel pesan (DENGAN KOLOM HARGA!)
            String createPesan
                    = "CREATE TABLE IF NOT EXISTS pesan ("
                    + "ID_Order INT NOT NULL,"
                    + "Nama_Barang VARCHAR(100),"
                    + "Jumlah INT,"
                    + "Harga INT DEFAULT 0,"  // ← KOLOM HARGA!
                    + "Tanggal_Pesan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (ID_Order)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createPesan);
            System.out.println("✅ Tabel pesan (dengan kolom Harga)");

            // 4. Tabel users
            String createUsers
                    = "CREATE TABLE IF NOT EXISTS users ("
                    + "id_user INT NOT NULL AUTO_INCREMENT,"
                    + "username VARCHAR(50) NOT NULL,"
                    + "password VARCHAR(255) NOT NULL,"
                    + "nama_lengkap VARCHAR(100),"
                    + "level VARCHAR(20) DEFAULT 'user',"
                    + "PRIMARY KEY (id_user),"
                    + "UNIQUE (username)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createUsers);
            System.out.println("✅ Tabel users");

            // 5. Tabel kategori
            String createKategori
                    = "CREATE TABLE IF NOT EXISTS kategori ("
                    + "ID_Kategori INT NOT NULL AUTO_INCREMENT,"
                    + "Nama_Kategori VARCHAR(50),"
                    + "PRIMARY KEY (ID_Kategori)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createKategori);
            System.out.println("✅ Tabel kategori");

            // ===============================
            // AUTO INSERT DATA DEFAULT
            // ===============================
            
            System.out.println("\n=== MENGISI DATA DEFAULT ===");
            
            ResultSet rs;

            // ===== 1. INSERT USERS (independent) =====
            rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate(
                        "INSERT INTO users (username, password, nama_lengkap, level) VALUES "
                        + "('admin', MD5('admin123'), 'Pemilik Toko', 'admin'),"
                        + "('kasir', MD5('kasir123'), 'Kasir Toko', 'user'),"
                        + "('gudang', MD5('gudang123'), 'Staff Gudang', 'user')"
                );
                System.out.println("✅ Users: admin, kasir, gudang");
            }

            // ===== 2. INSERT KATEGORI (independent) =====
            rs = stmt.executeQuery("SELECT COUNT(*) FROM kategori");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate(
                        "INSERT INTO kategori (Nama_Kategori) VALUES "
                        + "('Semen & Perekat'),"
                        + "('Batu & Pasir'),"
                        + "('Besi & Baja'),"
                        + "('Kayu & Triplek'),"
                        + "('Cat & Thinner'),"
                        + "('Pipa & Sanitasi'),"
                        + "('Alat Tukang'),"
                        + "('Listrik & Lampu'),"
                        + "('Seng & Atap'),"
                        + "('Paku & Sekrup'),"
                        + "('Keramik & Granit')"
                );
                System.out.println("✅ Kategori: 11 kategori");
            }

            // ===== 3. INSERT DISTRIBUTOR (HARUS SEBELUM BARANG!) =====
            rs = stmt.executeQuery("SELECT COUNT(*) FROM distributor");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate(
                        "INSERT INTO distributor (ID_Distributor, Nama_Distributor, Alamat, No_Telepon) VALUES "
                        + "(1, 'PT. Semen Tiga Roda', 'Jl. Industri No.1, Cibinong', '021-87654321'),"
                        + "(2, 'PT. Kayu Jati Mas', 'Jl. Hutan Jati No.45, Jepara', '0291-123456'),"
                        + "(3, 'CV. Besi Baja Makmur', 'Jl. Baja No.12, Cilegon', '0254-789012'),"
                        + "(4, 'UD. Cat Warna Indah', 'Jl. Kimia No.8, Tangerang', '021-5551234'),"
                        + "(5, 'PT. Pipa Sejahtera', 'Jl. Pipa No.33, Surabaya', '031-4567890')"
                );
                System.out.println("✅ Distributor: 5 supplier");
            }

            // ===== 4. INSERT BARANG (SETELAH DISTRIBUTOR ADA!) =====
            rs = stmt.executeQuery("SELECT COUNT(*) FROM barang");
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert dalam batch untuk menghindari query terlalu panjang
                
                // Batch 1: Semen & Perekat (ID_Distributor = 1)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(101, 1, 'Semen Tiga Roda 40kg', 'Sak', 65000, 200), " +
                    "(102, 1, 'Semen Gresik 50kg', 'Sak', 58000, 150), " +
                    "(103, 1, 'Semen Putih 25kg', 'Sak', 95000, 80), " +
                    "(104, 1, 'Perekat Keramik 20kg', 'Sak', 75000, 120), " +
                    "(105, 1, 'Beton Instan', 'Sak', 45000, 300), " +
                    "(106, 1, 'Semen Holcim 50kg', 'Sak', 62000, 180), " +
                    "(107, 1, 'Lem Kayu Rajawali', 'Kg', 25000, 100)"
                );

                // Batch 2: Batu & Pasir (ID_Distributor = 1)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(201, 1, 'Pasir Beton', 'M3', 250000, 50), " +
                    "(202, 1, 'Pasir Pasang', 'M3', 180000, 70), " +
                    "(203, 1, 'Batu Split 1-2', 'M3', 300000, 40), " +
                    "(204, 1, 'Batu Kali', 'M3', 220000, 35), " +
                    "(205, 1, 'Sirtu', 'M3', 150000, 60), " +
                    "(206, 1, 'Batu Split 2-3', 'M3', 280000, 45), " +
                    "(207, 1, 'Pasir Urug', 'M3', 120000, 80)"
                );

                // Batch 3: Besi & Baja (ID_Distributor = 3)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(301, 3, 'Besi Beton 10mm', 'Batang', 110000, 500), " +
                    "(302, 3, 'Besi Beton 8mm', 'Batang', 65000, 600), " +
                    "(303, 3, 'Besi Beton 12mm', 'Batang', 150000, 400), " +
                    "(304, 3, 'Wiremesh M8', 'Lembar', 250000, 100), " +
                    "(305, 3, 'Plat Besi 1.2mm', 'Lembar', 450000, 45), " +
                    "(306, 3, 'Baja Ringan 0.75mm', 'Batang', 85000, 300), " +
                    "(307, 3, 'Besi Siku 40x40', 'Batang', 95000, 200), " +
                    "(308, 3, 'Besi Hollow 4x4', 'Batang', 120000, 150)"
                );

                // Batch 4: Kayu & Triplek (ID_Distributor = 2)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(401, 2, 'Kayu Balok 6x12', 'Batang', 85000, 200), " +
                    "(402, 2, 'Kayu Usuk 5/7', 'Batang', 45000, 300), " +
                    "(403, 2, 'Triplek 9mm', 'Lembar', 120000, 150), " +
                    "(404, 2, 'Multiplek 12mm', 'Lembar', 185000, 120), " +
                    "(405, 2, 'Papan Mahoni', 'Lembar', 95000, 180), " +
                    "(406, 2, 'Kayu Kaso 5x7', 'Batang', 42000, 250), " +
                    "(407, 2, 'Triplek 4mm', 'Lembar', 75000, 200), " +
                    "(408, 2, 'Kayu Meranti', 'Batang', 125000, 100)"
                );

                // Batch 5: Cat & Thinner (ID_Distributor = 4)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(501, 4, 'Cat Dulux Exterior', 'Kaleng', 175000, 200), " +
                    "(502, 4, 'Cat Nippon Paint', 'Kaleng', 160000, 180), " +
                    "(503, 4, 'Cat Tembok Avian', 'Kaleng', 145000, 220), " +
                    "(504, 4, 'Thinner A', 'Kaleng', 45000, 300), " +
                    "(505, 4, 'Meni Besi', 'Kaleng', 65000, 150), " +
                    "(506, 4, 'Cat Kayu Biovarnish', 'Kaleng', 95000, 120), " +
                    "(507, 4, 'Cat Dasar Alkali', 'Kaleng', 85000, 100), " +
                    "(508, 4, 'Cat Emulsi', 'Kaleng', 125000, 150)"
                );

                // Batch 6: Pipa & Sanitasi (ID_Distributor = 5)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(601, 5, 'Pipa PVC 3/4 inch', 'Batang', 45000, 400), " +
                    "(602, 5, 'Pipa PVC 1 inch', 'Batang', 65000, 350), " +
                    "(603, 5, 'Pipa Galvanis 1 inch', 'Batang', 125000, 200), " +
                    "(604, 5, 'Kloset Duduk', 'Unit', 350000, 50), " +
                    "(605, 5, 'Wastafel Keramik', 'Unit', 285000, 40), " +
                    "(606, 5, 'Pipa PVC 2 inch', 'Batang', 95000, 250), " +
                    "(607, 5, 'Elbow PVC 1 inch', 'Buah', 8000, 500), " +
                    "(608, 5, 'Tee PVC 1 inch', 'Buah', 12000, 400), " +
                    "(609, 5, 'Kran Air Kuningan', 'Buah', 75000, 150), " +
                    "(610, 5, 'Shower Mandi', 'Set', 185000, 80)"
                );

                // Batch 7: Alat Tukang (ID_Distributor = 3)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(701, 3, 'Palu Besi', 'Buah', 45000, 200), " +
                    "(702, 3, 'Tang Kombinasi', 'Buah', 55000, 180), " +
                    "(703, 3, 'Gergaji Triplek', 'Buah', 35000, 250), " +
                    "(704, 3, 'Meteran 5m', 'Buah', 25000, 300), " +
                    "(705, 3, 'Sekop', 'Buah', 40000, 150), " +
                    "(706, 3, 'Cangkul', 'Buah', 55000, 120), " +
                    "(707, 3, 'Pahat Besi', 'Buah', 38000, 180), " +
                    "(708, 3, 'Obeng Set', 'Set', 65000, 200), " +
                    "(709, 3, 'Kunci Inggris 10 inch', 'Buah', 85000, 100), " +
                    "(710, 3, 'Gerinda Tangan', 'Unit', 450000, 50)"
                );

                // Batch 8: Listrik & Lampu (ID_Distributor = 4)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(801, 4, 'Kabel NYM 2.5mm', 'Roll', 185000, 100), " +
                    "(802, 4, 'Lampu LED 18W', 'Buah', 35000, 500), " +
                    "(803, 4, 'Saklar Ganda', 'Buah', 25000, 400), " +
                    "(804, 4, 'Stop Kontak', 'Buah', 20000, 450), " +
                    "(805, 4, 'MCB 6A', 'Buah', 45000, 200), " +
                    "(806, 4, 'Kabel NYA 2.5mm', 'Roll', 125000, 150), " +
                    "(807, 4, 'Lampu LED 12W', 'Buah', 28000, 600), " +
                    "(808, 4, 'Fitting Lampu', 'Buah', 8000, 800), " +
                    "(809, 4, 'MCB 10A', 'Buah', 55000, 180), " +
                    "(810, 4, 'Isolasi Listrik', 'Buah', 5000, 1000)"
                );

                // Batch 9: Seng & Atap (ID_Distributor = 3)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(901, 3, 'Seng Gelombang 180cm', 'Lembar', 85000, 200), " +
                    "(902, 3, 'Genteng Beton', 'Buah', 8000, 2000), " +
                    "(903, 3, 'Genteng Keramik', 'Buah', 12000, 1500), " +
                    "(904, 3, 'Asbes Gelombang', 'Lembar', 95000, 150), " +
                    "(905, 3, 'Nok Genteng', 'Buah', 15000, 500)"
                );

                // Batch 10: Paku & Sekrup (ID_Distributor = 3)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(1001, 3, 'Paku 2 inch', 'Kg', 18000, 500), " +
                    "(1002, 3, 'Paku 3 inch', 'Kg', 20000, 450), " +
                    "(1003, 3, 'Paku 5 inch', 'Kg', 25000, 400), " +
                    "(1004, 3, 'Sekrup 3cm', 'Kg', 35000, 300), " +
                    "(1005, 3, 'Baut M10', 'Buah', 2000, 2000), " +
                    "(1006, 3, 'Mur M10', 'Buah', 1500, 2500)"
                );

                // Batch 11: Keramik & Granit (ID_Distributor = 1)
                stmt.executeUpdate(
                    "INSERT INTO barang (ID_Barang, ID_Distributor, Nama_Barang, Satuan, Harga, Stok) VALUES " +
                    "(1101, 1, 'Keramik 40x40', 'Dus', 185000, 150), " +
                    "(1102, 1, 'Keramik 60x60', 'Dus', 325000, 100), " +
                    "(1103, 1, 'Granit 60x60', 'Dus', 485000, 80), " +
                    "(1104, 1, 'Nat Keramik', 'Kg', 25000, 200), " +
                    "(1105, 1, 'Lem Keramik', 'Sak', 65000, 150)"
                );
                
                System.out.println("✅ Barang: 70+ item toko bangunan");
            }

            System.out.println("\n=== DATABASE SETUP COMPLETE ===");
            System.out.println("🎉 Semua tabel dan data sample siap digunakan!");

        } catch (SQLException e) {
            System.err.println("❌ Error inisialisasi database:");
            e.printStackTrace();
        }
    }

    // Method untuk menutup koneksi
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("✅ Koneksi ditutup");
                con = null;
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal menutup koneksi: " + e.getMessage());
        }
    }
}