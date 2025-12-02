package Model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author kumil
 */
public class Barang {

    /**
     * @return the ID_Barang
     */
    public Integer getID_Barang() {
        return ID_Barang;
    }

    /**
     * @param ID_Barang the ID_Barang to set
     */
    public void setID_Barang(Integer ID_Barang) {
        this.ID_Barang = ID_Barang;
    }

    /**
     * @return the ID_Distributor
     */
    public Integer getID_Distributor() {
        return ID_Distributor;
    }

    /**
     * @param ID_Distributor the ID_Distributor to set
     */
    public void setID_Distributor(Integer ID_Distributor) {
        this.ID_Distributor = ID_Distributor;
    }
    
    /**
     * @return the Nama_Barang
     */
    public String getNama_Barang() {
        return Nama_Barang;
    }

    /**
     * @param Nama_Barang the Nama_Barang to set
     */
    public void setNama_Barang(String Nama_Barang) {
        this.Nama_Barang = Nama_Barang;
    }

    /**
     * @return the Satuan
     */
    public String getSatuan() {
        return Satuan;
    }

    /**
     * @param Satuan the Satuan to set
     */
    public void setSatuan(String Satuan) {
        this.Satuan = Satuan;
    }

    /**
     * @return the Harga
     */
    public Integer getHarga() {
        return Harga;
    }

    /**
     * @param Harga the Harga to set
     */
    public void setHarga(Integer Harga) {
        this.Harga = Harga;
    }

    /**
     * @return the Stok
     */
    public Integer getStok() {
        return Stok;
    }

    /**
     * @param Stok the Stok to set
     */
    public void setStok(Integer Stok) {
        this.Stok = Stok;
    }

    private Integer ID_Barang;
    private Integer ID_Distributor;
    private String Nama_Barang;
    private String Satuan;
    private Integer Harga;
    private Integer Stok;
}
