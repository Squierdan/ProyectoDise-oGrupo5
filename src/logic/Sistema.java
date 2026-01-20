package logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private static Sistema instance;
    private List<Usuario> usuarios;
    private List<Proyecto> proyectos;
    private List<Whitelist> whitelists;
    private List<Notificacion> notificaciones;
    private List<SolicitudAyudante> solicitudes;

    private Sistema() {
        usuarios = new ArrayList<>();
        proyectos = new ArrayList<>();
        whitelists = new ArrayList<>();
        notificaciones = new ArrayList<>();
        solicitudes = new ArrayList<>();
        cargarUsuarios();
        inicializarDatosDummy();
    }

    public static Sistema getInstance() {
        if (instance == null) {
            instance = new Sistema();
        }
        return instance;
    }

    private void cargarUsuarios() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String user = parts[0].trim();
                    String pass = parts[1].trim();
                    String role = parts[2].trim();
                    String nombres = parts[3].trim();
                    String apellidos = parts.length > 4 ? parts[4].trim() : "";

                    Usuario u = null;
                    if (role.equalsIgnoreCase("JEFE")) {
                        u = new JefeDIC(user, pass, nombres, apellidos, user, "DIC");
                    } else if (role.equalsIgnoreCase("DIRECTOR")) {
                        u = new DirectorProyecto(user, pass, nombres, apellidos, user, "DIC");
                    } else if (role.equalsIgnoreCase("AYUDANTE")) {
                        u = new AyudanteInvestigacion(user, pass, nombres, apellidos, "Ingenieria en Sistemas", 5, user);
                    }

                    if (u != null) {
                        usuarios.add(u);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            cargarUsuariosPorDefecto();
        }
    }

    private void cargarUsuariosPorDefecto() {
        usuarios.add(new JefeDIC("jefe", "123", "Carlos", "Martinez", "jefe@dic.edu", "DIC"));
        usuarios.add(new DirectorProyecto("director", "123", "Juan", "Perez", "director@dic.edu", "DIC"));
        usuarios.add(new AyudanteInvestigacion("ayudante", "123", "Pedro", "Lopez", "Ingenieria en Sistemas", 5, "ayudante@dic.edu"));
    }

    private void inicializarDatosDummy() {
        DirectorProyecto director = null;
        AyudanteInvestigacion ayudante = null;
        JefeDIC jefe = null;

        for (Usuario u : usuarios) {
            if (u instanceof DirectorProyecto) {
                director = (DirectorProyecto) u;
            } else if (u instanceof AyudanteInvestigacion) {
                ayudante = (AyudanteInvestigacion) u;
            } else if (u instanceof JefeDIC) {
                jefe = (JefeDIC) u;
            }
        }

        
    }

    public Usuario login(String usuario, String clave) {
        for (Usuario u : usuarios) {
            if (u.iniciarSesion(usuario, clave)) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public List<Whitelist> getWhitelists() {
        return whitelists;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public List<SolicitudAyudante> getSolicitudes() {
        return solicitudes;
    }

    public List<SolicitudAyudante> getSolicitudesPendientes() {
        List<SolicitudAyudante> pendientes = new ArrayList<>();
        for (SolicitudAyudante s : solicitudes) {
            if (!s.isProcesada()) {
                pendientes.add(s);
            }
        }
        return pendientes;
    }

    public List<AyudanteInvestigacion> getAyudantesSinProyecto() {
        List<AyudanteInvestigacion> libres = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof AyudanteInvestigacion) {
                AyudanteInvestigacion ay = (AyudanteInvestigacion) u;
                if (ay.getProyectoAsignado() == null) {
                    libres.add(ay);
                }
            }
        }
        return libres;
    }

    public List<DirectorProyecto> getDirectores() {
        List<DirectorProyecto> directores = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof DirectorProyecto) {
                directores.add((DirectorProyecto) u);
            }
        }
        return directores;
    }

    public void agregarNotificacion(Notificacion n) {
        notificaciones.add(n);
        n.getDestinatario().agregarNotificacion(n);
    }

    public void agregarWhitelist(Whitelist w) {
        whitelists.add(w);
    }

    public void agregarProyecto(Proyecto p) {
        proyectos.add(p);
    }

    public void agregarSolicitud(SolicitudAyudante s) {
        solicitudes.add(s);
    }
}
