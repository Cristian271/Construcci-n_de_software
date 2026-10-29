package vivero.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantaTest {
    // CalcularPrecioFinal
    @Test
    public void calcularPrecioFinal_SinFertilizante_RetornaPrecioBase() {
        // Preparación
        Planta planta = new PlantaExterior("P-01", "Rosal", 100.0, 10, "Rosa spp.", true);

        // Ejecución (fertilizante = false)
        double precioFinal = planta.calcularPrecioFinal( false); //antes tenia un atributo mas, pero fue removido por la refactorizacion

        // Verificación
        assertEquals(100.0, precioFinal, 0.001, "El precio final sin fertilizante debe ser igual al precio base");
    }

    @Test
    public void calcularPrecioFinal_ConFertilizante_SumaCuarentaYCincoAlPrecioBase() {
        // Preparación
        Planta planta = new PlantaExterior("P-02", "Bugambilia", 100.0, 5, "Bougainvillea", false);

        // Ejecución (fertilizante = true)
        double precioFinal = planta.calcularPrecioFinal( true); //antes tenia un atributo mas, pero fue removido por la refactorizacion

        // Verificación (100.0 + 45.0)
        assertEquals(145.0, precioFinal, 0.001, "El precio final con fertilizante debe sumar $45.0 al precio base");
    }

    @Test
    public void calcularPrecioFinal_PorcentajeLuzNoAfectaResultado_SinFertilizante() {
        // Preparación
        Planta planta = new PlantaExterior("P-03", "Lavanda", 80.0, 20, "Lavandula", true);

        // Ejecución con diferentes porcentajes de luz, pero sin fertilizante
        double precioConMuchaLuz = planta.calcularPrecioFinal( false); //antes tenia un atributo mas, pero fue removido por la refactorizacion
        double precioConPocaLuz = planta.calcularPrecioFinal( false); //antes tenia un atributo mas, pero fue removido por la refactorizacion

        // Verificación (ambos deben dar 80.0)
        assertEquals(precioConMuchaLuz, precioConPocaLuz, 0.001, "El parámetro porcentajeLuz no debería alterar el precio");
        assertEquals(80.0, precioConMuchaLuz, 0.001);
    }

    @Test
    public void calcularPrecioFinal_PorcentajeLuzNoAfectaResultado_ConFertilizante() {
        // Preparación
        Planta planta = new PlantaExterior("P-04", "Cactus", 50.0, 15, "Cactaceae", true);

        // Ejecución con diferentes porcentajes de luz, pero con fertilizante
        double precioConLuzMedia = planta.calcularPrecioFinal( true); //antes tenia un atributo mas, pero fue removido por la refactorizacion
        double precioConMuchaLuz = planta.calcularPrecioFinal( true); //antes tenia un atributo mas, pero fue removido por la refactorizacion

        // Verificación (ambos deben dar 50.0 + 45.0 = 95.0)
        assertEquals(95.0, precioConLuzMedia, 0.001);
        assertEquals(95.0, precioConMuchaLuz, 0.001, "El parámetro porcentajeLuz no tiene impacto en el cálculo");
    }

    @Test
    public void calcularPrecioFinal_PrecioBaseCeroConFertilizante_RetornaSoloCostoFertilizante() {
        // Preparación (Caso extremo: Planta regalada o precio base 0)
        Planta planta = new PlantaExterior("P-05", "Esqueje", 0.0, 50, "Desconocida", false);

        // Ejecución (fertilizante = true)
        double precioFinal = planta.calcularPrecioFinal( true); //antes tenia un atributo mas, pero fue removido por la refactorizacion

        // Verificación (0.0 + 45.0)
        assertEquals(45.0, precioFinal, 0.001, "Si el precio base es 0, el precio final debe ser solo el costo del fertilizante");
    }


    /*PRUEBAS DE ATRIBUTO BASE DE PLANTA*/
    @Test
    public void precioBase_LecturaYModificacionDirecta_FuncionaCorrectamente() {
        // 1. Preparación: Instanciamos una subclase concreta (PlantaInterior)
        Planta planta = new PlantaInterior("P-01", "Monstera Deliciosa", 280.0, 15, "Monstera deliciosa", 75);

        // 2. Verificación del estado inicial accediendo al atributo público
    /*
    assertEquals(280.0, planta.precioBase, 0.001, "El precio base inicial debe coincidir con el valor del constructor");
    */
        assertEquals(280.0, planta.getPrecioBase(), 0.001, "El precio base inicial debe coincidir con el valor del constructor");

        // 3. Ejecución: Modificación directa del atributo
    /*
    planta.precioBase = 320.5;
    */
        planta.setPrecioBase(320.5);

        // 4. Verificación del cambio
    /*
    assertEquals(320.5, planta.precioBase, 0.001, "El precio base debe reflejar el nuevo valor asignado directamente");
    */
        assertEquals(320.5, planta.getPrecioBase(), 0.001, "El precio base debe reflejar el nuevo valor asignado directamente");
    }

    @Test
    public void precioBase_PermiteValoresInvalidos_EvidenciaNecesidadDeEncapsulamiento() {
        // 1. Preparación
        Planta planta = new PlantaExterior("P-02", "Cactus Órgano", 190.0, 12, "Pachycereus", true);

        // 2. Ejecución:
    /*
    planta.precioBase = -50.0;
    */
        planta.setPrecioBase(-50.0);

        // 3. Verificación
    /*
    assertEquals(-50.0, planta.precioBase, 0.001,
            "Actualmente el atributo acepta valores negativos sin validación. " +
                    "Esto justifica encapsularlo para proteger la integridad del dato.");
    */
        assertEquals(-50.0, planta.getPrecioBase(), 0.001,
                "Actualmente el atributo acepta valores negativos sin validación. " +
                        "Esto justifica encapsularlo para proteger la integridad del dato.");
    }

    @Test
    public void precioBase_IndependenciaDeInstancias_ModificarUnaNoAfectaAOtra() {
        // 1. Preparación: Instanciamos dos plantas distintas
        Planta planta1 = new PlantaInterior("P-03", "Helecho", 150.0, 10, "Nephrolepis", 80);
        Planta planta2 = new PlantaExterior("P-04", "Lavanda", 85.0, 18, "Lavandula", true);

        // 2. Ejecución: Modificamos el precio base solo de la primera planta
    /*
    planta1.precioBase = 300.0;
    */
        planta1.setPrecioBase(300.0);

        // 3. Verificación: Aseguramos que los objetos mantienen su estado aislado
    /*
    assertEquals(300.0, planta1.precioBase, 0.001, "El precio de la planta 1 debió actualizarse");
    assertEquals(85.0, planta2.precioBase, 0.001, "El precio de la planta 2 debe permanecer intacto (85.0)");
    */
        assertEquals(300.0, planta1.getPrecioBase(), 0.001, "El precio de la planta 1 debió actualizarse");
        assertEquals(85.0, planta2.getPrecioBase(), 0.001, "El precio de la planta 2 debe permanecer intacto (85.0)");
    }
}