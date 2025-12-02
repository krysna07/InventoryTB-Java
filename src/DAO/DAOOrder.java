/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Helper.KoneksiDB;
import Model.Barang;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.sql.Connection;
import DAOInterface.IDAOOrder;
import Model.Order;

/**
 *
 * @author kumil
 */
public class DAOOrder implements IDAOOrder{
    
    public DAOOrder()
    {
        con = KoneksiDB.getConnection();
    }
    
        @Override
    public List<Order> getAll() {
        List<Order> lstOrd = null;
        try
        {
            lstOrd = new ArrayList<Order>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strRead);
            while(rs.next())
            {
                Order ord = new Order();
                ord.setID_Order(rs.getInt("ID_Order"));
                ord.setNama_Barang(rs.getString("Nama_Barang"));
                ord.setJumlah(rs.getInt("Jumlah"));
         
                lstOrd.add(ord);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error");
        }
        return lstOrd;
    }

    @Override
    public boolean insert(Order o) {
        boolean result = true;
        PreparedStatement statement = null;
        try
        {
            //statement = con.prepareStatement(strInsertID_Distributor);

            statement = con.prepareStatement(strInsert);
            statement.setInt(1 , o.getID_Order());
            statement.setString(2 , o.getNama_Barang());
            statement.setInt(3 , o.getJumlah());
            statement.execute();
            
        }catch(SQLException x)
        {
            System.out.println("gagal input");
            result = false;
        }
        finally
        {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("gagal input");
                result = false;
            }
        }
        return result;
    }

    @Override
    public void update(Order o) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strUpdate);
            statement.setInt(1 , o.getJumlah());
            statement.setString(2 , o.getNama_Barang());
            statement.setInt(3 , o.getID_Order());
            
            statement.executeUpdate();
        }catch(SQLException e)
        {
            System.out.println("gagal update");
        }
        finally
        {
            try {
                statement.close();
            } catch (SQLException x) {
                System.out.println("gagal updatee");
            }
        }
    }

    @Override
    public void delete(int ID_Order) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strDelete);
            statement.setInt(1 , ID_Order);
            statement.executeUpdate();
        }catch(SQLException x)
        {
            System.out.println("gagal delete");

        }
        finally
        {
            try {
                statement.close();
            } catch (SQLException e) {
                System.out.println("gagal deletee");
            }
        }
    }

    @Override
    public List<Order> getAllByName(String Nama_Barang) {
        List<Order> lstOrd = null;
        try
        {
            lstOrd = new ArrayList<Order>();
            PreparedStatement st = con.prepareStatement(strSearch);
            st.setString(1 , "%"+Nama_Barang+"%");
            ResultSet rs = st.executeQuery() ;
            while(rs.next())
            {
                Order ord = new Order();
                ord.setID_Order(rs.getInt("ID_Order"));
                ord.setNama_Barang(rs.getString("Nama_Barang"));
                ord.setJumlah(rs.getInt("Jumlah"));
                lstOrd.add(ord);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error");
        }
        return lstOrd;
    }
    
    @Override
    public List<Barang> getAllByNama_Barang() {
        List<Barang> lstBrg = null;
            try
            {
                lstBrg = new ArrayList<Barang>();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(strInsertNama_Barang) ;
                while(rs.next())
                {
                    Barang brg = new Barang();
                    brg.setNama_Barang(rs.getString("Nama_Barang"));
                    lstBrg.add(brg);
                }
            }
            catch(SQLException e)
            {
                System.err.println("Error");
            }
            return lstBrg;    
    }
    
    @Override
    public void kurangiStok(String Nama_Barang, int jumlah) {
        PreparedStatement statement = null;
        try {
            // Update query untuk mengurangi stok barang berdasarkan nama barang
            statement = con.prepareStatement(strUpdateStok);
            statement.setInt(1, jumlah);
            statement.setString(2, Nama_Barang);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("gagal mengurangi stok");
        } finally {
            try {
                statement.close();
            } catch (SQLException x) {
                System.out.println("gagal mengurangi stok");
            }
        }
    }   

    Connection con;
    //SQL Query
    String strRead = "select * from pesan order by ID_Order asc;";
    String strInsert = "insert into pesan(ID_Order, Nama_Barang, Jumlah) values(?,?,?);";
    String strUpdate = "update pesan set Jumlah=?, Nama_Barang=? where ID_Order=?  ;";
    String strDelete = "delete from pesan where ID_Order=?";
    String strSearch = "select * from pesan where Nama_Barang like ?;";
    String strInsertNama_Barang = "select Nama_Barang from barang;";
    String strUpdateStok = "UPDATE barang SET stok = stok - ? WHERE nama_barang = ?;";

     

}