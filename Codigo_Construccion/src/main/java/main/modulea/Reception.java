package main.modulea;

import data.estructures.queue.*;

import java.util.Scanner;
public class Reception extends QueueDoubleList {
    private int cont;

    public Reception(){
        this.cont = 0;
    }

    // Start -> El primero que sale, End -> El ultimo que sale ; Aclaración

    /** No tiene parametros de entrada ni salida.
     Cuando se agrega un elemento nuevo a la cola si está vacía se establce como el primero y el último, si no
     ingresa el elemento al final de cola, actualizando la etiqueta de end */
    @Override
    public void enqueue() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa el ID del camión: ");
        String nameBus = scanner.nextLine();
        if(isEmpty()){
            start = end = new BusQueue(nameBus); cont++;
        } else{
            BusQueue temp = new BusQueue(nameBus, end, null); cont++;
            end.setNext(temp);
            end = temp;

        }
    }
    /** No tiene paramateros de entrada con salida BusQueue (el elemento a eliminar).
     Cuando se elimina un elemento de la cola vacía notifica el estado de esta,
     si hay un solo elemento, reduce el tamaña de la pila y la actualiza a nula, y si tiene más elementos
     lo que hace es actualizar la etiqueta de inicio y el dato de elemento previo del segundo elemento*/
    @Override
    public BusQueue dequeue() {
        BusQueue deleted = null;
        if(isEmpty() || cont == 1){
            if(isEmpty()) {
                System.out.println("La cola esta vacía");
            } else{
                start = end = null;
                cont--;
            }

        } else{
            deleted = start;
            start.getNext().setPrevious(null);
            start = start.getNext();
            cont--;
        }


        return deleted;
    }
    /** Sin parametros de entrada, regresa un entero con el tamaño de la cola */
    @Override
    public int size() {
        return cont;
    }
    /** Sin parametros de entrada, regresa un BusQueue del elemento que se encuentra al inicio de la cola */
    @Override
    public BusQueue front() {
        return start;
    }
    /** Sin parametros de entrada, retorna true si la cola se encuentra vacía, o al contrario false */
    @Override
    public boolean isEmpty() {
        return cont == 0;
    }
    /** Funciones sin entrada ni salida, se encarga unicamente de mostrar las opciones del modulo */
    public static void menu(){
        System.out.println("----------------------------------------------------");
        System.out.println("---------------- ZONA DE RECEPCIÓN  ----------------");
        System.out.println("----------------------------------------------------");
        System.out.println("1) Registrar llegada de camión");
        System.out.println("2) Dar ingreso a patio");
        System.out.println("3) Ver proximo camión a atender");
        System.out.println("4) Visualizar todos los camiones en espera");
        System.out.println("5) Volver al menú principal");
    }

}
