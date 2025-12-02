package parqueadero.services;

import parqueadero.models.*;
import java.time.LocalDateTime;

public class ParkingService {
    private final HistorialService historialService = new HistorialService();
    private final ConfiguracionService configService = new ConfiguracionService();
    private final MensualidadService mensualidadService = new MensualidadService();

    public HistorialService getHistorialService() { return historialService; }

    public boolean registrarEntrada(String placa, int espacio, String tipoPago) {
        placa = placa.toUpperCase().trim();
        if (placa.isEmpty()) return false;
        if (historialService.findByPlaca(placa) != null) return false;
        if (mensualidadService.existeEspacio(espacio)) return false;
        if (historialService.getRegistrosActivos().stream().anyMatch(r -> r.getEspacio() == espacio)) return false;

        Configuracion cfg = configService.getConfig();
        long ocupados = historialService.getRegistrosActivos().size();
        if (ocupados >= cfg.getTotalEspacios() - cfg.getEspaciosMensualidad()) return false;

        Registro r = new Registro(placa, espacio, tipoPago);
        historialService.agregar(r);
        return true;
    }

    public boolean registrarSalida(String placa) {
        placa = placa.toUpperCase().trim();
        Registro r = historialService.findByPlaca(placa);
        if (r == null) return false;

        r.setSalida(LocalDateTime.now());
        r.setActivo(false);
        r.calcularMonto(configService.getConfig());
        historialService.actualizar(r);
        return true;
    }
}