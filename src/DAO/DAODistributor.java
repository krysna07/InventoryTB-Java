/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Helper.KoneksiDB;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.sql.Connection;
import DAOInterface.IDAODistributor;
import Model.Distributor;

public class DAODistributor implements IDAODistributor {
    
    private Connection con;
    
    // ✅ FIXED: Konsisten dengan struktur database (VARCHAR)
    private static final String SQL_READ = 
        "SELECT ID_Distributor, Nama_Distributor, Alamat, No_Telepon " +
        "FROM distributor ORDER BY ID_Distributor ASC";
    
    private static final String SQL_INSERT = 
        "INSERT INTO distributor(ID_Distributor, Nama_Distributor, Alamat, No_Telepon) " +
        "VALUES(?,?,?,?)";
    
    private static final String SQL_UPDATE = 
        "UPDATE distributor SET Nama_Distributor=?, Alamat=?, No_Telepon=? " +
        "WHERE ID_Distributor=?";
    
    private static final String SQL_DELETE = 
        "DELETE FROM distributor WHERE ID_Distributor=?";
    
    private static final String SQL_SEARCH = 
        "SELECT ID_Distributor, Nama_Distributor, Alamat, No_Telepon " +
        "FROM distributor WHERE Nama_Distributor LIKE ? " +
        "ORDER BY ID_Distributor ASC";
    
    public DAODistributor() {
        con = KoneksiDB.getConnection();
    }
    
    @Override
    public List<Distributor> getAll() {
        // ✅ FIXED: Initialize list dulu, bukan null
        List<Distributor> lstDis = new ArrayList<>();
        
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(SQL_READ)) {
            
            System.out.println("\n=== [DAO DISTRIBUTOR] LOADING DATA ===");
            
            while (rs.next()) {
                Distributor dis = new Distributor();
                dis.setID_Distributor(rs.getInt("ID_Distributor"));
                dis.setNama_Distributor(rs.getString("Nama_Distributor"));
                dis.setAlamat(rs.getString("Alamat"));
                
                // ✅ FIXED: Ambil sebagai String (bukan Int)
                dis.setNo_Telepon(rs.getString("No_Telepon"));
                
                lstDis.add(dis);
                
                System.out.printf("  - ID: %d | %s | %s%n",
                    dis.getID_Distributor(),
                    dis.getNama_Distributor(),
                    dis.getNo_Telepon());
            }
            
            System.out.println("Total distributor loaded: " + lstDis.size());
            
        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getAll]: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lstDis;
    }

    @Override
    public boolean insert(Distributor b) {
        try (PreparedStatement statement = con.prepareStatement(SQL_INSERT)) {
            
            statement.setInt(1, b.getID_Distributor());
            statement.setString(2, b.getNama_Distributor());
            statement.setString(3, b.getAlamat());
            
            // ✅ FIXED: Set sebagai String (bukan Int)
            statement.setString(4, b.getNo_Telepon());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.printf("[DAO] ✅ Distributor berhasil disimpan: %s%n", 
                    b.getNama_Distributor());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR DAO.insert]: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
   
    @Override
    public void update(Distributor b) {
        try (PreparedStatement statement = con.prepareStatement(SQL_UPDATE)) {
            
            statement.setString(1, b.getNama_Distributor());
            statement.setString(2, b.getAlamat());
            
            // ✅ FIXED: Set sebagai String
            statement.setString(3, b.getNo_Telepon());
            statement.setInt(4, b.getID_Distributor());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.printf("[DAO] ✅ Distributor updated: ID %d%n", 
                    b.getID_Distributor());
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR DAO.update]: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void delete(int ID_Distributor) {
        try (PreparedStatement statement = con.prepareStatement(SQL_DELETE)) {
            
            statement.setInt(1, ID_Distributor);
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                System.out.printf("[DAO] ✅ Distributor deleted: ID %d%n", ID_Distributor);
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR DAO.delete]: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<Distributor> getAllByName(String Nama_Distributor) {
        // ✅ FIXED: Initialize list dulu
        List<Distributor> lstDis = new ArrayList<>();
        
        try (PreparedStatement st = con.prepareStatement(SQL_SEARCH)) {
            
            st.setString(1, "%" + Nama_Distributor + "%");
            
            try (ResultSet rs = st.executeQuery()) {
                System.out.println("\n=== [DAO] SEARCH DISTRIBUTOR: " + Nama_Distributor + " ===");
                
                while (rs.next()) {
                    Distributor dis = new Distributor();
                    dis.setID_Distributor(rs.getInt("ID_Distributor"));
                    dis.setNama_Distributor(rs.getString("Nama_Distributor"));
                    dis.setAlamat(rs.getString("Alamat"));
                    
                    // ✅ FIXED: Konsisten String
                    dis.setNo_Telepon(rs.getString("No_Telepon"));
                    
                    lstDis.add(dis);
                }
                
                System.out.println("Found " + lstDis.size() + " distributor(s)");
            }
            
        } catch (SQLException e) {
            System.err.println("[ERROR DAO.getAllByName]: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lstDis;
    }
}