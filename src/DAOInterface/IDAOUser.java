/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOInterface;

import Model.Distributor;
import Model.User;
import java.util.List;

/**
 *
 * @author ACER
 */
public interface IDAOUser {
    //read
    public List<User> getAll();
    //insert
    public boolean insert(User u);
    //update
    public void update(User u);
    //delete
    public void delete(int ID_User); 
    //search
    public List<User> getAllByName(String Password);
}
