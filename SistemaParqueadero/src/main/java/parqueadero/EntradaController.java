package parqueadero;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EntradaController {

    @FXML private TextField txtPlaca;
    @FXML private ComboBox<String> cbTipoPago;
    @FXML private Spinner<Integer> spEspacio;
    @FXML private TableView<HistorialController.Movimiento> tablaActivos;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @FXML
    private void volverDashboard() {
        App.changeScene("/parqueadero/dashboard.fxml", "Dashboard");
    }

    @FXML
    private void registrar() {
        String placa = txtPlaca.getText().trim().toUpperCase();
        String tipoPago = cbTipoPago.getValue();
        Integer espacio = spEspacio.getValue();

        // === VALIDACIONES BÁSICAS ===
        if (placa.isEmpty() || placa.length() < 3) {
            alerta("Error", "Ingrese una placa válida (mínimo 3 caracteres)", Alert.AlertType.ERROR);
            return;
        }

        if (tipoPago == null) {
            alerta("Error", "Seleccione tipo de pago", Alert.AlertType.ERROR);
            return;
        }

        // ✅ VALIDACIÓN 1: VERIFICAR PLACA REPETIDA
        boolean placaRepetida = HistorialController.getListaHistorial()
                .stream()
                .anyMatch(m -> m.getPlaca().equalsIgnoreCase(placa) && m.isEstaDentro());

        if (placaRepetida) {
            alerta("Error", "🚗 La placa **" + placa + "** ya está dentro del parqueadero", Alert.AlertType.ERROR);
            return;
        }

        // ✅ VALIDACIÓN 2: VERIFICAR ESPACIO OCUPADO POR OTRO VEHÍCULO
        boolean espacioOcupadoPorVehiculo = HistorialController.getListaHistorial()
                .stream()
                .anyMatch(m -> m.getEspacio().equals(espacio.toString()) && m.isEstaDentro());

        if (espacioOcupadoPorVehiculo) {
            alerta("Error", "❌ El espacio **" + espacio + "** está ocupado por otro vehículo", Alert.AlertType.ERROR);
            return;
        }

        // ✅ VALIDACIÓN 3: VERIFICAR MENSUALIDAD (NUEVA)
        MensualidadesController.Mensualidad mensualidad = buscarMensualidadPorEspacio(espacio);
        if (mensualidad != null) {
            // Espacio está reservado por mensualidad
            if (tipoPago.equals("Por hora")) {
                // ❌ NO permite "Por hora" en espacio de mensualidad
                alerta("❌ Espacio Reservado",
                        String.format("🚫 El espacio **%d** está reservado para **MENSUALIDAD**\n\n" +
                                        "👤 **Ocupado por:** %s\n" +
                                        "🏷️ **Tipo requerido:** Mensualidad\n" +
                                        "⚠️ **No puede usar:** Vehículos por hora",
                                espacio, mensualidad.getInquilino()),
                        Alert.AlertType.ERROR);
                return;
            } else {
                // ✅ Permite "Mensualidad" en espacio reservado
                alerta("ℹ️ Espacio de Mensualidad",
                        String.format("✅ Espacio **%d** reservado para **%s**\n" +
                                        "🎫 Registrando vehículo con **MENSUALIDAD**",
                                espacio, mensualidad.getInquilino()),
                        Alert.AlertType.INFORMATION);
            }
        }

        // ✅ VALIDACIÓN 4: ESPACIOS RESERVADOS POR RANGO (1 a X)
        int espaciosMensuales = ConfiguracionController.Config.getEspaciosMensualidad();
        boolean esEspacioMensual = espacio <= espaciosMensuales;
        boolean esTipoHoraEnEspacioMensual = tipoPago.equals("Por hora") && esEspacioMensual;

        if (esTipoHoraEnEspacioMensual) {
            alerta("❌ Espacio Reservado",
                    String.format("🚫 Los espacios **1-%d** están reservados para **MENSUALIDADES**\n\n" +
                                    "⚠️ **No puede usar:** Vehículos por hora\n" +
                                    "🎫 **Use espacios:** %d-%d para hora",
                            espaciosMensuales, espaciosMensuales + 1,
                            ConfiguracionController.Config.getTotalEspacios()),
                    Alert.AlertType.ERROR);
            return;
        }

        // ✅ TODO VÁLIDO - REGISTRAR VEHÍCULO
        String horaIngresoCompleta = LocalDateTime.now().format(formatter);
        String horaIngresoVisible = horaIngresoCompleta.substring(11, 19);
        String tipoPagoDisplay = tipoPago.equals("Mensualidad") ? "Mensualidad" : "Por hora";

        HistorialController.Movimiento nuevoMovimiento = new HistorialController.Movimiento(
                placa,
                horaIngresoCompleta,
                "-",
                espacio.toString(),
                tipoPagoDisplay,
                true
        );

        HistorialController.agregarMovimiento(nuevoMovimiento);

        // ✅ MENSAJE DE ÉXITO PERSONALIZADO
        String mensajeExito = String.format(
                "🎉 **VEHÍCULO REGISTRADO CORRECTAMENTE**\n\n" +
                        "🚗 **DETALLES:**\n" +
                        "• Placa: **%s**\n" +
                        "• Espacio: **%d**\n" +
                        "• Tipo: **%s**\n" +
                        "• Hora: **%s**\n\n" +
                        "✅ Espacio asignado exitosamente",
                placa, espacio, tipoPagoDisplay, horaIngresoVisible
        );

        alerta("✅ INGRESO EXITOSO", mensajeExito, Alert.AlertType.INFORMATION);

        // Limpiar formulario
        txtPlaca.clear();
        cbTipoPago.setValue(null);
        spEspacio.getValueFactory().setValue(1);

        // Refrescar tabla
        tablaActivos.refresh();
    }

    // ✅ MÉTODO NUEVO: BUSCAR MENSUALIDAD POR ESPACIO
    private MensualidadesController.Mensualidad buscarMensualidadPorEspacio(int espacio) {
        return MensualidadesController.getListaMensualidades().stream()
                .filter(m -> m.getEspacio().equals(String.valueOf(espacio)))
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void initialize() {
        // Configurar ComboBox
        cbTipoPago.getItems().setAll("Por hora", "Mensualidad");
        cbTipoPago.setValue("Por hora");

        // Configurar Spinner
        int totalEspacios = ConfiguracionController.Config.getTotalEspacios();
        spEspacio.setValueFactory(new IntegerSpinnerValueFactory(1, totalEspacios, 1));
        spEspacio.setEditable(true);

        // Cargar vehículos activos
        ObservableList<HistorialController.Movimiento> vehiculosActivos =
                HistorialController.getListaHistorial().filtered(m -> m.isEstaDentro());
        tablaActivos.setItems(vehiculosActivos);
        tablaActivos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ✅ MOSTRAR ESTADÍSTICAS DE MENSUALIDADES EN CONSOLA
        int totalMensualidades = MensualidadesController.getListaMensualidades().size();
        int espaciosMensuales = ConfiguracionController.Config.getEspaciosMensualidad();
        System.out.println("📊 ENTRADA INICIALIZADA:");
        System.out.println("   🚗 Vehículos activos: " + vehiculosActivos.size());
        System.out.println("   🏠 Mensualidades: " + totalMensualidades);
        System.out.println("   📏 Espacios mensuales: 1-" + espaciosMensuales);

        // Mostrar mensualidades ocupadas
        if (totalMensualidades > 0) {
            System.out.println("   🔒 Espacios con mensualidad:");
            for (MensualidadesController.Mensualidad m : MensualidadesController.getListaMensualidades()) {
                System.out.println("      • Espacio " + m.getEspacio() + ": " + m.getInquilino());
            }
        }
    }

    // ✅ MÉTODO MEJORADO PARA ALERTAS
    private void alerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Personalizar botón OK
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();
    }
}