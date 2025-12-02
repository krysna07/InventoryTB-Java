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
import DAOInterface.IDAOBarang;
import Model.Distributor;

/**
 *
 * @author kumil
 */
public class DAOBarang implements IDAOBarang{
    
    public DAOBarang()
    {
        con = KoneksiDB.getConnection();
    }

    @Override
    public List<Barang> getAll() {
        List<Barang> lstBrg = null;
        try
        {
            lstBrg = new ArrayList<Barang>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strRead);
            while(rs.next())
            {
                Barang brg = new Barang();
                brg.setID_Barang(rs.getInt("ID_Barang"));
                brg.setID_Distributor(rs.getInt("ID_Distributor"));
                brg.setNama_Barang(rs.getString("Nama_Barang"));
                brg.setSatuan(rs.getString("Satuan"));
                brg.setHarga(rs.getInt("Harga"));
                brg.setStok(rs.getInt("Stok"));
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
    public boolean insert(Barang b) {
        boolean result = true;
        PreparedStatement statement = null;
        try
        {
            //statement = con.prepareStatement(strInsertID_Distributor);

            statement = con.prepareStatement(strInsert);
            statement.setInt(1 , b.getID_Barang());
            statement.setInt(2 , b.getID_Distributor());
            statement.setString(3 , b.getNama_Barang());
            statement.setString(4 , b.getSatuan());
            statement.setInt(5 , b.getHarga());
            statement.setInt(6 , b.getStok());
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
                System.out.println("gagal inputt");
                result = false;
            }
        }
        return result;
    }
   
    @Override
    public void update(Barang b) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strUpdate);
            statement.setString(1 , b.getNama_Barang());
            statement.setString(2 , b.getSatuan());
            statement.setInt(3 , b.getHarga());
            statement.setInt(4 , b.getStok());
            statement.setInt(5 , b.getID_Barang());
            statement.setInt(6 , b.getID_Distributor());
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
    public void delete(int ID_Barang) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strDelete);
            statement.setInt(1 , ID_Barang);
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
    public List<Barang> getAllByName(String Nama_Barang) {
        List<Barang> lstBrg = null;
        try
        {
            lstBrg = new ArrayList<Barang>();
            PreparedStatement st = con.prepareStatement(strSearch);
            st.setString(1 , "%"+Nama_Barang+"%");
            ResultSet rs = st.executeQuery() ;
            while(rs.next())
            {
                Barang brg = new Barang();
                brg.setID_Barang(rs.getInt("ID_Barang"));
                brg.setID_Distributor(rs.getInt("ID_Distributor"));
                brg.setNama_Barang(rs.getString("Nama_Barang"));
                brg.setSatuan(rs.getString("Satuan"));
                brg.setHarga(rs.getInt("Harga"));
                brg.setStok(rs.getInt("Stok"));
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
    public List<Distributor> getAllByID_Distributor() {
        List<Distributor> lstDis = null;
        try
        {
            lstDis = new ArrayList<Distributor>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strInsertID_Distributor) ;
            while(rs.next())
            {
                Distributor dis = new Distributor();
                dis.setID_Distributor(rs.getInt("ID_Distributor"));
                lstDis.add(dis);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error");
        }
        return lstDis;
    }

        Connection con;
    //SQL Query
    String strRead = "select * from barang order by ID_Barang asc;";
    String strInsert = "insert into barang(ID_Barang,ID_Distributor, Nama_Barang, Satuan, Harga, Stok) values(?,?,?,?,?,?);";
    String strUpdate = "update barang set Nama_Barang=?,Satuan=?, Harga=?, Stok=? where ID_Barang=? and ID_Distributor=?";
    String strDelete = "delete from barang where ID_Barang=?";
    String strSearch = "select * from barang where Nama_Barang like ?;";
    String strInsertID_Distributor = "select ID_Distributor from distributor;";

}
