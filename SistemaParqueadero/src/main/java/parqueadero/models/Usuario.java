package parqueadero.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Usuario {

    private String username;
    private String password;
    private String rol; // "ADMIN" o "USER"
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Constructor vacío (necesario para Gson)
    public Usuario() {}

    // Constructor con parámetros
    public Usuario(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    // Método para mostrar fecha bonita en la tabla
    public String getFechaCreacionFormateada() {
        return fechaCreacion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    // PROPIEDADES JAVAFX (ESTAS SON LAS QUE NECESITAS PARA LA TABLA)
    public StringProperty usernameProperty() {
        return new SimpleStringProperty(username);
    }

    public StringProperty passwordProperty() {
        return new SimpleStringProperty(password);
    }

    public StringProperty rolProperty() {
        return new SimpleStringProperty(rol);
    }

    public StringProperty fechaCreacionFormateadaProperty() {
        return new SimpleStringProperty(getFechaCreacionFormateada());
    }

    // Opcional: para mostrar bonito en consola
    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}