package parqueadero.models;

public class Configuracion {
    private int totalEspacios = 100;
    private int espaciosMensualidad = 20;
    private double tarifaHora = 3000.0;
    private double tarifaMinuto = 100.0;

    public Configuracion() {}

    public Configuracion(int totalEspacios, int espaciosMensualidad, double tarifaHora, double tarifaMinuto) {
        this.totalEspacios = totalEspacios;
        this.espaciosMensualidad = espaciosMensualidad;
        this.tarifaHora = tarifaHora;
        this.tarifaMinuto = tarifaMinuto;
    }

    // Getters y Setters
    public int getTotalEspacios() { return totalEspacios; }
    public void setTotalEspacios(int totalEspacios) { this.totalEspacios = totalEspacios; }
    public int getEspaciosMensualidad() { return espaciosMensualidad; }
    public void setEspaciosMensualidad(int espaciosMensualidad) { this.espaciosMensualidad = espaciosMensualidad; }
    public double getTarifaHora() { return tarifaHora; }
    public void setTarifaHora(double tarifaHora) { this.tarifaHora = tarifaHora; }
    public double getTarifaMinuto() { return tarifaMinuto; }
    public void setTarifaMinuto(double tarifaMinuto) { this.tarifaMinuto = tarifaMinuto; }
}