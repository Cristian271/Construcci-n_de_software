package main.modulebc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class ContainerContentTest {

    private ContainerContent container;
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        container = new ContainerContent();
        // Silenciar los System.out.println del método addProduct durante los tests
        System.setOut(new PrintStream(new java.io.ByteArrayOutputStream()));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * Simula la entrada de teclado que espera addProduct():
     * una línea con el nombre y una línea/valor con el peso.
     */
    private void simulateInput(String name, float weight) {
        String simulatedInput = name + System.lineSeparator() + weight + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
    }


    @Test
    @DisplayName("calculateWeight suma correctamente varios productos")
    void calculateWeight_multipleProducts_sumsWeights() {
        simulateInput("Manzana", 1.5f);
        container.addProduct();
        simulateInput("Pera", 2.0f);
        container.addProduct();
        simulateInput("Uva", 0.5f);
        container.addProduct();

        assertEquals(4.0f, container.calculateWeight(), 0.0001);
    }

}