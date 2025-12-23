package Model;

public class Distributor {
    private Integer ID_Distributor;
    private String Nama_Distributor;
    private String Alamat;
    
    // ✅ FIXED: Ubah dari Integer ke String (sesuai database VARCHAR)
    private String No_Telepon;
    
    // Constructor
    public Distributor() {}
    
    public Distributor(Integer idDistributor, String namaDistributor, String alamat, String noTelepon) {
        this.ID_Distributor = idDistributor;
        this.Nama_Distributor = namaDistributor;
        this.Alamat = alamat;
        this.No_Telepon = noTelepon;
    }
    
    // Getters and Setters
    public Integer getID_Distributor() {
        return ID_Distributor;
    }
    
    public void setID_Distributor(Integer ID_Distributor) {
        this.ID_Distributor = ID_Distributor;
    }
    
    public String getNama_Distributor() {
        return Nama_Distributor;
    }
    
    public void setNama_Distributor(String Nama_Distributor) {
        this.Nama_Distributor = Nama_Distributor;
    }
    
    public String getAlamat() {
        return Alamat;
    }
    
    public void setAlamat(String Alamat) {
        this.Alamat = Alamat;
    }
    
    // ✅ FIXED: Getter/Setter untuk String (bukan Integer)
    public String getNo_Telepon() {
        return No_Telepon;
    }
    
    public void setNo_Telepon(String No_Telepon) {
        this.No_Telepon = No_Telepon;
    }
    
    @Override
    public String toString() {
        return String.format("Distributor[ID=%d, Nama=%s, Telepon=%s, Alamat=%s]",
            ID_Distributor, Nama_Distributor, No_Telepon, Alamat);
    }
}