package taikhoan.controllerfx;
import java.io.IOException;


import dao.TaiKhoanDAO;
import taikhoan.viewfx.RegisterKH;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import taikhoan.viewfx.LoginViewFX;
import taikhoan.viewfx.Register;

public class RegisterControllerFX {
    private RegisterKH view;
    private TaiKhoanDAO dao;

    public RegisterControllerFX(RegisterKH view) {
        this.view = view;
       
        this.dao = new TaiKhoanDAO();

        view.btnRegister.setOnAction(e -> onRegister());
        view.btnBack.setOnAction(e -> onBack());
        view.btnShowPass.setOnAction(e -> togglePassword());
      
    }

    private void onRegister() {
        String user = view.txtUser.getText();
        String pass = view.txtPass.isVisible() ? view.txtPass.getText() : view.txtPassVisible.getText();
        String role = view.cbRole.getSelectionModel().getSelectedItem();

        if (user == null || user.trim().isEmpty()) {
            showAlert(AlertType.WARNING, "Vui lòng nhập username!");
            return;
        }
        if (pass == null || pass.isEmpty()) {
            showAlert(AlertType.WARNING, "Vui lòng nhập password!");
            return;
        }

        if (dao.existsByUsername(user)) {
            showAlert(AlertType.WARNING, "Tài khoản đã tồn tại!");
            return;
        }

        if (dao.register(user, pass, role)) {
            showAlert(AlertType.INFORMATION, "Đăng ký thành công!");
            try {
                LoginViewFX loginView = new LoginViewFX();
                
                new LoginControllerFX(loginView); 
                
                Stage currentStage = (Stage) view.btnRegister.getScene().getWindow();
                loginView.show(currentStage); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void onBack() {
        view.getStage().close();
        
        LoginViewFX loginView = new LoginViewFX();
        new LoginControllerFX(loginView); 
        loginView.show(new Stage());
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
