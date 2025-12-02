/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOInterface;

import Model.Distributor;
import java.util.List;

/**
 *
 * @author kumil
 */
public interface IDAODistributor {
    //read
    public List<Distributor> getAll();
    //insert
    public boolean insert(Distributor b);
    //update
    public void update(Distributor b);
    //delete
    public void delete(int ID_Distributor); 
    //search
    public List<Distributor> getAllByName(String Nama_Distributor);
}
