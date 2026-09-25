package main.modulea;

import data.estructures.queue.BusQueue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionTest {
    //PRUEBA DEL METODO ISEMPTY

    private Reception reception;

    @BeforeEach
    void setUp() {
        reception = new Reception();
    }

    @AfterEach
    void tearDown() {
        reception = null;
    }

    // ============================================================
    // CASO 1
    // La cola inicia vacía.
    // ============================================================
    @Test
    void caso01_colaInicialVacia() {
        assertTrue(reception.isEmpty());
        assertEquals(0, reception.size());
        assertNull(reception.front());
    }

    // ============================================================
    // CASO 2
    // Se intenta eliminar un elemento de una cola vacía.
    // ============================================================
    @Test
    void caso02_dequeueEnColaVacia() {
        BusQueue deleted = reception.dequeue();

        assertNull(deleted);
        assertEquals(0, reception.size());
        assertNull(reception.front());
    }

    // ============================================================
    // CASO 3
    // Verificación de desapilado básico.
    // ============================================================
    @Test
    void caso03_operacionesBasicasCola() {
        assertEquals(0, reception.size());
        assertNull(reception.front());
    }
}