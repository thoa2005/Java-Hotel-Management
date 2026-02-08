package model;

import java.time.LocalDate;

public class DatPhong {
    private int id;
    private int phongId;
    private int khachhangId;
    private LocalDate ngayNhan;
    private LocalDate ngayTra;
    private String trangThai;

    private String tenKhachHang;
    private String maPhong;
    
    
  
    private String loaiPhong;
    private double giaPhong;




    public String getLoaiPhong() {
		return loaiPhong;
	}

	public void setLoaiPhong(String loaiPhong) {
		this.loaiPhong = loaiPhong;
	}

	public double getGiaPhong() {
		return giaPhong;
	}

	public void setGiaPhong(double giaPhong) {
		this.giaPhong = giaPhong;
	}

	public DatPhong() {}

    public DatPhong(int id, int phongId, int khachhangId, LocalDate ngayNhan, LocalDate ngayTra, String trangThai) {
        this.id = id;
        this.phongId = phongId;
        this.khachhangId = khachhangId;
        this.ngayNhan = ngayNhan;
        this.ngayTra = ngayTra;
        this.trangThai = trangThai;
    }
    public long getSoNgayThue() {
        return java.time.temporal.ChronoUnit.DAYS.between(ngayNhan, ngayTra);
    }

    public double getTongTienDuKien() {
        return getSoNgayThue() * giaPhong;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPhongId() { return phongId; }
    public void setPhongId(int phongId) { this.phongId = phongId; }

    public int getKhachhangId() { return khachhangId; }
    public void setKhachhangId(int khachhangId) { this.khachhangId = khachhangId; }

    public LocalDate getNgayNhan() { return ngayNhan; }
    public void setNgayNhan(LocalDate ngayNhan) { this.ngayNhan = ngayNhan; }

    public LocalDate getNgayTra() { return ngayTra; }
    public void setNgayTra(LocalDate ngayTra) { this.ngayTra = ngayTra; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenKhachHang() { return tenKhachHang; }
    public void setTenKhachHang(String tenKhachHang) { this.tenKhachHang = tenKhachHang; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
}