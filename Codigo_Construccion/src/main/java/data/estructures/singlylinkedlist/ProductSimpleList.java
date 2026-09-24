package data.estructures.singlylinkedlist;

public abstract class ProductSimpleList{
    protected Product start;
    protected Product end;

    //Sin entrada ni salida especifica, se encaraga de agregar un producto
    public abstract void addProduct();
    //Sin entrada, con una salida tipo float, se encarga de clacular el peso total de cada objeto
    public abstract float calculateWeight();

}
