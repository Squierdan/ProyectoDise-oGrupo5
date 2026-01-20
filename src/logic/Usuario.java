package logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Usuario {
    protected String usuario;
    protected String clave;
    protected List<Notificacion> notificaciones;

    public Usuario(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
        this.notificaciones = new ArrayList<>();
    }

    public boolean iniciarSesion(String usuario, String clave) {
        return this.usuario.equals(usuario) && this.clave.equals(clave);
    }

    public void cerrarSesion() {
        // Placeholder for logout logic
    }

    public String getUsuario() {
        return usuario;
    }

    public boolean validarClave(String clave) {
        return this.clave.equals(clave);
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void agregarNotificacion(Notificacion n) {
        notificaciones.add(n);
    }

    public abstract String getNombreCompleto();

    public abstract String getRol();
}
