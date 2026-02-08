package model;

public class KhachHang {
    private int id;
    private String hoTen;
    private String sdt;
    private String cccd;  
    private String email; 
    private String gioiTinh;
    private int IDUser;

    public KhachHang() {
    }


	public KhachHang(int id, String hoTen, String sdt, String cccd, String email, String gioiTinh, int iDUser) {
		super();
		this.id = id;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.cccd = cccd;
		this.email = email;
		this.gioiTinh = gioiTinh;
		IDUser = iDUser;
	}


	public int getIDUser() {
		return IDUser;
	}



	public void setIDUser(int iDUser) {
		IDUser = iDUser;
	}

	public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getGioiTinh() { return gioiTinh; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    @Override
    public String toString() {
        return this.hoTen; 
    }
}
