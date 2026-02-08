package taikhoan.controllerfx;


import dao.TaiKhoanDAO;
import model.TaiKhoan;
import taikhoan.viewfx.LoginViewFX;
import taikhoan.viewfx.RegisterKH;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class LoginControllerFX {
    private LoginViewFX view;
    private TaiKhoanDAO dao;

    public LoginControllerFX(LoginViewFX view) {
        this.view = view;
        this.dao = new TaiKhoanDAO();

        view.btnLogin.setOnAction(e -> onLogin());
        view.btnRegister.setOnAction(e -> openRegister());
        view.btnShowPass.setOnAction(e -> togglePassword());
    }

    private void onLogin() {
        String user = view.txtUser.getText();
        String pass = view.txtPass.isVisible() ? view.txtPass.getText() : view.txtPassVisible.getText();
        String role = view.cbRole.getSelectionModel().getSelectedItem();

        TaiKhoan tk = dao.login(user, pass, role);

        if (tk != null) {
            try {
                String fxml = null;
                switch (role) {
                    case "QUANLY":
                        fxml = "/taikhoan/viewfx/fxml/QuanLyView.fxml";
                        break;
                    case "NHANVIEN":
                        fxml = "/taikhoan/viewfx/fxml/NhanVienView.fxml";
                        break;
                    case "KHACH":
                        fxml = "/taikhoan/viewfx/fxml/KhachHangView.fxml";
                        break;
                }

                if (fxml != null) {
                	UserSession.setIdUser(tk.getId());
                                    	
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                    Parent root = loader.load();

                    Stage currentStage = view.getStage();
                    javafx.scene.Scene currentScene = currentStage.getScene();

                    currentScene.getStylesheets().clear();

                    currentScene.setRoot(root);
                    currentStage.sizeToScene();

                    currentStage.setTitle("Hệ thống - " + role);
                    currentStage.sizeToScene(); 
                   
                } else {
                    showAlert(AlertType.ERROR, "Không xác định role để mở giao diện.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(AlertType.ERROR, "Lỗi khi mở giao diện: " + ex.getMessage());
            }
        } else {
            showAlert(AlertType.ERROR, "Sai tài khoản, mật khẩu hoặc role");
        }
    }

    private void openRegister() {
        Stage currentStage = view.getStage();
        
        RegisterKH rv = new RegisterKH();
        new RegisterControllerFX(rv);
        
        rv.show(currentStage);
    }

    private void showAlert(AlertType type, String msg) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    private void togglePassword() {
        if (view.txtPass.isVisible()) {
            view.txtPassVisible.setText(view.txtPass.getText());
            view.txtPassVisible.setVisible(true);
            view.txtPassVisible.setManaged(true);
            view.txtPass.setVisible(false);
            view.txtPass.setManaged(false);
            view.btnShowPass.setText("🕶");
        } else {
            view.txtPass.setText(view.txtPassVisible.getText());
            view.txtPass.setVisible(true);
            view.txtPass.setManaged(true);
            view.txtPassVisible.setVisible(false);
            view.txtPassVisible.setManaged(false);
            view.btnShowPass.setText("👁");
        }
    }
}