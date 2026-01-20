package ec.edu.dic.model;

import java.time.LocalDateTime;
import ec.edu.dic.model.enums.EstadoProyecto;
import ec.edu.dic.model.enums.TipoProyecto;

public class Proyecto {
    public String nombre;
    public int cantidadAyudantes;
    public EstadoProyecto estado;
    public TipoProyecto tipo_proyecto;
    public String descripcion;
    public LocalDateTime fecha_inicio;
    public LocalDateTime fecha_fin;
    public int ayudantes_actuales;
    public int ayudantes_retirados;
    public float presupuesto;

    public void calcularProgreso() { }
    public void calcularPorcentajeCobertura() { }
    public void registrarRetiroAyudante() { }
}
