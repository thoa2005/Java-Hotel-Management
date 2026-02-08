package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.NhanVien;
import util.DBConnection;

public class NhanVienDAO {


public NhanVien getByTaiKhoanId(int taikhoanId) {
    String sql = "SELECT * FROM nhanvien WHERE tai_khoan_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, taikhoanId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setId(rs.getInt("id"));
            nv.setTaikhoanId(rs.getInt("tai_khoan_id"));
            nv.setHoTen(rs.getString("ho_ten"));
            nv.setSdt(rs.getString("sdt"));
            nv.setEmail(rs.getString("email"));
            nv.setChucVu(rs.getString("chuc_vu"));
            nv.setLuong(rs.getDouble("luong"));
            return nv;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
}