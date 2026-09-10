public class Reporte {
    private String identificador;
    private String equipo;
    private String ubicacion;
    private String descripcion;
    private String prioridad;
    private String estado;

    /*
    Constructor a utilizar al momento de dar de alta un nuevo reporte
    con los datos capturados y el identificador previamente asignado.
    */
    public Reporte(
            String identificador,
            String equipo,
            String ubicacion,
            String descripcion,
            String prioridad,
            String estado) {
        this.identificador = identificador;
        this.equipo = equipo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
    }

    /*
    Método a llamar cuando se requiere consultar el código único
    del reporte para búsquedas o comparaciones.
    */
    public String obtenerIdentificador() {
        return identificador;
    }

    /*
    Método a llamar cuando se necesita presentar el nombre o tipo de equipo
    que presenta la falla técnica.
    */
    public String obtenerEquipo() {
        return equipo;
    }

    /*
    Método a llamar cuando se requiere saber en qué aula o laboratorio
    se encuentra la incidencia.
    */
    public String obtenerUbicacion() {
        return ubicacion;
    }

    /*
    Método a llamar cuando se desea consultar el detalle de la falla
    redactado por el usuario.
    */
    public String obtenerDescripcion() {
        return descripcion;
    }

    /*
    Método a llamar cuando se requiere evaluar o mostrar el nivel de urgencia
    asociado a la atención del reporte.
    */
    public String obtenerPrioridad() {
        return prioridad;
    }

    /*
    Método a llamar cuando se necesita conocer la fase de atención en la que
    se encuentra el reporte o validar transiciones.
    */
    public String obtenerEstado() {
        return estado;
    }

    /*
    Método a llamar cuando el área técnica actualiza la fase de atención del
    reporte tras iniciar labores o concluir la resolución de la falla.
    */
    public void establecerEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    /*
    Método a llamar cuando se necesite mostrar en pantalla o consola la ficha
    completa de la incidencia de forma legible y estructurada.
    */
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
