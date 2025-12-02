package parqueadero;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import parqueadero.services.UsuarioService;
import parqueadero.util.AlertUtil;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void login() {

        String user = txtUsuario.getText();
        String pass = txtPassword.getText();

        if (usuarioService.autenticar(user, pass)) {
            App.changeScene("dashboard.fxml", "Parqueadero - Dashboard");
        } else {
            AlertUtil.error("Credenciales incorrectas");
        }
    }
}
