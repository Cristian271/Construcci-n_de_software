package data.estructures.stack;

public interface Stack {
    /**Interfaz que define los comportamientos que las clases deben implementar cuando se quiera usar una pila
     Tiene comportamiento tipo LIFO, (se utiliza en el módulo de patio de contenedores)*/
    //Apila un contenedor más
    public void push(Container container);
    //Desapila un contenedor
    public Container pop();
    //Revisa el elemento en la cima de la pila
    public Container top();
    //Retorna elo tamaño de la pila
    public int size();
    //Verifica si la pila tiene elementos
    public boolean isEmpty();
}
