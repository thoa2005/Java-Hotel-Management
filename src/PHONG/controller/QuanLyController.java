package PHONG.controller;

import model.Phong; // Đảm bảo bạn đã có class Model Phong
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.PhongDAO;

public class QuanLyController implements Initializable {

    @FXML private TableView<Phong> tablePhong;
    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, String> colLoaiPhong;
    @FXML private TableColumn<Phong, Double> colGia;
    @FXML private TableColumn<Phong, String> colTrangThai;

    @FXML private TextField txtMaPhong;
    @FXML private TextField txtGia;
    @FXML private ComboBox<String> cbLoaiPhong;
    @FXML private ComboBox<String> cbTrangThai;

    private ObservableList<Phong> masterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colLoaiPhong.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        colGia.setCellValueFactory(new PropertyValueFactory<>("gia"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        cbLoaiPhong.getItems().addAll("VIP", "Thường", "Đôi");
        cbTrangThai.getItems().addAll("TRONG", "DA DAT");

        tablePhong.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                hienThiChiTiet(newValue);
            }
        });

        loadDataFromDatabase();
    }

    private void hienThiChiTiet(Phong p) {
        txtMaPhong.setText(p.getMaPhong());
        txtGia.setText(String.valueOf(p.getGia()));
        cbLoaiPhong.setValue(p.getLoaiPhong());
        cbTrangThai.setValue(p.getTrangThai());
    }

    private void loadDataFromDatabase() {
    	PhongDAO phongDao = new PhongDAO();
        List<Phong> list = phongDao.getAllPhong();
        
        masterData.clear();
        masterData.addAll(list);
        tablePhong.setItems(masterData);
    }

    @FXML
    private void handleThem() {
        String ma = txtMaPhong.getText();
        String loai = cbLoaiPhong.getValue();
        double gia = Double.parseDouble(txtGia.getText());
        String trangThai = cbTrangThai.getValue();

        Phong p = new Phong(0, ma, loai, gia, trangThai);

        PhongDAO phongDao = new PhongDAO(); 
        phongDao.insert(p);
        
        loadDataFromDatabase();
        handleLamMoi();
    }

    @FXML
    private void handleSua() {
    	Phong selected = tablePhong.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setMaPhong(txtMaPhong.getText());
            selected.setLoaiPhong(cbLoaiPhong.getValue());
            selected.setGia(Double.parseDouble(txtGia.getText()));
            selected.setTrangThai(cbTrangThai.getValue());

            PhongDAO phongDao = new PhongDAO();
            if (phongDao.update(selected)) {
                loadDataFromDatabase();
            }
        }
    }

    @FXML
    private void handleXoa() {
    	Phong selected = tablePhong.getSelectionModel().getSelectedItem();
        if (selected != null) {
            PhongDAO phongDao = new PhongDAO();
            phongDao.delete(selected.getId());
            
            loadDataFromDatabase();
            handleLamMoi();
        }
    }

    @FXML
    private void handleLamMoi() {
        txtMaPhong.clear();
        txtGia.clear();
        cbLoaiPhong.setValue(null);
        cbTrangThai.setValue(null);
        tablePhong.getSelectionModel().clearSelection();
    }
    @FXML
    void handleBack(ActionEvent event) {
   	try {
			Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

			String fxmlPath = "/taikhoan/viewfx/fxml/QuanLyView.fxml"; 

			Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
			stage.getScene().setRoot(root);

		} catch (Exception e) {
			e.printStackTrace();
		}
   }
}