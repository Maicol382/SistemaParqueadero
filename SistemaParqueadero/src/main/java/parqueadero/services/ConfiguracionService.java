package parqueadero.services;

import parqueadero.models.Configuracion;
import parqueadero.util.FileUtil;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfiguracionService {
    private static final String ARCHIVO = "data/config.json";
    private Configuracion config;

    public ConfiguracionService() { cargar(); }

    private void cargar() {
        if (Files.exists(Paths.get(ARCHIVO))) {
            config = FileUtil.read(ARCHIVO, Configuracion.class);
        }
        if (config == null) {
            config = new Configuracion(100, 20, 3000.0, 100.0);
            guardar();
        }
    }

    public Configuracion getConfig() { return config; }

    public void actualizar(Configuracion nueva) {
        this.config = nueva;
        guardar();
    }

    private void guardar() { FileUtil.write(ARCHIVO, config); }
}