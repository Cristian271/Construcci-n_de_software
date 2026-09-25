package main;

import main.Main;
import main.modulebc.Yard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * pruebas unitarias para "introduce a explaining variable"
 *
 * codigo evaluado: (Main.java - subMenu2):
 *     int p = scanner.nextInt();
 *     if (p < 0 || p >= pilas.length) {
 *         System.out.println("Pila no valida");
 *         continue;
 *     }
 */
public class ManagementSystemSubMenu2Test {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() throws Exception {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Por defecto, inicializamos el arreglo de pilas con 3 elementos (0, 1 y 2)
        configurarPilasEnSistema(3);

    }

    @AfterEach
    void tearDown() throws Exception {
        System.setIn(originalIn);
        System.setOut(originalOut);

        // restaurar el scanner con la entrada estándar original
        Field scannerField = Main.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(null, new Scanner(System.in));
    }

    /**
     * inyecta una cantidad determinada de pilas en el campo pilas.
     */
    private void configurarPilasEnSistema(int cantidad) throws Exception {
        Field pilasField = Main.class.getDeclaredField("pilas");
        pilasField.setAccessible(true);
        Yard[] testPilas = new Yard[cantidad];
        for (int i = 0; i < cantidad; i++) {
            testPilas[i] = new Yard(5);
        }
        pilasField.set(null, testPilas);
    }

    /**
     * simula entradas de usuario en la consola y llama a subMenu2().
     */
    private void ejecutarSubMenu2ConEntrada(String simulatedInput) throws Exception {
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Field scannerField = Main.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(null, new Scanner(System.in));

        Method subMenu2Method = Main.class.getDeclaredMethod("subMenu2");
        subMenu2Method.setAccessible(true);
        subMenu2Method.invoke(null);
    }


    /**
     * prueba 1: (p = -1).
     *
     * evalua la parte izquierda de la condición (p < 0).
     *
     * debe imprimir "Pila no valida" y reiniciar el ciclo sin ejecutar ninguna acción.
     */
    @Test
    @DisplayName("Prueba 1: (p = -1)")
    void test1_BordeInferiorInvalido() throws Exception {
        // Opción 1 (ingresar contenedor), p = -1 (inválido), opción 5 (salir)
        String input = "1\n-1\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Debe mostrar 'Pila no valida' cuando p es -1.");
    }

    /**
     * Prueba 2: (p = 0).
     *
     * la pila 0 debe ser aceptada; NO debe mostrar "Pila no valida".
     */
    @Test
    @DisplayName("Prueba 2: (p = 0)")
    void test2_BordeInferiorValido() throws Exception {
        // Opción 3 (ver tope en pila vacía), p = 0 (válido), opción 5 (salir)
        String input = "3\n0\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertFalse(output.contains("Pila no valida"), "No debe mostrar 'Pila no valida' cuando p es 0.");
    }

    /**
     * prueba 3: (p = 1).
     *
     * se espera acepta la pila sin advertencias de error.
     */
    @Test
    @DisplayName("Prueba 3: (p = 1)")
    void test3_ValorNominalInterior() throws Exception {
        // Opción 3 (ver tope), p = 1 (válido), opción 5 (salir)
        String input = "3\n1\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertFalse(output.contains("Pila no valida"), "No debe mostrar 'Pila no valida' para un índice interior válido (1).");
    }

    /**
     * prueba 4: (p = pilas.length - 1).
     *
     * verifica que el operador > o >= no excluya incorrectamente el último elemento valido.
     * Se espera que la pila es aceptada exitosamente sin imprimir "Pila no valida".
     */
    @Test
    @DisplayName("Prueba 4: (p = pilas.length - 1)")
    void test4_BordeSuperiorValido() throws Exception {
        // Con 3 pilas, el límite superior válido es 2.
        // Opción 3 (ver tope), p = 2 (válido), opción 5 (salir)
        String input = "3\n2\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertFalse(output.contains("Pila no valida"), "No debe mostrar 'Pila no valida' para el límite superior exacto (2).");
    }

    /**
     * prueba 5: (p = pilas.length).
     *
     * evalua la parte derecha de la condición (p >= pilas.length).
     * comportamiento esperado: debe atrapar el error de rango, mostrar "Pila no valida" y continuar el ciclo.
     */
    @Test
    @DisplayName("Prueba 5: (p = pilas.length)")
    void test5_BordeSuperiorInvalido() throws Exception {
        // Con 3 pilas, el índice 3 es el primer valor fuera de rango superior.
        // Opción 1 (ingresar contenedor), p = 3 (inválido), opción 5 (salir)
        String input = "1\n3\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Debe mostrar 'Pila no valida' cuando p == pilas.length (3).");
    }

    /**
     * prueba 6: Valores extremos lejanos (p = -100 y p = 999).
     *
     * evalua la resistencia ante números arbitrariamente grandes o negativos lejanos a los límites.
     * comportamiento esperado: ambos valores son rechazados con "Pila no valida".
     */
    @Test
    @DisplayName("Prueba 6: Valores extremos lejanos (p = -100 y p = 999)")
    void test6_ValoresExtremosLejanos() throws Exception {
        // Intento 1: Opción 1 con p = -100 (rechazado)
        // Intento 2: Opción 1 con p = 999 (rechazado)
        // Salida: Opción 5
        String input = "1\n-100\n1\n999\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Debe rechazar índices extremos fuera de rango.");
    }


    /**
     * prueba 7: caso borde de patio con una sola pila (pilas.length = 1).
     *
     * el limite inferior (0) y el límite superior (pilas.length - 1 = 0) coinciden,
     * si el sistema solo tiene 1 pila, únicamente p = 0 debe ser válido; p = 1 ya está fuera de rango.
     * comportamiento esperado: p = 0 es aceptado, pero p = 1 arroja "Pila no valida".
     */
    @Test
    @DisplayName("Prueba 7: caso borde de patio con 1 sola pila (pilas.length = 1)")
    void test7_PatioConUnaSolaPila() throws Exception {
        configurarPilasEnSistema(1); // 1 sola pila (índice legal únicamente 0)

        // Intento 1: Opción 3 con p = 0 (válido)
        // Intento 2: Opción 1 con p = 1 (inválido porque solo existe la pila 0)
        // Salida: Opción 5
        String input = "3\n0\n1\n1\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Con 1 sola pila, el índice 1 debe ser rechazado como inválido.");
    }

    /**
     * prueba 9: Caso borde de patio vacío (pilas.length = 0).
     *
     * con longitud 0, (p >= 0) siempre será verdadero incluso para p = 0 (0 >= 0).
     * comportamiento esperado: No debe permitir acceder a ninguna pila,
     * p = 0 debe ser rechazado con "Pila no valida".
     */
    @Test
    @DisplayName("Prueba 9: Caso borde de patio vacío (pilas.length = 0)")
    void test9_PatioVacioCeroPilas() throws Exception {
        configurarPilasEnSistema(0); // 0 pilas

        // Intento: Opción 1 con p = 0, luego Opción 5
        String input = "1\n0\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Con 0 pilas disponibles, cualquier índice (incluso 0) debe ser rechazado.");
    }

    /**
     * prueba 10: Opción de salida del menú (Opción 5).
     *
     * cuando se ingresa la opción 5, el bloque condicional de solicitar p NO se ejecuta.
     * comportamiento esperado: Sale inmediatamente del bucle sin solicitar el id de la pila ni evaluar p
     */
    @Test
    @DisplayName("Prueba 10: Opción 5 (Salir) no debe solicitar ID de pila")
    void test10_OpcionSalirNoPidePila() throws Exception {
        String input = "5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertFalse(output.contains("ID de Pila"), "Al presionar 5 (salir), no debe solicitar ningún ID de Pila.");
        assertFalse(output.contains("Pila no valida"), "No debe emitir mensajes de validación al salir.");
    }

    /**
     * prueba 11: opciones numéricas fuera de rango del menú (Opción 6).
     *
     * opciones que no correspondan a operaciones con pilas (1 a 4) no ejecutan la lectura de p,
     * evita pedir un ID de pila si la opción elegida no existe.
     */
    @Test
    @DisplayName("Prueba 11: Opción fuera de rango (Opción 6) no debe solicitar ID de pila")
    void test11_OpcionFueraDeRangoNoPidePila() throws Exception {
        // Opción 6 (fuera de 1..4), seguida de opción 5 (salir)
        String input = "6\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertFalse(output.contains("ID de Pila"), "Opciones fuera del rango 1..4 no deben solicitar ID de Pila.");
    }

    /**
     * prueba 12: Recuperación tras fallo (p inválido seguido de p válido).
     *
     * tras ingresar un indice inválido, el sistema no debe congelarse ni abortar,
     * debe volver al menú y permitir una operación válida en el siguiente intento.
     *
     * comportamiento esperado: Muestra "Pila no valida" en el primer intento,
     * y procesa el segundo intento válido sin caerse.
     */
    @Test
    @DisplayName("Prueba 12: Recuperación tras fallo (p inválido seguido de p válido)")
    void test13_RecuperacionTrasError() throws Exception {
        // Intento 1: Opción 1 con p = -1 (falla con "Pila no valida")
        // Intento 2: Opción 3 con p = 0 (éxito, opera normalmente)
        // Salida: Opción 5
        String input = "1\n-1\n3\n0\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Debe mostrar el mensaje de error para el intento fallido.");
        assertTrue(output.contains("ID de Pila"), "Debe volver a solicitar el ID de pila en el segundo intento.");
    }

    /**
     * PRUEBA 13: Múltiples fallos consecutivos en la selección de pila.
     *
     * si el usuario se equivoca varias veces consecutivas (-5, 10, 99), el sistema debe
     * capturar cada error independientemente y mantenerse en ejecución hasta la opción 5.
     */
    @Test
    @DisplayName("Prueba 13: Múltiples fallos consecutivos en seleccion de pila")
    void test14_MultiplesFallosConsecutivos() throws Exception {
        // Intento 1: Opción 1, p = -5 (error)
        // Intento 2: Opción 1, p = 10 (error)
        // Intento 3: Opción 1, p = 99 (error)
        // Salida: Opción 5
        String input = "1\n-5\n1\n10\n1\n99\n5\n";
        ejecutarSubMenu2ConEntrada(input);

        String output = outContent.toString();
        assertTrue(output.contains("Pila no valida"), "Debe rechazar todos los intentos erróneos.");
    }
}
