package parqueadero;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MensualidadesController {

    @FXML private TextField txtEspacio;
    @FXML private TextField txtInquilino;
    @FXML private TableView<Mensualidad> tablaMensualidades;
    @FXML private static Label lblTotalMensualidades;

    // ✅ LISTA PÚBLICA PARA SimpleDataManager
    public static ObservableList<Mensualidad> listaMensualidades = FXCollections.observableArrayList();

    // ✅ MÉTODOS ESTÁTICOS PARA SimpleDataManager
    public static ObservableList<Mensualidad> getListaMensualidades() {
        return listaMensualidades;
    }

    public static void limpiarMensualidades() {
        listaMensualidades.clear();
        actualizarContador();
    }

    public static void agregarMensualidad(Mensualidad mensualidad) {
        listaMensualidades.add(mensualidad);
        actualizarContador();
    }

    // ✅ CLASE Mensualidad
    public static class Mensualidad {
        private final SimpleStringProperty espacio;
        private final SimpleStringProperty inquilino;

        public Mensualidad(int espacio, String inquilino) {
            this.espacio = new SimpleStringProperty(String.valueOf(espacio));
            this.inquilino = new SimpleStringProperty(inquilino);
        }

        // Getters
        public String getEspacio() { return espacio.get(); }
        public SimpleStringProperty espacioProperty() { return espacio; }
        public String getInquilino() { return inquilino.get(); }
        public SimpleStringProperty inquilinoProperty() { return inquilino; }
    }

    @FXML
    private void initialize() {
        configurarTabla();
        actualizarContador();

        System.out.println("🏠 MENSUALIDADES INICIALIZADAS:");
        System.out.println("   📊 Total: " + listaMensualidades.size());
        for (Mensualidad m : listaMensualidades) {
            System.out.println("   🔒 Espacio " + m.getEspacio() + ": " + m.getInquilino());
        }
    }

    private void configurarTabla() {
        TableColumn<Mensualidad, String> colEspacio = new TableColumn<>("Espacio");
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("espacio"));
        colEspacio.setPrefWidth(100);

        TableColumn<Mensualidad, String> colInquilino = new TableColumn<>("Inquilino");
        colInquilino.setCellValueFactory(new PropertyValueFactory<>("inquilino"));
        colInquilino.setPrefWidth(300);

        tablaMensualidades.getColumns().setAll(colEspacio, colInquilino);
        tablaMensualidades.setItems(listaMensualidades);
    }

    private static void actualizarContador() {
        if (lblTotalMensualidades != null) {
            lblTotalMensualidades.setText("Total Mensualidades: " + listaMensualidades.size());
        }
    }

    @FXML
    private void agregar() {
        try {
            String espacioStr = txtEspacio.getText().trim();
            String inquilino = txtInquilino.getText().trim();

            if (espacioStr.isEmpty() || inquilino.isEmpty()) {
                mostrarAlerta("Error", "Complete todos los campos", Alert.AlertType.ERROR);
                return;
            }

            int espacio = Integer.parseInt(espacioStr);

            // Validaciones
            if (listaMensualidades.stream().anyMatch(m -> m.getEspacio().equals(espacioStr))) {
                mostrarAlerta("Error", "❌ El espacio " + espacio + " ya está reservado", Alert.AlertType.ERROR);
                return;
            }

            int totalEspacios = ConfiguracionController.Config.getTotalEspacios();
            if (espacio < 1 || espacio > totalEspacios) {
                mostrarAlerta("Error", "❌ Espacio debe estar entre 1 y " + totalEspacios, Alert.AlertType.ERROR);
                return;
            }

            // ✅ AGREGAR MENSUALIDAD
            Mensualidad nuevaMensualidad = new Mensualidad(espacio, inquilino);
            listaMensualidades.add(nuevaMensualidad);

            mostrarAlerta("✅ ÉXITO",
                    String.format("🏠 **Mensualidad registrada**\n\n" +
                                    "👤 Inquilino: **%s**\n" +
                                    "📏 Espacio: **%d**\n" +
                                    "💾 **¡Guardada permanentemente!**",
                            inquilino, espacio),
                    Alert.AlertType.INFORMATION);

            // Limpiar formulario
            txtEspacio.clear();
            txtInquilino.clear();
            tablaMensualidades.refresh();

            System.out.println("✅ Nueva mensualidad: Espacio " + espacio + " → " + inquilino);

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "❌ Ingrese un número válido para el espacio", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminar() {
        Mensualidad seleccionada = tablaMensualidades.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("❌ Confirmar Eliminación");
            confirmacion.setHeaderText("¿Eliminar esta mensualidad?");
            confirmacion.setContentText(
                    "Espacio: " + seleccionada.getEspacio() + "\n" +
                            "Inquilino: " + seleccionada.getInquilino()
            );

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                listaMensualidades.remove(seleccionada);
                mostrarAlerta("✅ Eliminado", "Mensualidad eliminada correctamente", Alert.AlertType.INFORMATION);
                tablaMensualidades.refresh();
                System.out.println("🗑️ Mensualidad eliminada: " + seleccionada.getEspacio());
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione una mensualidad para eliminar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void volver() {
        App.changeScene("dashboard.fxml", "Dashboard");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}