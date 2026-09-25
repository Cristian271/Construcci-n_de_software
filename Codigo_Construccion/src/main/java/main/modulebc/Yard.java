package main.modulebc;
import data.estructures.stack.*;

public class Yard extends ContainerSimpleList{
    private int count = 0;
    private final int limit;

    //Constructor de la clase que recibe un elemento entero que es el limite y retorna un Yard
    public Yard(int limit) {
        this.limit = limit; // Se asigna al crear la instancia
    }


    /** Sin datos de entrada, se encarga de eliminar cotenedores de la pila, devuelve el que se encuentra en el top de la pila,
     siempre y cuando, sea el que se busca*/
    // substitute algorithm
    @Override
    public Container popContainer(String id) {
        if (start==null) {
            System.out.println("El patio está vacío");
            return null;
        }

        Container auxiliar = null; // pila manual
        Container popped = null;

        while (start != null) {
            Container current = pop();
            if (current.getId().equals(id)) {
                popped = current;
                break;
            }
            current.setNext(auxiliar);
            auxiliar = current;
        }

        // regresar elementos
        while (auxiliar != null) {
            Container next = auxiliar.getNext();
            auxiliar.setNext(null);
            push(auxiliar);
            auxiliar = next;
        }

        return popped;
    }

    /** Recibe una entrada tipo Container, aunque no regresa nada se encarga de apilar un nuevo elemento
     si la lista está vacía lo agrega como primer y último dato, si hay elementos lo agrega como start
     pero siempre asegurando que no se rebase el limite de la pila*/
    @Override
    public void push(Container container) {
        if (count >= limit) {
            System.out.println("Columna de contenedores llena; (máximo: "+ limit +")");
            return;
        }
        if (start == null){
            start = end = container;
        } else {
            container.setNext(start);
            start = container;
        }
        count++;

    }
    /** Regresa un Container, sin recibir, nada se encarga de eliminar un elemento de la pila
     Verifica si esta vacía, si no lo está eliminado el elemento maracado como start y actauliza las etiquetas */
    @Override
    public Container pop() {
        if (start == null) {
            System.out.println("El patio está vacío.");
            return null;
        }
        Container temp = start;
        start = start.getNext();
        if (start == null) {
            end = null;
        }
        count--;

        return temp;
    }
    /**No necesita datos de entrada, solo retorna el Container que está en el tope de la pila*/
    @Override
    public Container top() {
        return start;
    }
    /** Sin recibir nada, solo retorna un entero con el tamaño de la pila*/
    @Override
    public int size() {
        return count;
    }
    /** No recibe datos de entrada pero se encarga de regresar un float con el resultado del proceso de calcular la suma total del peso de los elementos de la pila */
    public float calculateTotalStackWeight() {
        float sum = 0;
        Container current = start; // Recorremos desde el tope
        while (current != null) {
            sum += current.getList().calculateWeight();
            current = current.getNext();
        }
        return sum;
    }
    /** Sin datos de entrada ni salida, solo se encraga de mostrar las funciones del módulo */
    public static void menu() {
        System.out.println("\n----------------------------------------------------");
        System.out.println("---------- MENÚ: PATIO DE CONTENEDORES -------");
        System.out.println("----------------------------------------------------");
        System.out.println("1) Ingresar contenedor");
        System.out.println("2) Retirar para ruta");
        System.out.println("3) Ver contenedor en el tope");
        System.out.println("4) INSPECCIONAR CONTENIDO ");
        System.out.println("5) Volver al menú principal");
    }
    /** Sin datos de entrada ni salida, solo se encraga de mostrar las funciones dentro de una de las funciones del modulo */
    public static void menuInspeccion() {
        System.out.println("\n------GESTIÓN DE PRODUCTOS --------");
        System.out.println("1) Agregar producto al contenedor");
        System.out.println("2) Calcular peso total de la carga");
        System.out.println("3) Buscar producto por nombre");
        System.out.println("4) Regresar al menú del patio");
    }

}
