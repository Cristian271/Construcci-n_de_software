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

    // SMELL: Move Field (PDF diapositivas 37-38)
    // La tasa de impuesto ambiental depende exclusivamente de la clasificación ecológica
    // de la planta, no del registro de la venta global.
    // REFACTORIZACIÓN SUGERIDA: Mover este campo a 'Planta' o a una clase de catálogo de impuestos.
    public double tasaImpuestoAmbiental = 0.05;

    private List<String[]> lineasPedido;

    public Venta(String idVenta, Cliente cliente, LocalDate fecha) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.fecha = fecha;
        this.lineasPedido = new ArrayList<>();
        this.pagada = false;
    }

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

    // Replace Temp with query
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

    // SMELL: Split Temporary Variable (PDF diapositivas 20-21)
    // La variable 'temp' almacena primero el volumen del contenedor y luego se reasigna
    // para calcular la tarifa de flete, cumpliendo dos propósitos distintos.
    // REFACTORIZACIÓN SUGERIDA: Crear dos variables finales separadas (ej. 'volumen' y 'costoFlete').
    public double calcularEnvio(double alto, double ancho, double profundidad, double distanciaKm) {
        double temp = alto * ancho * profundidad; // Responsabilidad 1: volumen
        System.out.println("Volumen calculado de la carga: " + temp);

        temp = (distanciaKm * 3.5) + (temp * 0.02); // Responsabilidad 2: costo de envío
        System.out.println("Costo total de flete: " + temp);
        return temp;
    }

    // SMELL: Remove Assignments to Parameters (PDF diapositivas 22-23)
    // El parámetro recibido 'montoTotal' es modificado y reasignado directamente en el cuerpo del método.
    // REFACTORIZACIÓN SUGERIDA: Usar una variable local 'resultado' en lugar de reasignar el parámetro.
    public double aplicarCupon(double montoTotal, String cupon) {
        if (cupon != null && cupon.equals("PRIMAVERA20")) {
            montoTotal = montoTotal - 20.0;
        }
        if (montoTotal < 0.0) {
            montoTotal = 0.0;
        }
        return montoTotal;
    }

    // SMELL: Move Method (PDF diapositivas 34-36)
    // Este método utiliza exclusivamente atributos y reglas biológicas de la 'Planta'
    // sin hacer uso del estado propio de 'Venta'.
    // REFACTORIZACIÓN SUGERIDA: Mover este método a la clase 'Planta'.
    public double calcularCuidadoRiego(Planta planta, int diasSoleados) {
        double aguaDiaria = 0.25;
        if (planta.getTipoAmbiente().equals("EXTERIOR")) {
            aguaDiaria += 0.50;
        }
        return aguaDiaria * diasSoleados;
    }

    // SMELL: Decompose Conditional (PDF diapositivas 109-110)
    // El if contiene una condición compleja difícil de leer y las ramas ejecutan fórmulas extensas.
    // REFACTORIZACIÓN SUGERIDA: Extraer la condición a un método booleano (ej. 'esTemporadaInvernal()')
    // y cada cálculo a su respectivo método ('tarifaInvierno()', 'tarifaVerano()').
    public double calcularRecargoClima(double costoBase, LocalDate fechaPedido) {
        double costoFinal;
        if ((fechaPedido.getMonthValue() == 12 || fechaPedido.getMonthValue() <= 2) && costoBase > 100.0) {
            costoFinal = (costoBase * 1.15) + 35.0; // Recargo de protección contra heladas
        } else {
            costoFinal = costoBase * 1.02; // Tarifa normal de temporada
        }
        return costoFinal;
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