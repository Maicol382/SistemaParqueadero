package parqueadero;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import parqueadero.services.ConfiguracionService;
import parqueadero.services.ParkingService;

import static parqueadero.App.changeScene;

public class DashboardController {

    @FXML private Label lblTotal;
    @FXML private Label lblSeparados;
    @FXML private Label lblDisponibles;

    private final ParkingService parkingService = new ParkingService();
    private final ConfiguracionService configService = new ConfiguracionService();

    @FXML
    private void initialize() {
        refrescarDashboard();
    }

    public void refrescarDashboard() {
        int totalEspacios = configService.getConfig().getTotalEspacios();
        int espaciosMensualidad = configService.getConfig().getEspaciosMensualidad();
        int vehiculosDentro = parkingService.getHistorialService().getRegistrosActivos().size();

        int disponibles = totalEspacios - vehiculosDentro - espaciosMensualidad;

        lblTotal.setText(String.valueOf(totalEspacios));
        lblSeparados.setText(String.valueOf(espaciosMensualidad));
        lblDisponibles.setText(String.valueOf(disponibles > 0 ? disponibles : 0));

        if (disponibles <= 0) {
            lblDisponibles.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
        } else if (disponibles <= 5) {
            lblDisponibles.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;");
        } else {
            lblDisponibles.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        }
    }

    // ==== RUTAS CORREGIDAS (todas incluyen /parqueadero/) ====

    @FXML private void irEntrada() {
        changeScene("/parqueadero/entrada.fxml", "Registrar Ingreso");
    }

    @FXML private void irSalida() {
        changeScene("/parqueadero/salida.fxml", "Registrar Salida");
    }

    @FXML private void irHistorial() {
        changeScene("/parqueadero/historial.fxml", "Historial de Movimientos");
    }

    @FXML private void irUsuarios() {
        changeScene("/parqueadero/usuarios.fxml", "Gestión de Usuarios");
    }

    @FXML private void irConfig() {
        changeScene("/parqueadero/configuracion.fxml", "Configuración del Sistema");
    }

    @FXML private void irMensualidad() {
        changeScene("/parqueadero/mensualidades.fxml", "Gestión de Mensualidades");
    }

    @FXML private void irEstadisticas() {
        changeScene("/parqueadero/estadisticas.fxml", "Estadísticas del Parqueadero");
    }

    @FXML
    private void cerrarSesion() {
        changeScene("/parqueadero/login.fxml", "Parqueadero - Iniciar Sesión");
    }
}
