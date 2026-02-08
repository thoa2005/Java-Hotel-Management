package PHONG.controller;

import dao.PhongDAO;

import dao.DatPhongDAO;
import dao.KhachHangDAO;
import model.Phong;
import model.DatPhong;
import model.KhachHang;
import taikhoan.controllerfx.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import java.text.DecimalFormat;

public class KhachHangController {
	@FXML
	private FlowPane flowPanePhong;

	private PhongDAO phongDAO = new PhongDAO();
	private DatPhongDAO datPhongDAO = new DatPhongDAO();
	private KhachHangDAO khachHangDAO = new KhachHangDAO();
	
	int soNgay;
	String trangThai="Tất cả";
	String sapXep="Mặc định";

	@FXML
	public void initialize() {
		loadPhongData();
	}

	public void loadPhongData() {
		flowPanePhong.getChildren().clear();
		List<Phong> dsPhong = phongDAO.getAllPhongTrong();

		for (Phong p : dsPhong) {
			Button btnRoom = new Button();
			btnRoom.setPrefSize(150, 180);

			VBox layout = new VBox(10);
			layout.setAlignment(Pos.CENTER);

			Label lblMa = new Label("Phòng: " + p.getMaPhong());
			Label lblLoai = new Label("Loại: " + p.getLoaiPhong());
			lblLoai.getStyleClass().add("label-lp");

			DecimalFormat formatter = new DecimalFormat("###,###,###");
			String giaDinhDang = formatter.format(p.getGia());

			Label lblGia = new Label("Giá: " + giaDinhDang + " VNĐ");
			lblGia.getStyleClass().add("label-info");

			Label lblStatus = new Label("TRONG");
			btnRoom.getStyleClass().add("available");
			btnRoom.getStyleClass().add("room-button");
			btnRoom.getStyleClass().add("label-ma");

			layout.getChildren().addAll(lblMa, lblLoai, lblGia, lblStatus);

			btnRoom.setGraphic(layout);

			btnRoom.setOnAction(event -> {

				handleBooking(p);

			});

			flowPanePhong.getChildren().add(btnRoom);
		}
	}

	private void handleBooking(Phong p) {
		TextInputDialog dialog = new TextInputDialog("1");
		dialog.setTitle("Nhập số lượng");
		dialog.setHeaderText("Yêu cầu: Con số phải lớn hơn 0");
		dialog.setContentText("Số ngày:");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
		    try {
		        int value = Integer.parseInt(result.get());
		        
		        if (value > 0) {
		            this.soNgay = value;
		            processBooking(p);
		        } else {
		            Alert alert = new Alert(Alert.AlertType.WARNING, "Nhập số ngày\nVui lòng nhập số lớn hơn 0!");
		            alert.showAndWait();
		        }
		    } catch (NumberFormatException e) {
		        Alert alert = new Alert(Alert.AlertType.ERROR, "Bạn phải nhập một con số nguyên!");
		        alert.showAndWait();
		    }
		}
	}

	private void processBooking(Phong p) {
		int idUser = UserSession.getIdUser();
		KhachHang kh = khachHangDAO.getByUserId(idUser);
		if (kh == null) {
			showSimpleAlert("LỖI ĐẶT PHÒNG",
					"Bạn chưa khai báo thông tin\nvui lòng khai báo thông tin ở phần THÔNG TIN CÁ NHÂN");

		} else {

			if (kh != null) {
				if (phongDAO.capNhatTrangThai(p.getId(), "TRONG")) {

					DatPhong dp = new DatPhong();
					dp.setPhongId(p.getId());
					dp.setKhachhangId(kh.getId());
					dp.setNgayNhan(LocalDate.now());
					dp.setNgayTra(LocalDate.now().plusDays(soNgay)); 
					dp.setTrangThai("cho xac nhan"); 

					datPhongDAO.save(dp); 
					

					loadPhongData();
					showSimpleAlert("Thành công", "Đã gửi yêu cầu đặt phòng! vui lòng vào mục lịch xử đặt phòng để xem chi tiết");
				}
			}
		}
	}

	private void showSimpleAlert(String title, String content) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setTitle(title);
		a.setHeaderText(null);
		a.setContentText(content);
		a.showAndWait();
	}

	@FXML
	void handleBack(ActionEvent event) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			String fxmlPath = "/taikhoan/viewfx/fxml/KhachHangView.fxml"; 

			Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
			stage.getScene().setRoot(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void refreshDisplay( ) {
		flowPanePhong.getChildren().clear();
		List<Phong> dsPhong =  new ArrayList<Phong>( phongDAO.getAllPhongTrong());
		if(!trangThai.equals("Tất cả")) {
		dsPhong=phongDAO.getAllPhongTheoLP(trangThai);}
		if(sapXep.equals("Tang")) {
			dsPhong.sort(Comparator.comparingDouble(Phong::getGia));
		}
		else if(sapXep.equals("Giam")) {
			dsPhong.sort(Comparator.comparingDouble(Phong::getGia).reversed());
		}

		for (Phong p : dsPhong) {
		
			Button btnRoom = new Button();
			btnRoom.setPrefSize(150, 180);

	
			VBox layout = new VBox(10);
			layout.setAlignment(Pos.CENTER);

			Label lblMa = new Label("Phòng: " + p.getMaPhong());
			Label lblLoai = new Label("Loại: " + p.getLoaiPhong());
			lblLoai.getStyleClass().add("label-lp");


			DecimalFormat formatter = new DecimalFormat("###,###,###");
			String giaDinhDang = formatter.format(p.getGia());

			Label lblGia = new Label("Giá: " + giaDinhDang + " VNĐ");
			lblGia.getStyleClass().add("label-info");

			Label lblStatus = new Label("TRONG");
			btnRoom.getStyleClass().add("available");
			btnRoom.getStyleClass().add("room-button");
			btnRoom.getStyleClass().add("label-ma");

			layout.getChildren().addAll(lblMa, lblLoai, lblGia, lblStatus);

			btnRoom.setGraphic(layout);

			btnRoom.setOnAction(event -> {

				handleBooking(p);

			});

			flowPanePhong.getChildren().add(btnRoom);
		}
	}
	
	@FXML
	private void lPAll() {
		trangThai="Tất cả";
		refreshDisplay();
	}
	@FXML
	private void lPT() {
		trangThai="Thường";
		refreshDisplay();
	}
	@FXML
	private void lPV() {
		trangThai="VIP";
		refreshDisplay();
	}
	@FXML
	private void lPD() {
		trangThai="Đôi";
		refreshDisplay();
	}
	@FXML
	private void XSD() {
		sapXep="Default";
		refreshDisplay();
		
	}
	@FXML
	private void XST() {
		sapXep="Tang";
		refreshDisplay();
		
	}
	@FXML
	private void XSG() {
		sapXep="Giam";
		refreshDisplay();
		
	}
}