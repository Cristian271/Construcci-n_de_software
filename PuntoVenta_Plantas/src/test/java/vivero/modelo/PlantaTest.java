package vivero.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantaTest {

    @Test
    public void calcularPrecioFinal_SinFertilizante_RetornaPrecioBase() {
        // Preparación
        Planta planta = new PlantaExterior("P-01", "Rosal", 100.0, 10, "Rosa spp.", true);

        // Ejecución (fertilizante = false)
        double precioFinal = planta.calcularPrecioFinal(50.0, false);

        // Verificación
        assertEquals(100.0, precioFinal, 0.001, "El precio final sin fertilizante debe ser igual al precio base");
    }

    @Test
    public void calcularPrecioFinal_ConFertilizante_SumaCuarentaYCincoAlPrecioBase() {
        // Preparación
        Planta planta = new PlantaExterior("P-02", "Bugambilia", 100.0, 5, "Bougainvillea", false);

        // Ejecución (fertilizante = true)
        double precioFinal = planta.calcularPrecioFinal(50.0, true);

        // Verificación (100.0 + 45.0)
        assertEquals(145.0, precioFinal, 0.001, "El precio final con fertilizante debe sumar $45.0 al precio base");
    }

    @Test
    public void calcularPrecioFinal_PorcentajeLuzNoAfectaResultado_SinFertilizante() {
        // Preparación
        Planta planta = new PlantaExterior("P-03", "Lavanda", 80.0, 20, "Lavandula", true);

        // Ejecución con diferentes porcentajes de luz, pero sin fertilizante
        double precioConMuchaLuz = planta.calcularPrecioFinal(100.0, false);
        double precioConPocaLuz = planta.calcularPrecioFinal(0.0, false);

        // Verificación (ambos deben dar 80.0)
        assertEquals(precioConMuchaLuz, precioConPocaLuz, 0.001, "El parámetro porcentajeLuz no debería alterar el precio");
        assertEquals(80.0, precioConMuchaLuz, 0.001);
    }

    @Test
    public void calcularPrecioFinal_PorcentajeLuzNoAfectaResultado_ConFertilizante() {
        // Preparación
        Planta planta = new PlantaExterior("P-04", "Cactus", 50.0, 15, "Cactaceae", true);

        // Ejecución con diferentes porcentajes de luz, pero con fertilizante
        double precioConLuzMedia = planta.calcularPrecioFinal(45.5, true);
        double precioConMuchaLuz = planta.calcularPrecioFinal(99.9, true);

        // Verificación (ambos deben dar 50.0 + 45.0 = 95.0)
        assertEquals(95.0, precioConLuzMedia, 0.001);
        assertEquals(95.0, precioConMuchaLuz, 0.001, "El parámetro porcentajeLuz no tiene impacto en el cálculo");
    }

    @Test
    public void calcularPrecioFinal_PrecioBaseCeroConFertilizante_RetornaSoloCostoFertilizante() {
        // Preparación (Caso extremo: Planta regalada o precio base 0)
        Planta planta = new PlantaExterior("P-05", "Esqueje", 0.0, 50, "Desconocida", false);

        // Ejecución (fertilizante = true)
        double precioFinal = planta.calcularPrecioFinal(20.0, true);

        // Verificación (0.0 + 45.0)
        assertEquals(45.0, precioFinal, 0.001, "Si el precio base es 0, el precio final debe ser solo el costo del fertilizante");
    }
}