package logic;

import java.util.ArrayList;
import java.util.List;

public class JefeDIC extends Usuario {
    private String nombres;
    private String apellidos;
    private String correoInstitucional;
    private String departamento;
    private List<Proyecto> proyectosSupervisados;
    private List<DirectorProyecto> directoresRegistrados;

    public JefeDIC(String usuario, String clave, String nombres, String apellidos,
            String correoInstitucional, String departamento) {
        super(usuario, clave);
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correoInstitucional = correoInstitucional;
        this.departamento = departamento;
        this.proyectosSupervisados = new ArrayList<>();
        this.directoresRegistrados = new ArrayList<>();
    }

    public void subirDocumentoDeRegistro(Proyecto proyecto) {
        // Placeholder for document upload functionality
    }

    public void registrarDirector(DirectorProyecto director) {
        if (!directoresRegistrados.contains(director)) {
            directoresRegistrados.add(director);
        }
    }

    public void asignarProyecto(DirectorProyecto director, Proyecto proyecto) {
        director.asignarProyecto(proyecto);
        proyecto.setDirector(director);
        if (!proyectosSupervisados.contains(proyecto)) {
            proyectosSupervisados.add(proyecto);
        }
    }

    public void asignarCantidadDeAyudantes(Proyecto proyecto, int cantidad) {
        proyecto.setCantidadAyudantes(cantidad);
    }

    public List<Informe> visualizarInformes() {
        List<Informe> todos = new ArrayList<>();
        for (Proyecto p : proyectosSupervisados) {
            todos.addAll(p.getInformes());
        }
        return todos;
    }

    public List<DirectorProyecto> visualizarDirectores() {
        return new ArrayList<>(directoresRegistrados);
    }

    public List<AyudanteInvestigacion> visualizarAyudantes() {
        List<AyudanteInvestigacion> todos = new ArrayList<>();
        for (Proyecto p : proyectosSupervisados) {
            todos.addAll(p.getAyudantes());
        }
        return todos;
    }

    public void revisarInforme(Informe informe, String feedback) {
        informe.setFeedbackJefe(feedback);
        informe.setRevisadoPorJefe(true);
        informe.setEstado(EstadoInforme.PENDIENTE_JEFE);
    }

    public void firmarInforme(Informe informe) {
        informe.firmar();
    }

    public List<Proyecto> consultarProyectosActivos() {
        List<Proyecto> activos = new ArrayList<>();
        for (Proyecto p : proyectosSupervisados) {
            if (p.getEstado() == EstadoProyecto.ACTIVO) {
                activos.add(p);
            }
        }
        return activos;
    }

    public String compararAyudantesPrevistos(Proyecto proyecto) {
        int previstos = proyecto.getCantidadAyudantes();
        int actuales = proyecto.getAyudantesActuales();
        int retirados = proyecto.getAyudantesRetirados();
        return String.format("Previstos: %d, Actuales: %d, Retirados: %d, Cobertura: %.1f%%",
                previstos, actuales, retirados, proyecto.calcularPorcentajeCobertura());
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

    public List<Proyecto> getProyectosSupervisados() {
        return proyectosSupervisados;
    }

    public List<DirectorProyecto> getDirectoresRegistrados() {
        return directoresRegistrados;
    }

    @Override
    public String getRol() {
        return "Jefe DIC";
    }
}
