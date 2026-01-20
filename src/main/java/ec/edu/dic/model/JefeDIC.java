package ec.edu.dic.model;

public class JefeDIC extends Usuario {
    public String nombres;
    public String apellidos;
    public String correoInstitucional;
    public String departamento;

    public void subirDocumentoDeRegistro(Proyecto proyecto) { }
    public void registrarDirector(DirectorProyecto directorProyecto) { }
    public void asignarProyecto(DirectorProyecto directorProyecto, Proyecto proyecto) { }
    public void asignarCuposDirector(Proyecto proyecto, int cupos) { }
    public void visualizarInformes() { }
    public void visualizarDirectores() { }
    public void visualizarAyudantes() { }
    public void revisarInforme(Informe informe) { }
    public void firmarInforme(Informe informe) { }
    public void consultarProyectosActivos() { }
    public void compararAyudantesRetirados(Proyecto proyecto) { }
}
