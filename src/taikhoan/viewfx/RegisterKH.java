package taikhoan.viewfx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterKH {
    public TextField txtUser;
    public PasswordField txtPass;
    public ComboBox<String> cbRole;
    public Button btnRegister;
    public Button btnBack;
    public TextField txtPassVisible; 
    public Button btnShowPass;
    private Stage stage;

    public RegisterKH() {
        txtUser = new TextField();
        txtPass = new PasswordField();
        cbRole = new ComboBox<>();
        cbRole.getItems().addAll("KHACH");
        cbRole.getSelectionModel().selectFirst();
        btnRegister = new Button("Đăng ký");
        btnBack = new Button("Quay lại");
        txtPassVisible = new TextField();
        txtPassVisible.setVisible(false); 
        txtPassVisible.setManaged(false);
        
        btnShowPass = new Button("👁"); 
        btnShowPass.getStyleClass().add("button-eye");
    }

    public void show(Stage stage) {
        this.stage = stage;
        stage.setTitle("Đăng ký tài khoản");

        GridPane root = new GridPane();
        root.getStyleClass().add("root-background");
        
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);
        
        root.setAlignment(javafx.geometry.Pos.CENTER);

        root.add(new Label("Username:"), 0, 0);
        root.add(txtUser, 1, 0);
        root.add(new Label("Password:"), 0, 1);
        root.add(txtPass, 1, 1);
        root.add(new Label("Vai trò:"), 0, 2);
        root.add(cbRole, 1, 2);
        root.add(btnRegister, 0, 3);
        root.add(btnBack, 1, 3);

        Scene scene = new Scene(root, 600, 450);
        javafx.scene.layout.StackPane passGroup = new javafx.scene.layout.StackPane();
        passGroup.getChildren().addAll(txtPass, txtPassVisible);

        root.add(new Label("Password:"), 0, 1);
        root.add(passGroup, 1, 1);    
        root.add(btnShowPass, 2, 1);
        try {
            String css = getClass().getResource("login.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("Không tìm thấy file CSS cho trang đăng ký!");
        }

        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}
