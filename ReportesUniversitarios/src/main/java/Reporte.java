public class Reporte {
    private String identificador;
    private String equipo;
    private String ubicacion;
    private String descripcion;
    private Prioridad prioridad;
    private Estado estado;

    public Reporte(
            String equipo,
            String ubicacion,
            String descripcion,
            Prioridad prioridad) {
        this.equipo = equipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
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

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
