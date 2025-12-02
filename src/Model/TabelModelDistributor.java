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
public class TabelModelDistributor extends AbstractTableModel{
    
    public TabelModelDistributor(List<Distributor> lstDis)
    {
        this.lstDis = lstDis;
    }
    @Override
    public int getRowCount() {
        return this.lstDis.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }
    
    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return "ID_Distributor";
            case 1:
                return "Nama_Distributor";
            case 2:
                return "Alamat";
            case 3:
                return "No_Telepon";
            default:
                return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex){
            case 0:
                return lstDis.get(rowIndex).getID_Distributor();
            case 1:
                return lstDis.get(rowIndex).getNama_Distributor();
            case 2:
                return lstDis.get(rowIndex).getAlamat();
            case 3:
                return lstDis.get(rowIndex).getNo_Telepon();
            default:
                return null;
        }
    }
    
    List<Distributor> lstDis;
}
