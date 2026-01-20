package logic;

import java.time.LocalDateTime;
import java.util.Date;

public class Informe {
    private static int idCounter = 1;
    private int id;
    private TipoInforme tipo;
    private Date fecha;
    private EstadoInforme estado;
    private String actividades;
    private boolean revisadoPorDirector;
    private boolean revisadoPorJefe;
    private String feedbackDirector;
    private String feedbackJefe;
    private LocalDateTime fechaFirmaJefe;
    private String archivoUrl;
    private AyudanteInvestigacion autor;
    private Proyecto proyecto;

    public Informe(TipoInforme tipo, String actividades, AyudanteInvestigacion autor, Proyecto proyecto) {
        this.id = idCounter++;
        this.tipo = tipo;
        this.fecha = new Date();
        this.actividades = actividades;
        this.autor = autor;
        this.proyecto = proyecto;
        this.estado = EstadoInforme.BORRADOR;
        this.revisadoPorDirector = false;
        this.revisadoPorJefe = false;
        this.feedbackDirector = "";
        this.feedbackJefe = "";
        this.archivoUrl = "";
    }

    public void notificarAJefeDepartamento() {
        // Placeholder for notification
        System.out.println("Notificando al Jefe de Departamento sobre informe: " + id);
    }

    public void notificarADirector() {
        // Placeholder for notification
        System.out.println("Notificando al Director sobre informe: " + id);
    }

    public boolean validarEntrega() {
        return actividades != null && !actividades.isEmpty();
    }

    public void firmar() {
        this.revisadoPorJefe = true;
        this.estado = EstadoInforme.FIRMADO_JEFE;
        this.fechaFirmaJefe = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public TipoInforme getTipo() {
        return tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public EstadoInforme getEstado() {
        return estado;
    }

    public void setEstado(EstadoInforme estado) {
        this.estado = estado;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public boolean isRevisadoPorDirector() {
        return revisadoPorDirector;
    }

    public void setRevisadoPorDirector(boolean r) {
        this.revisadoPorDirector = r;
    }

    public boolean isRevisadoPorJefe() {
        return revisadoPorJefe;
    }

    public void setRevisadoPorJefe(boolean r) {
        this.revisadoPorJefe = r;
    }

    public String getFeedbackDirector() {
        return feedbackDirector;
    }

    public void setFeedbackDirector(String f) {
        this.feedbackDirector = f;
    }

    public String getFeedbackJefe() {
        return feedbackJefe;
    }

    public void setFeedbackJefe(String f) {
        this.feedbackJefe = f;
    }

    public LocalDateTime getFechaFirmaJefe() {
        return fechaFirmaJefe;
    }

    public String getArchivoUrl() {
        return archivoUrl;
    }

    public void setArchivoUrl(String url) {
        this.archivoUrl = url;
    }

    public AyudanteInvestigacion getAutor() {
        return autor;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }
}
