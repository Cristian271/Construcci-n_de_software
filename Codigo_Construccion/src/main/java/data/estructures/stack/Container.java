package data.estructures.stack;

import data.estructures.doublylinkedlist.StopBus;
import main.modulebc.ContainerContent;

public class Container {
    protected Container next;
    protected String id;
    protected ContainerContent list;
    protected StopBus positionC;

    /** Clase que establece el tipo de dato que se va a uitlizar (nodos) tipo  Container*/
    //Contructor que únicamente recibe un string (para el primer elemento), retornando un dato tipo Container
    public Container(String id) {
        this.id = id;
        this.next = null;
        this.list = new ContainerContent();
    }

    /* Setters y Getters que controlan el acceso a la información */
    /**Sin parametros de entrada que retorna un elemento Container, se encarga de obtener el siguiente elemento en la lista*/
    public Container getNext() {
        return next;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo Container que establece a quien se reconoce como siguiente elemento*/
    public void setNext(Container next) {
        this.next = next;
    }
    /** Sin paramatros de entrada, retorna el string con el identificador del elemento*/
    public String getId() {
        return id;
    }
    /**Recibe un string, sin retornar nada, solo establece el nombre del elemento*/
    public void setId(String id) {
        this.id = id;
    }
    /**Sin elementos de entrada, genero un dato tipo ContainerContent*/
    public ContainerContent getList() {
        return list;
    }
    /** Recibe un ConteinerContent, sin regresar nada, lo establece como una lista*/
    public void setList(ContainerContent list) {
        this.list = list;
    }
    /** getters and setters de positicionC, para recibir y cambiar el valor del atributo*/
    public StopBus getPositionC() {
        return positionC;
    }

    public void setPositionC(StopBus positionC) {
        this.positionC = positionC;
    }
}
