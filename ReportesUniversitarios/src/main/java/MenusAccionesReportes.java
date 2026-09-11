import java.util.Scanner;

public class MenusAccionesReportes {
    public static void menuAgregarReporte(Scanner scanner, GestorReportes gestor) {
        String equipo = "";
        String ubicacion = "";
        String descripcion = "";
        Prioridad prioridad = null;
        Estado estado = null;
        int opcionPrioridad = 0;
        int opcionEstado = 0;


        System.out.println("Ingrese el equipo a reportar");
        equipo = scanner.nextLine();

        System.out.println("Ingresa la ubicación del equipo ");
        ubicacion = scanner.nextLine();

        System.out.println("Ingresa la descripcion del reporte: ");
        descripcion = scanner.nextLine();


        while (prioridad==null){

            System.out.println("Selecciona la prioridad:");
            System.out.println("1. ALTA");
            System.out.println("2. MEDIA");
            System.out.println("3. BAJA");
            System.out.print("Opción: ");

            opcionPrioridad = scanner.nextInt();
            scanner.nextLine();

            if (opcionPrioridad==1) {
                prioridad = Prioridad.ALTA;
            } else if (opcionPrioridad == 2) {
                prioridad = Prioridad.MEDIA;
            } else if (opcionPrioridad == 3) {
                prioridad = Prioridad.BAJA;
            } else {
                System.out.println("Opción inválida, elige del 1-3");
            }
        }

        while (estado == null){
            System.out.println("Selecciona el estado del reporte:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. EN PROCESO");
            System.out.println("3. RESUELTA");
            System.out.print("Opción: ");

            opcionEstado= scanner.nextInt();
            scanner.nextLine();

            if (opcionEstado == 1) {
                estado = Estado.PENDIENTE;
            } else if (opcionEstado == 2) {
                estado = Estado.EN_PROCESO;
            } else if (opcionEstado == 3) {
                estado = Estado.RESUELTA;
            } else {
                System.out.println("Opción inválida, elige del 1-3");
            }
        }

        Reporte nuevoReporte = new Reporte(equipo, ubicacion, descripcion, prioridad, estado);
        gestor.agregarReporte(nuevoReporte);
        System.out.println("Incidencia Registrada");

    }
    public static void menuBuscarReporte(Scanner scanner, GestorReportes gestor){
        System.out.println("Ingresa el id del reporte que desees buscar");
        String idEnBusqueda = scanner.nextLine();

        if (idEnBusqueda != ""){
            Reporte reporteBuscado = gestor.buscarReporte(idEnBusqueda);
            if (reporteBuscado != null){
                System.out.println(reporteBuscado.obtenerDetalle());
            } else{
                System.out.println("No existe reporte con el id ingresado");
            }
        } else{
            System.out.println("Por favor ingresa un id");
        }
    }
    public static void menuActualizarReporte(Scanner scanner, GestorReportes gestor){

    }
}
