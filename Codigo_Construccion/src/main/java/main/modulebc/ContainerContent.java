package main.modulebc;

import data.estructures.singlylinkedlist.Product;
import data.estructures.singlylinkedlist.ProductSimpleList;

import java.util.Scanner;

public class ContainerContent extends ProductSimpleList {

    /** Sin parametros de entrada ni salida.
     Cada que se registra un nuevo producto, se solicita la información completa.
     Si la lista está vacía, el nuevo producto se establece como inicio y final.
     En caso contrario, se inserta un nuevo nodo al inicio de la lista, apuntando al nodo que antes era el primero.*/
    @Override
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Registro de Producto ---");
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine();
        System.out.print("Peso (en kg): ");
        //Extract method para que imprima a parte
        float weight = scanner.nextFloat();
        scanner.nextLine();

        if (start == null) {
            start = end = new Product(name, weight);
        } else {
            start = new Product(name, weight, start);
        }

    }
    /** Retorna el calculo del peso total de los productos, regresando el resultado en un float*/
    @Override
    public float calculateWeight() {
        float total=0;
        Product current = start;
        while (current != null){
            total += current.getWeight();
            current = current.getNext();

        }
        return total;
    }
    /** Recibe un string con el identificador de un producto y regresa en un booleano si lo encontró o no en la lista
     Funciona recorriendo toda la lista en orden comparando los identificadores de los producto, hasta regresar a l inicio o hallar el producto*/
    public boolean findProduct(String name) {
        if (start == null) {
            return false;
        }

        Product current = start;
        while (current != null) {
            if (current.getNameProduct().equalsIgnoreCase(name)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

}
