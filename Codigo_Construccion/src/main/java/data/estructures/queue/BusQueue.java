package data.estructures.queue;

public class BusQueue {
    protected BusQueue next;
    protected String nameBus;
    protected BusQueue previous;

    /** Clase que establece el tipo de dato que se va a uitlizar (nodos) tipo  BusQueue*/
    //Contructor que únicamente recibe un string (para el primer elemento), retornando un dato tipo  BusQueue
    public BusQueue(String nameBus){
        this.nameBus = nameBus;
        next = null;
        previous = null;
    }
    //Constructor que recibe un string y 2 datos BusQueue, generando un BusQueue
    public BusQueue (String nameBus, BusQueue previous, BusQueue next){
        this.nameBus = nameBus;
        this.previous = previous;
        this.next = next;
    }

    /*Setters y Getters que controlan el acceso a la información*/
    /**Sin parametros de entrada que retorna un elemento BusQueue, se encarga de obtener el siguiente elemento en la lista*/
    public BusQueue getNext(){
        return next;
    }
    /**Sin parametros de entrada y retorna un string perteneciente al nombre del elemento*/
    public String getNameBus(){
        return nameBus;
    }
    /**Sin parametros de entrada que retorna un elemento BusQueue, se encarga de obtener el elemento anterior en la lista*/
    public BusQueue getPrevious(){
        return previous;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo BusQueue que establece a quien se reconoce como siguiente elemento*/
    public void setNext(BusQueue next){
        this.next = next;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo string que establece el nombre identificador del elemento*/
    public void setNameBus(String nameBus){
        this.nameBus = nameBus;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo BusQueue que establece a quien se reconoce como elemento anteior*/
    public void setPrevious(BusQueue previous){
        this.previous = previous;
    }


}
