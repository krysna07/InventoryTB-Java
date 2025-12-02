/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

//tegar
import DAO.DAOBarang;
import Model.Barang;
import Model.TabelModelBarang;
import View.FormBarang;
import java.util.List;
import DAOInterface.IDAOBarang;
import Model.Distributor;
import javax.swing.JOptionPane;


/**
 *
 * @author kumil
 */
public class ControllerBarang {
    public ControllerBarang(FormBarang frmBarang)
    {
        this.frmBarang = frmBarang;
        iBarang = new DAOBarang();
        iBarang.getAllByID_Distributor();
    }
    
    public void isiTable()
    {
        lstBrg = iBarang.getAll();
        TabelModelBarang tabelBrg = new TabelModelBarang(lstBrg);
        frmBarang.getTabelData().setModel(tabelBrg);
    }
    
    public void Insert()
    {
         Barang b = new Barang();
         b.setID_Barang(Integer.parseInt(frmBarang.gettxtID_Barang().getText()));
         b.setID_Distributor(Integer.parseInt((String) frmBarang.gettxtID_Distributor().getSelectedItem()));
         b.setNama_Barang(frmBarang.gettxtNama_Barang().getText());
         b.setSatuan(frmBarang.gettxtSatuan().getText());
         b.setHarga(Integer.parseInt(frmBarang.gettxtHarga().getText()));
         b.setStok(Integer.parseInt(frmBarang.gettxtStok().getText()));
         boolean res = iBarang.insert(b);
         if(res)
             JOptionPane.showMessageDialog(null, "Input berhasil");
         else
             JOptionPane.showMessageDialog(null, "Gagal/Data Duplikat");

    }
    
    public void reset()
    {
        if(!frmBarang.gettxtID_Barang().isEnabled())
            frmBarang.gettxtID_Barang().setEnabled(true);
        frmBarang.gettxtID_Barang().setText("");
        frmBarang.gettxtID_Distributor().setSelectedItem("");
        frmBarang.gettxtNama_Barang().setText("");
        frmBarang.gettxtSatuan().setText("");
        frmBarang.gettxtHarga().setText("");
        frmBarang.gettxtStok().setText("");
    }
    
    public void isiField(int row)
    {
        frmBarang.gettxtID_Barang().setEnabled(false);
        frmBarang.gettxtID_Barang().setText(lstBrg.get(row).getID_Barang().toString());
        frmBarang.gettxtID_Distributor().setSelectedItem(lstBrg.get(row).getID_Distributor().toString());
        frmBarang.gettxtNama_Barang().setText(lstBrg.get(row).getNama_Barang());
        frmBarang.gettxtSatuan().setText(lstBrg.get(row).getSatuan());
        frmBarang.gettxtHarga().setText(lstBrg.get(row).getHarga().toString());
        frmBarang.gettxtStok().setText(lstBrg.get(row).getStok().toString());
    }
    
    public void update()
    {
         Barang b = new Barang();
         b.setNama_Barang(frmBarang.gettxtNama_Barang().getText());
         b.setSatuan(frmBarang.gettxtSatuan().getText());
         b.setHarga(Integer.parseInt(frmBarang.gettxtHarga().getText()));
         b.setStok(Integer.parseInt(frmBarang.gettxtStok().getText()));
         b.setID_Barang(Integer.parseInt(frmBarang.gettxtID_Barang().getText()));
         b.setID_Distributor(Integer.parseInt((String) frmBarang.gettxtID_Distributor().getSelectedItem()));
         iBarang.update(b);
         JOptionPane.showMessageDialog(null, "Update berhasil");
    }
    
    public void delete()
    {
         iBarang.delete(Integer.parseInt(frmBarang.gettxtID_Barang().getText()));
         JOptionPane.showMessageDialog(null, "Delete berhasil");
    }
    
    public void search()
    {
        lstBrg = iBarang.getAllByName(frmBarang.gettxtCariNama().getText());
        TabelModelBarang tabelBrg = new TabelModelBarang(lstBrg);
        frmBarang.getTabelData().setModel(tabelBrg);
    }
    
    public void JCOMBOID_DIS()
    {
        List<Distributor> lstDis = iBarang.getAllByID_Distributor();
        frmBarang.gettxtID_Distributor().removeAllItems();
        for (Distributor distributor : lstDis) {
            frmBarang.gettxtID_Distributor().addItem(String.valueOf(distributor.getID_Distributor()));
        }
    }
            
    FormBarang frmBarang;
    IDAOBarang iBarang;
    List<Barang> lstBrg;
    
}
