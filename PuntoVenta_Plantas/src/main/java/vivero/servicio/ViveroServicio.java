package vivero.servicio;

import vivero.modelo.Cliente;
import vivero.modelo.Planta;
import vivero.modelo.Venta;

import java.util.ArrayList;
import java.util.List;

public class ViveroServicio {
    private List<Planta> inventario;
    private List<Venta> registroVentas;

    public ViveroServicio() {
        this.inventario = new ArrayList<>();
        this.registroVentas = new ArrayList<>();
    }

    public void registrarPlanta(Planta planta) {
        this.inventario.add(planta);
    }

    public void registrarVenta(Venta venta) {
        this.registroVentas.add(venta);
    }

    // SMELL: Hide Delegate (PDF diapositivas 43-45)
    // El cliente del servicio debe recorrer toda la cadena interna:
    // venta.getCliente().getDireccion().getCiudad() para obtener una sola información.
    // REFACTORIZACIÓN SUGERIDA: Ofrecer un método delegado en 'Venta' (ej. venta.getCiudadCliente())
    // o en 'Cliente' para que el cliente no conozca la estructura interna de 'DireccionEnvio'.
    public String obtenerCiudadDestino(Venta venta) {
        return venta.getCliente().getDireccion().getCiudad();
    }

    // SMELL: Introduce Null Object (PDF diapositivas 121-123)
    // Se valida repetidamente si 'cliente.getMembresia() == null' para asignar un comportamiento
    // por defecto (0% de descuento).
    // REFACTORIZACIÓN SUGERIDA: Crear la clase 'MembresiaNula' que extienda o implemente 'Membresia'
    // y devuelva 0.0 sin obligar a condicionales de null checking.
    public double calcularDescuentoCliente(Cliente cliente, double total) {
        double porcentaje = 0.0;
        if (cliente.getMembresia() == null) {
            porcentaje = 0.0;
        } else {
            porcentaje = cliente.getMembresia().getPorcentajeDescuento();
        }
        return total * (1.0 - porcentaje);
    }

    // SMELL: Remove Control Flag (PDF diapositivas 115-116)
    // Se utiliza una variable booleana 'encontrado' como bandera para dirigir el flujo
    // del ciclo en lugar de usar directamente un 'break' o un 'return'.
    // REFACTORIZACIÓN SUGERIDA: Reemplazar el uso de la bandera por un retorno temprano 'return planta'
    // o romper el bucle con 'break'.
    public Planta buscarPlantaPorNombre(String nombre) {
        Planta resultado = null;
        boolean encontrado = false;

        for (int i = 0; i < inventario.size(); i++) {
            if (!encontrado) {
                if (inventario.get(i).getNombreComun().equalsIgnoreCase(nombre)) {
                    resultado = inventario.get(i);
                    encontrado = true;
                }
            }
        }
        return resultado;
    }

    // SMELL: Separate Query from Modifier (PDF diapositivas 135-136)
    // Este método devuelve un dato (consulta: 'Planta') y a la vez modifica el estado
    // del sistema (modificación: descuenta stock e imprime un log de auditoría).
    // REFACTORIZACIÓN SUGERIDA: Dividir en dos métodos: uno de consulta pura 'obtenerPlanta(id)'
    // y otro de modificación de estado 'descontarStock(id, cantidad)'.
    public Planta obtenerPlantaYDescontarStock(String id, int cantidad) {
        for (Planta p : inventario) {
            if (p.getId().equals(id)) {
                p.setStock(p.getStock() - cantidad); // Mutación de estado
                System.out.println("[AUDITORÍA] Se redujo el stock de la planta: " + p.getNombreComun());
                return p; // Consulta devuelta
            }
        }
        return null;
    }
}