package parqueadero;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class SalidaController {

    @FXML private TextField txtPlaca;
    @FXML private Label lblTiempo;
    @FXML private Label lblMonto;
    @FXML private TableView<HistorialController.Movimiento> tablaActivos;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void initialize() {
        configurarTabla();
        actualizarTabla();
    }

    private void configurarTabla() {
        TableColumn<HistorialController.Movimiento, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        TableColumn<HistorialController.Movimiento, String> colIngreso = new TableColumn<>("Ingreso");
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("ingreso"));

        TableColumn<HistorialController.Movimiento, String> colEspacio = new TableColumn<>("Espacio");
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("espacio"));

        TableColumn<HistorialController.Movimiento, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(new PropertyValueFactory<>("monto"));

        tablaActivos.getColumns().setAll(colPlaca, colIngreso, colEspacio, colTipo);
        tablaActivos.setPlaceholder(new Label("No hay vehículos dentro del parqueadero"));
    }

    @FXML
    private void registrarSalida() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        if (placa.isEmpty()) {
            mostrarAlerta("Error", "Por favor ingrese la placa del vehículo", Alert.AlertType.WARNING);
            return;
        }

        HistorialController.Movimiento movimiento = null;
        for (HistorialController.Movimiento m : HistorialController.getListaHistorial()) {
            if (m.getPlaca().equalsIgnoreCase(placa) && m.isEstaDentro()) {
                movimiento = m;
                break;
            }
        }

        if (movimiento == null) {
            mostrarAlerta("No encontrado", "La placa " + placa + " no está dentro del parqueadero", Alert.AlertType.WARNING);
            return;
        }

        try {
            LocalDateTime ahora = LocalDateTime.now();
            String salidaStr = ahora.format(formatter);

            LocalDateTime ingreso = LocalDateTime.parse(movimiento.getIngreso(), formatter);
            long minutosTotales = ChronoUnit.MINUTES.between(ingreso, ahora);
            long horas = minutosTotales / 60;
            long minutos = minutosTotales % 60;

            double monto = 0.0;
            if ("Por hora".equals(movimiento.getMonto())) {
                double tarifaHora = ConfiguracionController.Config.getTarifaHora();
                double tarifaMinuto = ConfiguracionController.Config.getTarifaMinuto();
                monto = horas * tarifaHora + minutos * tarifaMinuto;
            }

            // Registrar salida en el movimiento
            movimiento.setSalida(salidaStr);
            movimiento.setMonto(String.format("$%,.0f", monto));
            movimiento.setEstaDentro(false);

            // ACTUALIZAR ESTADÍSTICAS EN TIEMPO REAL (si la pantalla está abierta)
            App.refrescarEstadisticasSiAbierto();

            // Mostrar mensaje de éxito
            mostrarAlerta("SALIDA REGISTRADA CORRECTAMENTE",
                    String.format("Placa: %s\nTiempo estacionado: %d horas y %d minutos\nMonto a pagar: $%,.0f",
                            placa, horas, minutos, monto),
                    Alert.AlertType.INFORMATION);

            // Limpiar campos
            txtPlaca.clear();
            if (lblTiempo != null) lblTiempo.setText("-");
            if (lblMonto != null) lblMonto.setText("-");

            actualizarTabla();

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al registrar la salida:\n" + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void actualizarTabla() {
        Platform.runLater(() -> {
            tablaActivos.setItems(HistorialController.getListaHistorial().filtered(m -> m.isEstaDentro()));
            tablaActivos.refresh();
        });
    }

    @FXML
    private void volverDashboard() {
        App.changeScene("/parqueadero/dashboard.fxml", "Dashboard");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Platform.runLater(() -> {
            Alert alert = new Alert(tipo);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensaje);
            alert.initOwner(txtPlaca.getScene().getWindow());
            alert.showAndWait();
        });
    }
}