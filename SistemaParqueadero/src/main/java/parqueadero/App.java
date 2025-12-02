package parqueadero;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App extends Application {

    private static Stage primaryStage;
    private static EstadisticasController estadisticasController; // Referencia activa

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Iniciando Sistema de Parqueadero...");

        SimpleDataManager.iniciar();

        try {
            Files.createDirectories(Paths.get("data"));
            System.out.println("Carpeta 'data' preparada");
        } catch (IOException e) {
            System.err.println("Error creando carpeta data: " + e.getMessage());
        }

        primaryStage = stage;
        primaryStage.setTitle("Parqueadero Inteligente v2.0");
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            System.out.println("\nCerrando aplicación...");
            SimpleDataManager.cerrar();
            System.out.println("¡Datos guardados correctamente!");
            System.out.println("¡Gracias por usar el sistema!");
        });

        if (UsuariosController.getListaUsuarios().isEmpty()) {
            UsuariosController.agregarUsuario(
                    new UsuariosController.Usuario("admin", "1234", "Administrador")
            );
            System.out.println("USUARIO ADMIN CREADO → usuario: admin | contraseña: 1234");
        }

        // ====== RUTA CORREGIDA ======
        changeScene("/parqueadero/login.fxml", "Parqueadero - Iniciar Sesión");
        primaryStage.show();

        System.out.println("¡SISTEMA INICIADO CORRECTAMENTE!");
    }

    public static void changeScene(String fxmlFile, String title) {
        try {
            String normalizedPath = normalizeFxmlPath(fxmlFile);
            java.net.URL resourceUrl = App.class.getResource(normalizedPath);

            if (resourceUrl == null) {
                System.err.println("No se encontró el recurso FXML: " + normalizedPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Scene scene;
            scene = new Scene(loader.load(), 1100, 700);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            System.err.println("Error cargando " + fxmlFile + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String normalizeFxmlPath(String fxmlFile) {
        if (fxmlFile == null || fxmlFile.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo FXML no puede estar vacío");
        }

        if (fxmlFile.startsWith("/")) {
            return fxmlFile;
        }

        if (!fxmlFile.startsWith("parqueadero/")) {
            return "/parqueadero/" + fxmlFile;
        }

        return "/" + fxmlFile;
    }

    // Métodos para refrescar estadísticas en tiempo real
    public static void setEstadisticasController(EstadisticasController controller) {
        estadisticasController = controller;
    }

    public static void refrescarEstadisticasSiAbierto() {
        if (estadisticasController != null) {
            estadisticasController.actualizarEstadisticas();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
