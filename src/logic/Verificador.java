package logic;

import java.time.LocalDateTime;

public class Verificador {
    private static int idCounter = 1;
    private int id;
    private int idProyecto;
    private boolean documentacionCompleta;
    private boolean ayudantesConfirmados;
    private boolean presupuestoValidado;
    private boolean requisitosTecnicosOk;
    private LocalDateTime fechaVerificacion;
    private String observaciones;
    private Proyecto proyecto;

    public Verificador(Proyecto proyecto) {
        this.id = idCounter++;
        this.proyecto = proyecto;
        this.idProyecto = proyecto.getId();
        this.documentacionCompleta = false;
        this.ayudantesConfirmados = false;
        this.presupuestoValidado = false;
        this.requisitosTecnicosOk = false;
        this.observaciones = "";
        proyecto.setVerificador(this);
    }

    public boolean verificarCompletitud(Proyecto proyecto) {
        return documentacionCompleta && ayudantesConfirmados && presupuestoValidado && requisitosTecnicosOk;
    }

    public boolean validarAyudantesPrevistos() {
        this.ayudantesConfirmados = proyecto.getAyudantesActuales() > 0;
        return ayudantesConfirmados;
    }

    public boolean validarDocumentacion() {
        // Placeholder - in real implementation would check actual documents
        this.documentacionCompleta = true;
        return documentacionCompleta;
    }

    public String generarReporteVerificacion() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE VERIFICACIÓN ===\n");
        sb.append("Proyecto: ").append(proyecto.getNombre()).append("\n");
        sb.append("Documentación: ").append(documentacionCompleta ? "✓" : "✗").append("\n");
        sb.append("Ayudantes: ").append(ayudantesConfirmados ? "✓" : "✗").append("\n");
        sb.append("Presupuesto: ").append(presupuestoValidado ? "✓" : "✗").append("\n");
        sb.append("Requisitos Técnicos: ").append(requisitosTecnicosOk ? "✓" : "✗").append("\n");
        sb.append("Observaciones: ").append(observaciones).append("\n");
        return sb.toString();
    }

    public boolean aprobarParaEnvio() {
        if (verificarCompletitud(proyecto)) {
            this.fechaVerificacion = LocalDateTime.now();
            proyecto.setEstado(EstadoProyecto.APROBADO_DIC);
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public int getIdProyecto() {
        return idProyecto;
    }

    public boolean isDocumentacionCompleta() {
        return documentacionCompleta;
    }

    public void setDocumentacionCompleta(boolean d) {
        this.documentacionCompleta = d;
    }

    public boolean isAyudantesConfirmados() {
        return ayudantesConfirmados;
    }

    public void setAyudantesConfirmados(boolean a) {
        this.ayudantesConfirmados = a;
    }

    public boolean isPresupuestoValidado() {
        return presupuestoValidado;
    }

    public void setPresupuestoValidado(boolean p) {
        this.presupuestoValidado = p;
    }

    public boolean isRequisitosTecnicosOk() {
        return requisitosTecnicosOk;
    }

    public void setRequisitosTecnicosOk(boolean r) {
        this.requisitosTecnicosOk = r;
    }

    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String o) {
        this.observaciones = o;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }
}
