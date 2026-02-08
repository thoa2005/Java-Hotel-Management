package taikhoan.viewfx;



import javafx.scene.paint.*;

import javafx.scene.text.Font;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class LoginViewFX {
    public TextField txtUser;
    public PasswordField txtPass;
    public ComboBox<String> cbRole;
    public Button btnLogin;
    public Button btnRegister;
    private Stage stage;
    
    public TextField txtPassVisible; 
    public Button btnShowPass;
    public Label labelKs=new Label("HỆ THỐNG KHÁCH SẠN");
  

    public LoginViewFX() {
    	
    	labelKs.setFont(new javafx.scene.text.Font("Leyton",50));
        txtUser = new TextField();
        txtPass = new PasswordField();
        cbRole = new ComboBox<>();
        cbRole.getItems().addAll("QUANLY", "NHANVIEN", "KHACH");
        cbRole.getSelectionModel().selectFirst();
        btnLogin = new Button("Đăng nhập");
        btnRegister = new Button("Đăng ký");
        txtPass = new PasswordField();
        
        txtPassVisible = new TextField();
        txtPassVisible.setVisible(false);
        txtPassVisible.setManaged(false);
        
        btnShowPass = new Button("👁");
        btnShowPass.getStyleClass().add("button-eye");
       
        
    }

    public void show(Stage stage) {
        this.stage = stage;
        stage.setTitle("Đăng nhập hệ thống");
        BorderPane root= new BorderPane();

        GridPane root1 = new GridPane(); 
        root1.setPadding(new Insets(20));
        root1.setHgap(10);
        root1.setVgap(10);
        

        
   
        root1.getStyleClass().add("root-background"); 

    
        root1.setAlignment(javafx.geometry.Pos.CENTER); 

        root1.add(new Label("Username:"), 0, 0);
        root1.add(txtUser, 1, 0);
        root1.add(new Label(""), 0, 1);
        root1.add(txtPass, 1, 1);
        root1.add(new Label("Vai Trò:"), 0, 2);
        root1.add(cbRole, 1, 2);
        root1.add(btnLogin, 1, 3);
        root1.add(btnRegister, 1, 4);

      
        Scene scene = new Scene(root, 1000, 550); 
  
        javafx.scene.layout.StackPane passGroup = new javafx.scene.layout.StackPane();
        passGroup.getChildren().addAll(txtPass, txtPassVisible);

       
        root1.add(new Label("Mật khẩu:"), 0, 1);
        root1.add(passGroup, 1, 1);
        root1.add(btnShowPass, 2, 1); 
        root.setCenter(root1);

        try {
            String css = getClass().getResource("login.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("Không tìm thấy file login.css!");
        }

        stage.setScene(scene);
        stage.show();
    }

    public Stage getStage() {
        return stage;
    }
}