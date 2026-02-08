package taikhoan.controllerfx;
import javafx.scene.control.Alert;

import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import taikhoan.viewfx.LoginViewFX;
import taikhoan.controllerfx.LoginControllerFX;

import java.util.Optional;

public class LogoutHandlerFX {
    public static void logout(Stage currentStage) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText(null);
        confirm.setContentText("Bạn có chắc muốn đăng xuất?");

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            currentStage.close();
            Stage s = new Stage();
            LoginViewFX lv = new LoginViewFX();
            new LoginControllerFX(lv);
            lv.show(s);
        }
    }
}
