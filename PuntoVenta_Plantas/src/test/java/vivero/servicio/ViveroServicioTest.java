package vivero.servicio;

import org.junit.jupiter.api.Test;
import vivero.modelo.Planta;
import vivero.modelo.PlantaExterior;
import vivero.modelo.PlantaInterior;

import static org.junit.jupiter.api.Assertions.*;

class ViveroServicioTest {

    /*----------------CONSULTA Y MODIFICACION / R-19 SEPARATE QUERY FROM MODIFIER (obtenerPlantaYDescontarStock) ----------------------------------*/

    @Test
    public void obtenerPlantaYDescontarStock_PlantaExistente_RetornaInstanciaYReduceStock() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta planta = new PlantaInterior("P-10", "Monstera", 200.0, 15, "Monstera deliciosa", 70);
        servicio.registrarPlanta(planta);

        // 2. Ejecución
        // ANTES
        //Planta resultado = servicio.obtenerPlantaYDescontarStock("P-10", 3);

        // DESPUÉS DE REFACTORIZAR
        Planta resultado = servicio.obtenerPlantaPorId("P-10"); // Consulta pura
        servicio.descontarStock("P-10", 3);                    // Modificación de estado

        // 3. Verificación
        assertNotNull(resultado, "Debe retornar la instancia de la planta encontrada");
        assertEquals("Monstera", resultado.getNombreComun(), "Debe coincidir el nombre de la planta consultada");
        assertEquals(12, resultado.getStock(), "El stock debe haberse reducido de 15 a 12 unidades");
    }

    @Test
    public void obtenerPlantaYDescontarStock_PlantaNoExistente_RetornaNullYNoAlteraInventario() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta planta = new PlantaInterior("P-11", "Pothos", 90.0, 10, "Epipremnum aureum", 50);
        servicio.registrarPlanta(planta);

        // 2. Ejecución
        //Planta resultado = servicio.obtenerPlantaYDescontarStock("P-99", 2);
        Planta resultado = servicio.obtenerPlantaPorId("P-99"); // DESPUÉS DE REFACTORIZAR

        // 3. Verificación
        assertNull(resultado, "Debe retornar null si la ID de la planta no existe en el catálogo");
        assertEquals(10, planta.getStock(), "El stock de las demás plantas en inventario no debe verse afectado");
    }

    @Test
    public void obtenerPlantaYDescontarStock_DescuentoMultiple_AcumulaReduccionDeStockCorrectamente() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta planta = new PlantaExterior("P-12", "Lavanda", 80.0, 20, "Lavandula dentata", true);
        servicio.registrarPlanta(planta);

        // 2. Ejecución
        //ANTES
        //servicio.obtenerPlantaYDescontarStock("P-12", 5);
        //servicio.obtenerPlantaYDescontarStock("P-12", 3);

        // DESPUÉS DE REFACTORIZAR
        servicio.descontarStock("P-12", 5);
        servicio.descontarStock("P-12", 3);

        // 3. Verificación
        assertEquals(12, planta.getStock(), "Tras descontar 5 y luego 3 unidades, el stock restante debe ser 12");
    }
}