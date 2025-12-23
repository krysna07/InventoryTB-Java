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
                System.out.println("Koneksi Berhasil ke database inventory_tb");

                // Buat semua tabel jika belum ada
                createTablesIfNotExists();

            } catch (SQLException e) {
                System.out.println("Koneksi Gagal");
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

        } catch (SQLException e) {
            System.err.println("Gagal membuat database: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (tempConn != null) {
                    tempConn.close();
                }
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    private static void createTablesIfNotExists() {
        try (Statement stmt = con.createStatement()) {

            // 1. Tabel distributor
            String createDistributor
                    = "CREATE TABLE IF NOT EXISTS distributor ("
                    + "ID_Distributor INT NOT NULL,"
                    + "Nama_Distributor VARCHAR(100) NOT NULL,"
                    + "Alamat TEXT,"
                    + "No_Telepon VARCHAR(15),"
                    + "PRIMARY KEY (ID_Distributor)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createDistributor);

            // 2. Tabel barang
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

            // 3. Tabel pesan (transaksi)
            String createPesan
                    = "CREATE TABLE IF NOT EXISTS pesan ("
                    + "ID_Order INT NOT NULL,"
                    + "Nama_Barang VARCHAR(100),"
                    + "Jumlah INT,"
                    + "Harga INT DEFAULT 0,"
                    + "Tanggal_Pesan TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (ID_Order)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createPesan);

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

            // 5. Tabel kategori
            String createKategori
                    = "CREATE TABLE IF NOT EXISTS kategori ("
                    + "ID_Kategori INT NOT NULL AUTO_INCREMENT,"
                    + "Nama_Kategori VARCHAR(50),"
                    + "PRIMARY KEY (ID_Kategori)"
                    + ") ENGINE=InnoDB";
            stmt.executeUpdate(createKategori);

            // ===============================
            // INSERT DATA DEFAULT
            // ===============================
            ResultSet rs;

            // User default
            rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate(
                        "INSERT INTO users (username, password, nama_lengkap, level) VALUES "
                        + "('admin', MD5('admin123'), 'Pemilik Toko', 'admin'),"
                        + "('kasir', MD5('kasir123'), 'Kasir Toko', 'user'),"
                        + "('gudang', MD5('gudang123'), 'Staff Gudang', 'user')"
                );
            }

            // Kategori default
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
                        + "('Listrik & Lampu')"
                );
            }

            // Distributor default
            rs = stmt.executeQuery("SELECT COUNT(*) FROM distributor");
            rs.next();
            if (rs.getInt(1) == 0) {
                stmt.executeUpdate(
                        "INSERT INTO distributor VALUES "
                        + "(1,'PT. Semen Tiga Roda','Jl. Industri No.1','021-87654321'),"
                        + "(2,'PT. Kayu Jati Mas','Jepara','0291-123456'),"
                        + "(3,'CV. Besi Baja Makmur','Cilegon','0254-789012'),"
                        + "(4,'UD. Cat Warna Indah','Tangerang','021-5551234'),"
                        + "(5,'PT. Pipa Sejahtera','Surabaya','031-4567890')"
                );
            }

            System.out.println("Semua tabel & data default berhasil disiapkan.");

        } catch (SQLException e) {
            System.err.println("Error inisialisasi database:");
            e.printStackTrace();
        }
    }

    // Method untuk menutup koneksi
    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Koneksi ditutup");
                con = null;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menutup koneksi: " + e.getMessage());
        }
    }
}
