/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOInterface;

import Model.Barang;
import Model.Distributor;
import java.util.List;

/**
 *
 * @author kumil
 */
public interface IDAOBarang {
    //read
    public List<Barang> getAll();
    //combobox ID
    public List<Distributor> getAllByID_Distributor();
    //insert
    public boolean insert(Barang b);
    //update
    public void update(Barang b);
    //delete
    public void delete(int ID_Barang); 
    //search
    public List<Barang> getAllByName(String Nama_Barang);
}
