package logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {
    private static int idCounter = 1;
    private int id;
    private String nombre;
    private int cantidadAyudantes;
    private EstadoProyecto estado;
    private TipoProyecto tipoProyecto;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private int ayudantesActuales;
    private int ayudantesRetirados;
    private float presupuesto;
    private DirectorProyecto director;
    private List<AyudanteInvestigacion> ayudantes;
    private List<Informe> informes;
    private Verificador verificador;

    public Proyecto(String nombre, String descripcion, TipoProyecto tipoProyecto,
            int cantidadAyudantes, float presupuesto, DirectorProyecto director) {
        this.id = idCounter++;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoProyecto = tipoProyecto;
        this.cantidadAyudantes = cantidadAyudantes;
        this.presupuesto = presupuesto;
        this.director = director;
        this.estado = EstadoProyecto.POSTULACION;
        this.fechaInicio = LocalDateTime.now();
        this.fechaFin = null;
        this.ayudantesActuales = 0;
        this.ayudantesRetirados = 0;
        this.ayudantes = new ArrayList<>();
        this.informes = new ArrayList<>();
    }

    public double calcularProgreso() {
        if (informes.isEmpty())
            return 0.0;
        long aprobados = informes.stream()
                .filter(i -> i.getEstado() == EstadoInforme.FIRMADO_JEFE)
                .count();
        return (double) aprobados / informes.size() * 100;
    }

    public double calcularPorcentajeCobertura() {
        if (cantidadAyudantes == 0)
            return 0.0;
        return (double) ayudantesActuales / cantidadAyudantes * 100;
    }

    public void registrarRetiroAyudante() {
        if (ayudantesActuales > 0) {
            ayudantesActuales--;
            ayudantesRetirados++;
        }
    }

    public void agregarAyudante(AyudanteInvestigacion ayudante) {
        if (ayudantesActuales < cantidadAyudantes && !ayudantes.contains(ayudante)) {
            ayudantes.add(ayudante);
            ayudante.setProyectoAsignado(this);
            ayudantesActuales++;
        }
    }

    public void agregarInforme(Informe informe) {
        informes.add(informe);
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidadAyudantes() {
        return cantidadAyudantes;
    }

    public void setCantidadAyudantes(int cantidad) {
        this.cantidadAyudantes = cantidad;
    }

    public EstadoProyecto getEstado() {
        return estado;
    }

    public void setEstado(EstadoProyecto estado) {
        this.estado = estado;
    }

    public TipoProyecto getTipoProyecto() {
        return tipoProyecto;
    }

    public void setTipoProyecto(TipoProyecto tipo) {
        this.tipoProyecto = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fecha) {
        this.fechaFin = fecha;
    }

    public int getAyudantesActuales() {
        return ayudantesActuales;
    }

    public int getAyudantesRetirados() {
        return ayudantesRetirados;
    }

    public float getPresupuesto() {
        return presupuesto;
    }

    public DirectorProyecto getDirector() {
        return director;
    }

    public void setDirector(DirectorProyecto d) {
        this.director = d;
    }

    public List<AyudanteInvestigacion> getAyudantes() {
        return ayudantes;
    }

    public List<Informe> getInformes() {
        return informes;
    }

    public Verificador getVerificador() {
        return verificador;
    }

    public void setVerificador(Verificador v) {
        this.verificador = v;
    }
}
