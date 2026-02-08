package dao;

import java.sql.*;

import model.NhanVien;
import model.TaiKhoan;
import util.DBConnection;

public class TaiKhoanDAO {

    public TaiKhoan login(String user, String pass, String role) {
        String sql = "SELECT * FROM taikhoan WHERE username=? AND password=? AND role=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new TaiKhoan(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) return false;
        String sql = "SELECT 1 FROM taikhoan WHERE username = ? LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean register(String user, String pass, String role) {
        if (user == null || user.trim().isEmpty() || pass == null) return false;

        String sql = "INSERT INTO taikhoan(username,password,role) VALUES (?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
        	
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role);
            
            int i=ps.executeUpdate();
            System.out.println("chạy được đến đây1");
            return true;

        } catch (Exception e) {
           
            e.printStackTrace();
            return false;
        }
    }
    public TaiKhoan getByTaiKhoanId(int taikhoanId) {
        String sql = "SELECT * FROM taikhoan WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taikhoanId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TaiKhoan nv = new TaiKhoan();
                nv.setId(rs.getInt("id"));
                nv.setUsername(rs.getString("username"));
                nv.setRole(rs.getString("role"));
                return nv;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}