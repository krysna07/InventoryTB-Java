package Model;

public class Order {
    private int ID_Order;
    private String Nama_Barang;
    private int Jumlah;
    private int Harga;
    
    // Constructor
    public Order() {}
    
    public Order(int idOrder, String namaBarang, int jumlah, int harga) {
        this.ID_Order = idOrder;
        this.Nama_Barang = namaBarang;
        this.Jumlah = jumlah;
        this.Harga = harga;
    }
    
    // Getters and Setters
    public int getID_Order() {
        return ID_Order;
    }
    
    public void setID_Order(int ID_Order) {
        this.ID_Order = ID_Order;
    }
    
    public String getNama_Barang() {
        return Nama_Barang;
    }
    
    public void setNama_Barang(String Nama_Barang) {
        this.Nama_Barang = Nama_Barang;
    }
    
    public int getJumlah() {
        return Jumlah;
    }
    
    public void setJumlah(int Jumlah) {
        this.Jumlah = Jumlah;
    }
    
    public int getHarga() {
        return Harga;
    }
    
    public void setHarga(int Harga) {
        this.Harga = Harga;
    }
    
    public int getSubtotal() {
        return Jumlah * Harga;
    }
    
    @Override
    public String toString() {
        return String.format("Order[ID=%d, Barang=%s, Jumlah=%d, Harga=%,d, Subtotal=%,d]",
            ID_Order, Nama_Barang, Jumlah, Harga, getSubtotal());
    }
}