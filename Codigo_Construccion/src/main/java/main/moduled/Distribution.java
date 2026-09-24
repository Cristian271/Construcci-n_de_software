package main.moduled;
import data.estructures.doublylinkedlist.*;

public class Distribution extends DoubleList {
    protected int cont;

    //Constructor de la clase que no recibe argumentos
    public Distribution(){
        cont = 0;
        this.position = null;
    }

    /** Sin un retorno determinado, recibe un string identificador de un elemento
     Si la pila está vacía inserta el elemento como inicio y final, si no lo inserta al final actualizando la etiqueta end*/
    @Override
    public void insertEnd(String nameStop) {
        if(isEmpty()){
            start = end = new StopBus(nameStop);
            position = start;
            cont++;
        } else{
            StopBus temp = new StopBus(nameStop, end, null);
            end.setNext(temp);
            end = temp;
            cont++;
        }
    }
    /** Sin un retorno determinado, recibe un string identificador de un nuevo elemento y de 2 que se encuentra en la lista.
     Si la pila está vacía hace la notificación, si no recorre la lista tratando de encontran los 2 elemento que son parametros
     si enceuntra ambos entonces lo inserta en el medio, ajustando los datos que indican los elementos siguiente y anterior dentro de los propios elementos
     en caso contrario notifica que no se pudo realizar la acción*/
    @Override
    public void insertBetween(String one, String two, String nameStop) {
        if(isEmpty() || cont <= 1){
            System.out.println("No hay suficientes paradas");
        } else{
            StopBus current = start;
            int who = -1; // 1 -> Encontró al nodo uno primero; 2 -> encontró al nodo dos primero
            while(current!=null){
                if(current.getNameStop().equals(one)){
                    who = 1;
                    break;
                } else if(current.getNameStop().equals(two)){
                    who = 2;
                    break;
                }
                current = current.getNext();
            }

            switch (who){
                case -1:
                    System.out.println("No se pudo añadir la parada, error en alguno de los datos");
                    break;
                case 1:
                    if(current.getNext()!=null && current.getNext().getNameStop().equals(two)){
                        StopBus temp = new StopBus(nameStop, current, current.getNext());
                        current.getNext().setPrevious(temp);
                        current.setNext(temp);
                        cont++;
                    } else if(current.getPrevious() != null && current.getPrevious().getNameStop().equals(two)){
                        StopBus temp = new StopBus(nameStop, current.getPrevious(), current);
                        current.getPrevious().setNext(temp);
                        current.setPrevious(temp);
                        cont++;
                    } else{
                        System.out.println("No se pudo añadir, error en el nombre de la parada 2");
                    }
                    break;
                case 2:
                    if(current.getNext() != null && current.getNext().getNameStop().equals(one)){
                        StopBus temp = new StopBus(nameStop, current, current.getNext());
                        current.getNext().setPrevious(temp);
                        current.setNext(temp);
                        cont++;
                    } else if(current.getPrevious() != null && current.getPrevious().getNameStop().equals(one)){
                        StopBus temp = new StopBus(nameStop, current.getPrevious(), current);
                        current.getPrevious().setNext(temp);
                        current.setPrevious(temp);
                        cont++;
                    } else{
                        System.out.println("No se pudo añadir, error en el nombre de la parada 1");
                    }

            }

        }
    }
    /** Recibiendo el identificador de una parada intenta buscarla dentro de la lista, si la encuentra lo que hace
     es eliminarla y retornar un string con el identificador de la parada que fue eliminada*/
    @Override
    public String deleteStop(String name) {
        String deleted = null;
        if(isEmpty()){
            System.out.println("No hay paradas registradas");
        } else {
            StopBus current = start;
            while (current != null && !current.getNameStop().equals(name)) {
                current = current.getNext();
            }

            if (current == null) return null; // no hay

            //Es el único nodo
            if (current == start && current == end) {
                start = end = position = null;
            }
            //Es el inicio
            else if (current == start) {
                start = start.getNext();
                start.setPrevious(null);
                if (position == current) position = start;
            }
            //Es el final
            else if (current == end) {
                end = end.getPrevious();
                end.setNext(null);
                if (position == current) position = end;
            }
            // Está en medio
            else {
                current.getPrevious().setNext(current.getNext());
                current.getNext().setPrevious(current.getPrevious());
                if (position == current) position = current.getNext();
            }
            cont--;
            return current.getNameStop();
        }

        return deleted;
    }
    /** Sin datos de entrada, solo retorna un string con el identificador de la parada en la que se encuentra situado, si la hay*/
    public String showCurrentStop() {
        if (position == null) {
            return "Sin paradas configuradas";
        }
        String nombreParada = position.getNameStop();
        return nombreParada;
    }


    /** Sin recibir ni retornar ningún dato, mueve la etiqueta de la pocisión actual a la que sigue,
     con ayuda de la información del elemento en que se encuentra actualmente (mientras sea posible)*/
    @Override
    public void goNext() {

        if (position != null && position.getNext() != null){
            position = position.getNext();
        } else {
            System.out.println("ULtima parada, ya no se puede recorrer hacia el siguiente");
        }
    }
    /** Sin recibir ni retornar ningún dato, mueve la etiqueta de la pocisión actual a la anterior,
     con ayuda de la información del elemento en que se encuentra actualmente (mientras sea posible)*/
    @Override
    public void goBack() {

        if (position != null && position.getPrevious() != null){
            position = position.getPrevious();
        } else {
            System.out.println("Primera parada, ya no se puede recorrer para atrás");
        }
    }

    //necesario para saber cual es el inicio de rutas para todos los contenedores mandados a ruta
    public StopBus firstStopBus(){
        return start;
    }


    /** Sin recibir datos, retorna un boolean que indica si la fila se encuentra vacía */
    @Override
    public boolean isEmpty() {
        return start == null;
    }
    //Inline Method
    /** No recibe datos, solo se encarga de retornar un enetero con el número de paradas que se encuentran registradas en la lista*/
    public int getCont() {
        return cont;
    }

    /** Función sin parametros de entrada ni salida que se encarga de mostrar las opciones dentro del módulo*/
    public static void menu(){
        System.out.println("----------------------------------------------------");
        System.out.println("---------- MENU DE RUTAS DE DISTRIBUCIÓN  ----------");
        System.out.println("----------------------------------------------------");
        System.out.println("1) Agregar nueva parada final");
        System.out.println("2) Insertar parada entre destinos");
        System.out.println("3) Eliminar parada");
        System.out.println("4) SIMULAR RECORRIDO");
        System.out.println("5) Volver al menú principal");
    }



}
