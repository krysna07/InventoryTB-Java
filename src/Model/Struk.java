// Model/Struk.java
package Model;

import java.util.Date;
import java.util.List;

public class Struk {
    private final String idStruk;
    private final List<Order> items;
    private int total;
    private final Date tanggal;
    
    // Constructor, getter, setter
    public Struk(List<Order> items) {
        this.items = items;
        this.tanggal = new Date();
        this.idStruk = "STR" + System.currentTimeMillis();
        calculateTotal();
    }
    
    private void calculateTotal() {
        this.total = items.stream()
            .mapToInt(order -> order.getJumlah() * order.getHarga())
            .sum();
    }
    
    // Getters
    public String getIdStruk() { return idStruk; }
    public List<Order> getItems() { return items; }
    public int getTotal() { return total; }
    public Date getTanggal() { return tanggal; }
}