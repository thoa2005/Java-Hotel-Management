package taikhoan.viewfx;

import dao.TaiKhoanDAO;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import taikhoan.controllerfx.LoginControllerFX;

public class Register {
	public TextField txtUser;
	public PasswordField txtPass;
	public ComboBox<String> cbRole;
	public Button btnRegister;
	public Button btnBack;
	public TextField txtPassVisible;
	public Button btnShowPass;
	private Stage stage;
	private Register view;
	private TaiKhoanDAO dao;

	public Register() {
		this.dao = new TaiKhoanDAO();
		txtUser = new TextField();
		txtPass = new PasswordField();
		cbRole = new ComboBox<>();
		cbRole.getItems().addAll("QUANLY", "NHANVIEN");
		cbRole.getSelectionModel().selectFirst();
		btnRegister = new Button("Đăng ký");
		btnRegister.setOnAction(event -> {
			String user = txtUser.getText();
			String pass = txtPass.isVisible() ? txtPass.getText() : txtPassVisible.getText();
			String role = cbRole.getSelectionModel().getSelectedItem();

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

					Stage currentStage = (Stage) btnRegister.getScene().getWindow();
					loginView.show(currentStage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnBack = new Button("Quay lại");
		btnBack.setOnAction(event -> {
			try {

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/taikhoan/viewfx/fxml/QuanLyView.fxml"));
				Parent root = loader.load();

				Stage stage = (Stage) btnBack.getScene().getWindow();

				
				stage.getScene().setRoot(root);stage.sizeToScene();   
				stage.centerOnScreen(); 
			} catch (Exception e) {
				// TODO: handle exception
			}
		});
		txtPassVisible = new TextField();
		txtPassVisible.setVisible(false); 
		txtPassVisible.setManaged(false);

		btnShowPass = new Button("👁");
		btnShowPass.getStyleClass().add("button-eye");
		btnShowPass.setOnAction(event -> {
			if (txtPass.isVisible()) {
				txtPassVisible.setText(txtPass.getText());
				txtPassVisible.setVisible(true);
				txtPassVisible.setManaged(true);
				txtPass.setVisible(false);
				txtPass.setManaged(false);
				btnShowPass.setText("🕶");
			} else {
				txtPass.setText(txtPassVisible.getText());
				txtPass.setVisible(true);
				txtPass.setManaged(true);
				txtPassVisible.setVisible(false);
				txtPassVisible.setManaged(false);
				btnShowPass.setText("👁");
			}
		});
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

	private void showAlert(AlertType type, String msg) {
		Alert a = new Alert(type);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}

	public Stage getStage() {
		return stage;
	}
}
