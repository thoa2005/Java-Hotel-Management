package dao;

import util.DBConnection;
import model.KhachHang;
import taikhoan.controllerfx.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        
        String sql = "SELECT * FROM khachhang"; 
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getInt("id"), 
                    rs.getString("ho_ten"),
                    rs.getString("so_dien_thoai"), 
                    rs.getString("cccd"), 
                    rs.getString("email"), 
                    rs.getString("gioi_tinh"),
                    rs.getInt("id_user")
                );
                list.add(kh);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean add(KhachHang kh) {
        String sql = "INSERT INTO khachhang(ho_ten, so_dien_thoai, cccd, email, gioi_tinh,id_user) VALUES(?,?,?,?,?,?)";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getCccd());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getGioiTinh());
            ps.setInt(6, UserSession.getIdUser());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); 
        return false; }
    }

    public boolean update(KhachHang kh) {
        String sql = "UPDATE khachhang SET ho_ten=?, so_dien_thoai=?, cccd=?, email=?, gioi_tinh=? WHERE id=?";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setString(1, kh.getHoTen());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getCccd());
            ps.setString(4, kh.getEmail());
            ps.setString(5, kh.getGioiTinh());
            ps.setInt(6, kh.getId());
     
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace();
        return false; }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM khachhang WHERE id=?";
        try (Connection cons = DBConnection.getConnection();
             PreparedStatement ps = cons.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace();
        return false; }
    }
    public KhachHang getByUserId(int idUser) {
        String sql = "SELECT * FROM khachhang WHERE id_user = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new KhachHang(
                    rs.getInt("id"),
                    rs.getString("ho_ten"),
                    rs.getString("so_dien_thoai"),
                    rs.getString("cccd"),
                    rs.getString("email"),
                    rs.getString("gioi_tinh"),
                    rs.getInt("id_user") 
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
