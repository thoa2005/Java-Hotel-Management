package taikhoan.controllerfx;
import javafx.stage.Stage;

import taikhoan.viewfx.*;

public class RegisterLauncher {
    public static void openRegisterProgrammatic() {
        Stage s = new Stage();
        RegisterKH rv = new RegisterKH();
        new RegisterControllerFX(rv);
        rv.show(s);
    }

    public static void openLogin(Stage currentStage) {
        if (currentStage != null) currentStage.close();
        LoginViewFX lv = new LoginViewFX();
        new LoginControllerFX(lv);
        lv.show(new Stage());
    }
}