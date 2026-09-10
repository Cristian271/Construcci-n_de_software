import java.util.ArrayList;

public class GestorReportes {
    private ArrayList<Reporte> listaReportes;
    private int contadorConsecutivo;

    /*
    Constructor a utilizar al iniciar el sistema para inicializar
    el almacenamiento temporal de incidencias y el correlativo numérico.
    */
    public GestorReportes() {
        this.listaReportes = new ArrayList<Reporte>();
        this.contadorConsecutivo = 1;
    }

    /*
    Método a llamar cuando el usuario confirma la captura de datos de un nuevo reporte
    técnico y se requiere validar campos obligatorios y generar el código único INC.
    */
    public Reporte registrarReporte(
            String equipo,
            String ubicacion,
            String descripcion,
            String prioridad) {
        String identificadorGenerado = "";
        String estadoInicial = "Pendiente";
        Reporte nuevoReporte = null;

        if (equipo == null || equipo.trim().isEmpty()) {
            throw new IllegalArgumentException("El equipo no puede quedar vacio.");
        }
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            throw new IllegalArgumentException("La ubicacion no puede quedar vacia.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripcion no puede quedar vacia.");
        }
        if (prioridad == null || prioridad.trim().isEmpty()) {
            throw new IllegalArgumentException("La prioridad no puede quedar vacia.");
        }

        identificadorGenerado = "INC-" + String.format("%03d", contadorConsecutivo);
        contadorConsecutivo++;

        nuevoReporte = new Reporte(
                identificadorGenerado,
                equipo.trim(),
                ubicacion.trim(),
                descripcion.trim(),
                prioridad.trim(),
                estadoInicial);

        listaReportes.add(nuevoReporte);
        return nuevoReporte;
    }

    /*
    Método a llamar cuando la interfaz requiera imprimir todas las incidencias
    almacenadas hasta el momento en la sesión.
    */
    public ArrayList<Reporte> obtenerTodosLosReportes() {
        return listaReportes;
    }

    /*
    Método a llamar cuando el usuario ingresa un identificador de falla para consultar
    su información detallada o previo a actualizar su avance.
    */
    public Reporte buscarReportePorIdentificador(String identificadorBuscado) {
        Reporte reporteEncontrado = null;
        int indiceReporte = 0;
        Reporte reporteActual = null;

        if (identificadorBuscado == null || identificadorBuscado.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede estar vacio.");
        }

        for (indiceReporte = 0; indiceReporte < listaReportes.size(); indiceReporte++) {
            reporteActual = listaReportes.get(indiceReporte);
            if (reporteActual.obtenerIdentificador().equalsIgnoreCase(identificadorBuscado.trim())) {
                reporteEncontrado = reporteActual;
                break;
            }
        }

        return reporteEncontrado;
    }

    /*
    Método a llamar cuando el personal técnico concluye una etapa de atención
    y necesita progresar la incidencia siguiendo el flujo Pendiente -> En proceso -> Resuelta.
    */
    public void cambiarEstadoReporte(String identificadorBuscado) {
        Reporte reporteAActualizar = buscarReportePorIdentificador(identificadorBuscado);
        String estadoActual = "";

        if (reporteAActualizar == null) {
            throw new IllegalArgumentException("No se encontro ningun reporte con el identificador ingresado.");
        }

        estadoActual = reporteAActualizar.obtenerEstado();

        if (estadoActual.equalsIgnoreCase("Pendiente")) {
            reporteAActualizar.establecerEstado("En proceso");
        } else if (estadoActual.equalsIgnoreCase("En proceso")) {
            reporteAActualizar.establecerEstado("Resuelta");
        } else if (estadoActual.equalsIgnoreCase("Resuelta")) {
            throw new IllegalStateException("El reporte ya se encuentra en estado Resuelta y no puede modificarse.");
        }
    }
}
