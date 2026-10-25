package vivero.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {

    private Cliente clienteValido;
    private DireccionEnvio direccionValida;

    @BeforeEach
    void setUp() {
        direccionValida = new DireccionEnvio("Calle 60", "305", "Mérida", "97000");
        clienteValido = new Cliente(
                "C-01",
                "Alessandra Rivera",
                direccionValida,
                null,
                "52",
                "9991234567",
                "10"
        );
    }

    @Test
    @DisplayName("Guarda 1: Debe rechazar el pago si el cliente es nulo")
    void procesarPago_ClienteNulo_RetornaFalse() {
        // Arrange: venta sin cliente asignado
        Venta venta = new Venta("V-001", null, LocalDate.now());

        // Act
        boolean resultado = venta.procesarPago(500.0, 300.0);

        // Assert
        assertFalse(resultado, "El pago debe fallar cuando la venta no tiene cliente.");
        assertFalse(venta.isPagada(), "La venta no debe marcarse como pagada.");
    }

    @Test
    @DisplayName("Guarda 2: Debe rechazar el pago si la dirección del cliente es nula")
    void procesarPago_DireccionClienteNula_RetornaFalse() {
        // Arrange: cliente sin dirección de envío
        Cliente clienteSinDireccion = new Cliente(
                "C-02",
                "Carlos Pool",
                null,
                null,
                "52",
                "9997654321",
                "1"
        );
        Venta venta = new Venta("V-002", clienteSinDireccion, LocalDate.now());

        // Act
        boolean resultado = venta.procesarPago(500.0, 300.0);

        // Assert
        assertFalse(resultado, "El pago debe fallar si el cliente carece de dirección registrada.");
        assertFalse(venta.isPagada());
    }

    @Test
    @DisplayName("Guarda 3: Debe rechazar el pago si la venta ya fue pagada con anterioridad")
    void procesarPago_VentaYaPagada_RetornaFalse() {
        // Arrange: procesar un primer pago exitoso
        Venta venta = new Venta("V-003", clienteValido, LocalDate.now());
        boolean primerPago = venta.procesarPago(350.0, 300.0);
        assertTrue(primerPago, "El primer cobro debió ser exitoso.");

        // Act: intentar pagar una venta que ya tiene pagada = true
        boolean segundoPago = venta.procesarPago(300.0, 300.0);

        // Assert
        assertFalse(segundoPago, "No se puede volver a cobrar una venta que ya fue pagada.");
        assertTrue(venta.isPagada());
    }

    @Test
    @DisplayName("Guarda 4: Debe rechazar el pago si el monto entregado es menor al requerido")
    void procesarPago_MontoInsuficiente_RetornaFalse() {
        // Arrange
        Venta venta = new Venta("V-004", clienteValido, LocalDate.now());

        // Act: entrega $250 cuando se requieren $300
        boolean resultado = venta.procesarPago(250.0, 300.0);

        // Assert
        assertFalse(resultado, "El pago debe ser rechazado si el dinero entregado no cubre el total.");
        assertFalse(venta.isPagada(), "La venta debe permanecer sin pagar.");
    }

    @Test
    @DisplayName("Camino Principal: Debe aceptar el pago con monto exacto y marcar la venta como pagada")
    void procesarPago_MontoExacto_RetornaTrueYMarcaPagada() {
        // Arrange
        Venta venta = new Venta("V-005", clienteValido, LocalDate.now());

        // Act: pago con el importe exacto
        boolean resultado = venta.procesarPago(300.0, 300.0);

        // Assert
        assertTrue(resultado, "El pago con monto exacto debe ser aceptado.");
        assertTrue(venta.isPagada(), "La venta debe quedar en estado pagada = true.");
    }

    @Test
    @DisplayName("Camino Principal: Debe aceptar el pago con excedente de dinero")
    void procesarPago_MontoMayor_RetornaTrue() {
        // Arrange
        Venta venta = new Venta("V-006", clienteValido, LocalDate.now());

        // Act: entrega $500 cuando se requieren $300
        boolean resultado = venta.procesarPago(500.0, 300.0);

        // Assert
        assertTrue(resultado, "El pago con dinero de más debe ser aprobado.");
        assertTrue(venta.isPagada());
    }
    /*-----------------------------------------CALCULAR ENVIO-------------------------------------------------*/

    @Test
    public void calcularEnvio_ValoresEstandar_CalculaCostoCorrectamente() {
        // 1. Preparación
        Venta venta = new Venta("V-01", null, LocalDate.now());

        // 2. Ejecución
        // Volumen = 10.0 * 10.0 * 10.0 = 1000.0
        // Distancia = 5.0
        // Fórmula: (5.0 * 3.5) + (1000.0 * 0.02) = 17.5 + 20.0 = 37.5
        double costoEnvio = venta.calcularEnvio(10.0, 10.0, 10.0, 5.0);

        // 3. Verificación
        assertEquals(37.5, costoEnvio, 0.001, "El costo de envío con valores estándar no es el esperado");
    }

    @Test
    public void calcularEnvio_DistanciaCero_CalculaSoloCostoPorVolumen() {
        // 1. Preparación
        Venta venta = new Venta("V-02", null, LocalDate.now());

        // 2. Ejecución
        // Volumen = 20.0 * 5.0 * 10.0 = 1000.0
        // Distancia = 0.0 (ej. recogida local o el mismo punto)
        // Fórmula: (0.0 * 3.5) + (1000.0 * 0.02) = 0.0 + 20.0 = 20.0
        double costoEnvio = venta.calcularEnvio(20.0, 5.0, 10.0, 0.0);

        // 3. Verificación
        assertEquals(20.0, costoEnvio, 0.001, "Si la distancia es 0, el costo debe basarse únicamente en la tarifa por volumen");
    }

    @Test
    public void calcularEnvio_VolumenPequenoLargaDistancia_CalculaCostoCorrectamente() {
        // 1. Preparación
        Venta venta = new Venta("V-03", null, LocalDate.now());

        // 2. Ejecución
        // Volumen = 1.0 * 1.0 * 1.0 = 1.0
        // Distancia = 100.0
        // Fórmula: (100.0 * 3.5) + (1.0 * 0.02) = 350.0 + 0.02 = 350.02
        double costoEnvio = venta.calcularEnvio(1.0, 1.0, 1.0, 100.0);

        // 3. Verificación
        assertEquals(350.02, costoEnvio, 0.001, "El costo para un paquete pequeño a larga distancia no se calculó correctamente");
    }
}