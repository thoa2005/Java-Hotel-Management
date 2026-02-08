package DTP.controller;

import dao.DatPhongDAO;
import dao.KhachHangDAO; 
import dao.PhongDAO; 
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DatPhong;
import model.KhachHang;
import model.Phong;

import java.time.LocalDate;
import java.util.Optional;

public class DatPhongController {
	@FXML
	private ComboBox<KhachHang> cbCustomer;
	@FXML
	private ComboBox<Phong> cbRoom;
	@FXML
	private DatePicker dpCheckIn;
	@FXML
	private DatePicker dpCheckOut;

	@FXML
	private TableView<DatPhong> table;
	@FXML
	private TableColumn<DatPhong, Integer> colId;
	@FXML
	private TableColumn<DatPhong, String> colCustomer;
	@FXML
	private TableColumn<DatPhong, String> colRoom;
	@FXML
	private TableColumn<DatPhong, LocalDate> colCheckIn;
	@FXML
	private TableColumn<DatPhong, LocalDate> colCheckOut;
	@FXML
	private TableColumn<DatPhong, String> colStatus;

	private DatPhongDAO datPhongDAO = new DatPhongDAO();
	private PhongDAO phongDAO = new PhongDAO();
	private KhachHangDAO khachHangDAO = new KhachHangDAO();

	private ObservableList<DatPhong> listDatPhong = FXCollections.observableArrayList();
	@FXML
	private Button btnSave;

	@FXML
	public void initialize() {
		loadComboBoxData();

		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colCustomer.setCellValueFactory(new PropertyValueFactory<>("tenKhachHang"));
		colRoom.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
		colCheckIn.setCellValueFactory(new PropertyValueFactory<>("ngayNhan"));
		colCheckOut.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

		refreshTable();

		setupDateValidation();

		table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				fillForm(newVal);
			}
		});
	}

	private void loadComboBoxData() {
		cbCustomer.setItems(FXCollections.observableArrayList(khachHangDAO.getAll()));
		cbRoom.setItems(FXCollections.observableArrayList(phongDAO.getAllPhongTrong()));
	}

	private void setupDateValidation() {
		dpCheckIn.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				setDisable(empty || date.isBefore(LocalDate.now()));
			}
		});

		dpCheckOut.setDayCellFactory(picker -> new DateCell() {
			public void updateItem(LocalDate date, boolean empty) {
				super.updateItem(date, empty);
				if (dpCheckIn.getValue() != null) {
					setDisable(empty || date.isBefore(dpCheckIn.getValue().plusDays(1)));
				}
			}
		});
	}

	private void refreshTable() {
		listDatPhong.clear();
		listDatPhong.addAll(datPhongDAO.getAll());
		table.setItems(listDatPhong);
	}

	private void fillForm(DatPhong dp) {
		for (KhachHang kh : cbCustomer.getItems()) {
			if (kh.getId() == dp.getKhachhangId()) {
				cbCustomer.setValue(kh);
				break;
			}
		}
		for (Phong p : cbRoom.getItems()) {
			if (p.getId() == dp.getPhongId()) {
				cbRoom.setValue(p);
				break;
			}
		}
		dpCheckIn.setValue(dp.getNgayNhan());
		dpCheckOut.setValue(dp.getNgayTra());
	}

	@FXML
	private void handleSave() {
		if (validateInput()) {
			DatPhong dp = new DatPhong();
			dp.setKhachhangId(cbCustomer.getValue().getId());
			dp.setPhongId(cbRoom.getValue().getId());
			dp.setNgayNhan(dpCheckIn.getValue());
			dp.setNgayTra(dpCheckOut.getValue());

			if (datPhongDAO.save(dp)) {
				showAlert("Thành công", "Đã đặt phòng thành công!");
				refreshTable();
				loadComboBoxData();
				
				handleClear();
			} else {
				showAlert("Lỗi", "Không thể lưu dữ liệu.");
			}
		}
	}

	@FXML
	private void handleDelete() {
		DatPhong selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Hủy đặt phòng này?").showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				if (datPhongDAO.delete(selected.getId(), selected.getPhongId())) {
					refreshTable();
					handleClear();
				}
			}
		}
	}

	@FXML
	private void handleClear() {
		cbCustomer.setValue(null);
		cbRoom.setValue(null);
		dpCheckIn.setValue(null);
		dpCheckOut.setValue(null);
		table.getSelectionModel().clearSelection();
		loadComboBoxData();
	}

	private boolean validateInput() {
		if (cbCustomer.getValue() == null || cbRoom.getValue() == null || dpCheckIn.getValue() == null
				|| dpCheckOut.getValue() == null) {
			showAlert("Cảnh báo", "Vui lòng nhập đầy đủ thông tin!");
			return false;
		}
		if (dpCheckOut.getValue().isBefore(dpCheckIn.getValue())) {
			showAlert("Lỗi", "Ngày đi phải sau ngày đến!");
			return false;
		}
		return true;
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	@FXML
	private void handleUpdate() {
		DatPhong selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			selected.setPhongId(cbRoom.getValue().getId());
			selected.setKhachhangId(cbCustomer.getValue().getId());
			selected.setNgayNhan(dpCheckIn.getValue());
			selected.setNgayTra(dpCheckOut.getValue());


			if (datPhongDAO.update(selected)) {
				refreshTable();
				showAlert("Thành công", "Đã cập nhật đơn đặt phòng!");
			}
		}
	}

	@FXML
	private void handleNextStatus() {
		DatPhong selected = table.getSelectionModel().getSelectedItem();
		if (selected != null) {
			if (!selected.getTrangThai().equals("DA_TRA"))
			{
				selected.setTrangThai("Đã xác nhận");
				if (datPhongDAO.update(selected)) {
					loadComboBoxData();
					refreshTable();
					
					PhongDAO.updateStatus(PhongDAO.getPByMa(selected.getMaPhong()));
					
					
					
					showAlert("Thành công", "Đã cập nhật trạng thái!");
				}
			}
			else {
			showAlert("LỖI","Không thể cập nhật trạng thái phòng đã thanh toán");
			}

		}

	}

	@FXML
	private void handleBack(ActionEvent event) {
		try {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			String title = stage.getTitle();

			String fxmlPath = "/taikhoan/viewfx/fxml/KhachHangView.fxml"; 

			if (title.contains("QUANLY")) {
				fxmlPath = "/taikhoan/viewfx/fxml/QuanLyView.fxml";
			} else if (title.contains("NHANVIEN")) {
				fxmlPath = "/taikhoan/viewfx/fxml/NhanVienView.fxml";
			}

			Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
			stage.getScene().setRoot(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}