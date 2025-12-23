package Helper;

import Model.Order;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportStruk {

    public static void generateStruk(List<Order> orders, String idStruk) {

        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Data order kosong!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Struk Pembelian");
        chooser.setSelectedFile(new File("Struk_" + idStruk + ".pdf"));

        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String filePath = chooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".pdf")) {
            filePath += ".pdf";
        }

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ===== FONT (iText 2.1.7 style) =====
            Font fontTitle  = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font fontHeader = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font fontNormal = new Font(Font.HELVETICA, 10);
            Font fontSmall  = new Font(Font.HELVETICA, 9);

            // ===== HEADER =====
            Paragraph title = new Paragraph("TOKO BANGUNAN MAKMUR JAYA\n", fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph alamat = new Paragraph(
                    "Jl. Raya No. 123, Jakarta\nTelp: 021-1234567\n\n",
                    fontNormal
            );
            alamat.setAlignment(Element.ALIGN_CENTER);
            document.add(alamat);

            // ===== INFO STRUK =====
            Paragraph info = new Paragraph(
                    "No. Struk : " + idStruk + "\n" +
                    "Tanggal  : " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n" +
                    "Kasir    : Admin\n" +
                    "----------------------------------------\n\n",
                    fontNormal
            );
            document.add(info);

            // ===== DETAIL =====
            document.add(new Paragraph("DETAIL PEMBELIAN\n\n", fontHeader));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 4f, 2f, 3f});

            addHeaderCell(table, "No", fontHeader);
            addHeaderCell(table, "Nama Barang", fontHeader);
            addHeaderCell(table, "Jumlah", fontHeader);
            addHeaderCell(table, "Subtotal", fontHeader);

            int totalAll = 0;
            int no = 1;

            for (Order o : orders) {
                int subtotal = o.getJumlah() * o.getHarga();
                totalAll += subtotal;

                table.addCell(new Phrase(String.valueOf(no++), fontNormal));
                table.addCell(new Phrase(truncate(o.getNama_Barang(), 40), fontNormal));
                table.addCell(new Phrase(String.valueOf(o.getJumlah()), fontNormal));
                table.addCell(new Phrase("Rp " + formatRupiah(subtotal), fontNormal));
            }

            document.add(table);

            // ===== TOTAL =====
            document.add(new Paragraph("\n"));

            Paragraph total = new Paragraph(
                    "----------------------------------------\n" +
                    "Total Item  : " + orders.size() + "\n" +
                    "Total Bayar : Rp " + formatRupiah(totalAll) + "\n" +
                    "========================================\n\n",
                    fontHeader
            );
            document.add(total);

            // ===== FOOTER =====
            Paragraph footer = new Paragraph(
                    "Terima kasih telah berbelanja di Toko Kami\n",
                    fontSmall
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            JOptionPane.showMessageDialog(
                    null,
                    "Struk berhasil disimpan!\nTotal: Rp " + formatRupiah(totalAll),
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Gagal membuat PDF:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }
    }

    private static void addHeaderCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }

    public static String formatRupiah(int amount) {
        return String.format("%,d", amount).replace(',', '.');
    }
}
