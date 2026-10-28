package vivero.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteTelefonoTest {

    @Test
    public void constructor_AsignaDatosTelefonicosCorrectamente() {
        // 1. Preparación y Ejecución
        // Se omiten direccion y membresia (null) porque esta prueba se enfoca solo en teléfonos
        Cliente cliente = new Cliente("C-01", "Juan Pérez", null, null, "52", "9991234567", "101");

        // 2. Verificación de los getters
        assertEquals("52", cliente.getTelefonoCodigoPais(), "El código de país debe coincidir con el asignado");
        assertEquals("9991234567", cliente.getTelefonoNumero(), "El número debe coincidir con el asignado");
        assertEquals("101", cliente.getTelefonoExtension(), "La extensión debe coincidir con la asignada");
    }

    @Test
    public void getTelefonoCompleto_FormateaCadenaCorrectamente() {
        // 1. Preparación
        Cliente cliente = new Cliente("C-02", "María López", null, null, "1", "5559876543", "42");

        // 2. Ejecución
        String telefonoCompleto = cliente.getTelefonoCompleto();

        // 3. Verificación
        // El formato esperado según el código actual es: "+{codigo} ({numero}) ext. {extension}"
        assertEquals("+1 (5559876543) ext. 42", telefonoCompleto, "El formato del teléfono completo no es el esperado");
    }

    @Test
    public void setters_ModificanLosDatosTelefonicosCorrectamente() {
        // 1. Preparación
        Cliente cliente = new Cliente("C-03", "Carlos", null, null, "52", "0000000000", "0");

        // 2. Ejecución: Modificar los valores
        cliente.setTelefonoCodigoPais("34");
        cliente.setTelefonoNumero("600123456");
        cliente.setTelefonoExtension("99");

        // 3. Verificación de que los setters alteraron el estado y el formato final
        assertEquals("34", cliente.getTelefonoCodigoPais());
        assertEquals("600123456", cliente.getTelefonoNumero());
        assertEquals("99", cliente.getTelefonoExtension());
        assertEquals("+34 (600123456) ext. 99", cliente.getTelefonoCompleto(), "El formato debe reflejar los nuevos datos modificados por los setters");
    }
}
