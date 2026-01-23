package logic;

import java.time.LocalDateTime;

public class Whitelist {
    private static int idCounter = 1;
    private int id;
    private String correoAutorizado;
    private LocalDateTime fechaInvitacion;
    private boolean yaRegistrado;
    private DirectorProyecto directorAutorizador;
    private AyudanteInvestigacion ayudanteRegistrado;
    private Proyecto proyectoAsignado;

    public Whitelist(String correoAutorizado, DirectorProyecto director, Proyecto proyecto) {
        this.id = idCounter++;
        this.correoAutorizado = correoAutorizado;
        this.directorAutorizador = director;
        this.proyectoAsignado = proyecto;
        this.fechaInvitacion = LocalDateTime.now();
        this.yaRegistrado = false;
    }

    public void registrarAyudante(AyudanteInvestigacion ayudante) {
        if (ayudante.getCorreoInstitucional().equalsIgnoreCase(correoAutorizado)) {
            this.ayudanteRegistrado = ayudante;
            this.yaRegistrado = true;
        }
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCorreoAutorizado() {
        return correoAutorizado;
    }

    public LocalDateTime getFechaInvitacion() {
        return fechaInvitacion;
    }

    public boolean isYaRegistrado() {
        return yaRegistrado;
    }

    public DirectorProyecto getDirectorAutorizador() {
        return directorAutorizador;
    }

    public AyudanteInvestigacion getAyudanteRegistrado() {
        return ayudanteRegistrado;
    }

    public Proyecto getProyectoAsignado() {
        return proyectoAsignado;
    }
}
