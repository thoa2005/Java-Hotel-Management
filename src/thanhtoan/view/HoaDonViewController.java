package thanhtoan.view;

import thanhtoan.controller.HoaDonController;
import dao.DatPhongDAO;
import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.PhongDAO;
import dao.TaiKhoanDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.*;
import taikhoan.controllerfx.UserSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class HoaDonViewController {

    private TaiKhoan currentUser;
    private HoaDonController hoaDonController = new HoaDonController();
    private DatPhongDAO datPhongDAO = new DatPhongDAO();
    private TaiKhoanDAO taiKhoanDAO= new TaiKhoanDAO();
    private HoaDon lastCreated;
    private HoaDonDAO hdDao=new HoaDonDAO();
    private HoaDonController hdController= new HoaDonController();
   

    @FXML private ComboBox<DatPhongItem> cbDatPhong;
    @FXML private Label lblSoPhong, lblKhach, lblCheckIn, lblCheckOut,
            lblSoNgay, lblGia, lblTongTienBig, lblNgayLap, lblTienThua, lblSubTitle;
    @FXML private Button btnBack;
        
    @FXML private RadioButton rbtnTienMat, rbtnCK, rbtnThe;
    @FXML private TextField txtTienNhan;
    @FXML private Button btnCreate, btnPrint, btnRefresh;

    @FXML
    public void initialize() {
        ToggleGroup tg = new ToggleGroup();
        rbtnTienMat.setToggleGroup(tg);
        rbtnCK.setToggleGroup(tg);
        rbtnThe.setToggleGroup(tg);
        rbtnTienMat.setSelected(true);
        btnBack.setOnAction(e -> handleBack());
        cbDatPhong.setOnAction(e -> previewSelected());
        txtTienNhan.textProperty().addListener((o, a, b) -> updateChange());

        loadData();
    }
 
    public void setCurrentUser(TaiKhoan user) {
        this.currentUser = user;
        lblSubTitle.setText("Quản lý giao dịch | NV: " + user.getUsername());
    }
    @FXML
    private void handleBack() {
    	try {
    		

    		Stage stage = (Stage) btnBack.getScene().getWindow();
    		String title = stage.getTitle();
    		
    		String url= "";
    		if (title.contains("QUANLY")) {
    			 url = "/taikhoan/viewfx/fxml/QuanLyView.fxml";
    		}
    		else if (title.contains("NHANVIEN")) {
    			 url = "/taikhoan/viewfx/fxml/NhanVienView.fxml";
    			}
    		Parent root= FXMLLoader.load(getClass().getResource(url));
    		Scene scene= new Scene(root,1000,550);
    		stage.setScene(scene);
    		stage.show();
    		
    }
    		catch (Exception e) {
    	// TODO: handle exception
    }
    	}
    
    @FXML
    private void loadData() {
        cbDatPhong.getItems().clear();
        txtTienNhan.setText("0");
        List<DatPhong> list = datPhongDAO.getDatPhongDangThue();
        list.forEach(dp -> cbDatPhong.getItems().add(new DatPhongItem(dp)));
        clearDetails();
    }

    private void clearDetails() {
        lblSoPhong.setText("-");
        lblKhach.setText("-");
        lblCheckIn.setText("-");
        lblCheckOut.setText("-");
        lblSoNgay.setText("-");
        lblGia.setText("-");
        lblTongTienBig.setText("0 VND");
        lblTienThua.setText("Tiền thừa: 0 VND");
        btnPrint.setDisable(true);
        btnCreate.setDisable(false);
        lastCreated = null;
    }
    private void previewSelected() {
        DatPhongItem item = cbDatPhong.getValue();
        if (item == null) return;

        DatPhong dp = item.dp;
        lblSoPhong.setText(dp.getMaPhong());
        lblKhach.setText(dp.getTenKhachHang());
        lblCheckIn.setText(dp.getNgayNhan().toString());
        lblCheckOut.setText(dp.getNgayTra().toString());
        lblSoNgay.setText(dp.getSoNgayThue() + " ngày");
        lblGia.setText(String.format("%,.0f", dp.getGiaPhong()));
        lblTongTienBig.setText(String.format("%,.0f VND", dp.getTongTienDuKien()));
        lblNgayLap.setText("Ngày: " + java.time.LocalDate.now());
    }

    private void updateChange() {
        try {
            double paid = Double.parseDouble(txtTienNhan.getText().replace(",", ""));
            DatPhongItem item = cbDatPhong.getValue();
            if (item == null) return;
            double total = item.dp.getTongTienDuKien();
            double change = paid - total;

            if (change >= 0) {
                lblTienThua.setText("Tiền thừa: " + String.format("%,.0f VND", change));
            } else {
                lblTienThua.setText("Thiếu: " + String.format("%,.0f VND", Math.abs(change)));
            }
        } catch (Exception ignored) {}
    }
    
    @FXML
    public void createInvoice() {
        DatPhongItem item = cbDatPhong.getValue();
        if (item == null) {
            alert("Chọn phòng!");
            return;
        }

        double paid;
        try {
            paid = Double.parseDouble(txtTienNhan.getText());
        } catch (Exception e) {
            alert("Nhập số tiền!");
            return;
        }

        if (paid < item.dp.getTongTienDuKien()) {
            alert("Tiền chưa đủ!");
            return;
        }

       String phuongThuc = "TIEN_MAT";
        if (rbtnCK.isSelected()) phuongThuc = "CHUYEN_KHOAN";
        else if (rbtnThe.isSelected()) phuongThuc = "THE";

        TaiKhoan nv = taiKhoanDAO.getByTaiKhoanId(UserSession.getIdUser());
        HoaDon hd = new HoaDon();
        hd.setDatPhongId(item.dp.getId());
        hd.setNhanVienId(nv.getId());
        hd.setTongTien(item.dp.getTongTienDuKien());
        hd.setNgayLap(LocalDateTime.now());
        
        hd.setTrangThai("DA_THANH_TOAN");
        hd.setPhuongThuc(phuongThuc);

        if (hoaDonController.payAndCreate(hd, item.dp.getPhongId(), item.dp.getId())) {
            alert("Thanh toán thành công!");
            lastCreated = hd;
            btnPrint.setDisable(false);
            btnCreate.setDisable(true);
        }
        
    }

    private void alert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    static class DatPhongItem {
        DatPhong dp;
        DatPhongItem(DatPhong dp) { this.dp = dp; }
        @Override public String toString() {
            return "Phòng " + dp.getMaPhong() + " - " + dp.getTenKhachHang();
        }
    }
    @FXML 
    private void handleRefresh() {
    	initialize();
    }
    @FXML
    private void handleXHD() {
    	List<HoaDon> hd = new ArrayList<HoaDon>();
    	HoaDon hoaDon = lastCreated;
    	hoaDon.setTenKhachHang(lblKhach.getText());
    	hoaDon.setSoPhong(lblSoPhong.getText());
			if (hoaDon != null) {
				hd.add(hoaDon);
			}
		if (hd.isEmpty()) {
			showAlert("Hóa đơn không tồn tại.");
			return;
		}
		
			if (!"DA_THANH_TOAN".equals(hoaDon.getTrangThai())) {
				showAlert("Chỉ có thể xuất PDF khi đã thanh toán.");
				return;
		
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName("hoa_don_new" + ".pdf");
		java.io.File file = fileChooser.showSaveDialog(lblKhach.getScene().getWindow());
		if (file != null) {
			boolean ok = hdController.exportPdf(hd, file.getAbsolutePath());
			showAlert(ok ? "Xuất PDF thành công." : "Xuất PDF thất bại.");
		}
		loadData();
		
    }
    public void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông báo");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}
