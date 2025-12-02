/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOInterface;

import Model.Order;
import Model.Barang;
import java.util.List;

/**
 *
 * @author kumil
 */
public interface IDAOOrder {
    //read
    public List<Order> getAll();
    //insert
    public boolean insert(Order o);
    //update
    public void update(Order o);
    //delete
    public void delete(int ID_Order); 
    //search
    public List<Order> getAllByName(String Nama_Barang);
    //Combobox Nama Barang
    public List<Barang> getAllByNama_Barang();
    //kurangstok
    public void kurangiStok(String Nama_Barang, int jumlah);
}
