public class Reporte {
    private String identificador;
    private String equipo;
    private String ubicacion;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;

    public Reporte(
            String identificador,
            String equipo,
            String ubicacion,
            String descripcion,
            Prioridad prioridad,
            Estado estado) {
        this.identificador = identificador;
        this.equipo = equipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getEquipo() {
        return equipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String obtenerDetalle() {
        String detalleCompleto = "----------------------------------------\n"
                + "ID: " + identificador + "\n"
                + "Equipo: " + equipo + "\n"
                + "Ubicacion: " + ubicacion + "\n"
                + "Descripcion: " + descripcion + "\n"
                + "Prioridad: " + prioridad + "\n"
                + "Estado: " + estado + "\n"
                + "----------------------------------------";
        return detalleCompleto;
    }
}
