package vivero.servicio;

import org.junit.jupiter.api.Test;
import vivero.modelo.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /*----------------RF-07 / HIDE DELEGATE (obtenerCiudadDestino) ----------------------------------*/

    @Test
    public void obtenerCiudadDestino_ClienteYDireccionValidos_RetornaCiudadDestino() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        DireccionEnvio direccion = new DireccionEnvio("Calle 60", "305", "Merida", "97000");
        Cliente cliente = new Cliente("C-01", "Alessandra Rivera", direccion, null, "52", "9991234567", "10");
        Venta venta = new Venta("V-01", cliente, LocalDate.now());

        // 2. Ejecución (Venta con cliente y dirección "Merida" -> devuelve "Merida")
        String ciudad = servicio.obtenerCiudadDestino(venta);

        // 3. Verificación
        assertEquals("Merida", ciudad, "Para una venta con cliente y dirección válidos, obtenerCiudadDestino devuelve la misma ciudad");
    }

    @Test
    public void obtenerCiudadDestino_ClienteSinDireccion_LanzaNullPointerException() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Cliente clienteSinDireccion = new Cliente("C-02", "Juan Perez", null, null, "52", "9991234567", "10");
        Venta venta = new Venta("V-02", clienteSinDireccion, LocalDate.now());

        // 2. Ejecución y Verificación (Cliente sin dirección -> NullPointerException)
        assertThrows(NullPointerException.class, () -> {
            servicio.obtenerCiudadDestino(venta);
        }, "Si el cliente no tiene dirección asociada, debe lanzar NullPointerException");
    }

    @Test
    public void obtenerCiudadDestino_VentaSinCliente_LanzaNullPointerException() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Venta ventaSinCliente = new Venta("V-03", null, LocalDate.now());

        // 2. Ejecución y Verificación (Venta sin cliente -> NullPointerException)
        assertThrows(NullPointerException.class, () -> {
            servicio.obtenerCiudadDestino(ventaSinCliente);
        }, "Si la venta no tiene cliente asociado, debe lanzar NullPointerException");
    }

    /*----------------RF-14 / REMOVE CONTROL FLAG (buscarPlantaPorNombre) ----------------------------------*/

    @Test
    public void buscarPlantaPorNombre_NombreExactoConMayusculas_RetornaPlanta() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta monstera = new PlantaInterior("P-01", "Monstera Deliciosa", 280.0, 15, "Monstera deliciosa", 75);
        servicio.registrarPlanta(monstera);

        // 2. Ejecución (Nombre exacto de una planta con mayúsculas y minúsculas -> devuelve esa planta)
        Planta encontrada = servicio.buscarPlantaPorNombre("Monstera Deliciosa");

        // 3. Verificación
        assertNotNull(encontrada, "Debe encontrar la planta buscada");
        assertEquals("P-01", encontrada.getId(), "El ID debe coincidir con la planta registrada");
        assertEquals("Monstera Deliciosa", encontrada.getNombreComun(), "El nombre común debe coincidir");
    }

    @Test
    public void buscarPlantaPorNombre_NombreSoloMinusculas_RetornaPlanta() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta monstera = new PlantaInterior("P-01", "Monstera Deliciosa", 280.0, 15, "Monstera deliciosa", 75);
        servicio.registrarPlanta(monstera);

        // 2. Ejecución (Nombre de la planta en solo minúsculas -> devuelve esa planta)
        Planta encontrada = servicio.buscarPlantaPorNombre("monstera deliciosa");

        // 3. Verificación
        assertNotNull(encontrada, "Debe encontrar la planta ignorando mayúsculas y minúsculas");
        assertEquals("P-01", encontrada.getId(), "El ID debe coincidir con la planta registrada");
    }

    @Test
    public void buscarPlantaPorNombre_NombreInexistente_RetornaNull() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta monstera = new PlantaInterior("P-01", "Monstera Deliciosa", 280.0, 15, "Monstera deliciosa", 75);
        servicio.registrarPlanta(monstera);

        // 2. Ejecución (Nombre inexistente -> null)
        Planta encontrada = servicio.buscarPlantaPorNombre("Orquidea");

        // 3. Verificación
        assertNull(encontrada, "Debe retornar null cuando la planta no existe en el catálogo");
    }

    @Test
    public void buscarPlantaPorNombre_InventarioVacio_RetornaNull() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();

        // 2. Ejecución (Inventario vacío -> null)
        Planta encontrada = servicio.buscarPlantaPorNombre("Monstera Deliciosa");

        // 3. Verificación
        assertNull(encontrada, "Debe retornar null si el inventario está completamente vacío");
    }

    @Test
    public void buscarPlantaPorNombre_DosPlantasConMismoNombre_RetornaPrimeraRegistrada() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Planta primeraBugambilia = new PlantaInterior("P-01", "Bugambilia", 100.0, 10, "Bougainvillea", 60);
        Planta segundaBugambilia = new PlantaExterior("P-06", "Bugambilia", 110.0, 30, "Bougainvillea spectabilis", true);
        servicio.registrarPlanta(primeraBugambilia);
        servicio.registrarPlanta(segundaBugambilia);

        // 2. Ejecución (Dos plantas con el mismo nombre -> devuelve la que se registró primero)
        Planta encontrada = servicio.buscarPlantaPorNombre("Bugambilia");

        // 3. Verificación
        assertNotNull(encontrada, "Debe encontrar una planta con el nombre buscado");
        assertEquals("P-01", encontrada.getId(), "Si hay duplicados, debe retornar la primera planta en orden de registro");
    }

    /*----------------RF-16 / INTRODUCE NULL OBJECT (calcularDescuentoCliente) ----------------------------------*/

    @Test
    public void calcularDescuentoCliente_ClienteConMembresiaNull_PagaTotalCompleto() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Cliente clienteSinMembresia = new Cliente("C-01", "Carlos Sanchez", null, null, "52", "9991234567", "10");

        // 2. Ejecución (Cliente con membresía null, total 200 -> 200)
        double totalConDescuento = servicio.calcularDescuentoCliente(clienteSinMembresia, 200.0);

        // 3. Verificación
        assertEquals(200.0, totalConDescuento, 0.001, "Un cliente sin membresía (null) debe pagar el total completo (0% de descuento)");
    }

    @Test
    public void calcularDescuentoCliente_ClienteConMembresiaPlatino_AplicaQuincePorCientoDescuento() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Membresia platino = new Membresia("Platino", 0.15);
        Cliente clientePlatino = new Cliente("C-02", "Maria Lopez", null, platino, "52", "9991234567", "10");

        // 2. Ejecución (Cliente con membresía Platino, total 200 -> 170)
        double totalConDescuento = servicio.calcularDescuentoCliente(clientePlatino, 200.0);

        // 3. Verificación
        assertEquals(170.0, totalConDescuento, 0.001, "Un cliente con membresía Platino debe recibir el 15% de descuento (pagando $170 de $200)");
    }

    @Test
    public void calcularDescuentoCliente_TotalCero_RetornaCeroEnAmbosCasosDeMembresia() {
        // 1. Preparación
        ViveroServicio servicio = new ViveroServicio();
        Cliente clienteSinMembresia = new Cliente("C-01", "Carlos Sanchez", null, null, "52", "9991234567", "10");
        Membresia platino = new Membresia("Platino", 0.15);
        Cliente clientePlatino = new Cliente("C-02", "Maria Lopez", null, platino, "52", "9991234567", "10");

        // 2. Ejecución (Total 0 -> 0 en ambos casos de membresía)
        double totalSinMembresia = servicio.calcularDescuentoCliente(clienteSinMembresia, 0.0);
        double totalConPlatino = servicio.calcularDescuentoCliente(clientePlatino, 0.0);

        // 3. Verificación
        assertEquals(0.0, totalSinMembresia, 0.001, "Con total 0 y membresía null el monto resultante debe ser 0.0");
        assertEquals(0.0, totalConPlatino, 0.001, "Con total 0 y membresía Platino el monto resultante debe ser 0.0");
    }
}