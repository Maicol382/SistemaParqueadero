package parqueadero.services;

import parqueadero.models.Registro;
import parqueadero.util.FileUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HistorialService {
    private static final String ARCHIVO = "data/historial.json";
    private List<Registro> registros = new ArrayList<>();

    public HistorialService() { cargar(); }

    public void agregar(Registro r) {
        registros.add(r);
        guardar();
    }

    public void actualizar(Registro r) {
        registros.removeIf(reg -> reg.getId().equals(r.getId()));
        registros.add(r);
        guardar();
    }

    public Registro findByPlaca(String placa) {
        return registros.stream()
                .filter(r -> r.isActivo() && r.getPlaca().equalsIgnoreCase(placa))
                .findFirst()
                .orElse(null);
    }

    public List<Registro> getHistorial() { return new ArrayList<>(registros); }

    public List<Registro> getRegistrosActivos() {
        return registros.stream().filter(Registro::isActivo).toList();
    }

    private void cargar() {
        if (Files.exists(Paths.get(ARCHIVO))) {
            List<Registro> loaded = FileUtil.read(ARCHIVO,
                    new com.google.gson.reflect.TypeToken<List<Registro>>(){}.getType());
            if (loaded != null) registros = loaded;
        }
    }

    private void guardar() {
        FileUtil.write(ARCHIVO, registros);
    }
}