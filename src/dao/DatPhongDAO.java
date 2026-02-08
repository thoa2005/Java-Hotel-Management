package dao;

import model.DatPhong;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatPhongDAO {

    public boolean save(DatPhong dp) {
        String sqlInsert = "INSERT INTO datphong (phong_id, khachhang_id, ngay_nhan, ngay_tra, trang_thai) VALUES (?, ?, ?, ?, ?)";

        Connection c = DBConnection.getConnection();
        if (c == null) return false;

        try {
            c.setAutoCommit(false); 

            try (PreparedStatement ps = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, dp.getPhongId());
                ps.setInt(2, dp.getKhachhangId());
                ps.setDate(3, Date.valueOf(dp.getNgayNhan()));
                ps.setDate(4, Date.valueOf(dp.getNgayTra()));
                ps.setString(5, "Đang xử lý");

                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) dp.setId(rs.getInt(1));
            }

           

            c.commit(); 
            return true;
        } catch (SQLException e) {
            try { c.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { c.setAutoCommit(true); c.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<DatPhong> getAll() {
        List<DatPhong> list = new ArrayList<>();
        String sql = "SELECT dp.*, kh.ho_ten, p.ma_phong " +
                     "FROM datphong dp " +
                     "JOIN khachhang kh ON dp.khachhang_id = kh.id " +
                     "JOIN phong p ON dp.phong_id = p.id " +
                     "ORDER BY dp.id DESC";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setId(rs.getInt("id"));
                dp.setPhongId(rs.getInt("phong_id"));
                dp.setKhachhangId(rs.getInt("khachhang_id"));
                dp.setNgayNhan(rs.getDate("ngay_nhan").toLocalDate());
                dp.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
                dp.setTrangThai(rs.getString("trang_thai"));
                
                dp.setTenKhachHang(rs.getString("ho_ten"));
                dp.setMaPhong(rs.getString("ma_phong"));
                
                list.add(dp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean delete(int id, int phongId) {
        String sqlDelete = "DELETE FROM datphong WHERE id = ?";
        String sqlUpdateRoom = "UPDATE phong SET trang_thai = 'TRONG' WHERE id = ?";

        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement ps1 = c.prepareStatement(sqlDelete)) {
                ps1.setInt(1, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = c.prepareStatement(sqlUpdateRoom)) {
                ps2.setInt(1, phongId);
                ps2.executeUpdate();
            }

            c.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(DatPhong dp) {
        String sql = "UPDATE datphong SET phong_id = ?, khachhang_id = ?, ngay_nhan = ?, ngay_tra = ?, trang_thai = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, dp.getPhongId());
            ps.setInt(2, dp.getKhachhangId());
            ps.setDate(3, java.sql.Date.valueOf(dp.getNgayNhan()));
            ps.setDate(4, java.sql.Date.valueOf(dp.getNgayTra()));
            ps.setString(5, dp.getTrangThai());  
            ps.setInt(6, dp.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<DatPhong> getByIdKhach(int khachHangId) {
        List<DatPhong> list = new ArrayList<>();
        String sql = "SELECT dp.*, p.ma_phong FROM datphong dp " +
                     "JOIN phong p ON dp.phong_id = p.id " +
                     "WHERE dp.khachhang_id = ?";
        
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setInt(1, khachHangId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setId(rs.getInt("id"));
                dp.setPhongId(rs.getInt("phong_id"));
                
                Date dNhan = rs.getDate("ngay_nhan");
                if (dNhan != null) dp.setNgayNhan(dNhan.toLocalDate());
                
                Date dTra = rs.getDate("ngay_tra");
                if (dTra != null) dp.setNgayTra(dTra.toLocalDate());
                
                dp.setTrangThai(rs.getString("trang_thai"));
                dp.setMaPhong(rs.getString("ma_phong")); 
                list.add(dp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean delete(int id) {
        String sql = "DELETE FROM datphong WHERE id = ?";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public DatPhong getById(int id) {
        String sql = "SELECT dp.*, p.ma_phong, p.loai_phong, p.gia, kh.ho_ten " +
                     "FROM datphong dp " +
                     "JOIN phong p ON dp.phong_id = p.id " +
                     "JOIN khachhang kh ON dp.khachhang_id = kh.id " +
                     "WHERE dp.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setId(rs.getInt("id"));
                dp.setPhongId(rs.getInt("phong_id"));
                dp.setKhachhangId(rs.getInt("khachhang_id"));
                dp.setNgayNhan(rs.getDate("ngay_nhan").toLocalDate());
                dp.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
                dp.setTrangThai(rs.getString("trang_thai"));
                dp.setMaPhong(rs.getString("ma_phong"));
                dp.setLoaiPhong(rs.getString("loai_phong"));
                dp.setGiaPhong(rs.getDouble("gia"));
                dp.setTenKhachHang(rs.getString("ho_ten"));
                return dp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<DatPhong> getDatPhongDangThue() {
        List<DatPhong> list = new ArrayList<>();
        String sql = "SELECT dp.*, p.ma_phong, p.loai_phong, p.gia, kh.ho_ten " +
                     "FROM datphong dp " +
                     "JOIN phong p ON dp.phong_id = p.id " +
                     "JOIN khachhang kh ON dp.khachhang_id = kh.id " +
                     "WHERE dp.trang_thai = 'đã xác nhận'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DatPhong dp = new DatPhong();
                dp.setId(rs.getInt("id"));
                dp.setPhongId(rs.getInt("phong_id"));
                dp.setKhachhangId(rs.getInt("khachhang_id"));
                dp.setNgayNhan(rs.getDate("ngay_nhan").toLocalDate());
                dp.setNgayTra(rs.getDate("ngay_tra").toLocalDate());
                dp.setTrangThai(rs.getString("trang_thai"));
                dp.setMaPhong(rs.getString("ma_phong"));
                dp.setLoaiPhong(rs.getString("loai_phong"));
                dp.setGiaPhong(rs.getDouble("gia"));
                dp.setTenKhachHang(rs.getString("ho_ten"));
                list.add(dp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public static int getIdByMP(int mp) {
    	Connection con = DBConnection.getConnection();
    	int id=0;
    	String sql= "SELECT id FROM datphong WHERE phong_id=?";
    	try {
			PreparedStatement ps= con.prepareStatement(sql);
			ps.setInt(1, mp);
			ResultSet rs= ps.executeQuery();
			
				id=rs.getInt("id");
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return id;
    
    }
}