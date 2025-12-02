package parqueadero;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SimpleDataManager {

    private static final String HISTORIAL_FILE = "data/historial.txt";
    private static final String CONFIG_FILE = "data/config.txt";

    public static void cargarHistorial() {
        File file = new File(HISTORIAL_FILE);
        if (!file.exists()) {
            System.out.println("No existe historial previo. Se creará uno nuevo al registrar movimientos.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            HistorialController.getListaHistorial().clear();

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;

                String[] partes = linea.split("\\|", -1);
                if (partes.length < 7) {
                    System.err.println("Línea inválida en historial (esperaba 7 campos): " + linea);
                    continue;
                }

                String placa     = partes[0].trim();
                String ingreso   = partes[1].trim();
                String salida    = partes[2].trim();
                String espacio   = partes[3].trim();
                String tipo      = partes[4].trim();  // "Carro", "Moto", etc.
                String monto     = partes[5].trim();
                boolean estaDentro = Boolean.parseBoolean(partes[6].trim());

                // Ajustar salida si viene como "-"
                if (salida.equals("-") || salida.isEmpty()) {
                    salida = null;
                }

                HistorialController.Movimiento m = new HistorialController.Movimiento(
                        placa,
                        ingreso,
                        salida != null ? salida : "",
                        espacio,
                        monto,
                        estaDentro
                );

                // Si necesitas usar el tipo más adelante (filtro, tarifa diferente, etc.)
                // puedes guardarlo en un campo nuevo en Movimiento o usar un Map
                // Por ahora lo ignoramos en el objeto, pero está disponible si lo necesitas

                HistorialController.getListaHistorial().add(m);
            }

            System.out.println("Historial cargado: " + HistorialController.getListaHistorial().size() + " movimientos.");

            // ACTUALIZAR ESTADÍSTICAS SI LA PANTALLA ESTÁ ABIERTA
            App.refrescarEstadisticasSiAbierto();

        } catch (Exception e) {
            System.err.println("Error crítico al cargar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void guardarHistorial() {
        try {
            Files.createDirectories(Paths.get("data"));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(HISTORIAL_FILE))) {
                for (HistorialController.Movimiento m : HistorialController.getListaHistorial()) {
                    String tipo = "Carro"; // Puedes mejorarlo más adelante agregando un campo "tipo" al Movimiento
                    // Si en el futuro agregas tipo al objeto Movimiento, cámbialo por m.getTipo()

                    bw.write(String.format("%s|%s|%s|%s|%s|%s|%b%n",
                            m.getPlaca(),
                            m.getIngreso(),
                            m.getSalida() != null && !m.getSalida().isEmpty() ? m.getSalida() : "-",
                            m.getEspacio(),
                            tipo,
                            m.getMonto() != null ? m.getMonto() : "0",
                            m.isEstaDentro()
                    ));
                }
            }
            System.out.println("Historial guardado correctamente en " + HISTORIAL_FILE);

            // Actualizamos estadísticas al guardar (solo si la pantalla está visible)
            App.refrescarEstadisticasSiAbierto();

        } catch (Exception e) {
            System.err.println("Error al guardar historial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void cargarConfiguracion() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.out.println("No hay configuración previa. Usando valores por defecto.");
            ConfiguracionController.Config.setTarifaHora(5000);
            ConfiguracionController.Config.setTarifaMinuto(100);
            guardarConfiguracion();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea1 = br.readLine();
            String linea2 = br.readLine();

            if (linea1 != null && linea2 != null) {
                ConfiguracionController.Config.setTarifaHora(Double.parseDouble(linea1.trim()));
                ConfiguracionController.Config.setTarifaMinuto(Double.parseDouble(linea2.trim()));
                System.out.println("Configuración cargada correctamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al leer configuración. Usando valores por defecto.");
            ConfiguracionController.Config.setTarifaHora(5000);
            ConfiguracionController.Config.setTarifaMinuto(100);
        }
    }

    public static void guardarConfiguracion() {
        try {
            Files.createDirectories(Paths.get("data"));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
                bw.write(ConfiguracionController.Config.getTarifaHora() + "\n");
                bw.write(ConfiguracionController.Config.getTarifaMinuto() + "\n");
            }
            System.out.println("Configuración guardada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al guardar configuración: " + e.getMessage());
        }
    }

    public static void iniciar() {
        cargarConfiguracion();
        cargarHistorial();
        System.out.println("Sistema de datos iniciado correctamente.");
    }

    public static void cerrar() {
        guardarHistorial();
        guardarConfiguracion();
        System.out.println("Todos los datos han sido guardados. ¡Hasta pronto!");
    }
}