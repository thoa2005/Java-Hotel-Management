package model;

import java.time.LocalDateTime;

public class HoaDon {
    private int id;
    private int datPhongId;
    private int nhanVienId;
    private double tongTien;
    private LocalDateTime ngayLap;
    private String phuongThuc;
    private String trangThai; 

    private String soPhong;
    private String tenKhachHang;
    private String tenNhanVien;
    private LocalDateTime ngayCheckin;
    private LocalDateTime ngayCheckout;

    public HoaDon() {}

    public HoaDon(int id, int datPhongId, int nhanVienId, double tongTien, 
                  LocalDateTime ngayLap, String phuongThuc, String trangThai) {
        this.id = id;
        this.datPhongId = datPhongId;
        this.nhanVienId = nhanVienId;
        this.tongTien = tongTien;
        this.ngayLap = ngayLap;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getDatPhongId() { return datPhongId; }
    public void setDatPhongId(int datPhongId) { this.datPhongId = datPhongId; }
    
    public int getNhanVienId() { return nhanVienId; }
    public void setNhanVienId(int nhanVienId) { this.nhanVienId = nhanVienId; }
    
    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }
    
    public LocalDateTime getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDateTime ngayLap) { this.ngayLap = ngayLap; }
    
    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
    
    public String getTrangThai() { return trangThai; }
    
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    
    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }
    
    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }
    
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String tenNhanVien) { this.tenNhanVien = tenNhanVien; }
    
    public LocalDateTime getNgayCheckin() { return ngayCheckin; }
    public void setNgayCheckin(LocalDateTime ngayCheckin) { this.ngayCheckin = ngayCheckin; }
    
    public LocalDateTime getNgayCheckout() { return ngayCheckout; }
    public void setNgayCheckout(LocalDateTime ngayCheckout) { this.ngayCheckout = ngayCheckout; }

    public String getPhuongThucText() {
        switch (phuongThuc) {
            case "TIENMAT": return "Tiền mặt";
            case "CHUYENKHOAN": return "Chuyển khoản";
            case "THE": return "Quẹt thẻ";
            default: return phuongThuc;
        }
    }
}