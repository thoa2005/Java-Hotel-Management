package util;

import com.itextpdf.text.*;

import com.itextpdf.text.pdf.*;

import javafx.collections.ObservableList;
import model.HoaDon;

import java.io.FileOutputStream;
import java.util.List;

public class InvoiceExporter {
    public static boolean exportPdf(List<HoaDon> hd, String filePath) {
        try {
        	
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            for (HoaDon hoaDon : hd) {
            	
                doc.add(new Paragraph("HOA DON #" + hoaDon.getId()));
                doc.add(new Paragraph("Khach: " + hoaDon.getTenKhachHang()));
                doc.add(new Paragraph("Phong: " + hoaDon.getSoPhong()));
                doc.add(new Paragraph("Tien: " + hoaDon.getTongTien()));
                doc.add(new Paragraph("Ngay lap: " + hoaDon.getNgayLap()));
               
			} doc.close();
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}