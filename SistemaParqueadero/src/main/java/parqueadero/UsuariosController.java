package parqueadero;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsuariosController {

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;
    @FXML private ComboBox<String> cbRol;
    @FXML private TableView<Usuario> tablaUsuarios;

    // ✅ CAMBIAR A PÚBLICA PARA SimpleDataManager
    public static ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    // ✅ MÉTODOS ESTÁTICOS PARA SimpleDataManager
    public static ObservableList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public static void limpiarUsuarios() {
        listaUsuarios.clear();
    }

    public static void agregarUsuario(Usuario usuario) {
        listaUsuarios.add(usuario);
    }

    public static class Usuario {
        private final SimpleStringProperty username;
        private final SimpleStringProperty password;
        private final SimpleStringProperty rol;
        private final SimpleStringProperty fechaCreacion;

        public Usuario(String username, String password, String rol) {
            this.username = new SimpleStringProperty(username);
            this.password = new SimpleStringProperty(password);
            this.rol = new SimpleStringProperty(rol);
            this.fechaCreacion = new SimpleStringProperty(
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            );
        }

        // Getters
        public String getUsername() { return username.get(); }
        public SimpleStringProperty usernameProperty() { return username; }
        public String getPassword() { return password.get(); }
        public SimpleStringProperty passwordProperty() { return password; }
        public String getRol() { return rol.get(); }
        public SimpleStringProperty rolProperty() { return rol; }
        public String getFechaCreacionFormateada() { return fechaCreacion.get(); }
        public SimpleStringProperty fechaCreacionProperty() { return fechaCreacion; }
    }

    @FXML
    private void initialize() {
        cbRol.getItems().addAll("Administrador", "Operador");
        cbRol.setValue("Operador");

        TableColumn<Usuario, String> colUsername = new TableColumn<>("Usuario");
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

        TableColumn<Usuario, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacionFormateada"));

        tablaUsuarios.getColumns().setAll(colUsername, colRol, colFecha);
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void crearUsuario() {
        String user = txtUser.getText().trim();
        String pass = txtPass.getText();
        String rol = cbRol.getValue();

        if (user.isEmpty() || pass.isEmpty() || rol == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Complete todos los campos");
            alert.show();
            return;
        }

        if (listaUsuarios.stream().anyMatch(u -> u.getUsername().equals(user))) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("El usuario ya existe");
            alert.show();
            return;
        }

        Usuario nuevoUsuario = new Usuario(user, pass, rol);
        listaUsuarios.add(nuevoUsuario);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Usuario creado exitosamente");
        alert.show();

        txtUser.clear();
        txtPass.clear();
        cbRol.setValue("Operador");
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado != null && !seleccionado.getUsername().equals("admin")) {
            listaUsuarios.remove(seleccionado);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Usuario eliminado");
            alert.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No se puede eliminar el usuario admin");
            alert.show();
        }
    }

    @FXML
    private void volverDashboard() {
        App.changeScene("/parqueadero/dashboard.fxml", "Dashboard");
    }
}