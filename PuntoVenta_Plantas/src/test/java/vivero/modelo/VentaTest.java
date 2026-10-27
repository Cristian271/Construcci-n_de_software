package vivero.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class VentaTest {
    /*----------------CALCULO DESCUENTO / R-01 REPLACE TEMP WITH QUERY (calcularDescuentoTotal) ----------------------------------*/

    @Test
    public void calcularDescuentoTotal_PrecioBaseMayorAMil_AplicaDiezPorCientoDescuento() {
        // 1. Preparación
        Venta venta = new Venta("V-16", null, LocalDate.now());
        // Cantidad = 3, Precio = $400.0 -> Precio Base = $1200.0 (Mayor a $1000.0)
        // Descuento aplicable: 10% -> Total = 1200.0 * 0.90 = $1080.0

        // 2. Ejecución
        double resultado = venta.calcularDescuentoTotal(3, 400.0);
        // double resultado = venta.calcularDescuentoTotal(3, 400.0); // DESPUÉS DE REFACTORIZAR (usando método basePrice())

        // 3. Verificación
        assertEquals(1080.0, resultado, 0.001, "Para precios bases superiores a $1000.0 debe aplicar un descuento del 10%");
    }

    @Test
    public void calcularDescuentoTotal_PrecioBaseMenorOIgualAMil_AplicaCincoPorCientoDescuento() {
        // 1. Preparación
        Venta venta = new Venta("V-17", null, LocalDate.now());
        // Cantidad = 2, Precio = $250.0 -> Precio Base = $500.0 (Menor a $1000.0)
        // Descuento aplicable: 5% -> Total = 500.0 * 0.95 = $475.0

        // 2. Ejecución
        double resultado = venta.calcularDescuentoTotal(2, 250.0);

        // 3. Verificación
        assertEquals(475.0, resultado, 0.001, "Para precios bases menores o iguales a $1000.0 debe aplicar un descuento del 5%");
    }

    @Test
    public void calcularDescuentoTotal_PrecioBaseLimiteExactoMil_AplicaCincoPorCientoDescuento() {
        // 1. Preparación
        Venta venta = new Venta("V-18", null, LocalDate.now());
        // Cantidad = 2, Precio = $500.0 -> Precio Base = $1000.0 (Umbral límite exacto)
        // Descuento aplicable: 5% -> Total = 1000.0 * 0.95 = $950.0

        // 2. Ejecución
        double resultado = venta.calcularDescuentoTotal(2, 500.0);

        // 3. Verificación
        assertEquals(950.0, resultado, 0.001, "Un precio base exacto de $1000.0 aún pertenece al rango del 5% de descuento");
    }
    
    /*----------------CALCULO IMPUESTO / R-05 MOVE FIELD: atributo tasaImpuestoAmbiental ----------------------------------*/

    @Test
    public void tasaImpuesto_ValorPorDefecto_RetornaCincoPorCiento() {
        // 1. Preparación
        Venta venta = new Venta("V-01", null, LocalDate.now());
        // Planta planta = new PlantaInterior("P-01", "Monstera", 200.0, 10, "Monstera deliciosa", 70); // DESPUÉS DE REFACTORIZAR

        // 2. Ejecución
        double tasa = venta.tasaImpuestoAmbiental;
        // double tasa = planta.getTasaImpuestoAmbiental(); // DESPUÉS DE REFACTORIZAR

        // 3. Verificación
        assertEquals(0.05, tasa, 0.001, "La tasa de impuesto ambiental inicial debe ser de 0.05");
    }

    @Test
    public void tasaImpuesto_CalculoSobrePrecioBase_CalculaMontoCorrectamente() {
        // 1. Preparación
        Venta venta = new Venta("V-02", null, LocalDate.now());
        // Planta planta = new PlantaInterior("P-01", "Monstera", 200.0, 10, "Monstera deliciosa", 70); // DESPUES DE REFACTORIZAR
        double precioBase = 200.0;

        // 2. Ejecución
        double impuestoCalculado = precioBase * venta.tasaImpuestoAmbiental;
        // double impuestoCalculado = planta.getPrecioBase() * planta.getTasaImpuestoAmbiental(); // DESPUES DE REFACTORIZAR

        // 3. Verificación
        assertEquals(10.0, impuestoCalculado, 0.001, "El 5% de impuesto sobre $200.0 debe ser de $10.0");
    }

    @Test
    public void tasaImpuesto_ModificacionTasa_MantieneConsistenciaEnVenta() {
        // 1. Preparación
        Venta venta = new Venta("V-03", null, LocalDate.now());
        // Planta planta = new PlantaInterior("P-01", "Monstera", 200.0, 10, "Monstera deliciosa", 70); // DESPUES DE REFACTORIZAR

        // 2. Ejecución
        venta.tasaImpuestoAmbiental = 0.08;
        // planta.setTasaImpuestoAmbiental(0.08); // DESPUÉS DE REFACTORIZAR

        // 3. Verificación
        assertEquals(0.08, venta.tasaImpuestoAmbiental, 0.001, "La tasa modificada debe reflejar 0.08");
        // assertEquals(0.08, planta.getTasaImpuestoAmbiental(), 0.001, "La tasa modificada debe reflejar 0.08"); // DESPUES DE REFACTORIZAR
    }
    /*----------------CALCULO TOTAL / R-10 Replace Magic Number  (calcularTotal) ----------------------------------*/
    @Test
    public void calcularTotal_SubtotalMenorAUmbral_AplicaCostoEmbalajeEIva() {
        // 1. Preparación
        Venta venta = new Venta("V-04", null, LocalDate.now());

        // 2. Ejecución
        // Subtotal = $100 (menor a 500)  IVA (16%) = $16, embalaje = $25 / Total = $141
        double total = venta.calcularTotal(100.0);

        // 3. Verificación
        assertEquals(141.0, total, 0.001, "Para subtotales menores a $500 debe cobrar $25 de embalaje más 16% de IVA");
    }

    @Test
    public void calcularTotal_SubtotalMayorOIgualAUmbral_ExentaEmbalajeYAplicaIva() {
        // 1. Preparación
        Venta venta = new Venta("V-05", null, LocalDate.now());

        // 2. Ejecución
        // Subtotal = $500 (igual al umbral) IVA (16%)= $80, embalaje = 0 / Total = $580
        double total = venta.calcularTotal(500.0);

        // 3. Verificación
        assertEquals(580.0, total, 0.001, "Para subtotales de $500 o más el embalaje debe ser gratis");
    }

    @Test
    public void calcularTotal_SubtotalCero_CalculaSoloCostoBaseDeEmbalaje() {
        // 1. Preparación
        Venta venta = new Venta("V-06", null, LocalDate.now());

        // 2. Ejecución
        double total = venta.calcularTotal(0.0);

        // 3. Verificación
        assertEquals(25.0, total, 0.001, "Un subtotal de 0 en compra menor a $500 solo cobra el embalaje fijo de $25");
    }
    /*----------------APLICAR DESCUENTO / RF-12: Consolidate Duplicate Conditional Fragments (aplicarDescuentoMayoreo)----------------------------------*/

    @Test
    public void aplicarDescuentoMayoreo_DiezMasPlantas_AplicaQuincePorCiento() {
        // 1. Preparación
        Venta venta = new Venta("V-07", null, LocalDate.now());

        // 2. Ejecución
        double totalConDescuento = venta.aplicarDescuentoMayoreo(1000.0, 10);

        // 3. Verificación
        assertEquals(850.0, totalConDescuento, 0.001, "Debe descontar el 15% del subtotal para 10 o más plantas");
    }

    @Test
    public void aplicarDescuentoMayoreo_MenosDeDiezPlantas_AplicaCincoPorCiento() {
        // 1. Preparación
        Venta venta = new Venta("V-08", null, LocalDate.now());

        // 2. Ejecución
        double totalConDescuento = venta.aplicarDescuentoMayoreo(1000.0, 5);

        // 3. Verificación
        assertEquals(950.0, totalConDescuento, 0.001, "Debe descontar el 5% del subtotal para menos de 10 plantas");
    }

    @Test
    public void aplicarDescuentoMayoreo_LimiteExactoPlantas_AplicaCincoPorCiento() {
        // 1. Preparación
        Venta venta = new Venta("V-09", null, LocalDate.now());

        // 2. Ejecución
        double totalConDescuento = venta.aplicarDescuentoMayoreo(500.0, 9);

        // 3. Verificación
        assertEquals(475.0, totalConDescuento, 0.001, "Con 9 plantas exactas debe aplicar aún la tarifa del 5%");
    }
    /*----------------RECAGO CLIMA / R-14 DECOMPOSE CONDITIONAL (calcularRecargoClima) ----------------------------------*/

    @Test
    public void calcularRecargoClima_MesInvernalYCostoMayorAUmbral_AplicaRecargoHeladas() {
        // 1. Preparación
        // Diciembre (mes 12) y costo base de $200.0 (mayor a $100.0)
        // Fórmula Invierno: (200.0 * 1.15) + 35.0 = 230.0 + 35.0 = $265.0
        LocalDate fechaDiciembre = LocalDate.of(2026, 12, 15);
        Venta venta = new Venta("V-19", null, fechaDiciembre);

        // 2. Ejecución
        double costoFinal = venta.calcularRecargoClima(200.0, fechaDiciembre);
        // double costoFinal = venta.calcularRecargoClimaRefactorizado(200.0, fechaDiciembre); // DESPUÉS DE REFACTORIZAR (usando esTemporadaInvernal())

        // 3. Verificación
        assertEquals(265.0, costoFinal, 0.001, "En invierno con costo > $100 debe aplicar el 15% de recargo más $35 de protección");
    }

    @Test
    public void calcularRecargoClima_MesInvernalPeroCostoMenorOUmbral_AplicaTarifaNormal() {
        // 1. Preparación
        // Enero (mes 1) pero costo base de $100.0 (igual al umbral, no mayor)
        // Tarifa normal: 100.0 * 1.02 = $102.0
        LocalDate fechaEnero = LocalDate.of(2026, 1, 20);
        Venta venta = new Venta("V-20", null, fechaEnero);

        // 2. Ejecución
        double costoFinal = venta.calcularRecargoClima(100.0, fechaEnero);

        // 3. Verificación
        assertEquals(102.0, costoFinal, 0.001, "En mes invernal con costo <= $100 solo debe aplicar la tarifa normal del 2%");
    }

    @Test
    public void calcularRecargoClima_MesFueraDeInviernoYCostoElevado_AplicaTarifaNormal() {
        // 1. Preparación
        // Julio (mes 7) y costo base elevado de $500.0
        // Tarifa normal: 500.0 * 1.02 = $510.0
        LocalDate fechaJulio = LocalDate.of(2026, 7, 10);
        Venta venta = new Venta("V-21", null, fechaJulio);

        // 2. Ejecución
        double costoFinal = venta.calcularRecargoClima(500.0, fechaJulio);

        // 3. Verificación
        assertEquals(510.0, costoFinal, 0.001, "Fuera de la temporada invernal solo debe aplicarse el incremento estándar del 2%");
    }

    /*----------------PROCESAR PAGO / RF-15: Replace Nested Conditional (procesarPago)----------------------------------*/

    private Cliente clienteValido;

    @BeforeEach
    public void setUp() {
        DireccionEnvio direccion = new DireccionEnvio("Calle 60", "305", "Mérida", "97000");
        clienteValido = new Cliente("C-01", "Alessandra Rivera", direccion, null, "52", "9991234567", "10");
    }

    @Test
    public void procesarPago_ClienteEsNulo_RechazaElPago() {
        // 1. Preparación
        Venta venta = new Venta("V-10", null, LocalDate.now());

        // 2. Ejecución
        boolean resultado = venta.procesarPago(500.0, 300.0);

        // 3. Verificación
        assertFalse(resultado, "El pago debe ser rechazado si la venta no tiene cliente");
        assertFalse(venta.isPagada(), "La venta no debe marcarse como pagada");
    }

    @Test
    public void procesarPago_MontoInsuficiente_RechazaElPago() {
        // 1. Preparación
        Venta venta = new Venta("V-11", clienteValido, LocalDate.now());

        // 2. Ejecución
        boolean resultado = venta.procesarPago(200.0, 300.0);

        // 3. Verificación
        assertFalse(resultado, "El pago debe rechazarse si se paga menos de lo requerido");
        assertFalse(venta.isPagada(), "La venta debe permanecer como no pagada");
    }

    @Test
    public void procesarPago_DatosValidosYMontoSuficiente_ProcesaPagoExitosamente() {
        // 1. Preparación
        Venta venta = new Venta("V-12", clienteValido, LocalDate.now());

        // 2. Ejecución
        boolean resultado = venta.procesarPago(350.0, 300.0);

        // 3. Verificación
        assertTrue(resultado, "El pago debe ser aprobado cuando los datos y monto son válidos");
        assertTrue(venta.isPagada(), "La venta debe actualizar su estado a pagada");
    }

    /*----------------APLICAR RECARGO / RF-12: Consolidate Duplicate Conditional Fragments (aplicarDescuentoMayoreo)----------------------------------*/

    @Test
    public void recargo_TarifaBaseEstandar_SumaCostoFijoDeEnvioExpress() {
        // 1. Preparación
        Venta venta = new Venta("V-13", null, LocalDate.now());

        // 2. Ejecución
        double resultado = venta.recargar(100.0);
        // double resultado = venta.aplicarRecargoEnvioExpress(100.0); // DESPUES DE REFACTORIZAR

        // 3. Verificación
        assertEquals(150.0, resultado, 0.001, "El método debe añadir $50.0 al costo base del envío");
    }

    @Test
    public void recargo_TarifaBaseCero_RetornaUnicamenteCostoFijo() {
        // 1. Preparación
        Venta venta = new Venta("V-14", null, LocalDate.now());

        // 2. Ejecución
        double resultado = venta.recargar(0.0);
        // double resultado = venta.aplicarRecargoEnvioExpress(0.0); // DESPUES DE REFACTORIZAR

        // 3. Verificación
        assertEquals(50.0, resultado, 0.001, "Con tarifa base de $0 el total debe ser exactamente el recargo de $50.0");
    }

    @Test
    public void recargo_TarifaBaseElevada_MantieneCalculoCorrecto() {
        // 1. Preparación
        Venta venta = new Venta("V-15", null, LocalDate.now());

        // 2. Ejecución
        double resultado = venta.recargar(450.50);
        // double resultado = venta.aplicarRecargoEnvioExpress(450.50); // DESPUES DE REFACTORIZAR

        // 3. Verificación
        assertEquals(500.50, resultado, 0.001, "Para una tarifa de $450.50 el total con recargo debe ser $500.50");
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