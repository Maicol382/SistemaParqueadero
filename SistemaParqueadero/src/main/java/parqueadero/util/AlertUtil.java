// parqueadero/util/AlertUtil.java
package parqueadero.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtil {

    public static void error(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static void error(String message) {
        error("Error", message);
    }

    public static void info(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void info(String message) {
        info("Información", message);
    }

    public static void success(String message) {
        show(Alert.AlertType.INFORMATION, "Éxito", message);
    }

    public static boolean confirm(String message) {
        return confirm("Confirmar acción", message);
    }

    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}