package Model;

/**
 * Kelas Model untuk merepresentasikan data Pengguna (User)
 * Menggunakan properti sesuai kolom di tabel 'users'.
 */
public class User {
    
    // Properti (Field)
    private int idUser;
    private String username;
    private String password;
    private String namaLengkap;
    private String level; 
    
    // --- Constructor Kosong ---
    public User() {
        // Digunakan saat membuat objek baru atau mengisi data dari database (DAO)
    }
    
    // --- Getter (Mengambil nilai) ---

    public int getIdUser() {
        return idUser;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public String getLevel() {
        return level;
    }

    // --- Setter (Mengatur/Mengisi nilai) ---

    public void setIdUser(int idUser) {
        // Menggunakan nama parameter yang sama dengan field
        this.idUser = idUser; 
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}