package taikhoan.controllerfx;
import java.io.IOException;
import java.util.Optional;

import QLKH.controller.KhachHangController;
import dao.KhachHangDAO;
import dao.TaiKhoanDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.KhachHang;
import model.TaiKhoan;
import taikhoan.viewfx.Register;
import taikhoan.viewfx.RegisterKH;

public class QuanLyController {
    @FXML public Button btnLogout;
    @FXML public Button btnQLPhong;
    @FXML public Button btnQLNV;
    @FXML public Button btnQLKH;
    @FXML public Button btnThongKe;

    @FXML public TableView<?> tablePhong;
    @FXML public TableView<?> tableNhanVien;
    @FXML public TableView<KhachHang> tableKhachHang;

    @FXML public Button btnThem;
    @FXML public Button btnSua;
    @FXML public Button btnXoa;
    
    @FXML public Button btnConfirmDatPhong;
    @FXML private Label lblU;
    TaiKhoanDAO tkd=new TaiKhoanDAO();
    TaiKhoan tk= tkd.getByTaiKhoanId(UserSession.getIdUser());

    
    @FXML
    private TabPane mainTabPane; 
    @FXML
    private Tab tabKhachHang;   
  
    @FXML
    private TableColumn<KhachHang, Integer> colMa;
    @FXML
    private TableColumn<KhachHang, String> colTen, colSDT, colEmail;
   

    private KhachHangDAO khDAO = new KhachHangDAO();
    private ObservableList<KhachHang> khList = FXCollections.observableArrayList();
    
    @FXML
    public void handleQLPhong(ActionEvent event) {
    	try {
    		Parent root= FXMLLoader.load(getClass().getResource("/PHONG/view/QuanLy.fxml"));
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
    public void handleQLNV(ActionEvent event) {
        try {
			Parent root= FXMLLoader.load(getClass().getResource("/DTP/view/DatPhongView.fxml"));
			Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			Scene scene=new Scene(root,1000,550);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    public void handleThongKe(ActionEvent event) {
    	try {
    		Parent root= FXMLLoader.load(getClass().getResource("/thanhtoan/view/HoaDonListView.fxml"));
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
    public void handleThem() {
        System.out.println("handleThem");
    }

    @FXML
    public void handleSua() {
        System.out.println("handleSua");
    }

    @FXML
    public void handleXoa() {
        System.out.println("handleXoa");
    }

   
    @FXML
    public void handleLogout() {
        try {
            Stage st = (Stage) btnLogout.getScene().getWindow();
            LogoutHandlerFX.logout(st);
            System.out.println("Đã thực hiện đăng xuất Quản Lý");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML private Button btnQuanLy;
   
    @FXML
   public void handleRegister(ActionEvent event) {
    	Alert alert=new  Alert(AlertType.CONFIRMATION, "Đăng ký tài Khoản cho NHÂN VIÊN hoặc QUẢN LÝ khác.",ButtonType.YES,ButtonType.NO);
    	Optional<ButtonType> result = alert.showAndWait();
    	
    	if(result.isPresent() && result.get() == ButtonType.YES) {
    	Stage st=(Stage) ((Node) event.getSource()).getScene().getWindow();
    	taikhoan.viewfx.Register rg= new Register();
    	rg.show(st);
    	}
    }
    @FXML
    private void handleLogin(ActionEvent event) {
        String role = "";
        Button clickedButton = (Button) event.getSource();

        if (clickedButton == btnQuanLy) role = "QuanLy";
        openMainView(role, clickedButton);
    }
    @FXML
    private void handleExit() {
        Platform.exit();
        System.exit(0);
    }

    private void openMainView(String role, Button btn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/QLKH/view/KhachHangView.fxml"));
            Parent root = loader.load();

            KhachHangController controller = loader.getController();
            controller.setRole(role);

            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setScene(new Scene(root));
       
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize() {
    	lblU.setText("username: "+tk.getUsername());
    	
        colMa.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTen.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colSDT.setCellValueFactory(new PropertyValueFactory<>("sdt"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableKhachHang.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-background-color: white;");
        
        colMa.setStyle("-fx-text-fill: white;");
        colTen.setStyle("-fx-text-fill: white;");
        colSDT.setStyle("-fx-text-fill: white;");
        colEmail.setStyle("-fx-text-fill: white;");
    }
    @FXML
    private void handleTabKhachHangSelected(Event event) {
        if (tabKhachHang.isSelected()) {
            loadKhachHangData();
        }
    }

    private void loadKhachHangData() {
        khList.clear();
        khList.addAll(khDAO.getAll()); 
        tableKhachHang.setItems(khList);
        tableKhachHang.refresh();
    }
    @FXML
    private void handleThanhToan(ActionEvent event) {
    	try {
    		Parent root= FXMLLoader.load(getClass().getResource("/thanhtoan/view/HoaDonView.fxml"));
    		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    		Scene scene= new Scene(root,1000,550);
    		stage.setScene(scene);
    		stage.show();
    	  } catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	  }
    }
    

}
