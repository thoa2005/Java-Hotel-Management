package taikhoan.controllerfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Phong;
import model.TaiKhoan;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import viewfx.KhachHangViewFX; // Dòng này có thể xóa nếu không dùng ở đâu khác
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.FlowPane;

import java.io.IOException;

import QLKH.controller.*;
import dao.PhongDAO;
import dao.TaiKhoanDAO;


public class KhachHangController {
	@FXML private Label lblKhachHang; 
	

    @FXML private TableColumn<Phong, String> colMaPhong;
    @FXML private TableColumn<Phong, String> colLoaiPhong;
    @FXML private TableColumn<Phong, Double> colGia;
    @FXML private TableColumn<Phong, String> colTrangThai;

    private PhongDAO phongDAO = new PhongDAO();
    private ObservableList<Phong> phongList = FXCollections.observableArrayList();
    
    @FXML public Button btnLogout;
    @FXML public Button btnXemPhong;
    @FXML public Button btnDatPhong;
    @FXML public Button btnLichSu;
    @FXML public Button btnThongTin;
    @FXML public TableView<Phong> tableCenter;
    @FXML public DatePicker dpFrom;
    @FXML public DatePicker dpTo;
    @FXML public Button btnConfirmDatPhong;
    @FXML private Label lblU;
    TaiKhoanDAO tkd=new TaiKhoanDAO();
    TaiKhoan tk= tkd.getByTaiKhoanId(UserSession.getIdUser());

    public KhachHangController() {
    }

    @FXML
    public void initialize() {
    	lblU.setText("username: "+tk.getUsername());
    	
        TableView<Phong> table = (TableView<Phong>) tableCenter;

        colMaPhong.setCellValueFactory(new PropertyValueFactory<>("maPhong"));
        colLoaiPhong.setCellValueFactory(new PropertyValueFactory<>("loaiPhong"));
        colGia.setCellValueFactory(new PropertyValueFactory<>("gia"));
        colTrangThai.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

        loadRoomData();
    	
        if (lblKhachHang != null) {
            lblKhachHang.setText(""); 
            typewriterEffect(lblKhachHang, "Phục Vụ Những Gì Bạn Xứng Đáng");
        }
    }

    private void typewriterEffect(Label label, String text) {
        label.setTextOverrun(OverrunStyle.CLIP);
        
        final int[] charIndex = {0};
        final boolean[] isAdding = {true};
        final int[] pauseCount = {0};

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
    public void handleLogout() {
        try {
            Stage st = (Stage) btnLogout.getScene().getWindow();
            LogoutHandlerFX.logout(st);
            System.out.println("Đã thực hiện đăng xuất khách hàng.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleXemPhong(ActionEvent event) {
      try {
		Parent root= FXMLLoader.load(getClass().getResource("/PHONG/view/Khachhang.fxml"));
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene= new Scene(root,1000,550);
		stage.setScene(scene);
		stage.show();
	  } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
      
    }

    @FXML
    public void handleDatPhong() {
    	
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/DTP/view/DatPhongKhachHangView.fxml"));
		

    	
    	Stage stage = (Stage) btnDatPhong.getScene().getWindow();

    	
    	Scene scene = new Scene(root,1000,550);
    	stage.setScene(scene);

    	stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    public void handleLichSu() {
        System.out.println("handleLichSu");
        showInfo("Hiển thị lịch sử đặt phòng.");
    }

    @FXML
    public void handleThongTin() {
        System.out.println("handleThongTin");
        showInfo("Mở thông tin cá nhân.");
    }


    private void showAlert(AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    private void showInfo(String msg) {
        System.out.println(msg);
    }
    @FXML private Button btnKhachHang;

    @FXML
    private void handleLogin(ActionEvent event) {
    	
    	    try {
    	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QLKH/view/KhachHangView.fxml"));
    	        Parent root = loader.load();
    	        
    	        
                QLKH.controller.KhachHangController controller = loader.getController();
                controller.setRole("KhachHang"); 

    	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	        
    	        stage.getScene().setRoot(root);
    	        stage.sizeToScene();
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
    	
    }
    @FXML
    private void handleExit() {
        Platform.exit();
        System.exit(0);
    }
    private void loadRoomData() {
        phongList.clear();
        phongList.addAll(phongDAO.getAllPhong()); 
        
        ((TableView<Phong>) tableCenter).setItems(phongList);
    }

   
}