package logic;

import java.time.LocalDateTime;

public class Notificacion {
    private static int idCounter = 1;
    private int id;
    private String mensaje;
    private TipoNotificacion tipo;
    private LocalDateTime fechaEnvio;
    private Usuario destinatario;

    public Notificacion(String mensaje, TipoNotificacion tipo, Usuario destinatario) {
        this.id = idCounter++;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.destinatario = destinatario;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void enviarCorreo() {
        // Placeholder for email sending
        System.out.println("Enviando correo a " + destinatario.getUsuario() + ": " + mensaje);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }
}
