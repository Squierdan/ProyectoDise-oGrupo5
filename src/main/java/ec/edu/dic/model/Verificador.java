package ec.edu.dic.model;

import java.time.LocalDateTime;

public class Verificador {
    public int id;
    public int id_proyecto;
    public boolean documentacion_completa;
    public boolean ayudantes_confirmados;
    public boolean presupuesto_validado;
    public boolean requisitos_tecnicos_ok;
    public LocalDateTime fecha_verificacion;
    public String observaciones;

    public void verificarCompletitud(Proyecto proyecto) { }
    public void validarAyudantesPrevistos() { }
    public void validarDocumentacion() { }
    public void generarReporteVerificacion() { }
    public void aprobarParaFirma() { }
}
