package data.estructures.stack;

public abstract class ContainerSimpleList implements Stack {
    protected Container start;
    protected Container end;
    //Con etrada tipo string, con una salida tipo Container, se encarga de eliminar un contenedor de la lista que no necesariamente esta en la cima de la pila
    public abstract Container popContainer(String id);

}
