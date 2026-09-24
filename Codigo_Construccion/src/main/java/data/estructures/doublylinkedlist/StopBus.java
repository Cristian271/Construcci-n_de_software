package data.estructures.doublylinkedlist;

public class StopBus {
    protected StopBus next;
    protected StopBus previous;
    protected String nameStop;

    /** Clase que establece el tipo de dato que se va a uitlizar (nodos) tipo StopBus*/
    //Constructor que únicamente recibe un string (para el primer elemento), retornando un dato tipo StopBus
    public StopBus(String nameStop){
        this.nameStop = nameStop;
        next = previous = null;
    }
    //Constructor que recibe un string y 2 datos StopBus, generando un StopBus
    public StopBus(String nameStop, StopBus previous, StopBus next){
        this.next = next;
        this.previous = previous;
        this.nameStop = nameStop;
    }
    /**Funcion (getter, control se acceso a atributos) sin parametros de entrada que retorna un elemento StopBus, se encarga de obtener el siguiente elemento en la lista*/
    public StopBus getNext(){
        return next;
    }
    /**Funcion (getter, control se acceso a atributos) sin parametros de entrada que retorna un elemento StopBus, se encarga de obtener el elemento anterior en la lista*/
    public StopBus getPrevious(){
        return previous;
    }
    /**Funcion (getter, control se acceso a atributos) sin parametros de entrada y retorna un string perteneciente al nombre de un elemento*/
    public String getNameStop(){
        return nameStop;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo StopBus (setter, control se acceso a atributos) que establece a quien se reconoce como siguiente elemento*/
    public void setNext(StopBus next){
        this.next = next;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo StopBus (setter, control se acceso a atributos) que establece a quien se reconoce como elemento anterior*/
    public void setPrevious(StopBus previous){
        this.previous = previous;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo string (setter, control se acceso a atributos) que establece el nombre identificador del elemento*/
    public void setNameStop(String nameStop){
        this.nameStop = nameStop;
    }


}
