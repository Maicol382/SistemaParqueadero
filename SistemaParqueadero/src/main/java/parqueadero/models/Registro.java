package parqueadero.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Registro {
    private String id = UUID.randomUUID().toString();
    private String placa;
    private int espacio;
    private String tipoPago;
    private LocalDateTime entrada;
    private LocalDateTime salida;
    private double monto;
    private boolean activo = true;

    public Registro() {}

    public Registro(String placa, int espacio, String tipoPago) {
        this.placa = placa.toUpperCase();
        this.espacio = espacio;
        this.tipoPago = tipoPago;
        this.entrada = LocalDateTime.now();
    }

    // Getters y setters
    public String getId() { return id; }
    public String getPlaca() { return placa; }
    public int getEspacio() { return espacio; }
    public String getTipoPago() { return tipoPago; }
    public LocalDateTime getEntrada() { return entrada; }
    public LocalDateTime getSalida() { return salida; }
    public void setSalida(LocalDateTime salida) { this.salida = salida; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public void calcularMonto(Configuracion config) {
        if (entrada == null || salida == null) {
            this.monto = 0.0;
            return;
        }
        long minutos = java.time.Duration.between(entrada, salida).toMinutes();
        if (minutos <= 0) {
            this.monto = 0.0;
            return;
        }
        double tarifa = "Hora".equalsIgnoreCase(tipoPago)
                ? (minutos / 60.0) * config.getTarifaHora()
                : minutos * config.getTarifaMinuto();
        this.monto = Math.ceil(tarifa);
    }

    // JavaFX Properties para TableView
    public StringProperty placaProperty() { return new SimpleStringProperty(placa); }
    public StringProperty horaIngresoProperty() {
        return new SimpleStringProperty(entrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }
    public StringProperty salidaProperty() {
        return salida == null ? new SimpleStringProperty("-") :
                new SimpleStringProperty(salida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }
    public StringProperty espacioProperty() { return new SimpleStringProperty(String.valueOf(espacio)); }
    public StringProperty tipoPagoProperty() { return new SimpleStringProperty(tipoPago); }
    public StringProperty montoProperty() { return new SimpleStringProperty(String.format("$ %,d", (int)monto)); }
    public StringProperty ingresoProperty() {
        return new SimpleStringProperty(entrada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }
}