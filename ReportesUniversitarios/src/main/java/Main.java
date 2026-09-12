import java.util.ArrayList;
import java.util.Scanner;

public class Main {


    public static void menuPrincipal(){
        System.out.println("**************************************");
        System.out.println(" Bienvenido al Gestor de Incidencias");
        System.out.println("**************************************");
        System.out.println("1. Registrar un nuevo reporte.");
        System.out.println("2. Buscar un reporte.");
        System.out.println("3. Actualizar estado de un reporte.");
        System.out.println("4. Listar los reportes existentes.");
        System.out.println("5. Salir del sistema.");
        System.out.println("Ingrese el número correspondiente a la tarea que desee realizar: ");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorReportes gestorReportes = new GestorReportes();
        String seleccion = "0";
        String controlMenuPrincipal = "0";

        do {
            menuPrincipal();
            seleccion = scanner.nextLine();
            switch (seleccion){
                case "1":
                    MenusAccionesReportes.menuAgregarReporte(scanner, gestorReportes);
                    break;
                case "2":
                    MenusAccionesReportes.menuBuscarReporte(scanner, gestorReportes);
                    break;
                case "3":
                    MenusAccionesReportes.menuActualizarEstado(scanner, gestorReportes);
                    break;
                case "4":
                    gestorReportes.imprimirReportes();
                    break;
                case "5":
                    System.out.println("¡Adiós!");
                    controlMenuPrincipal="1";
                    break;
                default:
                    System.out.println("Por favor seleccione una opción válida");
            }
        } while (controlMenuPrincipal.equals("0"));
    }

}
