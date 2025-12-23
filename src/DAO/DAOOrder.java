package DAO;

import Helper.KoneksiDB;
import Model.Barang;
import Model.Order;
import DAOInterface.IDAOOrder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAOOrder implements IDAOOrder {

    private Connection con;

    // SQL Query Constants
    private static final String GET_ALL_ORDERS_WITH_PRICE
            = "SELECT p.ID_Order, p.Nama_Barang, p.Jumlah, "
            + "COALESCE(p.Harga, b.Harga, 0) as Harga "
            + // ← Prioritas: harga di pesan > harga di barang > 0
            "FROM pesan p "
            + "LEFT JOIN barang b ON p.Nama_Barang = b.Nama_Barang "
            + "ORDER BY p.ID_Order ASC";
    private static final String INSERT_ORDER
            = "INSERT INTO pesan(ID_Order, Nama_Barang, Jumlah, Harga) VALUES(?,?,?,?)";

    private static final String UPDATE_ORDER
            = "UPDATE pesan SET Jumlah=?, Nama_Barang=? WHERE ID_Order=?";

    private static final String DELETE_ORDER
            = "DELETE FROM pesan WHERE ID_Order=?";

    private static final String SEARCH_ORDER_WITH_PRICE
            = "SELECT p.*, b.Harga FROM pesan p "
            + "LEFT JOIN barang b ON p.Nama_Barang = b.Nama_Barang "
            + "WHERE p.Nama_Barang LIKE ?";

    private static final String GET_ALL_BARANG_LENGKAP
            = "SELECT Nama_Barang, Harga, Stok, Satuan FROM barang ORDER BY Nama_Barang";

    private static final String UPDATE_STOK
            = "UPDATE barang SET stok = stok - ? WHERE nama_barang = ?";

    private static final String GET_HARGA_BY_NAMA
            = "SELECT Harga FROM barang WHERE Nama_Barang = ?";

    private static final String GET_BARANG_BY_NAMA
            = "SELECT Nama_Barang, Harga, Stok, Satuan FROM barang WHERE Nama_Barang = ?";

    public DAOOrder() {
        con = KoneksiDB.getConnection();
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_ORDERS_WITH_PRICE)) {

            System.out.println("\n=== [DAO] LOADING SEMUA ORDER DENGAN HARGA ===");

            while (rs.next()) {
                Order order = new Order();
                order.setID_Order(rs.getInt("ID_Order"));
                order.setNama_Barang(rs.getString("Nama_Barang"));
                order.setJumlah(rs.getInt("Jumlah"));

                // AMBIL HARGA DARI JOIN dengan barang
                int harga = rs.getInt("Harga");

                // Cek jika harga null (karena LEFT JOIN)
                if (rs.wasNull()) {
                    System.out.println("[WARNING] Harga NULL untuk: " + order.getNama_Barang());
                    harga = 0;
                }

                order.setHarga(harga);
                orders.add(order);

                System.out.printf("  - ID: %d | %-30s | Jumlah: %3d | Harga: Rp %,10d%n",
                        order.getID_Order(),
                        order.getNama_Barang(),
                        order.getJumlah(),
                        order.getHarga());
            }

            System.out.println("Total order loaded: " + orders.size());

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getAll]: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    @Override
    public boolean insert(Order order) {
        try (PreparedStatement stmt = con.prepareStatement(INSERT_ORDER)) {
            stmt.setInt(1, order.getID_Order());
            stmt.setString(2, order.getNama_Barang());
            stmt.setInt(3, order.getJumlah());
            stmt.setInt(4, order.getHarga());  // ← TAMBAHKAN INI

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("[DAO] Order berhasil disimpan: " + order.getNama_Barang()
                        + " | Harga: Rp " + order.getHarga());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.insert]: " + e.getMessage());
        }

        return false;
    }

    @Override
    public void update(Order order) {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_ORDER)) {
            stmt.setInt(1, order.getJumlah());
            stmt.setString(2, order.getNama_Barang());
            stmt.setInt(3, order.getID_Order());

            int affectedRows = stmt.executeUpdate();
            System.out.println("[DAO] Order updated. Affected rows: " + affectedRows);

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.update]: " + e.getMessage());
        }
    }

    @Override
    public void delete(int idOrder) {
        try (PreparedStatement stmt = con.prepareStatement(DELETE_ORDER)) {
            stmt.setInt(1, idOrder);

            int affectedRows = stmt.executeUpdate();
            System.out.println("[DAO] Order deleted. Affected rows: " + affectedRows);

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.delete]: " + e.getMessage());
        }
    }

    @Override
    public List<Order> getAllByName(String namaBarang) {
        List<Order> orders = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(SEARCH_ORDER_WITH_PRICE)) {
            stmt.setString(1, "%" + namaBarang + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n=== [DAO] SEARCH ORDER: " + namaBarang + " ===");

                while (rs.next()) {
                    Order order = new Order();
                    order.setID_Order(rs.getInt("ID_Order"));
                    order.setNama_Barang(rs.getString("Nama_Barang"));
                    order.setJumlah(rs.getInt("Jumlah"));

                    // Ambil harga dari JOIN
                    int harga = rs.getInt("Harga");
                    if (rs.wasNull()) {
                        harga = 0;
                    }
                    order.setHarga(harga);

                    orders.add(order);
                }

                System.out.println("Found " + orders.size() + " orders");
            }

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getAllByName]: " + e.getMessage());
        }

        return orders;
    }

    @Override
    public List<Barang> getAllByNama_Barang() {
        List<Barang> barangList = new ArrayList<>();

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_BARANG_LENGKAP)) {

            System.out.println("\n=== [DAO] LOADING SEMUA BARANG (UNTUK COMBOBOX) ===");

            while (rs.next()) {
                Barang barang = new Barang();
                barang.setNama_Barang(rs.getString("Nama_Barang"));
                barang.setHarga(rs.getInt("Harga"));
                barang.setStok(rs.getInt("Stok"));
                barang.setSatuan(rs.getString("Satuan"));

                barangList.add(barang);

                System.out.printf("  - %-30s | Harga: Rp %,10d | Stok: %4d %s%n",
                        barang.getNama_Barang(),
                        barang.getHarga(),
                        barang.getStok(),
                        barang.getSatuan());
            }

            System.out.println("Total barang: " + barangList.size());

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getAllByNama_Barang]: " + e.getMessage());
            e.printStackTrace();
        }

        return barangList;
    }

    @Override
    public Barang getBarangDenganHarga(String namaBarang) {
        try (PreparedStatement stmt = con.prepareStatement(GET_BARANG_BY_NAMA)) {
            stmt.setString(1, namaBarang);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Barang barang = new Barang();
                    barang.setNama_Barang(rs.getString("Nama_Barang"));
                    barang.setHarga(rs.getInt("Harga"));
                    barang.setStok(rs.getInt("Stok"));
                    barang.setSatuan(rs.getString("Satuan"));

                    System.out.println("[DAO] Barang ditemukan: " + barang.getNama_Barang()
                            + " | Harga: Rp " + barang.getHarga());

                    return barang;
                } else {
                    System.out.println("[DAO WARNING] Barang tidak ditemukan: " + namaBarang);
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getBarangDenganHarga]: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int getHargaByNama(String namaBarang) {
        try (PreparedStatement stmt = con.prepareStatement(GET_HARGA_BY_NAMA)) {
            stmt.setString(1, namaBarang);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int harga = rs.getInt("Harga");
                    System.out.println("[DAO] Harga ditemukan: " + namaBarang + " = Rp " + harga);
                    return harga;
                } else {
                    System.out.println("[DAO WARNING] Harga tidak ditemukan untuk: " + namaBarang);
                    return 0;
                }
            }

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getHargaByNama]: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void kurangiStok(String namaBarang, int jumlah) {
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_STOK)) {
            stmt.setInt(1, jumlah);
            stmt.setString(2, namaBarang);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("[DAO] Stok berkurang: " + namaBarang + " -" + jumlah + " unit");
            } else {
                System.out.println("[DAO WARNING] Gagal mengurangi stok. Barang tidak ditemukan: " + namaBarang);
            }

        } catch (SQLException e) {
            System.err.println("[ERROR DAO.kurangiStok]: " + e.getMessage());
        }
    }
}
