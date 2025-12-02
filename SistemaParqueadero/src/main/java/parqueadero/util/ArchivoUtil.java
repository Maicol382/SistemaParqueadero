package parqueadero.util;

import java.io.*;

public class ArchivoUtil {

    private static final String RUTA = "data/";

    static {
        new File(RUTA).mkdirs(); // Crea carpeta si no existe
    }

    public static void guardar(String nombreArchivo, String contenido) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RUTA + nombreArchivo))) {
            bw.write(contenido);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String leer(String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA + nombreArchivo))) {
            StringBuilder sb = new StringBuilder();
            String linea;
            while ((linea = br.readLine()) != null) {
                sb.append(linea).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }
}