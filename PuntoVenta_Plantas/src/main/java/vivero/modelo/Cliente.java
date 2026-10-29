package vivero.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cliente {
    private String id;
    private String nombre;
    private DireccionEnvio direccion;
    private Membresia membresia; // Puede ser null


    // RF-09: Encapsulate Collection
    private List<Venta> comprasPrevias;
    private Telefono telefono;

    public Cliente(String id, String nombre, DireccionEnvio direccion, Membresia membresia,
                   String telefonoCodigoPais, String telefonoNumero, String telefonoExtension) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.membresia = (membresia != null) ? membresia : new MembresiaNula();
        this.comprasPrevias = new ArrayList<>();
        this.telefono = new Telefono(telefonoCodigoPais, telefonoNumero, telefonoExtension);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public DireccionEnvio getDireccion() {
        return direccion;
    }

    // RF-07: Hide Delegate
    public String getCiudad() {
        return direccion.getCiudad();
    }

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = (membresia != null) ? membresia : new MembresiaNula();
    }

    public Telefono getTelefono() {
        return telefono;
    }
    public void setTelefono(Telefono telefono) {
        this.telefono = telefono;
    }
    // =========================================================================
    // REFACTORIZACIÓN: Encapsulate Collection
    // =========================================================================
    // El getter ahora retorna una lista inmodificable para proteger los datos internos
    public List<Venta> getComprasPrevias() {
        return Collections.unmodifiableList(this.comprasPrevias);
    }
    // Se ofrece un método explícito para agregar una venta al historial
    public void agregarCompra(Venta venta) {
        this.comprasPrevias.add(venta);
    }
}
