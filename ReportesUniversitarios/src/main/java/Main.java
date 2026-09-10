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
        System.out.println("3. Actualizar la infromación de un reporte.");
        System.out.println("4. Listar los reportes existentes.");
        System.out.println("5. Salir del sistema.");
    }

    public static void main(String[] args) {
        GestorReportes gestorReportes = new GestorReportes();
        String seleccion = "0";

        do {
            menuPrincipal();
            switch (seleccion){
                case "1":
                    gestorReportes.agregarReporte();
                case "2":
                    gestorReportes.buscarReporte();
                case "3":
                    gestorReportes.actualizarEstado();
                case "4":
                    gestorReportes.imprimirReportes();
                case "5":
                    System.out.println("¡Adiós!");
                default:
                    System.out.println("Por favor seleccione una opción válida");
            }
        } while (seleccion.equals("0"));
    }

}
