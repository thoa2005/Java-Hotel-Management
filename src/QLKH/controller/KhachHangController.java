package QLKH.controller;

import dao.KhachHangDAO;

import model.KhachHang;
import taikhoan.controllerfx.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;

public class KhachHangController {

	@FXML
	private TableView<KhachHang> tableKhachHang;
	@FXML
	private TableColumn<KhachHang, Integer> colID;
	@FXML
	private TableColumn<KhachHang, String> colName;
	@FXML
	private TableColumn<KhachHang, String> colPhone;
	@FXML
	private TableColumn<KhachHang, String> colCCCD;
	@FXML
	private TableColumn<KhachHang, String> colEmail;
	@FXML
	private TableColumn<KhachHang, String> colGender;
	@FXML
	private TableColumn<KhachHang, String> colIDUser;

	@FXML
	private TextField tfID, tfName, tfPhone, tfCCCD, tfEmail, tfSearch, tfIDuser;
	@FXML
	private ComboBox<String> cbGender;
	@FXML
	private Button btnAdd, btnEdit, btnDelete, btnSort;
	@FXML
	private HBox boxSearch;

	private KhachHangDAO dao;
	private ObservableList<KhachHang> masterData = FXCollections.observableArrayList();
	private String userRole;

	private boolean isSorted = false;

	public void initialize() {
		dao = new KhachHangDAO();

		colID.setCellValueFactory(new PropertyValueFactory<>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		colPhone.setCellValueFactory(new PropertyValueFactory<>("sdt"));
		colCCCD.setCellValueFactory(new PropertyValueFactory<>("cccd"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colGender.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		colIDUser.setCellValueFactory(new PropertyValueFactory<>("IDUser"));

		cbGender.getItems().addAll("Nam", "Nu");

		tableKhachHang.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				fillForm(newSelection);
			}
		});

		tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
			filterData(newValue);
		});

		loadData();
	}

	public void setRole(String role) {
		this.userRole = role;

		btnAdd.setVisible(false);
		btnAdd.setManaged(false);
		btnEdit.setVisible(false);
		btnEdit.setManaged(false);
		btnDelete.setVisible(false);
		btnDelete.setManaged(false);
		boxSearch.setVisible(false);
		boxSearch.setManaged(false);

		tableKhachHang.setVisible(true);
		tableKhachHang.setManaged(true);

		if ("QuanLy".equalsIgnoreCase(role)) {
			btnAdd.setVisible(true);
			btnAdd.setManaged(true);
			btnEdit.setVisible(true);
			btnEdit.setManaged(true);
			btnDelete.setVisible(true);
			btnDelete.setManaged(true);
			boxSearch.setVisible(true);
			boxSearch.setManaged(true);

		} else if ("NhanVien".equalsIgnoreCase(role)) {
			boxSearch.setVisible(true);
			boxSearch.setManaged(true);

		} else if ("KhachHang".equalsIgnoreCase(role)) {
			dao = new KhachHangDAO();
			int currentIdUser = UserSession.getIdUser();
			if (currentIdUser > 0) {
				KhachHang kh = dao.getByUserId(currentIdUser);
				if (kh != null) {
					fillForm(kh); 
				}
			}
			btnAdd.setVisible(true);
			btnAdd.setManaged(true);

			btnAdd.setText("Gửi Khai Báo");

			tableKhachHang.setVisible(false);
			tableKhachHang.setManaged(false);
		}
	}

	private void filterData(String keyword) {
		FilteredList<KhachHang> filteredData = new FilteredList<>(masterData, p -> true);
		filteredData.setPredicate(khachHang -> {
			if (keyword == null || keyword.isEmpty()) {
				return true;
			}
			String lowerCaseFilter = keyword.toLowerCase();
			return khachHang.getHoTen().toLowerCase().contains(lowerCaseFilter);
		});

		SortedList<KhachHang> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(tableKhachHang.comparatorProperty());
		tableKhachHang.setItems(sortedData);
	}

	@FXML
	private void handleSort() {
		if (!isSorted) {
			FXCollections.sort(masterData, new Comparator<KhachHang>() {
				@Override
				public int compare(KhachHang o1, KhachHang o2) {
					String name1 = o1.getHoTen().substring(o1.getHoTen().lastIndexOf(" ") + 1);
					String name2 = o2.getHoTen().substring(o2.getHoTen().lastIndexOf(" ") + 1);
					return name1.compareToIgnoreCase(name2);
				}
			});

			btnSort.setText("Hủy sắp xếp (Mặc định)");
			isSorted = true; 

		} else {
			FXCollections.sort(masterData, Comparator.comparingInt(KhachHang::getId));

			btnSort.setText("Sắp xếp tên A-Z");
			isSorted = false; 
		}

		filterData(tfSearch.getText());
	}

	private void loadData() {
		masterData.clear();
		masterData.addAll(dao.getAll());
		tableKhachHang.setItems(masterData);
	}

	@FXML
	private void handleAdd() {
		String idStr = tfID.getText();
		if (idStr==null||idStr.trim().isEmpty()) {
			KhachHang kh = new KhachHang(0, tfName.getText(), tfPhone.getText(), tfCCCD.getText(), tfEmail.getText(),
					cbGender.getValue(), UserSession.getIdUser());
			
			if (dao.add(kh)) {
				showAlert("Thành công", "Đã thêm/khai báo khách hàng mới.");
				loadData();
				
			} else {
				showAlert("Lỗi", "Thêm thất bại!");
			}
		}
		else {
			int id = Integer.parseInt(idStr);
			KhachHang kh = new KhachHang(id, tfName.getText(), tfPhone.getText(), tfCCCD.getText(), tfEmail.getText(),
					cbGender.getValue(), UserSession.getIdUser());
			if (dao.update(kh)) {
				showAlert("Thành công", "Đã khai báo khách hàng.");
				loadData();
				
			} else {
				showAlert("Lỗi", "Thêm thất bại!");
			}
			
		}
	}

	@FXML
	private void handleUpdate() {
		try {
			int id = Integer.parseInt(tfID.getText());
			KhachHang kh = new KhachHang(id, tfName.getText(), tfPhone.getText(), tfCCCD.getText(), tfEmail.getText(),
					cbGender.getValue(), UserSession.getIdUser());
			if (dao.update(kh)) {
				showAlert("Thành công", "Cập nhật thành công.");
				loadData();
			}
		} catch (Exception e) {
			showAlert("Lỗi", "Vui lòng chọn khách hàng để sửa.");
		}
	}

	@FXML
	private void handleDelete() {
		try {
			int id = Integer.parseInt(tfID.getText());
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Xác nhận");
			alert.setHeaderText("Bạn có chắc muốn xóa?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.isPresent() && result.get() == ButtonType.OK) {
				if (dao.delete(id)) {
					showAlert("Thành công", "Xóa thành công.");
					loadData();
					handleClear();
				}
			}
		} catch (Exception e) {
			showAlert("Lỗi", "Vui lòng chọn khách hàng để xóa.");
		}
	}

	@FXML
	private void handleClear() {
		tfID.clear();
		tfName.clear();
		tfPhone.clear();
		tfCCCD.clear();
		tfEmail.clear();
		cbGender.getSelectionModel().clearSelection();
	}

	@FXML
	private void handleLogout() {

		try {
			Stage stage = (Stage) btnAdd.getScene().getWindow();
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

	private void fillForm(KhachHang kh) {
		tfID.setText(String.valueOf(kh.getId()));
		tfName.setText(kh.getHoTen());
		tfPhone.setText(kh.getSdt());
		tfCCCD.setText(kh.getCccd());
		tfEmail.setText(kh.getEmail());
		cbGender.setValue(kh.getGioiTinh());
	}

	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
