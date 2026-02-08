package thanhtoan.controller;

import java.util.List;


import dao.DatPhongDAO;
import dao.HoaDonDAO;
import javafx.collections.ObservableList;
import model.DatPhong;
import model.HoaDon;
import util.InvoiceExporter;

public class HoaDonController {
    private HoaDonDAO hdDao = new HoaDonDAO();
    private DatPhongDAO dpDao = new DatPhongDAO();

    public DatPhong previewDatPhong(int id) {
        return dpDao.getById(id);
    }

    public boolean payAndCreate(HoaDon hd, int phongId, int datPhongId) {
        return hdDao.processPayment(hd, phongId, datPhongId);
    }

    public boolean exportPdf(List<HoaDon> hd, String filePath) {
    	for (HoaDon hoaDon : hd) {
    		 if (!"DA_THANH_TOAN".equals(hoaDon.getTrangThai())) return false;
		}
       
        return InvoiceExporter.exportPdf(hd, filePath);
    }

    public List<HoaDon> listAll() { return hdDao.getAll(); }
    public List<HoaDon> listByKhach(int taikhoanId) { return hdDao.getByKhachHangTaiKhoanId(taikhoanId); }
    public HoaDon getById(int id) { return hdDao.getById(id); }
}