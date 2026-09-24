package data.estructures.queue;

public interface Queue {
    /**Interfaz que define los comportamientos que las clases deben implementar cuando se quiera usar una queue
     Tiene comportamiento tipo FIFO, (se utiliza en el módulo de recepción)*/
    //Debe añadir un elemento al final de lalista
    public void enqueue();
    //Elimina el primer elemento de la lista
    public BusQueue dequeue();
    //Devuelve el tamaño de la lista
    public int size();
    //Devuelve información del primer elemento de la lista
    public BusQueue front();
    //Verifica si la lista tiene elementos o está vacía
    public boolean isEmpty();
}
