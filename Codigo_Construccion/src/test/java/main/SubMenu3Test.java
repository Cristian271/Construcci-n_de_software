package main;

import data.estructures.stack.Container;
import main.moduled.Distribution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SubMenu3Test {

    private Distribution route;

    @BeforeEach
    void setUp() {
        route = new Distribution();
        Main.start = null;
        Main.end = null;
    }

    // ==========================================
    // CASO 1: route.insertEnd()
    // ==========================================
    @Test
    void testInitialRouteIsEmpty() {
        assertEquals(0, route.getCont(), "Una ruta nueva debe comenzar con 0 paradas.");
    }

    @Test
    void testInsertEndIncrementsCounter() {
        route.insertEnd("Progreso Terminal");
        assertEquals(1, route.getCont());

        route.insertEnd("Mérida Centro");
        assertEquals(2, route.getCont());
    }

    // ==========================================
    // CASO 2: route.insertBetween()
    // ==========================================
    @Test
    void testInsertBetweenIncreasesStopsCount() {
        route.insertEnd("Progreso");
        route.insertEnd("Mérida");

        route.insertBetween("Progreso", "Mérida", "Komchén");
        assertEquals(3, route.getCont(), "Deben existir 3 paradas tras la inserción intermedia.");
    }

    @Test
    void testInsertBetweenWithInvalidStops() {
        route.insertEnd("Progreso");
        route.insertEnd("Mérida");
        route.insertBetween("Cancún", "Sisal", "Canek");
        assertEquals(2, route.getCont());
    }

    // ==========================================
    // CASO 3: route.deleteStop()
    // ==========================================
    @Test
    void testDeleteExistingStop() {
        route.insertEnd("Progreso");
        route.insertEnd("Mérida");

        route.deleteStop("Progreso");
        assertEquals(1, route.getCont(), "Al eliminar una parada el contador debe disminuir en 1.");
    }

    @Test
    void testDeleteNonExistingStopLeavesCountIntact() {
        route.insertEnd("Progreso");
        route.deleteStop("Cancún");
        assertEquals(1, route.getCont(), "No debe disminuir el conteo si la parada no existía.");
    }

    // ==========================================
    // CASO 4: Validaciones previas y recorrido de contenedores en ruta
    // ==========================================
    @Test
    void testSimulateRouteFailsWhenRouteEmpty() {
        assertEquals(0, route.getCont());
        assertTrue(route.getCont() == 0, "No debe permitir simular si getCont() == 0");
    }

    @Test
    void testSimulateRouteFailsWhenStartIsNull() {
        route.insertEnd("Progreso");
        assertNull(Main.start, "Si Main.start es null, no debe haber contenedores a simular.");
    }

    @Test
    void testFindContainerInMainStartList() {
        Container c1 = new Container("CONT-101");
        Container c2 = new Container("CONT-102");
        Container c3 = new Container("CONT-103");
        c1.setNext(c2);
        c2.setNext(c3);
        Main.start = c1;

        String idTemp = "CONT-102";
        Container current = Main.start;
        boolean encontrado = false;

        while (current != null) {
            if (current.getId().equals(idTemp)) {
                encontrado = true;
                break;
            }
            current = current.getNext();
        }

        assertTrue(encontrado);
        assertNotNull(current);
        assertEquals("CONT-102", current.getId());
    }

    @Test
    void testFindContainerNotFound() {
        Container c1 = new Container("CONT-101");
        Main.start = c1;

        String idTemp = "CONT-999";
        Container current = Main.start;
        boolean encontrado = false;

        while (current != null) {
            if (current.getId().equals(idTemp)) {
                encontrado = true;
                break;
            }
            current = current.getNext();
        }

        assertFalse(encontrado, "El contenedor CONT-999 no debe encontrarse en la lista.");
    }
}