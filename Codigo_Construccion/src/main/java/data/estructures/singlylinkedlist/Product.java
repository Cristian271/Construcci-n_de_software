package data.estructures.singlylinkedlist;

public class Product {
    protected Product next;
    protected String nameProduct;
    protected float weight;

    /** Clase que establece el tipo de dato que se va a uitlizar (nodos) tipo Product*/
    //Constructor que recibe un string y un float (para el primer elemento), retornando un dato tipo Product
    public Product(String nameProduct, float weight){
        this.nameProduct = nameProduct;
        this.weight = weight;
        next = null;
    }
    //Constructor que recibe un string, un float y un Product para generar otro dato Product
    public Product(String nameProduct, float weight, Product next){
        this(nameProduct, weight);
        this.next = next;
    }
    /*Setters y Getters que controlan el acceso a la información*/
    /**Sin parametros de entrada que retorna un elemento Product, se encarga de obtener el siguiente elemento en la lista*/
    public Product getNext() {
        return next;
    }
    /**Sin parametros de salida que recibe un dato tipo Product, establece a quien se reconoce como siguiente elemento*/
    public void setNext(Product next) {
        this.next = next;
    }
    /**Sin parametros de entrada y retorna un string perteneciente al nombre del elemento*/
    public String getNameProduct() {
        return nameProduct;
    }
    /**Funcion sin parametros de salida que recibe un dato tipo string que establece el nombre identificador del elemento*/
    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }
    /**Sin parametros de entrada, obtine un float con onformación de peso*/
    public float getWeight() {
        return weight;
    }
    /**Sin retorno específico, recibe un float estableciendolo como el peso del elemento*/
    public void setWeight(float weight) {
        this.weight = weight;
    }
}
