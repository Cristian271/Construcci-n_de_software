package vivero.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private String idVenta;
    private Cliente cliente;
    private LocalDate fecha;
    private boolean pagada;

    // Constantes simbólicas para RF-10
    private static final double TASA_IVA = 0.16;
    private static final double COSTO_EMBALAJE_ESTANDAR = 25.0;
    private static final double UMBRAL_EMBALAJE_GRATIS = 500.0;

    // RF-05: Move Field
    public double tasaImpuestoAmbiental = 0.05;

    //Atributo RF-11: Replace Array with Object
    private List<String[]> lineasPedido;

    public Venta(String idVenta, Cliente cliente, LocalDate fecha) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fecha = fecha;
        this.lineasPedido = new ArrayList<>();
        this.pagada = false;
    }
    //Método RF-11: Replace Array with Object
    public void agregarLinea(String nombrePlanta, int cantidad, double precioUnitario) {
        String[] item = new String[3];
        item[0] = nombrePlanta;
        item[1] = String.valueOf(cantidad);
        item[2] = String.valueOf(cantidad * precioUnitario);
        this.lineasPedido.add(item);
    }

    public List<String[]> getLineasPedido() {
        return lineasPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean isPagada() {
        return pagada;
    }

    // RF-01: Replace Temp with query
    public double calcularDescuentoTotal(int cantidad, double precioUnitario) {
        double factorDescuento;

        if (calcularPrecioBase(cantidad, precioUnitario) > 1000.0) {
            factorDescuento = 0.90;
        } else {
            factorDescuento = 0.95;
        }

        return calcularPrecioBase(cantidad, precioUnitario) * factorDescuento;
    }
    public double calcularPrecioBase(int cantidad, double precioUnitario){
        return cantidad * precioUnitario;
    }

    // RF-12 Extraer las sentencias duplicadas de historial y notificación fuera de la estructura if-else.
    public double aplicarDescuentoMayoreo(double subtotal, int totalPlantas) {
        double totalConDescuento;

        if (totalPlantas >= 10) {
            totalConDescuento = subtotal * 0.85; // 15% de descuento
        } else {
            totalConDescuento = subtotal * 0.95; // 5% de descuento
        }

        // Fragmentos que se ejecutaban en ambas ramas
        System.out.println("Descuento aplicado (Detalles)");
        System.out.println("Descuento aplicado por volumen: " + totalPlantas + " unidades.");
        System.out.println("Descuento procesado para venta ID: " + this.idVenta);

        return totalConDescuento;
    }

    // RF-02: Split Temporary Variable
    public double calcularEnvio(double alto, double ancho, double profundidad, double distanciaKm) {
        double volumen = alto * ancho * profundidad; // Responsabilidad 1: volumen
        System.out.println("Volumen calculado de la carga: " + volumen);

        double costo = (distanciaKm * 3.5) + (volumen * 0.02); // Responsabilidad 2: costo de envío
        System.out.println("Costo total de flete: " + costo);
        return costo;
    }

    // RF-03: Remove Assignments to Parameters
    public double aplicarCupon(double montoTotal, String cupon) {
        if (cupon != null && cupon.equals("PRIMAVERA20")) {
            montoTotal = montoTotal - 20.0;
        }
        if (montoTotal < 0.0) {
            montoTotal = 0.0;
        }
        return montoTotal;
    }

    // RF-04: Move Method
    public double calcularCuidadoRiego(Planta planta, int diasSoleados) {
        double aguaDiaria = 0.25;
        if (planta.getTipoAmbiente().equals("EXTERIOR")) {
            aguaDiaria += 0.50;
        }
        return aguaDiaria * diasSoleados;
    }

    // RF-13: Decompose Conditional
    public double calcularRecargoClima(double costoBase, LocalDate fechaPedido) {
        if (esTemporadaInvernal(fechaPedido, costoBase)) {
            return calcularTarifaInvierno(costoBase);
        }
        return calcularTarifaNormal(costoBase);
    }
    private boolean esTemporadaInvernal(LocalDate fecha, double costoBase) {
        boolean esMesInvernal = (fecha.getMonthValue() == 12 || fecha.getMonthValue() <= 2);
        boolean superaUmbralMinimo = costoBase > 100.0;
        return esMesInvernal && superaUmbralMinimo;
    }

    private double calcularTarifaInvierno(double costoBase) {
        double recargoPorHeladas = costoBase * 0.15;
        double empaqueProtector = 35.0;
        return costoBase + recargoPorHeladas + empaqueProtector;
    }

    private double calcularTarifaNormal(double costoBase) {
        return costoBase * 1.02;
    }

    //RF-15 Reemplaza el anidamiento profundo por clausulas que retornan inmediatamente
    public boolean procesarPago(double montoEntregado, double montoRequerido) {
        //Cliente debe existir
        if (cliente == null) {
            return false;
        }
        // Cliente debe tener direccion
        if (cliente.getDireccion() == null) {
            return false;
        }
        // No procesar ventas ya pagadas
        if (pagada) {
            return false;
        }
        // Dinero dado suficiente
        if (montoEntregado < montoRequerido) {
            return false;
        }
        //  Si pasa las validaciones con exito
        pagada = true;
        return true;
    }

    //RF-10 cambiar los numeros por constantes simbolicas con nombre
    public double calcularTotal(double subtotal) {
        double costoEmbalaje;
        if (subtotal<UMBRAL_EMBALAJE_GRATIS){
            costoEmbalaje = COSTO_EMBALAJE_ESTANDAR;
        } else {
            costoEmbalaje = 0.0;
        }
        double impuestoIva = subtotal * TASA_IVA;
        return subtotal + impuestoIva + costoEmbalaje;
    }
    //RF-18 Rename Method, de recargar (ambiguo, poco descriptivo) a un nombre que se exprese mejor
    public double aplicarRecargoEnvioExpress(double base) {
        return base + 50.0;
    }
}