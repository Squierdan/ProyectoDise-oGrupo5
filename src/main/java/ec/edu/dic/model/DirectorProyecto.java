package ec.edu.dic.model;

public class DirectorProyecto extends Usuario {
    public String nombres;
    public String apellidos;
    public String correoInstitucional;
    public String departamento;

    public void registrarAyudante(AyudanteInvestigacion ayudanteInvestigacion) { }
    public void desactivarAyudante(AyudanteInvestigacion ayudanteInvestigacion) { }
    public void revisarInforme(Informe informe) { }
    public void visualizarCantidadCuposDisponibles() { }
    public void consultarInformesAyudantes() { }
    public void crearProyecto(Proyecto proyecto) { }
    public void generarInformeProyecto(Informe informe) { }
}
