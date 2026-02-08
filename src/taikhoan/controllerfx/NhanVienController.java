package taikhoan.controllerfx;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import dao.PhongDAO;
import dao.TaiKhoanDAO;
import javafx.animation.Animation;

import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Phong;
import model.TaiKhoan;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.OverrunStyle;


public class NhanVienController {

	@FXML
	private Label lblNhanVien;

	@FXML
	private FlowPane flowPanePhong;

	@FXML
	public Button btnLogout;
	@FXML
	public Button btnDanhSach;
	@FXML
	public Button btnCheckIn;
	@FXML
	public Button btnCheckOut;
	@FXML
	public Button btnLapHoaDon;

	@FXML
	public TableView<?> tableBookings;
	@FXML
	public TableView<?> tableInvoices;
	@FXML
	private Label lblU;

	private PhongDAO phongDAO = new PhongDAO();
	TaiKhoanDAO tkd = new TaiKhoanDAO();
	TaiKhoan tk = tkd.getByTaiKhoanId(UserSession.getIdUser());

	@FXML
	public void initialize() {
		lblU.setText("username: " + tk.getUsername());

		loadPhongData();
		if (lblNhanVien != null) {
			lblNhanVien.setText("");
			typewriterEffect(lblNhanVien, "Nhân Viên Hoàn Hảo");
		}
	}

	public void loadPhongData() {
		flowPanePhong.getChildren().clear();
		List<Phong> dsPhong = phongDAO.getAllPhong();

		for (Phong p : dsPhong) {
			Button btnRoom = new Button();
			btnRoom.setPrefSize(150, 180);

			VBox layout = new VBox(10);
			layout.setAlignment(Pos.CENTER);

			Label lblMa = new Label("Phòng: " + p.getMaPhong());
			Label lblLoai = new Label("Loại: " + p.getLoaiPhong());

			DecimalFormat formatter = new DecimalFormat("###,###,###");
			String giaDinhDang = formatter.format(p.getGia());

			Label lblGia = new Label("Giá: " + giaDinhDang + " VNĐ");
			lblGia.getStyleClass().add("label-info");

			Label lblStatus = new Label(p.getTrangThai());
			if (p.getTrangThai().equals("DA DAT")) {
				btnRoom.getStyleClass().add("available1");
			} else {
				btnRoom.getStyleClass().add("available");
			}
			btnRoom.getStyleClass().add("room-button");
			btnRoom.getStyleClass().add("label-ma");

			layout.getChildren().addAll(lblMa, lblLoai, lblGia, lblStatus);

			btnRoom.setGraphic(layout);

			btnRoom.setOnAction(event -> {

			});

			flowPanePhong.getChildren().add(btnRoom);
		}
	}

	private void typewriterEffect(Label label, String text) {
		label.setTextOverrun(OverrunStyle.CLIP);

		final int[] charIndex = { 0 };
		final boolean[] isAdding = { true };
		final int[] pauseCount = { 0 };

		Timeline timeline = new Timeline();

		KeyFrame keyFrame = new KeyFrame(Duration.millis(150), event -> {
			if (isAdding[0]) {
				if (charIndex[0] < text.length()) {
					charIndex[0]++; 
					label.setText(text.substring(0, charIndex[0]));
				} else {
					pauseCount[0]++;
					if (pauseCount[0] > 15) { 
						isAdding[0] = false;
						pauseCount[0] = 0;
					}
				}
			} else {
				if (charIndex[0] > 0) {
					label.setText(text.substring(0, charIndex[0]));
					charIndex[0]--;
				} else {
					label.setText("");
					pauseCount[0]++;
					if (pauseCount[0] > 5) {
						isAdding[0] = true;
						pauseCount[0] = 0;
						charIndex[0] = 0;
					}
				}
			}
		});

		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	@FXML
	public void handleDanhSach(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/DTP/view/DatPhongView.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 1000, 550);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleCheckIn() {
		showAlert(AlertType.INFORMATION, "Hệ thống: Check-in thành công!");
	}

	@FXML
	public void handleCheckOut() {
		showAlert(AlertType.INFORMATION, "Hệ thống: Check-out thành công!");
	}

	@FXML
	public void handleLapHoaDon(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/thanhtoan/view/HoaDonView.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 1000, 550);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	public void handleLogout() {
		try {
			
			Stage st = (Stage) btnLogout.getScene().getWindow();

			LogoutHandlerFX.logout(st);

			System.out.println("Đã thực hiện đăng xuất thành công.");
		} catch (Exception e) {
			System.out.println("Lỗi đăng xuất: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@FXML
	public void handleDSK(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/QLKH/view/KhachHangView.fxml"));
			Parent root = loader.load();

			QLKH.controller.KhachHangController controller = loader.getController();
			controller.setRole("NhanVien"); 

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			stage.getScene().setRoot(root);
			stage.sizeToScene();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	@FXML
	private void handleDSHD(ActionEvent event) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/thanhtoan/view/HoaDonListView.fxml"));
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			Scene scene = new Scene(root, 1000, 550);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showAlert(AlertType type, String msg) {
		Alert a = new Alert(type);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}

}