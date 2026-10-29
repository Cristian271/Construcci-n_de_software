package vivero;

import vivero.modelo.*;
import vivero.servicio.ViveroServicio;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ViveroServicio servicio = new ViveroServicio();

        // =========================================================================
        // 1. CATÁLOGO ESTÁTICO DE 10 PLANTAS
        // =========================================================================
        Planta p1 = new PlantaInterior("P-01", "Monstera Deliciosa", 280.0, 15, "Monstera deliciosa", 75);
        Planta p2 = new PlantaInterior("P-02", "Pothos Dorado", 95.0, 25, "Epipremnum aureum", 50);
        Planta p3 = new PlantaInterior("P-03", "Sansevieria", 150.0, 20, "Dracaena trifasciata", 30);
        Planta p4 = new PlantaInterior("P-04", "Calathea Orbifolia", 220.0, 10, "Calathea orbifolia", 80);
        Planta p5 = new PlantaInterior("P-05", "Ficus Lyrata", 350.0, 8, "Ficus lyrata", 60);

        Planta p6 = new PlantaExterior("P-06", "Bugambilia", 110.0, 30, "Bougainvillea spectabilis", true);
        Planta p7 = new PlantaExterior("P-07", "Lavanda Francesa", 85.0, 18, "Lavandula dentata", true);
        Planta p8 = new PlantaExterior("P-08", "Cactus Órgano", 190.0, 12, "Pachycereus marginatus", true);
        Planta p9 = new PlantaExterior("P-09", "Helecho Espada", 130.0, 14, "Nephrolepis exaltata", false);
        Planta p10 = new PlantaExterior("P-10", "Rosal Miniatura", 140.0, 16, "Rosa chinensis", false);

        Planta[] catalogo = { p1, p2, p3, p4, p5, p6, p7, p8, p9, p10 };
        for (Planta p : catalogo) {
            servicio.registrarPlanta(p);
        }

        boolean ejecutando = true;
        int contadorVentas = 1000;

        while (ejecutando) {
            System.out.println("\n=================================================");
            System.out.println("        SISTEMA DE VIVERO 'MÉRIDA VERDE'        ");
            System.out.println("=================================================");
            System.out.println("1. Registrar nueva venta");
            System.out.println("2. Consultar stock de planta (Búsqueda)");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            String entradaOpcion = scanner.nextLine().trim();

            switch (entradaOpcion) {
                case "1":
                    contadorVentas++;
                    procesarFlujoVenta(scanner, servicio, catalogo, "V-" + contadorVentas);
                    break;

                case "2":
                    // =========================================================================
                    // CONSULTA DE INVENTARIO (LLAMA A MÉTODO CON CONTROL FLAG)
                    // =========================================================================
                    System.out.println("\n--- CONSULTA DE STOCK EN INVENTARIO ---");
                    System.out.print("Ingresa el nombre común de la planta a consultar: ");
                    String nombreBusqueda = scanner.nextLine().trim();

                    // SMELL: Remove Control Flag (PDF diapositivas 115-116)
                    // Se ejecuta la búsqueda interna basada en bandera booleana
                    Planta buscada = servicio.buscarPlantaPorNombre(nombreBusqueda);
                    if (buscada != null) {
                        System.out.println("\n[✓] Planta encontrada:");
                        System.out.println("    - Nombre: " + buscada.getNombreComun());
                        System.out.println("    - Ambiente: " + buscada.getTipoAmbiente());
                        // SMELL: Encapsulate Field (PDF diapositivas 83-84)
                        System.out.printf("    - Precio base: $%.2f\n", buscada.getPrecioBase());
                        System.out.println("    - Stock disponible: " + buscada.getStock() + " unidades");
                    } else {
                        System.out.println("\n[X] No se encontró ninguna planta con el nombre: '" + nombreBusqueda + "'.");
                    }
                    break;

                case "3":
                    System.out.println("\nGracias por utilizar el sistema de vivero. ¡Hasta pronto!");
                    ejecutando = false;
                    break;

                default:
                    System.out.println("(!) Opción inválida. Elige 1, 2 o 3.");
                    break;
            }
        }

        scanner.close();
    }

    private static void procesarFlujoVenta(Scanner scanner, ViveroServicio servicio, Planta[] catalogo, String idVenta) {
        System.out.println("\n--- REGISTRO DE DATOS DEL CLIENTE ---");
        System.out.print("Nombre completo: ");
        String nombreCliente = scanner.nextLine();

        System.out.print("Calle: ");
        String calle = scanner.nextLine();

        System.out.print("Número: ");
        String numero = scanner.nextLine();

        System.out.print("Ciudad: ");
        String ciudad = scanner.nextLine();

        System.out.print("Código Postal: ");
        String cp = scanner.nextLine();

        DireccionEnvio direccion = new DireccionEnvio(calle, numero, ciudad, cp);

        System.out.print("¿Cuenta con membresía Platino? (s/n): ");
        boolean tieneMembresia = scanner.nextLine().trim().equalsIgnoreCase("s");
        Membresia membresia = tieneMembresia ? new Membresia("PLATINO", 0.15) : null;

        // Smell: Extract Class (datos telefónicos fragmentados)
        Cliente cliente = new Cliente("C-" + System.currentTimeMillis() % 1000,
                nombreCliente, direccion, membresia, "52", "9991234567", "101");

        // SMELL: Encapsulate Collection (PDF diapositivas 85-88)
        System.out.println("Compras previas del cliente en historial: " + cliente.getComprasPrevias().size());

        Venta venta = new Venta(idVenta, cliente, LocalDate.now());
        boolean seguirComprando = true;
        double subtotalAcumulado = 0.0;
        int totalUnidades = 0;

        while (seguirComprando) {
            System.out.println("\n-------------------------------------------------");
            System.out.println("               CATÁLOGO DE PLANTAS               ");
            System.out.println("-------------------------------------------------");
            for (int i = 0; i < catalogo.length; i++) {
                // SMELL: Encapsulate Field (PDF diapositivas 83-84)
                System.out.printf("[%d] %-20s | Tipo: %-8s | Precio: $%.2f | Stock: %d\n",
                        (i + 1),
                        catalogo[i].getNombreComun(),
                        catalogo[i].getTipoAmbiente(),
                        catalogo[i].getPrecioBase(),
                        catalogo[i].getStock());
            }
            System.out.println("[0] Proceder al cobro de las plantas seleccionadas");
            System.out.print("Selecciona el número de planta: ");

            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("(!) Entrada inválida.");
                continue;
            }

            if (opcion == 0) {
                break;
            }

            if (opcion >= 1 && opcion <= catalogo.length) {
                Planta plantaElegida = catalogo[opcion - 1];

                System.out.print("Cantidad deseada: ");
                int cantidad;
                try {
                    cantidad = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("(!) Cantidad inválida.");
                    continue;
                }

                if (cantidad > 0 && cantidad <= plantaElegida.getStock()) {
                    System.out.print("¿Desea incluir fertilizante especial (+$45.00)? (s/n): ");
                    boolean fertilizante = scanner.nextLine().trim().equalsIgnoreCase("s");

                    // SMELL: Remove Parameter (PDF diapositivas 133-134)
                    double precioUnitarioCalculado = plantaElegida.calcularPrecioFinal(fertilizante);

                    // SMELL: Replace Temp with Query (PDF diapositivas 16-17)
                    double subtotalItem = venta.calcularDescuentoTotal(cantidad, precioUnitarioCalculado);

                    // SMELL: Replace Array with Object (PDF diapositivas 71-72)
                    venta.agregarLinea(plantaElegida.getNombreComun(), cantidad, precioUnitarioCalculado);

                    // SMELL: Separate Query from Modifier (PDF diapositivas 135-136)
                    servicio.obtenerPlantaYDescontarStock(plantaElegida.getId(), cantidad);

                    // SMELL: Move Method (PDF diapositivas 34-36)
                    double litrosRiego = venta.calcularCuidadoRiego(plantaElegida, 7);
                    System.out.printf("-> Agregada. Cuidado sugerido: %.2f L de agua por semana.\n", litrosRiego);

                    subtotalAcumulado += subtotalItem;
                    totalUnidades += cantidad;
                } else {
                    System.out.println("(!) Cantidad no válida o stock insuficiente (Disponible: " + plantaElegida.getStock() + ").");
                }
            } else {
                System.out.println("(!) Opción fuera de rango.");
            }

            System.out.print("\n¿Deseas agregar otra planta al carrito? (s/n): ");
            seguirComprando = scanner.nextLine().trim().equalsIgnoreCase("s");
        }

        if (totalUnidades == 0) {
            System.out.println("\n[!] No se agregaron plantas al carrito. Venta cancelada.");
            return;
        }

        // =========================================================================
        // RESUMEN Y CÁLCULOS DE LA VENTA
        // =========================================================================
        System.out.println("\n=================================================");
        System.out.println("               RESUMEN DE VENTA                  ");
        System.out.println("=================================================");

        // SMELL: Hide Delegate (PDF diapositivas 43-45)
        System.out.println("Ciudad destino: " + servicio.obtenerCiudadDestino(venta));

        // SMELL: Consolidate Duplicate Conditional Fragments (PDF diapositivas 113-114)
        double totalMayoreo = venta.aplicarDescuentoMayoreo(subtotalAcumulado, totalUnidades);
        System.out.printf("Subtotal con mayoreo: $%.2f\n", totalMayoreo);

        // SMELL: Introduce Null Object (PDF diapositivas 121-123)
        double totalConMembresia = servicio.calcularDescuentoCliente(cliente, totalMayoreo);
        System.out.printf("Total tras descuento de lealtad: $%.2f\n", totalConMembresia);

        // SMELL: Remove Assignments to Parameters (PDF diapositivas 22-23)
        System.out.print("\nIngrese cupón de descuento (o presione ENTER si no tiene): ");
        String cupon = scanner.nextLine().trim();
        double totalConCupon = venta.aplicarCupon(totalConMembresia, cupon);

        // SMELL: Split Temporary Variable (PDF diapositivas 20-21)
        double costoFlete = venta.calcularEnvio(30.0, 30.0, 40.0, 15.0);

        // SMELL: Decompose Conditional (PDF diapositivas 109-110)
        double recargoClima = venta.calcularRecargoClima(costoFlete, venta.getFecha());

        // SMELL: Rename Method (PDF diapositivas 128-130)
        double envioFinal = venta.aplicarRecargoEnvioExpress(recargoClima); //RF-18
        System.out.printf("Gastos de logística y envío: $%.2f\n", envioFinal);

        // SMELL: Replace Magic Number with Symbolic Constant (PDF diapositivas 81-82)
        double totalPagar = venta.calcularTotal(totalConCupon) + envioFinal;
        System.out.printf("\n>>> TOTAL A PAGAR (CON IVA Y RECARGOS): $%.2f <<<\n", totalPagar);

        // =========================================================================
        // PROCESAMIENTO DE PAGO CON REINTENTO
        // =========================================================================
        boolean cobrado = false;
        while (!cobrado) {
            System.out.printf("\nTotal a liquidar: $%.2f\n", totalPagar);
            System.out.print("Ingrese el monto con el que desea pagar (o 0 para cancelar la compra): $");

            double efectivo;
            try {
                efectivo = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("(!) Por favor ingrese una cantidad numérica válida.");
                continue;
            }

            if (efectivo == 0.0) {
                System.out.println("\n[!] Pago cancelado por el usuario. La venta no se registró.");
                break;
            }

            // SMELL: Replace Nested Conditional with Guard Clauses (PDF diapositivas 117-118)
            cobrado = venta.procesarPago(efectivo, totalPagar);

            if (cobrado) {
                double cambio = efectivo - totalPagar;
                System.out.println("\n[✓] Pago procesado con éxito.");
                System.out.printf("[✓] Su cambio: $%.2f\n", cambio);
                servicio.registrarVenta(venta);
                // Inserción directa en colección mutable (Smell)
                cliente.getComprasPrevias().add(venta);
            } else {
                System.out.printf("\n[X] Monto insuficiente. Faltan: $%.2f. Intente nuevamente.\n", (totalPagar - efectivo));
            }
        }
    }
}