package parqueadero.services;

import parqueadero.models.Mensualidad;
import parqueadero.util.FileUtil;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MensualidadService {
    private static final String ARCHIVO = "data/mensualidades.json";
    private List<Mensualidad> lista = new ArrayList<>();

    public MensualidadService() { cargar(); }

    public List<Mensualidad> getMensualidades() { return new ArrayList<>(lista); }

    public boolean existeEspacio(int espacio) {
        return lista.stream().anyMatch(m -> m.getEspacio() == espacio);
    }

    public void crear(int espacio, String inquilino) {
        lista.add(new Mensualidad(espacio, inquilino));
        guardar();
    }

    public void eliminar(int espacio) {
        lista.removeIf(m -> m.getEspacio() == espacio);
        guardar();
    }

    private void cargar() {
        if (Files.exists(Paths.get(ARCHIVO))) {
            List<Mensualidad> loaded = FileUtil.read(ARCHIVO,
                    new com.google.gson.reflect.TypeToken<List<Mensualidad>>(){}.getType());
            if (loaded != null) lista = loaded;
        }
    }

    private void guardar() { FileUtil.write(ARCHIVO, lista); }
}