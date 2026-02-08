// filepath: d:\Project\Java\qlks_thanhtoan_hoadon\src\dao\HoaDonDAO.java
package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.HoaDon;
import util.DBConnection;

public class HoaDonDAO {

    // Tạo hóa đơn mới
    public boolean create(HoaDon hd) {
        String sql = "INSERT INTO hoadon (dat_phong_id, nhan_vien_id, tong_tien, ngay_lap, phuong_thuc, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, 'DA_THANH_TOAN')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hd.getDatPhongId());
            ps.setInt(2, hd.getNhanVienId());
            ps.setDouble(3, hd.getTongTien());
            ps.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap()));
            ps.setString(5, hd.getPhuongThuc());
            int result = ps.executeUpdate();
            if (result > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    hd.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HoaDon> getAll() {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT hd.*, p.ma_phong, kh.ho_ten as ten_khach, tk.username as ten_nv, dp.ngay_nhan, dp.ngay_tra \r\n"
        		+ "FROM hoadon hd \r\n"
        		+ "JOIN datphong dp ON hd.dat_phong_id = dp.id \r\n"
        		+ "JOIN phong p ON dp.phong_id = p.id \r\n"
        		+ "JOIN khachhang kh ON dp.khachhang_id = kh.id \r\n"
        		+ "JOIN taikhoan tk ON hd.nhan_vien_id = tk.id -- ĐỔI TỪ nhanvien SANG taikhoan\r\n"
        		+ "WHERE 1=1 ORDER BY hd.ngay_lap DESC";
        System.out.println("SQL: " + sql);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HoaDon hd = mapResultSet(rs);
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HoaDon> getByKhachHangTaiKhoanId(int taikhoanId) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT hd.*, p.ma_phong, kh.ho_ten as ten_khach, tk.username as ten_nv \r\n"
        		+ "FROM hoadon hd\r\n"
        		+ "JOIN datphong dp ON hd.dat_phong_id = dp.id\r\n"
        		+ "JOIN khachhang kh ON dp.khachhang_id = kh.id\r\n"
        		+ "JOIN taikhoan tk ON hd.taikhoan_id = tk.id -- Đây là nhân viên lập hóa đơn\r\n"
        		+ "WHERE kh.tai_khoan_id = ?  -- Đây là ID tài khoản của Khách hàng đang đăng nhập";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taikhoanId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = mapResultSet(rs);
                list.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<HoaDon> search(String tenKhach, LocalDate tuNgay, LocalDate denNgay) {
        List<HoaDon> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT hd.*, p.ma_phong, kh.ho_ten as ten_khach, tk.username as nguoi_lap, " +
            "dp.ngay_nhan, dp.ngay_tra " +
            "FROM hoadon hd " +
            "JOIN datphong dp ON hd.dat_phong_id = dp.id " +
            "JOIN phong p ON dp.phong_id = p.id " +
            "JOIN khachhang kh ON dp.khachhang_id = kh.id " +
            "JOIN taikhoan tk ON hd.nhan_vien_id = tk.id WHERE 1=1 "); 
        
        List<Object> params = new ArrayList<>();
        
        if (tenKhach != null && !tenKhach.trim().isEmpty()) {
            sql.append("AND kh.ho_ten LIKE ? ");
            params.add("%" + tenKhach + "%");
        }
        if (tuNgay != null) {
            sql.append("AND DATE(hd.ngay_lap) >= ? ");
            params.add(java.sql.Date.valueOf(tuNgay));
        }
        if (denNgay != null) {
            sql.append("AND DATE(hd.ngay_lap) <= ? ");
            params.add(java.sql.Date.valueOf(denNgay));
        }

        sql.append(" ORDER BY hd.ngay_lap DESC"); 

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon getById(int id) {
        String sql = "SELECT hd.*, p.ma_phong, kh.ho_ten as ten_khach, tk.username as nguoi_lap, " + 
                     "dp.ngay_nhan, dp.ngay_tra " +
                     "FROM hoadon hd " +
                     "JOIN datphong dp ON hd.dat_phong_id = dp.id " +
                     "JOIN phong p ON dp.phong_id = p.id " +
                     "JOIN khachhang kh ON dp.khachhang_id = kh.id " +
                     "JOIN taikhoan tk ON hd.nhan_vien_id = tk.id " + 
                     "WHERE hd.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByDatPhongId(int datPhongId) {
        String sql = "SELECT COUNT(*) FROM hoadon WHERE dat_phong_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, datPhongId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 public boolean processPayment(HoaDon hd, int phongId, int datPhongId) {
    String insertSql = "INSERT INTO hoadon (dat_phong_id, nhan_vien_id, tong_tien, ngay_lap, phuong_thuc, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
    String updateDatPhongSql = "UPDATE datphong SET trang_thai = 'DA_TRA' WHERE id = ?";
    String updatePhongSql = "UPDATE phong SET trang_thai = 'TRONG' WHERE id = ?";
    Connection conn = null;
    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false);
        try (PreparedStatement psIns = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            psIns.setInt(1, hd.getDatPhongId());
            psIns.setInt(2, hd.getNhanVienId());
            psIns.setDouble(3, hd.getTongTien());
            psIns.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap()));
            psIns.setString(5, hd.getPhuongThuc());
            psIns.setString(6, "DA_THANH_TOAN"); 
            psIns.executeUpdate();
            try (ResultSet rs = psIns.getGeneratedKeys()) {
                if (rs.next()) {
                    hd.setId(rs.getInt(1));
                }
            }
        }
        try (PreparedStatement psDp = conn.prepareStatement(updateDatPhongSql)) {
            psDp.setInt(1, datPhongId);
            psDp.executeUpdate();
        }
        try (PreparedStatement psP = conn.prepareStatement(updatePhongSql)) {
            psP.setInt(1, phongId);
            psP.executeUpdate();
        }
        conn.commit();
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    } finally {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }
    return false;
}
    private HoaDon mapResultSet(ResultSet rs) throws SQLException {
        HoaDon hd = new HoaDon();
        hd.setId(rs.getInt("id"));
        hd.setDatPhongId(rs.getInt("dat_phong_id"));
        hd.setNhanVienId(rs.getInt("nhan_vien_id"));
        hd.setTongTien(rs.getDouble("tong_tien"));
        hd.setNgayLap(rs.getTimestamp("ngay_lap").toLocalDateTime());
        hd.setPhuongThuc(rs.getString("phuong_thuc"));
        hd.setTrangThai(rs.getString("trang_thai"));
        hd.setSoPhong(rs.getString("ma_phong"));
        hd.setTenKhachHang(rs.getString("ten_khach"));
        hd.setTenNhanVien(rs.getString("nguoi_lap"));
        hd.setNgayCheckin(rs.getDate("ngay_nhan").toLocalDate().atStartOfDay());
        hd.setNgayCheckout(rs.getDate("ngay_tra").toLocalDate().atStartOfDay());
        return hd;
    }
    public static int getHDByMP(int maP) {
    	int id=0;
    	Connection con= DBConnection.getConnection();
    	String sql= "SELECT id FROM hoadon WHERE dat_phong_id=?";
    	PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, maP);
	    	ResultSet rs=ps.executeQuery();
	    	if(rs.next()) {
	    		id=rs.getInt("id");
	    	}
	    	else {System.out.println("anh sai rồi");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(id+"đây là id");
    	return id;
    	
    	
    	
    }
}