package Model;

public class Order {

    /**
     * @return the ID_Order
     */
    public Integer getID_Order() {
        return ID_Order;
    }

    /**
     * @param ID_Order the ID_Order to set
     */
    public void setID_Order(Integer ID_Order) {
        this.ID_Order = ID_Order;
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
     * @return the Jumlah
     */
    public Integer getJumlah() {
        return Jumlah;
    }

    /**
     * @param Jumlah the Jumlah to set
     */
    public void setJumlah(Integer Jumlah) {
        this.Jumlah = Jumlah;
    }

    private Integer ID_Order;
    private String Nama_Barang;
    private Integer Jumlah;
}
