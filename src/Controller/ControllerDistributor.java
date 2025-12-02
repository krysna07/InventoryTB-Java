/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

//tegar
import DAO.DAODistributor;
import java.util.List;
import DAOInterface.IDAODistributor;
import Model.Distributor;
import Model.TabelModelDistributor;
import View.FormDistributor;
import javax.swing.JOptionPane;


/**
 *
 * @author kumil
 */
public class ControllerDistributor {
    
    public ControllerDistributor(FormDistributor frmDistributor)
    {
        this.frmDistributor = frmDistributor;
        iDistributor = new DAODistributor();
    }
    
    public void isiTable()
    {
        lstDis = iDistributor.getAll();
        TabelModelDistributor tabelDis = new TabelModelDistributor(lstDis);
        frmDistributor.getTabelData().setModel(tabelDis);
    }
    
    public void Insert()
    {
         Distributor b = new Distributor();
         b.setID_Distributor(Integer.parseInt(frmDistributor.gettxtID_Distributor().getText()));
         b.setNama_Distributor(frmDistributor.gettxtNama_Distributor().getText());
         b.setAlamat(frmDistributor.gettxtAlamat().getText());
         b.setNo_Telepon(Integer.parseInt(frmDistributor.gettxtNo_Telepon().getText()));
         boolean res = iDistributor.insert(b);
         if(res)
             JOptionPane.showMessageDialog(null, "Input berhasil");
         else
             JOptionPane.showMessageDialog(null, "Gagal/Data Duplikat");

    }
    
    public void reset()
    {
        if(!frmDistributor.gettxtID_Distributor().isEnabled())
            frmDistributor.gettxtID_Distributor().setEnabled(true);
        frmDistributor.gettxtID_Distributor().setText("");
        frmDistributor.gettxtNama_Distributor().setText("");
        frmDistributor.gettxtAlamat().setText("");
        frmDistributor.gettxtNo_Telepon().setText("");
    }
    
    public void isiField(int row)
    {
        frmDistributor.gettxtID_Distributor().setEnabled(false);
        frmDistributor.gettxtID_Distributor().setText(lstDis.get(row).getID_Distributor().toString());
        frmDistributor.gettxtNama_Distributor().setText(lstDis.get(row).getNama_Distributor());
        frmDistributor.gettxtAlamat().setText(lstDis.get(row).getAlamat());
        frmDistributor.gettxtNo_Telepon().setText(lstDis.get(row).getNo_Telepon().toString());
    }
    
    public void update()
    {
         Distributor b = new Distributor();
         b.setNama_Distributor(frmDistributor.gettxtNama_Distributor().getText());
         b.setAlamat(frmDistributor.gettxtAlamat().getText());
         b.setNo_Telepon(Integer.parseInt(frmDistributor.gettxtNo_Telepon().getText()));
         b.setID_Distributor(Integer.parseInt(frmDistributor.gettxtID_Distributor().getText()));
         iDistributor.update(b);
         JOptionPane.showMessageDialog(null, "Update berhasil");
    }
    
    public void delete()
    {
         iDistributor.delete(Integer.parseInt(frmDistributor.gettxtID_Distributor().getText()));
         JOptionPane.showMessageDialog(null, "Delete berhasil");
    }
    
    public void search()
    {
        lstDis = iDistributor.getAllByName(frmDistributor.gettxtCariNama().getText());
        TabelModelDistributor tabelDis = new TabelModelDistributor(lstDis);
        frmDistributor.getTabelData().setModel(tabelDis);
    }
    
    FormDistributor frmDistributor;
    IDAODistributor iDistributor;
    List<Distributor> lstDis;

}
