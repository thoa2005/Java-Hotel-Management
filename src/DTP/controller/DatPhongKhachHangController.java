package DTP.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.KhachHang;
import model.DatPhong;
import dao.KhachHangDAO;
import dao.DatPhongDAO;
import dao.PhongDAO;
import taikhoan.controllerfx.UserSession;

public class DatPhongKhachHangController {

    @FXML private TextField txtTenKH, txtSDT, txtCCCD, txtEmail;
    @FXML private TableView<DatPhong> tableLichSu;
    @FXML private TableColumn<DatPhong, String> colMaPhong, colNgayNhan, colNgayTra, colTrangThai;

    private KhachHangDAO khachHangDAO = new KhachHangDAO();
    private DatPhongDAO datPhongDAO = new DatPhongDAO();
    private PhongDAO phongDAO = new PhongDAO();

    @FXML
    public void initialize() {
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colNgayNhan.setCellValueFactory(new PropertyValueFactory<>("ngayNhan"));
        colNgayTra.setCellValueFactory(new PropertyValueFactory<>("ngayTra"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        refreshData();
    }

    private void refreshData() {
        int idLog = UserSession.getIdUser();
        KhachHang kh = khachHangDAO.getByUserId(idLog);

        if (kh != null) {
            txtTenKH.setText(kh.getHoTen());
            txtSDT.setText(kh.getSdt());
            txtCCCD.setText(kh.getCccd());
            txtEmail.setText(kh.getEmail());

            ObservableList<DatPhong> list = FXCollections.observableArrayList(datPhongDAO.getByIdKhach(kh.getId()));
            tableLichSu.setItems(list);
        }
    }

    @FXML
    private void handleDelete() {
        DatPhong selected = tableLichSu.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Thông báo", "Vui lòng chọn phòng để hủy!");
            return;
        }

        if ("Đang xử lý".equals(selected.getTrangThai())) {
            if (datPhongDAO.delete(selected.getId())) {
                phongDAO.capNhatTrangThai(selected.getPhongId(), "Trống");
                
                refreshData();
                showAlert("Thành công", "Đã hủy phòng thành công!");
            }
        } else {
            showAlert("Lỗi", "Không thể hủy phòng đã nhận hoặc đã hoàn tất!");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleBack(ActionEvent event) {
    	try {
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
			String title = stage.getTitle();

			String fxmlPath = "/taikhoan/viewfx/fxml/KhachHangView.fxml"; 

			Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
			stage.getScene().setRoot(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}