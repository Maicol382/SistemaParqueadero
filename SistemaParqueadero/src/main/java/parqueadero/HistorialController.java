package parqueadero;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HistorialController {

    @FXML private TableView<Movimiento> tablaHistorial;

    public static ObservableList<Movimiento> listaHistorial = FXCollections.observableArrayList();

    public static ObservableList<Movimiento> getListaHistorial() {
        return listaHistorial;
    }

    public static class Movimiento {
        private final SimpleStringProperty placa;
        private final SimpleStringProperty ingreso;
        private final SimpleStringProperty salida;
        private final SimpleStringProperty espacio;
        private final SimpleStringProperty monto;
        private final SimpleBooleanProperty estaDentro;

        public Movimiento(String placa, String ingreso, String salida, String espacio, String monto, boolean estaDentro) {
            this.placa = new SimpleStringProperty(placa);
            this.ingreso = new SimpleStringProperty(ingreso);
            this.salida = new SimpleStringProperty(salida);
            this.espacio = new SimpleStringProperty(espacio);
            this.monto = new SimpleStringProperty(monto);
            this.estaDentro = new SimpleBooleanProperty(estaDentro);
        }

        public String getPlaca() { return placa.get(); }
        public SimpleStringProperty placaProperty() { return placa; }
        public String getIngreso() { return ingreso.get(); }
        public SimpleStringProperty ingresoProperty() { return ingreso; }
        public String getSalida() { return salida.get(); }
        public SimpleStringProperty salidaProperty() { return salida; }
        public String getEspacio() { return espacio.get(); }
        public SimpleStringProperty espacioProperty() { return espacio; }
        public String getMonto() { return monto.get(); }
        public SimpleStringProperty montoProperty() { return monto; }
        public boolean isEstaDentro() { return estaDentro.get(); }
        public SimpleBooleanProperty estaDentroProperty() { return estaDentro; }

        public void setSalida(String salida) { this.salida.set(salida); }
        public void setMonto(String monto) { this.monto.set(monto); }
        public void setEstaDentro(boolean estaDentro) { this.estaDentro.set(estaDentro); }
    }

    @FXML
    private void initialize() {
        TableColumn<Movimiento, String> colPlaca = new TableColumn<>("Placa");
        colPlaca.setCellValueFactory(new PropertyValueFactory<>("placa"));

        TableColumn<Movimiento, String> colIngreso = new TableColumn<>("Ingreso");
        colIngreso.setCellValueFactory(new PropertyValueFactory<>("ingreso"));

        TableColumn<Movimiento, String> colSalida = new TableColumn<>("Salida");
        colSalida.setCellValueFactory(new PropertyValueFactory<>("salida"));

        TableColumn<Movimiento, String> colEspacio = new TableColumn<>("Espacio");
        colEspacio.setCellValueFactory(new PropertyValueFactory<>("espacio"));

        TableColumn<Movimiento, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));

        tablaHistorial.getColumns().setAll(colPlaca, colIngreso, colSalida, colEspacio, colMonto);
        tablaHistorial.setItems(listaHistorial);
    }

    @FXML
    private void volverDashboard() {
        App.changeScene("/parqueadero/dashboard.fxml", "Dashboard");
    }

    public static void agregarMovimiento(Movimiento movimiento) {
        listaHistorial.add(movimiento);
    }

    public static void actualizarSalida(String placa, String horaSalida, String monto) {
        for (Movimiento m : listaHistorial) {
            if (m.getPlaca().equalsIgnoreCase(placa) && m.isEstaDentro()) {
                m.setSalida(horaSalida);
                m.setMonto(monto);
                m.setEstaDentro(false);
                break;
            }
        }
    }
}