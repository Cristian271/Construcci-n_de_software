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

    // RF-07: Hide Delegate
     public String obtenerCiudadDestino(Venta venta) {
        return venta.getCliente().getDireccion().getCiudad();
    }

    // RF-16: Introduce Null Object
    public double calcularDescuentoCliente(Cliente cliente, double total) {
        double porcentaje = 0.0;
        if (cliente.getMembresia() == null) {
            porcentaje = 0.0;
        } else {
            porcentaje = cliente.getMembresia().getPorcentajeDescuento();
        }
        return total * (1.0 - porcentaje);
    }

    // RF-14: Remove Control Flag
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

    // RF-19: Separate Query from Modifier
    public Planta obtenerPlantaPorId(String idPlanta) {
        for (Planta p : inventario) {
            if (p.getId().equals(idPlanta)) {
                return p;
            }
        }
        return null;
    }
    public void descontarStock(String idPlanta, int cantidad) {
        Planta planta = obtenerPlantaPorId(idPlanta);
        if (planta != null) {
            int nuevoStock = planta.getStock() - cantidad;
            planta.setStock(nuevoStock);
        }
    }
}