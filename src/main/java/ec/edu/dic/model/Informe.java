package ec.edu.dic.model;

import java.time.LocalDateTime;
import ec.edu.dic.model.enums.EstadoInforme;
import ec.edu.dic.model.enums.TipoInforme;

public class Informe {
    public String tipo;
    public LocalDateTime fecha;
    public EstadoInforme estado;
    public String actividades;
    public boolean revisadoPorDirector;
    public boolean revisadoPorJefe;
    public String feedback_director;
    public String feedback_jefe;
    public LocalDateTime fecha_firma_jefe;
    public String archivo_url;

    public void notificarAJefeDepartamento() { }
    public void notificarADirector() { }
    public void validarEntrega() { }
}
