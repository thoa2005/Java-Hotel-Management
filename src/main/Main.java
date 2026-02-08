package main;

import taikhoan.controllerfx.LoginControllerFX;
import javafx.application.Application;
import javafx.stage.Stage;
import taikhoan.viewfx.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginViewFX view = new LoginViewFX();
        new LoginControllerFX(view);
        view.show(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}