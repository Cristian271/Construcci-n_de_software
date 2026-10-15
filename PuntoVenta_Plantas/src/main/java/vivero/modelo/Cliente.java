package vivero.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String id;
    private String nombre;
    private DireccionEnvio direccion;
    private Membresia membresia; // Puede ser null

    // SMELL: Extract Class (PDF diapositivas 39-41)
    // 'Cliente' está acumulando datos y responsabilidades de números telefónicos
    // que cambian juntos y forman un concepto propio (código país, número, extensión).
    // REFACTORIZACIÓN SUGERIDA: Extraer estos campos a una clase dedicada 'Telefono'.
    private String telefonoCodigoPais;
    private String telefonoNumero;
    private String telefonoExtension;

    // SMELL: Encapsulate Collection (PDF diapositivas 85-88)
    // La colección interna se expone directamente a través del getter sin protección.
    // REFACTORIZACIÓN SUGERIDA: Retornar Collections.unmodifiableList(...) y ofrecer
    // métodos explícitos como 'agregarCompra(Venta v)'.
    private List<Venta> comprasPrevias;

    public Cliente(String id, String nombre, DireccionEnvio direccion, Membresia membresia,
                   String telefonoCodigoPais, String telefonoNumero, String telefonoExtension) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.membresia = membresia;
        this.telefonoCodigoPais = telefonoCodigoPais;
        this.telefonoNumero = telefonoNumero;
        this.telefonoExtension = telefonoExtension;
        this.comprasPrevias = new ArrayList<>();
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

    public Membresia getMembresia() {
        return membresia;
    }

    public void setMembresia(Membresia membresia) {
        this.membresia = membresia;
    }

    // Métodos asociados al smell 'Extract Class'
    public String getTelefonoCompleto() {
        return "+" + telefonoCodigoPais + " (" + telefonoNumero + ") ext. " + telefonoExtension;
    }

    public String getTelefonoCodigoPais() {
        return telefonoCodigoPais;
    }

    public void setTelefonoCodigoPais(String telefonoCodigoPais) {
        this.telefonoCodigoPais = telefonoCodigoPais;
    }

    public String getTelefonoNumero() {
        return telefonoNumero;
    }

    public void setTelefonoNumero(String telefonoNumero) {
        this.telefonoNumero = telefonoNumero;
    }

    public String getTelefonoExtension() {
        return telefonoExtension;
    }

    public void setTelefonoExtension(String telefonoExtension) {
        this.telefonoExtension = telefonoExtension;
    }

    // Método con smell 'Encapsulate Collection'
    public List<Venta> getComprasPrevias() {
        return this.comprasPrevias;
    }
}