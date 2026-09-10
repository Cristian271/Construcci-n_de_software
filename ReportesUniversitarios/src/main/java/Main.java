import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static GestorReportes gestorReportes = new GestorReportes();
    private static Scanner lectorTeclado = new Scanner(System.in);

    /*
    Punto de entrada principal de la aplicacion que arranca el ciclo
    interactivo para que el personal opere el gestor de incidencias.
    */
    public static void main(String[] args) {
        int opcionSeleccionada = 0;
        String textoEntrada = "";
        boolean continuarEjecucion = true;

        do {
            mostrarMenuOpciones();
            System.out.print("Seleccione una opcion: ");
            textoEntrada = lectorTeclado.nextLine();

            try {
                opcionSeleccionada = Integer.parseInt(textoEntrada);
            } catch (NumberFormatException excepcionNumero) {
                opcionSeleccionada = -1;
            }

            switch (opcionSeleccionada) {
                case 1: {
                    ejecutarRegistrarReporte();
                    break;
                }
                case 2: {
                    ejecutarListarReportes();
                    break;
                }
                case 3: {
                    ejecutarBuscarReporte();
                    break;
                }
                case 4: {
                    ejecutarCambiarEstadoReporte();
                    break;
                }
                case 5: {
                    System.out.println("Cerrando aplicacion de incidencias.");
                    continuarEjecucion = false;
                    break;
                }
                default: {
                    System.out.println("Opcion no valida. Por favor ingrese un numero del 1 al 5.");
                    break;
                }
            }
            System.out.println();
        } while (continuarEjecucion);

        lectorTeclado.close();
    }

    /*
    Metodo a llamar al inicio de cada ciclo de interaccion para desplegar
    al usuario las acciones disponibles en el sistema de reportes.
    */
    private static void mostrarMenuOpciones() {
        System.out.println("========================================");
        System.out.println("       MINI GESTOR DE INCIDENCIAS       ");
        System.out.println("========================================");
        System.out.println("1. Registrar reporte");
        System.out.println("2. Listar todos los reportes");
        System.out.println("3. Buscar reporte por identificador");
        System.out.println("4. Cambiar estado de un reporte");
        System.out.println("5. Salir");
        System.out.println("========================================");
    }

    /*
    Metodo a llamar cuando el operador selecciona la opcion de alta de incidencia,
    solicitando los datos por teclado y procesando el registro mediante el gestor.
    */
    private static void ejecutarRegistrarReporte() {
        String equipoIngresado = "";
        String ubicacionIngresada = "";
        String descripcionIngresada = "";
        String prioridadIngresada = "";
        Reporte reporteRegistrado = null;

        System.out.println("\n--- REGISTRAR REPORTE ---");
        System.out.print("Ingrese equipo/elemento: ");
        equipoIngresado = lectorTeclado.nextLine();

        System.out.print("Ingrese ubicacion (aula/laboratorio): ");
        ubicacionIngresada = lectorTeclado.nextLine();

        System.out.print("Ingrese descripcion del problema: ");
        descripcionIngresada = lectorTeclado.nextLine();

        System.out.print("Ingrese prioridad (Baja / Media / Alta): ");
        prioridadIngresada = lectorTeclado.nextLine();

        try {
            reporteRegistrado = gestorReportes.registrarReporte(
                    equipoIngresado,
                    ubicacionIngresada,
                    descripcionIngresada,
                    prioridadIngresada);
            System.out.println("\nReporte registrado con exito con identificador: "
                    + reporteRegistrado.obtenerIdentificador());
        } catch (IllegalArgumentException excepcionValidacion) {
            System.out.println("\nError al registrar: " + excepcionValidacion.getMessage());
        }
    }

    /*
    Metodo a llamar cuando el operador requiere una vista panoramica de todas
    las incidencias registradas en memoria durante la sesion.
    */
    private static void ejecutarListarReportes() {
        ArrayList<Reporte> listaReportes = null;
        int indiceReporte = 0;
        Reporte reporteActual = null;

        System.out.println("\n--- LISTADO DE REPORTES ---");
        listaReportes = gestorReportes.obtenerTodosLosReportes();

        if (listaReportes.isEmpty()) {
            System.out.println("No hay reportes registrados actualmente.");
        } else {
            for (indiceReporte = 0; indiceReporte < listaReportes.size(); indiceReporte++) {
                reporteActual = listaReportes.get(indiceReporte);
                System.out.println(reporteActual.obtenerDetalle());
            }
        }
    }

    /*
    Metodo a llamar cuando se necesita consultar la ficha tecnica de un reporte especifico
    a traves de su codigo unico identificador.
    */
    private static void ejecutarBuscarReporte() {
        String identificadorConsulta = "";
        Reporte reporteLocalizado = null;

        System.out.println("\n--- BUSCAR REPORTE ---");
        System.out.print("Ingrese identificador (ejemplo INC-001): ");
        identificadorConsulta = lectorTeclado.nextLine();

        try {
            reporteLocalizado = gestorReportes.buscarReportePorIdentificador(identificadorConsulta);
            if (reporteLocalizado != null) {
                System.out.println("\nReporte encontrado:");
                System.out.println(reporteLocalizado.obtenerDetalle());
            } else {
                System.out.println("\nNo se encontro ningun reporte con el identificador ingresado.");
            }
        } catch (IllegalArgumentException excepcionParametro) {
            System.out.println("\nError: " + excepcionParametro.getMessage());
        }
    }

    /*
    Metodo a llamar cuando el personal tecnico actualiza la etapa de atencion de una
    incidencia conforme progresa en su resolucion.
    */
    private static void ejecutarCambiarEstadoReporte() {
        String identificadorModificar = "";
        Reporte reporteActualizado = null;

        System.out.println("\n--- CAMBIAR ESTADO DE REPORTE ---");
        System.out.print("Ingrese identificador del reporte: ");
        identificadorModificar = lectorTeclado.nextLine();

        try {
            gestorReportes.cambiarEstadoReporte(identificadorModificar);
            reporteActualizado = gestorReportes.buscarReportePorIdentificador(identificadorModificar);
            System.out.println("\nEstado actualizado con exito.");
            System.out.println("Nuevo estado: " + reporteActualizado.obtenerEstado());
        } catch (IllegalArgumentException excepcionParametro) {
            System.out.println("\nError: " + excepcionParametro.getMessage());
        } catch (IllegalStateException excepcionEstado) {
            System.out.println("\nTransicion no permitida: " + excepcionEstado.getMessage());
        }
    }
}
