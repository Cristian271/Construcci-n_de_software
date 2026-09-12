import java.util.Scanner;

public class MenusAccionesReportes {
    public static void menuAgregarReporte(Scanner scanner, GestorReportes gestor) {
        String equipo = "";
        String ubicacion = "";
        String descripcion = "";
        Prioridad prioridad = null;
        int opcionPrioridad = 0;


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


        Reporte nuevoReporte = new Reporte(equipo, ubicacion, descripcion, prioridad);
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
    public static void menuActualizarEstado(Scanner scanner, GestorReportes gestor){
        String idActualizar;
        Reporte reporteActualizar;
        Estado nuevoEstado =  null;
        int opcionEstado = 0;
        System.out.println("Ingrese Id de reporte a actualizar: ");
        idActualizar = scanner.nextLine();
        if (idActualizar != null){
            reporteActualizar = gestor.buscarReporte(idActualizar);
            if (reporteActualizar.getEstado()!= Estado.RESUELTA){
                if (reporteActualizar.getPrioridad()== Prioridad.ALTA){
                    if(reporteActualizar.getEstado()==Estado.PENDIENTE){
                        reporteActualizar.setEstado(Estado.EN_PROCESO);
                        System.out.println("Reporte en proceso");
                    } else if(reporteActualizar.getEstado()== Estado.EN_PROCESO){
                        reporteActualizar.setEstado(Estado.RESUELTA);
                        System.out.println("Reporte resuelto");
                    }
                } else {
                    do {
                        System.out.println("Elige el nuevo estado del reporte");
                        System.out.println("1.- EN PROCESO");
                        System.out.println("2.- RESUELTA");
                        System.out.println("Opcion: ");
                        opcionEstado = scanner.nextInt();
                        scanner.nextLine();
                        if (opcionEstado == 1) {
                            nuevoEstado = Estado.EN_PROCESO;
                        } else if (opcionEstado == 2) {
                            nuevoEstado = Estado.RESUELTA;
                        } else {
                            System.out.println("Opción inválida, elige del 1-2");
                        }
                    } while (opcionEstado!=1 && opcionEstado!=2 );
                    gestor.actualizarEstado(reporteActualizar, nuevoEstado);
                    System.out.println("Estado actualizado con éxito");
                }

            } else {
                System.out.println("Tu reporte ya fue resuelto");
            }

        } else {
            System.out.println("Por favor ingresa un id");
        }
    }
}
