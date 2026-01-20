package logic;

import java.util.ArrayList;
import java.util.List;

public class AyudanteInvestigacion extends Usuario {
    private String nombres;
    private String apellidos;
    private String carrera;
    private int semestre;
    private String correoInstitucional;
    private EstadoAyudante estadoAyudante;
    private boolean visiblePublicamente;
    private Proyecto proyectoAsignado;
    private List<Informe> informes;

    public AyudanteInvestigacion(String usuario, String clave, String nombres, String apellidos,
            String carrera, int semestre, String correoInstitucional) {
        super(usuario, clave);
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.carrera = carrera;
        this.semestre = semestre;
        this.correoInstitucional = correoInstitucional;
        this.estadoAyudante = EstadoAyudante.ACTIVO;
        this.visiblePublicamente = true;
        this.informes = new ArrayList<>();
    }

    public void registrarInforme(Informe informe) {
        informes.add(informe);
        if (proyectoAsignado != null) {
            proyectoAsignado.agregarInforme(informe);
        }
    }

    public List<Informe> descargarInforme() {
        return new ArrayList<>(informes);
    }

    public void consultarDashboard() {
        // Implemented in visual layer
    }

    public void darseDeBaja(Proyecto proyecto, String motivo) {
        if (this.proyectoAsignado != null && this.proyectoAsignado.equals(proyecto)) {
            proyecto.registrarRetiroAyudante();
            this.proyectoAsignado = null;
            this.estadoAyudante = EstadoAyudante.INACTIVO;
        }
    }

    public void cambiarVisibilidad() {
        this.visiblePublicamente = !this.visiblePublicamente;
    }

    // Getters and Setters
    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    @Override
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public String getCarrera() {
        return carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public EstadoAyudante getEstadoAyudante() {
        return estadoAyudante;
    }

    public void setEstadoAyudante(EstadoAyudante estado) {
        this.estadoAyudante = estado;
    }

    public boolean isVisiblePublicamente() {
        return visiblePublicamente;
    }

    public Proyecto getProyectoAsignado() {
        return proyectoAsignado;
    }

    public void setProyectoAsignado(Proyecto p) {
        this.proyectoAsignado = p;
    }

    public List<Informe> getInformes() {
        return informes;
    }

    @Override
    public String getRol() {
        return "Ayudante";
    }
}
