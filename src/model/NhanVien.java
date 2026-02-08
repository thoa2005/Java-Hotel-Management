package model;

public class NhanVien {
    private int id;
    private Integer taikhoanId;
    private String hoTen;
    private String sdt;
    private String email;
    private String chucVu;
    private double luong;

    public NhanVien() {}

    public NhanVien(int id, Integer taikhoanId, String hoTen, String sdt, String email, String chucVu, double luong) {
        this.id = id;
        this.taikhoanId = taikhoanId;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
        this.luong = luong;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Integer getTaikhoanId() { return taikhoanId; }
    public void setTaikhoanId(Integer taikhoanId) { this.taikhoanId = taikhoanId; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }
    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }
}