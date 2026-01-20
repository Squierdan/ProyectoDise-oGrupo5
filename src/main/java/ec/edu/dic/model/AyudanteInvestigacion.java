package ec.edu.dic.model;

import ec.edu.dic.model.enums.EstadoAyudante;

public class AyudanteInvestigacion extends Usuario {
    public String nombres;
    public String apellidos;
    public String carrera;
    public int semestre;
    public String correoInstitucional;
    public EstadoAyudante estadoAyudante;
    public boolean visible_publicamente;

    public void registrarInforme(Informe informe) { }
    public void descargarInforme() { }
    public void consultarDashboard() { }
    public void darseDeBaja(Proyecto proyecto, String motivo) { }
    public void cambiarVisibilidad() { }
}
