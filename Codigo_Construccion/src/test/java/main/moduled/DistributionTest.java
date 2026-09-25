package main.moduled;

import data.estructures.doublylinkedlist.StopBus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistributionTest {
    //  PRUEBAS PARA METODO INSERTBEETWEEN

    // ============================================================
    // CASO 1
    // Inserta entre dos elementos consecutivos.
    // ============================================================
    @Test
    void caso01_insertBetweenNormal() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("A", "B", "X");

        assertEquals(3, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("X", d.firstStopBus().getNext().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 2
    // Los parámetros están invertidos.
    // ============================================================
    @Test
    void caso02_insertBetweenParametrosInvertidos() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("B", "A", "X");

        assertEquals(3, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("X", d.firstStopBus().getNext().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 3
    // Inserta en medio de una lista de tres elementos.
    // ============================================================
    @Test
    void caso03_insertBetweenListaDeTres() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("A", "B", "X");

        assertEquals(4, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("X", d.firstStopBus().getNext().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNext().getNameStop());
        assertEquals("C", d.firstStopBus().getNext().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 4
    // Inserta entre B y C.
    // ============================================================
    @Test
    void caso04_insertBetweenMedio() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("B", "C", "X");

        StopBus a = d.firstStopBus();
        StopBus b = a.getNext();
        StopBus x = b.getNext();
        StopBus c = x.getNext();

        assertEquals("A", a.getNameStop());
        assertEquals("B", b.getNameStop());
        assertEquals("X", x.getNameStop());
        assertEquals("C", c.getNameStop());
        assertEquals(4, d.getCont());
    }


    // ============================================================
    // CASO 5
    // Inserta usando los parámetros invertidos entre B y C.
    // ============================================================
    @Test
    void caso05_insertBetweenMedioInvertido() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("C", "B", "X");

        StopBus a = d.firstStopBus();

        assertEquals("A", a.getNameStop());
        assertEquals("B", a.getNext().getNameStop());
        assertEquals("X", a.getNext().getNext().getNameStop());
        assertEquals("C", a.getNext().getNext().getNext().getNameStop());
        assertEquals(4, d.getCont());
    }


    // ============================================================
    // CASO 6
    // Lista vacía.
    // ============================================================
    @Test
    void caso06_listaVacia() {
        Distribution d = new Distribution();

        d.insertBetween("A", "B", "X");

        assertEquals(0, d.getCont());
        assertTrue(d.isEmpty());
        assertNull(d.firstStopBus());
    }


    // ============================================================
    // CASO 7
    // Solo existe una parada.
    // ============================================================
    @Test
    void caso07_unaSolaParada() {
        Distribution d = new Distribution();

        d.insertEnd("A");

        d.insertBetween("A", "B", "X");

        assertEquals(1, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertNull(d.firstStopBus().getNext());
    }


    // ============================================================
    // CASO 8
    // No existe ninguna de las dos paradas.
    // ============================================================
    @Test
    void caso08_ningunaParadaExiste() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("X", "Y", "Z");

        assertEquals(2, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNameStop());
    }


    // ============================================================
    // CASO 9
    // Solo existe la primera parada, pero no es vecina.
    // ============================================================
    @Test
    void caso09_segundaParadaNoExiste() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("A", "X", "Z");

        assertEquals(3, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNameStop());
        assertEquals("C", d.firstStopBus().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 10
    // Existe la segunda parada, pero no es vecina.
    // ============================================================
    @Test
    void caso10_primeraParadaNoExiste() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("X", "C", "Z");

        assertEquals(3, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNameStop());
        assertEquals("C", d.firstStopBus().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 11
    // Inserción entre el primer y segundo nodo.
    // ============================================================
    @Test
    void caso11_insertarAlInicioDeLaLista() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("A", "B", "X");

        StopBus a = d.firstStopBus();
        StopBus x = a.getNext();
        StopBus b = x.getNext();

        assertEquals("A", a.getNameStop());
        assertEquals("X", x.getNameStop());
        assertEquals("B", b.getNameStop());

        assertNull(a.getPrevious());
        assertEquals(a, x.getPrevious());
        assertEquals(x, b.getPrevious());
    }


    // ============================================================
    // CASO 12
    // Verifica correctamente los enlaces previous.
    // ============================================================
    @Test
    void caso12_verificarEnlacesPrevious() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("B", "C", "X");

        StopBus a = d.firstStopBus();
        StopBus b = a.getNext();
        StopBus x = b.getNext();
        StopBus c = x.getNext();

        assertEquals(a, b.getPrevious());
        assertEquals(b, x.getPrevious());
        assertEquals(x, c.getPrevious());
    }


    // ============================================================
    // CASO 13
    // Verifica los enlaces next.
    // ============================================================
    @Test
    void caso13_verificarEnlacesNext() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");

        d.insertBetween("B", "C", "X");

        StopBus a = d.firstStopBus();
        StopBus b = a.getNext();
        StopBus x = b.getNext();
        StopBus c = x.getNext();

        assertEquals(b, a.getNext());
        assertEquals(x, b.getNext());
        assertEquals(c, x.getNext());
        assertNull(c.getNext());
    }


    // ============================================================
    // CASO 14
    // Inserta varias veces.
    // ============================================================
    @Test
    void caso14_variasInserciones() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("A", "B", "X");
        d.insertBetween("A", "X", "Y");

        assertEquals(4, d.getCont());

        StopBus a = d.firstStopBus();

        assertEquals("A", a.getNameStop());
        assertEquals("Y", a.getNext().getNameStop());
        assertEquals("X", a.getNext().getNext().getNameStop());
        assertEquals("B", a.getNext().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 15
    // Inserta consecutivamente entre los mismos elementos.
    // ============================================================
    @Test
    void caso15_insertarDosVecesMismoLugar() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("A", "B", "X");
        d.insertBetween("A", "X", "Y");

        assertEquals(4, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("Y", d.firstStopBus().getNext().getNameStop());
        assertEquals("X", d.firstStopBus().getNext().getNext().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 16
    // Los dos identificadores son iguales.
    // ============================================================
    @Test
    void caso16_mismosIdentificadores() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("A", "A", "X");

        assertEquals(2, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNameStop());
    }


    // ============================================================
    // CASO 17
    // Nombre de parada nuevo igual a una existente.
    // ============================================================
    @Test
    void caso17_nombreNuevoRepetido() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");

        d.insertBetween("A", "B", "A");

        assertEquals(3, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertEquals("A", d.firstStopBus().getNext().getNameStop());
        assertEquals("B", d.firstStopBus().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 18
    // Lista de cuatro elementos, inserción en el centro.
    // ============================================================
    @Test
    void caso18_listaCuatroElementos() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");
        d.insertEnd("D");

        d.insertBetween("B", "C", "X");

        assertEquals(5, d.getCont());

        StopBus a = d.firstStopBus();

        assertEquals("A", a.getNameStop());
        assertEquals("B", a.getNext().getNameStop());
        assertEquals("X", a.getNext().getNext().getNameStop());
        assertEquals("C", a.getNext().getNext().getNext().getNameStop());
        assertEquals("D", a.getNext().getNext().getNext().getNext().getNameStop());
    }


    // ============================================================
    // CASO 19
    // Lista grande.
    // ============================================================
    @Test
    void caso19_listaGrande() {
        Distribution d = new Distribution();

        for (int i = 1; i <= 10; i++) {
            d.insertEnd("P" + i);
        }

        d.insertBetween("P5", "P6", "NUEVA");

        assertEquals(11, d.getCont());

        StopBus current = d.firstStopBus();

        for (int i = 1; i <= 5; i++) {
            assertEquals("P" + i, current.getNameStop());
            current = current.getNext();
        }

        assertEquals("NUEVA", current.getNameStop());
        assertEquals("P6", current.getNext().getNameStop());
    }


    // ============================================================
    // CASO 20
    // Verificación completa de una inserción en una lista grande.
    // ============================================================
    @Test
    void caso20_verificacionCompleta() {
        Distribution d = new Distribution();

        d.insertEnd("A");
        d.insertEnd("B");
        d.insertEnd("C");
        d.insertEnd("D");
        d.insertEnd("E");

        d.insertBetween("C", "D", "X");

        assertEquals(6, d.getCont());

        StopBus a = d.firstStopBus();

        StopBus b = a.getNext();
        StopBus c = b.getNext();
        StopBus x = c.getNext();
        StopBus dNode = x.getNext();
        StopBus e = dNode.getNext();

        // Orden
        assertEquals("A", a.getNameStop());
        assertEquals("B", b.getNameStop());
        assertEquals("C", c.getNameStop());
        assertEquals("X", x.getNameStop());
        assertEquals("D", dNode.getNameStop());
        assertEquals("E", e.getNameStop());

        // Enlaces hacia atrás
        assertNull(a.getPrevious());
        assertEquals(a, b.getPrevious());
        assertEquals(b, c.getPrevious());
        assertEquals(c, x.getPrevious());
        assertEquals(x, dNode.getPrevious());
        assertEquals(dNode, e.getPrevious());

        // Enlaces hacia adelante
        assertEquals(b, a.getNext());
        assertEquals(c, b.getNext());
        assertEquals(x, c.getNext());
        assertEquals(dNode, x.getNext());
        assertEquals(e, dNode.getNext());
        assertNull(e.getNext());
    }



    // PRUEBAS PARA METODO SHOWCURRENTSTOP
    // ============================================================
    // CASO 1
    // No hay paradas insertadas
    // ============================================================
    @Test
    void caso1_showCurrentStop_SinParadas() {
        Distribution distribution = new Distribution();
        assertEquals("Sin paradas configuradas", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 2
    // Insertando la primera parada
    // ============================================================

    @Test
    void caso2_showCurrentStop_UnaParada() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Estación Central");
        assertEquals("Estación Central", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 3
    // Parada permanece en la primera, aun despues de insertar paradas
    // ============================================================

    @Test
    void caso3_showCurrentStop_Parada1DespuesVariasParadas() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");
        distribution.insertEnd("Parada C");

        assertEquals("Parada A", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 4
    // Parada, despues de ir a la siguiente parada en la lista
    // ============================================================

    @Test
    void caso4_ShowCurrentStop_GoNext() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");

        distribution.goNext();

        assertEquals("Parada B", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 5
    // Parada, despues de regresar a la anterior parada en la lista
    // ============================================================

    @Test
    void caso5_ShowCurrentStop_GoBack() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");

        distribution.goNext();
        distribution.goBack();

        assertEquals("Parada A", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 6
    // Parada actual no debe salir del maximo (la ultima parada)
    // ============================================================

    @Test
    void caso6_ShowCurrentStop_DentroLimiteFinal() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");

        distribution.goNext(); // posicion final en B
        distribution.goNext(); // tratar de seguir

        assertEquals("Parada B", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 7
    // Parada actual no debe salir del maximo (primera parada)
    // ============================================================

    @Test
    void caso7_ShowCurrentStop_DentroLimiteInicio() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");

        distribution.goBack(); //  tratar de retroceder

        assertEquals("Parada A", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 8
    // Al eliminarse una parada, la actual es la siguiente
    // ============================================================

    @Test
    void caso8_ShowCurrentStop_ParadaEliminada() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada B");
        distribution.insertEnd("Parada C");

        distribution.goNext(); // posicion en B
        distribution.deleteStop("Parada B"); // al borrar B , pasa a C

        assertEquals("Parada C", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 9
    // Al eliminarse una parada que es unica, debe mostrar que no hay
    // ============================================================

    @Test
    void caso9_ShowCurrentStop_ParadaUnicaEliminada() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada Unica");
        distribution.deleteStop("Parada Unica");

        assertEquals("Sin paradas configuradas", distribution.showCurrentStop());
    }
    // ============================================================
    // CASO 10
    // Al usar insertbetween, la parada actual debe conservarse en el inicio
    // ============================================================

    @Test
    void caso10_ShowCurrentStop_InsertBetween() {
        Distribution distribution = new Distribution();
        distribution.insertEnd("Parada A");
        distribution.insertEnd("Parada C");

        // position está en Parada A
        distribution.insertBetween("Parada A", "Parada C", "Parada B");

        assertEquals("Parada A", distribution.showCurrentStop());
    }

    //PRUEBAS PARA METODO ISEMPTY
    // ============================================================
    // CASO 1
    // Lista vacía.
    // ============================================================
    @Test
    void caso01_listaVacia() {
        Distribution d = new Distribution();

        d.insertBetween("A", "B", "X");

        assertTrue(d.isEmpty());
        assertEquals(0, d.getCont());
        assertNull(d.firstStopBus());
    }

    // ============================================================
    // CASO 2
    // Solo existe una parada.
    // ============================================================
    @Test
    void caso02_unaSolaParada() {
        Distribution d = new Distribution();

        d.insertEnd("A");

        d.insertBetween("A", "B", "X");

        assertEquals(1, d.getCont());
        assertEquals("A", d.firstStopBus().getNameStop());
        assertNull(d.firstStopBus().getNext());
    }

    // ============================================================
    // CASO 3
    // No hay paradas configuradas al inicio.
    // ============================================================
    @Test
    void caso03_showCurrentStopSinParadas() {
        Distribution d = new Distribution();

        assertEquals("Sin paradas configuradas", d.showCurrentStop());
    }

    // ============================================================
    // CASO 4
    // Al eliminarse una parada que es única, la lista queda vacía.
    // ============================================================
    @Test
    void caso04_ShowCurrentStop_ParadaUnicaEliminada() {
        Distribution d = new Distribution();

        d.insertEnd("Parada Unica");
        d.deleteStop("Parada Unica");

        assertTrue(d.isEmpty());
        assertEquals(0, d.getCont());
        assertNull(d.firstStopBus());
        assertEquals("Sin paradas configuradas", d.showCurrentStop());
    }
}