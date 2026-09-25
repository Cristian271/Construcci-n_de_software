package main;

import data.estructures.queue.BusQueue;
import data.estructures.stack.Container;
import main.modulea.Reception;
import main.modulebc.Yard;
import main.moduled.Distribution;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Reception busQueue = new Reception();
    private static Distribution route = new Distribution();
    private static Yard[] pilas; // pilas multiples
    private static int stackLimit;
    private static int activeRoutes;
    // Estos atributos son para crear una lista simple de los contenedores mandados a ruta
    protected static Container start = null;
    protected static Container end = null;

    /**Punto de entrada para el sistema*/
    public static void main(String[] args) {
        configuracionInicial();
        int option = 0;
        do {
            menuPrincipal();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    subMenu1();
                    break;
                case 2:
                    subMenu2();
                    break;
                case 3:
                    subMenu3();
                    break;
                case 4:
                    showGeneralReport();
                    break;
                case 5:
                    System.out.println("Programa finalizado");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        } while (option != 5);
    }

    /**Es la primera función a la que se llama una vez se inicializa el programa.
        Sin parametros de entrada o salida, en su ejecución le solicita al usuario la cantidad de pilas de contenedores que se van a gestionar y el limite de capacidad de estas.
        De este modo es que inicializa el arreglo de pilas y establce el limite en cada elemento.
    */
    private static void configuracionInicial() {
        System.out.println("=== CONFIGURACIÓN INICIAL DEL PUERTO (CAPACIDAD MÁXIMA)  ===");
        System.out.println("Ingrese la cantidad de columnas de contenedores (Pilas) que hay: ");
        int cant = scanner.nextInt();
        System.out.println("Defina la altura máxima de cada pila: ");
        stackLimit = scanner.nextInt();
        scanner.nextLine();

        pilas = new Yard[cant];
        for (int i = 0; i < cant; i++) {
            pilas[i] = new Yard(stackLimit);
        }
    }

    /** Función sin parametros de entrada ni salida.
    Se encaraga de una función de impresión para mostrarle al usuario las opciones disponibles dentro del sistema.
     */
    private static void menuPrincipal() {
        System.out.println("\n====================================================");
        System.out.println("   PUERTO PROGRESO SYSTEM - GESTIÓN");
        System.out.println("====================================================");
        System.out.println("[1] ZONA DE RECEPCIÓN");
        System.out.println("[2] PATIO DE CONTENEDORES");
        System.out.println("[3] LOGÍSTICA Y RUTAS ");
        System.out.println("[4] REPORTE GENERAL ");
        System.out.println("[5] SALIR");
        System.out.println("Seleccione una opción: ");
    }

    /** Función que se despliega en la selección del primer módulo (A), la zona de recepción.
    Se encaraga de desplegar el menú de este módulo y regsitrar la opción seleccionada.
    Además, utiliza los elementos establecidos en BusQueue para llamar a realizar las acciones solicitas.
    */
    private static void subMenu1() {
        int option;
        do {
            Reception.menu();
            System.out.println("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    busQueue.enqueue();
                    break;
                case 2:
                    BusQueue bus = busQueue.dequeue();
                    if (bus != null) {
                        System.out.println("Camión " + bus.getNameBus() + " ingresando al patio");
                    }
                    break;
                case 3:
                    if (!busQueue.isEmpty()) {
                        System.out.println("Siguiente: " + busQueue.front().getNameBus());
                    } else {
                        System.out.println("No hay camiones en espera");
                    }
                    break;
                case 4:
                    busQueue.print();
                    break;
                case 5:
                    System.out.println("Volviendo ...");
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (option != 5);
    }

    /** Función que se despliega en la selección del segundo módulo (B), el patio de contenedores.
    Se encaraga de desplegar el menú de este módulo y regsitrar la opción seleccionada.
    Maneja la información de los camiones, es decir, la información de las pilas. Tambien genera una lista simple con todos
    los camiones mandados a ruta
    */
    private static void subMenu2() {
        int option;
        do {
            Yard.menu();
            System.out.println("Seleccione una opción: ");
            option = scanner.nextInt();
            if (option >= 1 && option <= 4) {
                System.out.println("ID de Pila (numero de 0 a " + (pilas.length - 1) + "): ");
                //refact: introduce explaining variable p -> idPila en subMenu2 y inspect
                int idPila = scanner.nextInt();
                scanner.nextLine();
                if (idPila < 0 || idPila >= pilas.length) {
                    System.out.println("Pila no valida");
                    continue;
                }


                switch (option) {
                    case 1:
                        System.out.println("ID del nuevo contenedor: ");
                        String idC = scanner.nextLine();
                        pilas[idPila].push(new Container(idC));
                        break;
                    case 2:
                        if (route.isEmpty()) {
                            System.out.println("No hay paradas creadas. Cree ruta primero.");
                            break;
                        }
                        System.out.println("ID del contenedor a retirar");
                        String idS = scanner.nextLine();
                        Container popped = pilas[idPila].popContainer(idS);
                        if (popped == null){
                            System.out.println("No se encontró el contenedor con el id: "+ idS);
                        } else {
                            System.out.println("Se retiró el contenedor y se mandó a la ruta, id: "+ popped.getId());
                            activeRoutes++;
                            popped.setPositionC(route.firstStopBus());
                            if(start == null){
                                start = end = popped;
                            } else{
                                end.setNext(popped);
                                end = popped;
                            }

                    }


                        break;
                    case 3:
                        if (!pilas[idPila].isEmpty()) {
                            System.out.println("Tope actual: " + pilas[idPila].top().getId());
                        }
                        break;
                    case 4:
                        inspect(idPila);
                        break;
                    default:
                        System.out.println("Opción no válida.");
                        break;
                }
            }
        } while (option != 5);
    }

    /** Función que se despliega en la selección del tercer módulo (C), el manifiesto de carga.
    Se encaraga de desplegar el menú de este módulo y regsitrar la opción seleccionada.
    Se trata de la descripción de los productos de contenedores mediante el uso de listas simplemente ligadas.
     Funcion que inspecciona cualquier contendor de la pila
    */
    private static void inspect(int idPila) {
        if (pilas[idPila].isEmpty()) {
            System.out.println("No hay nada para inspeccionar en esta pila.");
            return;
        }

        System.out.print("Ingrese ID del contenedor a inspeccionar: ");
        String id = scanner.nextLine();

        // Buscar contenedor sin alterar la pila
        Container actual = pilas[idPila].top();
        while (actual != null && !actual.getId().equals(id)) {
            actual = actual.getNext();
        }
        if (actual == null) {
            System.out.println("No se encontró el contenedor");
            return;
        }
        int option;
        do {
            System.out.println("\n--- INSPECCIONANDO: CONTENEDOR " + actual.getId() + " ---");
            Yard.menuInspeccion();
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    actual.getList().addProduct();
                    break;
                case 2:
                    System.out.println("Peso total: " + actual.getList().calculateWeight() + " kg");
                    break;
                case 3:
                    System.out.print("Producto a buscar: ");
                    String search = scanner.nextLine();
                    if (actual.getList().findProduct(search)) {
                        System.out.println("El producto SI se encuentra en el contenedor");
                    } else {
                        System.out.println("El producto NO se encuentra en este contenedor.");
                    }
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (option != 4);
    }

    /**Este submenu es el encargado de gestionar la distribución, es decir, crear paradas, eliminar paradas y simular rutas*/
    //
    private static void subMenu3() {
        int option;
        do {
            Distribution.menu();
            System.out.println("Seleccione una opción: ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    System.out.println("Escriba el nombre de la nueva parada: ");
                    route.insertEnd(scanner.nextLine());
                    break;
                case 2:
                    System.out.println("A continuación, escriba las paradas entre las que se quiere insertar ");
                    System.out.println("Parada 1: ");
                    String stop1 = scanner.nextLine();
                    System.out.println("Parada 2: ");
                    String stop2 = scanner.nextLine();
                    System.out.println("Nueva parada intermedia: ");
                    String newStop = scanner.nextLine();
                    route.insertBetween(stop1, stop2, newStop);
                    break;
                case 3:
                    System.out.println("Nombre de parada a cancelar: ");
                    route.deleteStop(scanner.nextLine());
                    break;
                case 4:
                    if (route.isEmpty()) {
                        System.out.println("No hay paradas en la ruta para simular.");
                    } else {
                        System.out.println("Ingrese el id del contenedor que se encuentra en ruta para simular su recorrido");
                        String idtemp = scanner.nextLine();
                        boolean encontrado = false;
                        Container current = start;
                        if(start == null){
                            System.out.println("No hay contenedores en ruta");
                            return;
                        }

                        while(current != null){
                            if(current.getId().equals(idtemp)){
                                encontrado = true;
                                break;
                            }
                            current = current.getNext();
                        }
                        if(encontrado){
                            simulateRoute(current);
                        } else{
                            System.out.println("No se puedo localizar el contenedor, asegurese que los datos esten correctos");
                        }

                    }
                    break;
                case 5:
                    System.out.println("Volviendo ... ");
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (option != 5);
    }

    /** Función que se despliega la información del cuarto módulo (D), reporte general.
    No tiene entradas, solo muestra el estado de los camiones (colas), el inventario (pilas) y las rutas.
    */
    private static void showGeneralReport() {
        System.out.println("\n[ESTADO DE RECEPCIÓN]:");
        System.out.println(">> Camiones en espera: " + busQueue.size());
        if (!busQueue.isEmpty()) {
            System.out.println(">> Próximo en turno: " + busQueue.front().getNameBus());
        }


        System.out.println("\n[ESTADO DE INVENTARIO]:");
        for (int i = 0; i < pilas.length; i++) {
            int actualSize = pilas[i].size();

            System.out.print(" >> Pila " + (char) ('A' + i) + ": [");
            for (int j = 0; j < stackLimit; j++) {
                if (j < actualSize) {
                    System.out.print("|"); // Espacio ocupado
                } else {
                    System.out.print(" "); // Espacio vacío
                }
            }
            System.out.print("] (" + actualSize + "/" + stackLimit + ")");
            float stackWeight = pilas[i].calculateTotalStackWeight();
            System.out.printf(" - Peso Total: %.2f kg", stackWeight);

            if (actualSize >= stackLimit) {
                System.out.print(" - ¡CRÍTICO: LLENA!");
            }
            System.out.println();
        }

        System.out.println("\n[ESTADO DE LOGÍSTICA]:");
        System.out.println(" >> Rutas activas: " + activeRoutes);
        System.out.println(" >> Próximo destino: " + route.showCurrentStop());
        System.out.println(" >> Total de paradas programadas: " + route.getCont());
        System.out.println("\n--Presione Enter para volver al menú principal--");
        scanner.nextLine();
    }

    /**Es un simulador de la rutas.
    Recibe el contenedor que vamos a simular su ruta, el cual, recibimos su posición, y realizamos toda la simulación, al terminar
    se actualiza el atributo positionC del contenedor, con el valor de position
    */
    private static void simulateRoute(Container simulate) {
        int option;
        route.setPosition(simulate.getPositionC());
        do {
            System.out.println("\n----NAVEGACIÓN DE RUTA ----");
            System.out.println("Parada actual: " + route.showCurrentStop());
            System.out.println("1) Avanzar");
            System.out.println("2) Retroceder");
            System.out.println("3) Terminar simulación");
            System.out.print("Seleccione dirección: ");

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    route.goNext();
                    break;
                case 2:
                    route.goBack();
                    break;
                case 3:
                    System.out.println("Simulación finalizada");
                    break;
                default:
                    System.out.println("Movimiento no válido");
                    break;
            }
        } while (option != 3);

        simulate.setPositionC(route.getPosition());
    }

}
