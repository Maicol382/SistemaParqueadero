// parqueadero/models/Vehiculo.java  (activo en parqueadero)
package parqueadero.models;

public class Vehiculo {
    private String placa;
    private int espacio;
    private String tipoPago;

    public Vehiculo() {}

    public Vehiculo(String placa, int espacio, String tipoPago) {
        this.placa = placa;
        this.espacio = espacio;
        this.tipoPago = tipoPago;
    }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public int getEspacio() { return espacio; }
    public void setEspacio(int espacio) { this.espacio = espacio; }

    public String getTipoPago() { return tipoPago; }
    public void setTipoPago(String tipoPago) { this.tipoPago = tipoPago; }
}