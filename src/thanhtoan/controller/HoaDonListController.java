package thanhtoan.controller;

import java.time.LocalDate;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.text.TableView.TableCell;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import dao.HoaDonDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import model.HoaDon;
import model.TaiKhoan;

public class HoaDonListController {

	@FXML
	private TextField txtTenKhach;

	@FXML
	private DatePicker spFromDate;

	@FXML
	private DatePicker spToDate;

	@FXML
	private ComboBox<String> cbStatus;

	@FXML
	private Button btnSearch;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnView;

	@FXML
	private Button btnExport;

	@FXML
	private TableView<HoaDon> tbl;

	@FXML
	private Label lblTitle;

	@FXML
	private Label lblSub;
	@FXML
	private Button btnBack;
	private HoaDonDAO hdDao = new HoaDonDAO();
	private HoaDonController hdController = new HoaDonController();
	private TaiKhoan currentUser;
	private ObservableList<HoaDon> dataList = FXCollections.observableArrayList();

	public void setCurrentUser(TaiKhoan user) {
		this.currentUser = user;
		if (currentUser != null) {
			lblSub.setText(
					"Quản lý và theo dõi lịch sử thanh toán của khách hàng.\nXin chào, " + currentUser.getUsername());
			if ("KHACH".equals(currentUser.getRole())) {
				txtTenKhach.setText(currentUser.getUsername());
				txtTenKhach.setDisable(true);
			}
		}
		loadInitial();
	}

	@FXML
	private TableColumn<HoaDon, Integer> colId;
	@FXML
	private TableColumn<HoaDon, String> colPhong;
	@FXML
	private TableColumn<HoaDon, String> colKhachHang;
	@FXML
	private TableColumn<HoaDon, String> colNhanVien;
	@FXML
	private TableColumn<HoaDon, Date> colNgayLap;
	@FXML
	private TableColumn<HoaDon, String> colPTTT;
	@FXML
	private TableColumn<HoaDon, Double> colTongTien;
	@FXML
	private TableColumn<HoaDon, String> colTrangThai;

	@FXML
	public void initialize() {
		tbl.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colPhong.setCellValueFactory(new PropertyValueFactory<>("soPhong"));
		colKhachHang.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
		colNhanVien.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
		colNgayLap.setCellValueFactory(new PropertyValueFactory<>("ngayLap"));
		colPTTT.setCellValueFactory(new PropertyValueFactory<>("phuongThucText"));
		colTongTien.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
		colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
		cbStatus.setItems(FXCollections.observableArrayList("Tất cả", "CHUA_THANH_TOAN", "DA_THANH_TOAN"));
		cbStatus.setValue("Tất cả");
		cbStatus.setOnAction(e -> doSearch());
		tbl.setItems(dataList);
	}

	public void loadInitial() {
		dataList.clear();
		System.out.println("Current User Role: " + (currentUser != null ? currentUser.getRole() : "null"));
		List<HoaDon> list;
		if ("KHACH".equals(currentUser.getRole())) {
			list = hdDao.getByKhachHangTaiKhoanId(currentUser.getId());
		} else {
			list = hdDao.getAll();
		}
		dataList.addAll(list);
	}

	public void doSearch() {
		String ten = txtTenKhach.getText().trim();
		LocalDate from = spFromDate.getValue();
		LocalDate to = spToDate.getValue();
		String status = cbStatus.getValue();

		List<HoaDon> list = hdDao.search(ten.isEmpty() ? null : ten, from, to);

		if (!"Tất cả".equals(status)) {
			list.removeIf(h -> !status.equals(h.getTrangThai()));
		}

		dataList.clear();
		dataList.addAll(list);
	}

	public void handleBack() {
		try {

			Stage stage = (Stage) btnBack.getScene().getWindow();
			String title = stage.getTitle();

			String url = "";
			if (title.contains("QUANLY")) {
				url = "/taikhoan/viewfx/fxml/QuanLyView.fxml";
			} else if (title.contains("NHANVIEN")) {
				url = "/taikhoan/viewfx/fxml/NhanVienView.fxml";
			}
			Parent root = FXMLLoader.load(getClass().getResource(url));
			Scene scene = new Scene(root, 1000, 550);
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void doView() {
		HoaDon selected = tbl.getSelectionModel().getSelectedItem();
		if (selected == null) {
			showAlert("Chọn hóa đơn để xem.");
			return;
		}
		HoaDon hd = hdDao.getById(selected.getId());
		if (hd == null) {
			showAlert("Không tìm thấy hóa đơn.");
			return;
		}
		showAlert("Hóa đơn #" + hd.getId() + "\nKhách: " + hd.getTenKhachHang() + "\nPhòng: " + hd.getSoPhong()
				+ "\nNgày: " + hd.getNgayLap() + "\nTổng: " + String.format("%,.0f", hd.getTongTien()) + "\nPT: "
				+ hd.getPhuongThucText() + "\nTrạng thái: " + hd.getTrangThai());
	}

	public void doExport() {
		ObservableList<HoaDon> selected = tbl.getSelectionModel().getSelectedItems();
		if (selected.isEmpty()) {
			showAlert("Chọn hóa đơn để xuất.");
			return;
		}
		List<HoaDon> hd = new ArrayList<HoaDon>();
		for (HoaDon hoaDon : selected) {
			hoaDon = hdDao.getById(hoaDon.getId());
			if (hoaDon != null) {
				hd.add(hoaDon);
			}

		}

		if (hd.isEmpty()) {
			showAlert("Hóa đơn không tồn tại.");
			return;
		}
		for (HoaDon hoaDon : hd) {
			if (!"DA_THANH_TOAN".equals(hoaDon.getTrangThai())) {
				showAlert("Chỉ có thể xuất PDF khi đã thanh toán.");
				return;
				
			}
		}
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialFileName("danh_sach_hoadon" + ".pdf");
		java.io.File file = fileChooser.showSaveDialog(tbl.getScene().getWindow());
		if (file != null) {
			boolean ok = hdController.exportPdf(hd, file.getAbsolutePath());
			showAlert(ok ? "Xuất PDF thành công." : "Xuất PDF thất bại.");
		}
	}

	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Thông báo");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}