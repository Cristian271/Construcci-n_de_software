import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void menuPrincipal(){
        System.out.println("**************************************");
        System.out.println(" Bienvenido al Gestor de Incidencias");
        System.out.println("**************************************");
        System.out.println("Ingrese el número correspondiente a la tarea que desee realizar: ");
        System.out.println("1. Registrar un nuevo reporte.");
        System.out.println("2. Buscar un reporte.");
        System.out.println("3. Actualizar estado de un reporte.");
        System.out.println("4. Listar los reportes existentes.");
        System.out.println("5. Salir del sistema.");
    }

    public static void menuAgregarReporte(Scanner scanner, GestorReportes gestor) {
        String equipo;
        String ubicacion;
        String descripcion;
        Prioridad prioridad = null;
        Estado estado = null;
        int opcionPrioridad;
        int opcionEstado;


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

    }
    public static void menuBuscarReporte(Scanner scanner, GestorReportes gestor){


    }
    public static void menuActualizarReporte(Scanner scanner, GestorReportes gestor){

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorReportes gestorReportes = new GestorReportes();
        String seleccion = "0";

        do {
            menuPrincipal();
            switch (seleccion){
                case "1":
                    menuAgregarReporte(scanner, gestorReportes);
                    break;
                case "2":
                    menuBuscarReporte(scanner, gestorReportes);
                    break;
                case "3":
                    menuActualizarReporte(scanner, gestorReportes);
                    break;
                case "4":
                    gestorReportes.imprimirReportes();
                    break;
                case "5":
                    System.out.println("¡Adiós!");
                    break;
                default:
                    System.out.println("Por favor seleccione una opción válida");
            }
        } while (seleccion.equals("0"));
    }

}
