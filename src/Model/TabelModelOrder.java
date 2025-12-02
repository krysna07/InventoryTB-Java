package Model;


import javax.swing.table.AbstractTableModel;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kumil
 */
public class TabelModelOrder extends AbstractTableModel{ 
    
    public TabelModelOrder(List<Order> lstOdr)
    {
        this.lstOdr = lstOdr;
    }
    @Override
    public int getRowCount() {
        return this.lstOdr.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "ID_Order";
            case 1:
                return "Nama_Barang";
            
            case 2:
                return "Jumlah";
            default:
                return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return lstOdr.get(rowIndex).getID_Order();
            case 1:
                return lstOdr.get(rowIndex).getNama_Barang();
         
            case 2:
                return lstOdr.get(rowIndex).getJumlah();
            default:
                return null;
        }
    }
    
    List<Order> lstOdr;
}
