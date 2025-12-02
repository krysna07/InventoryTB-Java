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

/**
 *
 * @author kumil
 */
public class DAODistributor implements IDAODistributor{
    
    public DAODistributor()
    {
        con = KoneksiDB.getConnection();
    }
    
    @Override
    public List<Distributor> getAll() {
        List<Distributor> lstDis = null;
        try
        {
            lstDis = new ArrayList<Distributor>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(strRead);
            while(rs.next())
            {
                Distributor dis = new Distributor();
                dis.setID_Distributor(rs.getInt("ID_Distributor"));
                dis.setNama_Distributor(rs.getString("Nama_Distributor"));
                dis.setAlamat(rs.getString("Alamat"));
                dis.setNo_Telepon(rs.getInt("No_Telepon"));
                lstDis.add(dis);
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error");
        }
        return lstDis;
    }

    @Override
    public boolean insert(Distributor b) {
        boolean result = true;
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strInsert);
            statement.setInt(1 , b.getID_Distributor());
            statement.setString(2 , b.getNama_Distributor());
            statement.setString(3 , b.getAlamat());
            statement.setInt(4 , b.getNo_Telepon());
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
    public void update(Distributor b) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strUpdate);
            statement.setString(1 , b.getNama_Distributor());
            statement.setString(2 , b.getAlamat());
            statement.setInt(3 , b.getNo_Telepon());
            statement.setInt(4 , b.getID_Distributor());
            statement.executeUpdate();
        }catch(SQLException e)
        {
            System.out.println("gagal update");

        }
        finally        {
            try {
                statement.close();
            } catch (SQLException x) {
                System.out.println("gagal updatee");
            }
        }
    }
    
    @Override
    public void delete(int ID_Distributor) {
        PreparedStatement statement = null;
        try
        {
            statement = con.prepareStatement(strDelete);
            statement.setInt(1 , ID_Distributor);
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
    public List<Distributor> getAllByName(String Nama_Distributor) {
        List<Distributor> lstDis = null;
        try
        {
            lstDis = new ArrayList<Distributor>();
            PreparedStatement st = con.prepareStatement(strSearch);
            st.setString(1 , "%"+Nama_Distributor+"%");
            ResultSet rs = st.executeQuery() ;
            while(rs.next())
            {
                Distributor dis = new Distributor();
                dis.setID_Distributor(rs.getInt("ID_Distributor"));
                dis.setNama_Distributor(rs.getString("Nama_Distributor"));
                dis.setAlamat(rs.getString("Alamat"));
                dis.setNo_Telepon(rs.getInt("No_telepon"));
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
    String strRead = "select * from distributor order by ID_Distributor asc;";
    String strInsert = "insert into distributor(ID_Distributor, Nama_Distributor, Alamat, No_Telepon) values(?,?,?,?);";
    String strUpdate = "update distributor set Nama_Distributor=?, Alamat=?, No_Telepon=? where ID_Distributor=?";
    String strDelete = "delete from distributor where ID_Distributor=?";
    String strSearch = "select * from distributor where Nama_Distributor like ?;";

}
