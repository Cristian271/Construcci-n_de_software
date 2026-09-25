package main.modulebc;

import data.estructures.stack.Container;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class YardTest {
    //PRUEBAS PARA EL METODO ISEMPTY

    private Yard yard;

    @BeforeEach
    void setUp() {
        yard = new Yard(5);
    }

    @AfterEach
    void tearDown() {
        yard = null;
    }

    // ============================================================
    // CASO 1
    // La pila inicia vacía.
    // ============================================================
    @Test
    void caso01_pilaInicialVacia() {
        assertTrue(yard.isEmpty());
        assertEquals(0, yard.size());
        assertNull(yard.top());
    }

    // ============================================================
    // CASO 2
    // Se inserta un contenedor correctamente.
    // ============================================================
    @Test
    void caso02_pushUnElemento() {
        Container container = new Container("CONT-101");

        yard.push(container);

        assertFalse(yard.isEmpty());
        assertEquals(1, yard.size());
        assertNotNull(yard.top());
        assertEquals("CONT-101", yard.top().getId());
    }

    // ============================================================
    // CASO 3
    // Se vacía la pila tras hacer pop del único elemento.
    // ============================================================
    @Test
    void caso03_popUnicoElementoVaciaPila() {
        yard.push(new Container("CONT-101"));

        Container popped = yard.pop();

        assertEquals("CONT-101", popped.getId());
        assertEquals(0, yard.size());
        assertNull(yard.top());
    }

    // ============================================================
    // CASO 4
    // Intento de pop en una pila vacía.
    // ============================================================
    @Test
    void caso04_popEnPilaVacia() {
        Container popped = yard.pop();

        assertNull(popped);
        assertEquals(0, yard.size());
        assertNull(yard.top());
    }

    // ============================================================
    // CASO 5
    // Intentar ingresar más elementos del límite permitido.
    // ============================================================
    @Test
    void caso05_pushExcedeLimite() {
        Yard yard = new Yard(2);

        yard.push(new Container("C1"));
        yard.push(new Container("C2"));
        yard.push(new Container("C3"));

        assertEquals(2, yard.size());
        assertEquals("C2", yard.top().getId());
    }

    // ============================================================
    // CASO 6
    // Retirar un contenedor específico con popContainer.
    // ============================================================
    @Test
    void caso06_popContainerExistente() {
        yard.push(new Container("C1"));
        yard.push(new Container("C2"));
        yard.push(new Container("C3"));

        Container popped = yard.popContainer("C2");

        assertNotNull(popped);
        assertEquals("C2", popped.getId());
        assertEquals(2, yard.size());
        assertEquals("C3", yard.top().getId());
    }
}