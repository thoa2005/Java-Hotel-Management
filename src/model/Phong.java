package model;

public class Phong {
    private int id;
    private String ma_phong;
    private String loai_phong;
    private double gia;
    private String trang_thai;

    public Phong(int id, String maPhong, String loaiPhong, double gia, String trangThai) {
        this.id = id;
        this.ma_phong = maPhong;
        this.loai_phong = loaiPhong;
        this.gia = gia;
        this.trang_thai = trangThai;
    }

    
    public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getMaPhong() {
		return ma_phong;
	}


	public void setMaPhong(String ma_phong) {
		this.ma_phong = ma_phong;
	}


	public String getLoaiPhong() {
		return loai_phong;
	}


	public void setLoaiPhong(String loai_phong) {
		this.loai_phong = loai_phong;
	}


	public double getGia() {
		return gia;
	}


	public void setGia(double gia) {
		this.gia = gia;
	}


	public String getTrangThai() {
		return trang_thai;
	}


	public void setTrangThai(String trang_thai) {
		this.trang_thai = trang_thai;
	}


	@Override
    public String toString() {
        return this.ma_phong; 
    }
}