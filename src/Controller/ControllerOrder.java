/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

//tegar
import DAO.DAOOrder;
import Model.Order;
import Model.TabelModelOrder;
import View.FormOrder;
import java.util.List;
import DAOInterface.IDAOOrder;
import Model.Barang;
import javax.swing.JOptionPane;


/**
 *
 * @author kumil
 */
public class ControllerOrder {
    public ControllerOrder(FormOrder frmOrder)
    {
        this.frmOrder = frmOrder;
        iOrder = new DAOOrder();
        iOrder.getAllByNama_Barang();
    }
    
    public void isiTable()
    {
        lstOrd = iOrder.getAll();
        TabelModelOrder tabelOrd = new TabelModelOrder(lstOrd);
        frmOrder.getTabelData().setModel(tabelOrd);
    }
    
    public void Insert()
    {
        Order o = new Order();
        o.setID_Order(Integer.parseInt(frmOrder.gettxtID_Order().getText()));
        o.setNama_Barang(frmOrder.gettxtNama_Barang().getSelectedItem().toString());
        o.setJumlah(Integer.valueOf(frmOrder.gettxtJumlah().getText()));
        
        boolean res = iOrder.insert(o);
        if (res) {
            JOptionPane.showMessageDialog(null, "Input berhasil");
            iOrder.kurangiStok(o.getNama_Barang(), o.getJumlah()); // Mengurangi stok barang
        } else {
            JOptionPane.showMessageDialog(null, "Gagal/Data Duplikat");
        }

    }
    
    public void reset()
    {
        if(!frmOrder.gettxtID_Order().isEnabled())
            frmOrder.gettxtID_Order().setEnabled(true);
        frmOrder.gettxtID_Order().setText("");
        frmOrder.gettxtNama_Barang().setSelectedItem("");
        frmOrder.gettxtJumlah().setText("");
    }
    
    public void isiField(int row)
    {
        frmOrder.gettxtID_Order().setEnabled(false);
        frmOrder.gettxtID_Order().setText(lstOrd.get(row).getID_Order().toString());
        frmOrder.gettxtNama_Barang().setSelectedItem(lstOrd.get(row).getNama_Barang());
        frmOrder.gettxtJumlah().setText(lstOrd.get(row).getJumlah().toString());

    }
    
    public void update()
    {
         Order o = new Order();
         o.setJumlah(Integer.parseInt(frmOrder.gettxtJumlah().getText()));
         o.setNama_Barang((String) frmOrder.gettxtNama_Barang().getSelectedItem());
         o.setID_Order(Integer.parseInt(frmOrder.gettxtID_Order().getText()));
         

         iOrder.update(o);
         JOptionPane.showMessageDialog(null, "Update berhasil");
    }
    
    public void delete()
    {
         iOrder.delete(Integer.parseInt(frmOrder.gettxtID_Order().getText()));
         JOptionPane.showMessageDialog(null, "Delete berhasil");
    }
    
    public void search()
    {
        lstOrd = iOrder.getAllByName(frmOrder.gettxtCariNama().getText());
        TabelModelOrder tabelOrd = new TabelModelOrder(lstOrd);
        frmOrder.getTabelData().setModel(tabelOrd);
    }
    
    public void JCOMBONama_BRG()
    {
        List<Barang> lstBrg = iOrder.getAllByNama_Barang();
        frmOrder.gettxtNama_Barang().removeAllItems();
        for (Barang barang : lstBrg) {
            frmOrder.gettxtNama_Barang().addItem(barang.getNama_Barang());
        }
    }


            
    FormOrder frmOrder;
    IDAOOrder iOrder;
    List<Order> lstOrd;
    
}