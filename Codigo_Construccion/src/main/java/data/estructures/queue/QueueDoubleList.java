package data.estructures.queue;

public abstract class QueueDoubleList implements Queue{
    protected BusQueue start;
    protected BusQueue end;
    /**No tiene datos de entrada ni salida, sin ambargo, se encaraga del recorrido de la lista de camiones mostrando el nombre identificador de cada uno*/
    public void print(){
        BusQueue current = start;
        if(current == null){
            System.out.println("La cola esta vacía");
        } else {
            while (current != null) {
                System.out.println("Nombre del camión: " + current.getNameBus());
                current = current.getNext();
            }
        }
    }

}
