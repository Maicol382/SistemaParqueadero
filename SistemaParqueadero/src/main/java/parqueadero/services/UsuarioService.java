package parqueadero.services;

import parqueadero.models.Usuario;
import parqueadero.util.FileUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;

public class UsuarioService {
    private static final String ARCHIVO = "data/usuarios.json";
    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioService() {
        cargar();
        if (usuarios.isEmpty()) {
            usuarios.add(new Usuario("admin", "admin123", "ADMIN"));
            usuarios.add(new Usuario("cajero", "1234", "USER"));
            guardar();
        }
    }

    private void cargar() {
        if (Files.exists(Paths.get(ARCHIVO))) {
            List<Usuario> data = FileUtil.read(ARCHIVO, new TypeToken<List<Usuario>>(){}.getType());
            if (data != null) usuarios.addAll(data);
        }
    }

    private void guardar() { FileUtil.write(ARCHIVO, usuarios); }

    public boolean autenticar(String user, String pass) {
        return usuarios.stream().anyMatch(u ->
                u.getUsername().equals(user) && u.getPassword().equals(pass));
    }

    public List<Usuario> getAll() { return new ArrayList<>(usuarios); }

    public boolean crear(String user, String pass, String rol) {
        if (usuarios.stream().anyMatch(u -> u.getUsername().equals(user))) return false;
        usuarios.add(new Usuario(user, pass, rol));
        guardar();
        return true;
    }

    public boolean eliminar(String user) {
        if ("admin".equals(user)) return false;
        boolean ok = usuarios.removeIf(u -> u.getUsername().equals(user));
        if (ok) guardar();
        return ok;
    }
}