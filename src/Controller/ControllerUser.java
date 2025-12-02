package Controller;

import DAO.DAOUser;
import DAOInterface.IDAOUser;
import Model.TabelModelUser;
import Model.User;
import View.FormUser;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Controller untuk mengelola interaksi antara FormUser, DAOUser, dan ModelUser.
 * @author ACER
 */
public class ControllerUser {
    
    FormUser frmUser;
    IDAOUser iUser;
    List<User> lstUser;
    
    // --- Constructor ---
    public ControllerUser(FormUser frmUser)
    {
        this.frmUser = frmUser;
        // KOREKSI: Pastikan DAOUser mengimplementasikan IDAOUser (IDAOUser iUser = new DAOUser())
        // Jika DAOUser tidak mengimplementasikan interface, ganti IDAOUser menjadi DAOUser di deklarasi.
        iUser = (IDAOUser) new DAOUser(); 
    }
    
    // --- Metode Tampil Data ---
    public void isiTable()
    {
        // KOREKSI: Asumsi metode di IDAOUser/DAOUser adalah getAll()
        lstUser = iUser.getAll(); 
        TabelModelUser tabelUser = new TabelModelUser(lstUser);
        frmUser.getTabelData().setModel(tabelUser);
    }
    
    // --- Metode Tambah Data ---
    public void Insert()
    {
         User u = new User();
         
         // PERHATIAN: ID_User biasanya AUTO INCREMENT, jadi tidak perlu di-set saat Insert.
         // Jika Anda tetap ingin mengaturnya, gunakan setIdUser:
         // u.setIdUser(Integer.parseInt(frmUser.gettxtID_User().getText())); 
         
         u.setUsername((frmUser.gettxtUsername().getText()));
         u.setPassword(frmUser.gettxtPassword().getText());
         
         // Asumsi DAOUser memiliki metode insert(User u)
         boolean res = iUser.insert(u);
         if(res)
             JOptionPane.showMessageDialog(null, "Input berhasil");
         else
             JOptionPane.showMessageDialog(null, "Gagal/Data Duplikat");
    }
    
    // --- Metode Reset Form ---
    public void reset()
    {
        if(!frmUser.gettxtID_User().isEnabled())
             frmUser.gettxtID_User().setEnabled(true);
         
        frmUser.gettxtID_User().setText("");
        frmUser.gettxtUsername().setText("");
        frmUser.gettxtPassword().setText("");
    }
    
    // --- Metode Isi Field dari Tabel ---
    public void isiField(int row)
    {
        frmUser.gettxtID_User().setEnabled(false);
        
        // KOREKSI: getID_User() GANTI ke getIdUser()
        // KOREKSI: getIdUser mengembalikan int, langsung konversi ke String
        frmUser.gettxtID_User().setText(String.valueOf(lstUser.get(row).getIdUser())); 
        
        frmUser.gettxtUsername().setText(lstUser.get(row).getUsername());
        frmUser.gettxtPassword().setText(lstUser.get(row).getPassword());
    }
    
    // --- Metode Update Data ---
    public void update() {
        User u = new User();
        
        // KOREKSI: setID_User() GANTI ke setIdUser()
        u.setIdUser(Integer.parseInt(frmUser.gettxtID_User().getText()));
        
        u.setUsername(frmUser.gettxtUsername().getText());
        u.setPassword(frmUser.gettxtPassword().getText());
        
        iUser.update(u);
        JOptionPane.showMessageDialog(null, "Update berhasil");
    }
    
    // --- Metode Delete Data ---
    public void delete()
    {
         iUser.delete(Integer.parseInt(frmUser.gettxtID_User().getText()));
         JOptionPane.showMessageDialog(null, "Delete berhasil");
    }
    
    // --- Metode Pencarian ---
    public void search()
    {
        // Asumsi DAOUser memiliki getAllByName()
        lstUser = iUser.getAllByName(frmUser.gettxtCariNama().getText());
        TabelModelUser tabelUser = new TabelModelUser(lstUser);
        frmUser.getTabelData().setModel(tabelUser);
    }
    
}