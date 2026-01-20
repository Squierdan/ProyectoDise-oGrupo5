package logic;

import java.time.LocalDateTime;

public class SolicitudAyudante {
    private static int idCounter = 1;
    private int id;
    private DirectorProyecto director;
    private AyudanteInvestigacion ayudante;
    private Proyecto proyecto;
    private LocalDateTime fechaSolicitud;
    private boolean aprobada;
    private boolean procesada;
    private String observaciones;

    public SolicitudAyudante(DirectorProyecto director, AyudanteInvestigacion ayudante, Proyecto proyecto) {
        this.id = idCounter++;
        this.director = director;
        this.ayudante = ayudante;
        this.proyecto = proyecto;
        this.fechaSolicitud = LocalDateTime.now();
        this.aprobada = false;
        this.procesada = false;
        this.observaciones = "";
    }

    public void aprobar() {
        this.aprobada = true;
        this.procesada = true;
        proyecto.agregarAyudante(ayudante);
    }

    public void rechazar(String motivo) {
        this.aprobada = false;
        this.procesada = true;
        this.observaciones = motivo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public DirectorProyecto getDirector() {
        return director;
    }

    public AyudanteInvestigacion getAyudante() {
        return ayudante;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public boolean isProcesada() {
        return procesada;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
