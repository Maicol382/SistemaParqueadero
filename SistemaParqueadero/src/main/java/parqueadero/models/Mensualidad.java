package parqueadero.models;

public class Mensualidad {
    private int espacio;
    private String inquilino;

    public Mensualidad() {}

    public Mensualidad(int espacio, String inquilino) {
        this.espacio = espacio;
        this.inquilino = inquilino;
    }

    public int getEspacio() { return espacio; }
    public void setEspacio(int espacio) { this.espacio = espacio; }
    public String getInquilino() { return inquilino; }
    public void setInquilino(String inquilino) { this.inquilino = inquilino; }
}