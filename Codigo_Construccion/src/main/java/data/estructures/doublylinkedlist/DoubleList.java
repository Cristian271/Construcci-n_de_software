package data.estructures.doublylinkedlist;

public abstract class DoubleList {
    protected StopBus start;
    protected StopBus end;
    protected StopBus position;

    /**Es una clase que maneja la lista doblemente ligada de sus elementos (nodos) las funciones sirven para menjar la información, manteniendo apuntadores*/
    //Recibe un elemento string, sin un retorno específico pero se encarga de insertar un elemento al final de la lista
    public abstract void insertEnd(String nameStop);

    //Recibe 3 parametros string, sin una salida, se encarga de ingresar un elemento entre otros 2 dentro de la lista
    public abstract void insertBetween(String one, String two, String nameStop);

    //Recibe un parametro string, se encarga de eliminar un elemento y devuelve el nombre del elemento que se elimina
    public abstract String deleteStop(String name);

    //Sin parametros de entrada o salida, se encarga de avanzar al siguiente elemento de la lista
    public abstract void goNext();

    //Sin parametros de entrada o salida, se encarga de avanzar al elemento anterior en la lista
    public abstract void goBack();

    //Sin parametros de entrada, se encarga de verificar si la lista carece de elementos y retorna true o false según el caso
    public abstract boolean isEmpty();

    //Sin parametros de entrada o salida, se encarga de mostrar la información de los elementos en el orden en que se encuentran alamacenados en la lista
    // fue muy util para debuggear, pero termino sin uso, lo dejamos como evidencia
    public void imprimir(){
        StopBus node = start;
        while(node != null){
            System.out.println(node.getNameStop());
            node = node.getNext();
        }
    }
    // getter and setter de position, necesario para la simulación de rutas
    public StopBus getPosition() {
        return position;
    }

    public void setPosition(StopBus position) {
        this.position = position;
    }
}
