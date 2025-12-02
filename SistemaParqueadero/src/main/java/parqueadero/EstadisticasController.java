package parqueadero;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EstadisticasController {

    @FXML private Label lblIngresosDia;
    @FXML private Label lblVehiculosHoy;
    @FXML private Label lblTotalMes;

    @FXML
    private void initialize() {
        App.setEstadisticasController(this);
        actualizarEstadisticas();
    }

    // ← SOLUCIÓN: RUTA CORREGIDA + MAYÚSCULAS
    @FXML
    private void volverDashboard() {
        App.changeScene("/parqueadero/dashboard.fxml", "Dashboard");
    }

    public void actualizarEstadisticas() {
        Platform.runLater(() -> {
            LocalDate hoy = LocalDate.now();
            String fechaHoy = hoy.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String mesActual = hoy.getYear() + "-" + String.format("%02d", hoy.getMonthValue());

            long vehiculosHoy = 0;
            double ingresosDia = 0.0;
            double totalMes = 0.0;

            for (HistorialController.Movimiento m : HistorialController.getListaHistorial()) {
                if (!m.isEstaDentro() && m.getSalida() != null && m.getSalida().length() >= 10) {
                    String fechaSalida = m.getSalida().substring(0, 10);

                    if (fechaSalida.equals(fechaHoy)) {
                        vehiculosHoy++;
                        ingresosDia += extraerMonto(m.getMonto());
                    }
                    if (m.getSalida().startsWith(mesActual)) {
                        totalMes += extraerMonto(m.getMonto());
                    }
                }
            }

            lblIngresosDia.setText(String.format("$%,.0f", ingresosDia));
            lblVehiculosHoy.setText(String.valueOf(vehiculosHoy));
            lblTotalMes.setText(String.format("$%,.0f", totalMes));
        });
    }

    private double extraerMonto(String texto) {
        if (texto == null || texto.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(texto.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
}
