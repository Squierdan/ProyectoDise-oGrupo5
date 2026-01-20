package logic;

import java.util.ArrayList;
import java.util.List;

public class DirectorProyecto extends Usuario {
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String departamento;
    private List<Proyecto> proyectos;
    private List<Whitelist> whitelists;

    public DirectorProyecto(String usuario, String clave, String nombres, String apellidos,
            String correoInstitucional, String departamento) {
        super(usuario, clave);
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoInstitucional = correoInstitucional;
        this.departamento = departamento;
        this.proyectos = new ArrayList<>();
        this.whitelists = new ArrayList<>();
    }

    public void registrarAyudante(AyudanteInvestigacion ayudante, Proyecto proyecto) {
        if (proyectos.contains(proyecto)) {
            proyecto.agregarAyudante(ayudante);
        }
    }

    public void desactivarAyudante(AyudanteInvestigacion ayudante) {
        ayudante.setEstadoAyudante(EstadoAyudante.INACTIVO);
    }

    public void revisarInforme(Informe informe, String feedback) {
        informe.setFeedbackDirector(feedback);
        informe.setRevisadoPorDirector(true);
        informe.setEstado(EstadoInforme.REVISADO_DIRECTOR);
    }

    public int visualizarCantidadCuposDisponibles(Proyecto proyecto) {
        return proyecto.getCantidadAyudantes() - proyecto.getAyudantesActuales();
    }

    public List<Informe> consultarInformesAyudantes(Proyecto proyecto) {
        return proyecto.getInformes();
    }

    public Proyecto crearProyecto(String nombre, String descripcion, TipoProyecto tipo, int cantidadAyudantes,
            float presupuesto) {
        Proyecto p = new Proyecto(nombre, descripcion, tipo, cantidadAyudantes, presupuesto, this);
        proyectos.add(p);
        return p;
    }

    public Informe generarInformeProyecto(Proyecto proyecto) {
        Informe informe = new Informe(TipoInforme.PROYECTO_DIRECTOR, "Informe consolidado del proyecto", null,
                proyecto);
        proyecto.agregarInforme(informe);
        return informe;
    }

    public Whitelist invitarAyudante(String correo, Proyecto proyecto) {
        Whitelist w = new Whitelist(correo, this, proyecto);
        whitelists.add(w);
        return w;
    }

    public SolicitudAyudante solicitarAgregarAyudante(AyudanteInvestigacion ayudante, Proyecto proyecto) {
        SolicitudAyudante solicitud = new SolicitudAyudante(this, ayudante, proyecto);
        Sistema.getInstance().agregarSolicitud(solicitud);
        return solicitud;
    }

    // Getters
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

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public String getDepartamento() {
        return departamento;
    }

    public List<Proyecto> getProyectos() {
        return proyectos;
    }

    public List<Whitelist> getWhitelists() {
        return whitelists;
    }

    public void asignarProyecto(Proyecto p) {
        if (!proyectos.contains(p)) {
            proyectos.add(p);
        }
    }

    @Override
    public String getRol() {
        return "Director";
    }
}
