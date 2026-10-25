package vivero.modelo;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClienteTest {

    @Test
    public void comprasPrevias_AntesDeRefactorizar_PermiteModificacionDirecta() {
        DireccionEnvio direccion = new DireccionEnvio("Calle 1", "123", "Mérida", "97000");
        Cliente cliente = new Cliente("C-01", "Ana Pérez", direccion, null, "52", "9991234567", "101");
        Venta venta = new Venta("V-01", cliente, LocalDate.now());

        cliente.getComprasPrevias().add(venta);

        assertEquals(1, cliente.getComprasPrevias().size(), "La lista debe contener una venta");
        assertTrue(cliente.getComprasPrevias().contains(venta), "La lista debe contener la venta específica agregada");
    }
}
