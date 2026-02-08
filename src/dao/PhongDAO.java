package dao;
import model.Phong;
import util.DBConnection;
import java.sql.*;
import java.util.*;
public class PhongDAO {
    public List<Phong> getAllPhong(){
        List<Phong> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();
            Statement st=c.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM phong")){
            while(rs.next()){
                list.add(new Phong(
                    rs.getInt("id"),
                    rs.getString("ma_phong"),
                    rs.getString("loai_phong"),
                    rs.getDouble("gia"),
                    rs.getString("trang_thai")
                ));
            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }
    public void insert(Phong p) {
        String sql = "INSERT INTO phong VALUES (NULL,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getLoaiPhong());
            ps.setDouble(3, p.getGia());
            ps.setString(4, p.getTrangThai());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean capNhatTrangThai(int phongId, String trangThaiMoi) {
        String sql = "UPDATE phong SET trang_thai = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, trangThaiMoi);
            ps.setInt(2, phongId);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Phong> getAllPhongTrong(){
        List<Phong> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();
            Statement st=c.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM phong WHERE trang_thai= 'TRONG'")){
            while(rs.next()){
                list.add(new Phong(
                    rs.getInt("id"),
                    rs.getString("ma_phong"),
                    rs.getString("loai_phong"),
                    rs.getDouble("gia"),
                    rs.getString("trang_thai")
                ));
            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }
    public void delete(int id) {
    	try {
    		Connection con= DBConnection.getConnection();
    		String sql= "DELETE From phong WHERE id=?";
    		PreparedStatement ps=con.prepareStatement(sql);
    		ps.setInt(1, id);
    		ps.executeUpdate();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    public boolean update(Phong p) {
        String sql = "UPDATE phong SET ma_phong = ?, loai_phong = ?, gia = ?, trang_thai = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setString(1, p.getMaPhong());
            ps.setString(2, p.getLoaiPhong());
            ps.setDouble(3, p.getGia());
            ps.setString(4, p.getTrangThai());
            ps.setInt(5, p.getId());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static void updateStatus(int id) {
    	Connection con= DBConnection.getConnection();
    	String sql="UPDATE phong SET trang_thai='DA DAT' WHERE id=?";
    	try {
			PreparedStatement ps= con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public static int getPByMa(String ma) {
    	int kq=0;
    	Connection con= DBConnection.getConnection();
    	String sql="SELECT * FROM phong WHERE ma_phong=?";
    	try {
			PreparedStatement ps= con.prepareStatement(sql);
			ps.setString(1, ma);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				kq=rs.getInt("id");
			
			}
			else {
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}return kq;
    }
    public List<Phong> getAllPhongTheoLP(String lP){
        List<Phong> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection();
            Statement st=c.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM phong WHERE trang_thai='TRONG' AND loai_phong= '"+lP+"'");
        		
            		)
        		{
            while(rs.next()){
                list.add(new Phong(
                    rs.getInt("id"),
                    rs.getString("ma_phong"),
                    rs.getString("loai_phong"),
                    rs.getDouble("gia"),
                    rs.getString("trang_thai")
                ));
            }
        }catch(Exception e){e.printStackTrace();}
        return list;
    }
   
}