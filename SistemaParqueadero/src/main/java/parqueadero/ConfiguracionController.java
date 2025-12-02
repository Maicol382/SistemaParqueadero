package parqueadero;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ConfiguracionController {

    @FXML private TextField txtTotalEspacios;
    @FXML private TextField txtEspMensual;
    @FXML private TextField txtTarifaHora;
    @FXML private TextField txtTarifaMinuto;

    // Configuración global en memoria (compartida por toda la app)
    public static class Config {
        private static int totalEspacios = 100;
        private static int espaciosMensualidad = 20;
        private static double tarifaHora = 5000;
        private static double tarifaMinuto = 100;

        // ✅ MÉTODOS ESTÁTICOS PÚBLICOS PARA SimpleDataManager
        public static int getTotalEspacios() { return totalEspacios; }
        public static void setTotalEspacios(int value) { totalEspacios = value; }

        public static int getEspaciosMensualidad() { return espaciosMensualidad; }
        public static void setEspaciosMensualidad(int value) { espaciosMensualidad = value; }

        public static double getTarifaHora() { return tarifaHora; }
        public static void setTarifaHora(double value) { tarifaHora = value; }

        public static double getTarifaMinuto() { return tarifaMinuto; }
        public static void setTarifaMinuto(double value) { tarifaMinuto = value; }
    }

    @FXML
    private void volverDashboard() {
        App.changeScene("dashboard.fxml", "Dashboard");
    }

    @FXML
    private void initialize() {
        // Cargar valores actuales
        txtTotalEspacios.setText(String.valueOf(Config.getTotalEspacios()));
        txtEspMensual.setText(String.valueOf(Config.getEspaciosMensualidad()));
        txtTarifaHora.setText(String.valueOf((int)Config.getTarifaHora()));
        txtTarifaMinuto.setText(String.valueOf((int)Config.getTarifaMinuto()));
    }

    @FXML
    private void guardar() {
        try {
            int total = Integer.parseInt(txtTotalEspacios.getText().trim());
            int mensual = Integer.parseInt(txtEspMensual.getText().trim());
            double hora = Double.parseDouble(txtTarifaHora.getText().trim());
            double minuto = Double.parseDouble(txtTarifaMinuto.getText().trim());

            if (total < 1) {
                alerta("El total de espacios debe ser mayor a 0", Alert.AlertType.ERROR);
                return;
            }
            if (mensual > total) {
                alerta("Los espacios mensuales no pueden superar el total", Alert.AlertType.ERROR);
                return;
            }
            if (hora < 0 || minuto < 0) {
                alerta("Las tarifas no pueden ser negativas", Alert.AlertType.ERROR);
                return;
            }

            // Guardar en memoria global
            Config.setTotalEspacios(total);
            Config.setEspaciosMensualidad(mensual);
            Config.setTarifaHora(hora);
            Config.setTarifaMinuto(minuto);

            alerta("Configuración guardada correctamente", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            alerta("Todos los campos deben ser números válidos", Alert.AlertType.ERROR);
        }
    }

    private void alerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(tipo == Alert.AlertType.ERROR ? "Error" : "Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}