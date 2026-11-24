const EstadoTurno = require("../src/modelo/EstadoTurno");
const ErrorDominio = require("../src/modelo/ErrorDominio");
const GestorTurnos = require("../src/modelo/GestorTurnos");

describe("GestorTurnos", () => {
    function crearGestorDeterminista(valorAleatorio = 0.5) {
        let contadorFecha = 0;

        return new GestorTurnos({
            generadorAleatorio: () => valorAleatorio,
            proveedorFecha: () => {
                contadorFecha += 1;
                return "2026-11-24T12:00:0" + contadorFecha + ".000Z";
            }
        });
    }

    test(
        "CV-01: respeta disciplina FIFO al asignar en distintas ventanillas",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        const t1 = gestor.registrarTurno();
        const t2 = gestor.registrarTurno();
        const t3 = gestor.registrarTurno();

        const asigV02 = gestor.asignarSiguiente("V02");
        const asigV01 = gestor.asignarSiguiente("V01");
        const fila = gestor.consultarFila();
        const ventanillas = gestor.consultarVentanillas();

        expect(asigV02.turno.id).toBe(t1.id);
        expect(asigV02.turno.estado).toBe(EstadoTurno.EN_ATENCION);
        expect(asigV02.ventanilla.colorIndicador).toBe("rojo");
        expect(asigV01.turno.id).toBe(t2.id);
        expect(fila).toHaveLength(1);
        expect(fila[0].id).toBe(t3.id);
        expect(ventanillas.find((v) => v.id === "V03").colorIndicador)
            .toBe("verde");
    });

    test(
        "RN-02: determina orden FIFO por secuencia y no por texto del ID",
        () => {
        const gestor = crearGestorDeterminista(0.2);

        for (let i = 0; i < 12; i += 1) {
            gestor.registrarTurno();
        }

        const fila = gestor.consultarFila();
        expect(fila[0].secuencia).toBe(1);
        expect(fila[8].secuencia).toBe(9);
        expect(fila[9].secuencia).toBe(10);
        expect(fila[11].secuencia).toBe(12);

        gestor.cargarEstado(
            [
                {
                    id: "Z-001",
                    secuencia: 2,
                    fechaHoraRegistro: "2026-11-24T10:00:05.000Z",
                    estado: EstadoTurno.EN_ESPERA,
                    ventanillaAsignada: null,
                    tiempoAtencionSegundos: null
                },
                {
                    id: "A-999",
                    secuencia: 10,
                    fechaHoraRegistro: "2026-11-24T09:00:00.000Z",
                    estado: EstadoTurno.EN_ESPERA,
                    ventanillaAsignada: null,
                    tiempoAtencionSegundos: null
                }
            ],
            [
                { id: "V01", turnoActual: null },
                { id: "V02", turnoActual: null },
                { id: "V03", turnoActual: null }
            ]
        );

        const asignado = gestor.asignarSiguiente("V01");
        expect(asignado.turno.id).toBe("Z-001");
        expect(asignado.turno.secuencia).toBe(2);
    });

    test(
        "CV-02: rechaza asignar en ventanilla ocupada sin alterar datos",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.asignarSiguiente("V02");

        const antes = gestor.exportarEstado();

        expect(() => gestor.asignarSiguiente("V02")).toThrow(/V02/);
        expect(gestor.exportarEstado()).toEqual(antes);
    });

    test(
        "CV-03: impide cuarta atención con las tres ventanillas ocupadas",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();

        gestor.asignarSiguiente("V01");
        gestor.asignarSiguiente("V02");
        gestor.asignarSiguiente("V03");

        expect(() => gestor.asignarSiguiente("V01")).toThrow(ErrorDominio);
        expect(() => gestor.asignarSiguiente("V02")).toThrow(ErrorDominio);
        expect(() => gestor.asignarSiguiente("V03")).toThrow(ErrorDominio);
        expect(() => gestor.asignarSiguiente("V04")).toThrow(ErrorDominio);
        expect(gestor.consultarFila()).toHaveLength(1);
        expect(gestor.consultarFila()[0].id).toBe("T-004");
    });

    test(
        "CV-04: finaliza atención y permite reasignar en la misma ventanilla",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();

        gestor.asignarSiguiente("V02");
        const fin = gestor.finalizarAtencion("V02");

        expect(fin.turno.estado).toBe(EstadoTurno.ATENDIDO);
        expect(fin.ventanilla.ocupada).toBe(false);
        expect(fin.ventanilla.colorIndicador).toBe("verde");

        const nueva = gestor.asignarSiguiente("V02");
        expect(nueva.turno.id).toBe("T-002");
        expect(nueva.ventanilla.ocupada).toBe(true);
        expect(nueva.ventanilla.colorIndicador).toBe("rojo");
    });

    test(
        "CV-05: cancela turno en espera, omite en FIFO y conserva historial",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();

        const cancelado = gestor.cancelarTurno("t-002");
        expect(cancelado.estado).toBe(EstadoTurno.CANCELADO);

        const fila = gestor.consultarFila();
        expect(fila.map((t) => t.id)).toEqual(["T-001", "T-003"]);

        const asig1 = gestor.asignarSiguiente("V01");
        const asig2 = gestor.asignarSiguiente("V02");
        expect(asig1.turno.id).toBe("T-001");
        expect(asig2.turno.id).toBe("T-003");

        const historial = gestor.consultarHistorial();
        expect(historial).toHaveLength(3);
        expect(historial.find((t) => t.id === "T-002").estado)
            .toBe(EstadoTurno.CANCELADO);
    });

    test(
        "CV-06 y RF-09: rechaza cancelación de estados no válidos e IDs",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();

        gestor.asignarSiguiente("V01");
        gestor.asignarSiguiente("V02");
        gestor.finalizarAtencion("V02");
        gestor.cancelarTurno("T-003");

        const estadoAntes = gestor.exportarEstado();

        expect(() => gestor.cancelarTurno("T-001")).toThrow(/T-001/);
        expect(() => gestor.cancelarTurno("T-002")).toThrow(/T-002/);
        expect(() => gestor.cancelarTurno("T-003")).toThrow(/T-003/);
        expect(() => gestor.cancelarTurno("T-999")).toThrow(/T-999/);
        expect(() => gestor.cancelarTurno("")).toThrow(ErrorDominio);
        expect(() => gestor.cancelarTurno("   ")).toThrow(ErrorDominio);
        expect(() => gestor.cancelarTurno(null)).toThrow(ErrorDominio);
        expect(gestor.exportarEstado()).toEqual(estadoAntes);
    });

    test(
        "RF-05 y RF-07: rechaza asignar fila vacía y finalizar libre",
        () => {
        const gestor = crearGestorDeterminista(0.5);
        const estadoInicial = gestor.exportarEstado();

        expect(() => gestor.asignarSiguiente("V01")).toThrow(/V01/);
        expect(() => gestor.finalizarAtencion("V02")).toThrow(/V02/);
        expect(() => gestor.asignarSiguiente("V99")).toThrow(/V99/);
        expect(() => gestor.finalizarAtencion("")).toThrow(ErrorDominio);
        expect(gestor.exportarEstado()).toEqual(estadoInicial);
    });

    test(
        "asigna tiempo aleatorio entre 10 y 20 segundos incluyendo límites",
        () => {
        const gestorMin = crearGestorDeterminista(0);
        gestorMin.registrarTurno();
        expect(gestorMin.asignarSiguiente("V01").turno.tiempoAtencionSegundos)
            .toBe(10);

        const gestorMax = crearGestorDeterminista(0.999999);
        gestorMax.registrarTurno();
        expect(gestorMax.asignarSiguiente("V01").turno.tiempoAtencionSegundos)
            .toBe(20);

        const gestorReal = new GestorTurnos();

        for (let i = 0; i < 40; i += 1) {
            gestorReal.registrarTurno();
            const asignacion = gestorReal.asignarSiguiente("V01");
            const tiempo = asignacion.turno.tiempoAtencionSegundos;

            expect(tiempo).toBeGreaterThanOrEqual(10);
            expect(tiempo).toBeLessThanOrEqual(20);
            gestorReal.finalizarAtencion("V01");
        }

        expect(GestorTurnos.INTERVALO_LLEGADA_SEGUNDOS).toBe(30);
    });

    test(
        "finaliza turno automáticamente cuando el contador llega a cero",
        () => {
        const gestor = crearGestorDeterminista(0);
        gestor.registrarTurno();
        gestor.asignarSiguiente("V01");

        for (let segundo = 1; segundo < 10; segundo += 1) {
            const avance = gestor.avanzarSegundoVentanillas();
            expect(avance.huboActualizacion).toBe(true);
            expect(avance.finalizados).toHaveLength(0);
        }

        const avanceFinal = gestor.avanzarSegundoVentanillas();
        expect(avanceFinal.finalizados).toHaveLength(1);
        expect(avanceFinal.finalizados[0].turno.estado)
            .toBe(EstadoTurno.ATENDIDO);
        expect(avanceFinal.finalizados[0].ventanilla.colorIndicador)
            .toBe("verde");
    });

    test("CV-10: historial refleja los cuatro estados posibles", () => {
        const gestor = crearGestorDeterminista(0.5);
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();
        gestor.registrarTurno();

        gestor.asignarSiguiente("V01");
        gestor.finalizarAtencion("V01");
        gestor.asignarSiguiente("V02");
        gestor.cancelarTurno("T-003");

        const historial = gestor.consultarHistorial();
        const estados = historial.map((t) => t.estado);

        expect(estados).toEqual([
            EstadoTurno.ATENDIDO,
            EstadoTurno.EN_ATENCION,
            EstadoTurno.CANCELADO,
            EstadoTurno.EN_ESPERA
        ]);
    });

    test(
        "rechaza cargar estados inconsistentes entre turnos y ventanillas",
        () => {
            const gestor = crearGestorDeterminista(0.5);
            const ventanillasBase = [
                {
                    id: "V01",
                    turnoActual: "T-001",
                    tiempoAsignadoSegundos: 12,
                    segundosRestantes: 12
                },
                { id: "V02", turnoActual: null },
                { id: "V03", turnoActual: null }
            ];

        expect(() => gestor.cargarEstado([], ventanillasBase))
            .toThrow(ErrorDominio);

        expect(() => gestor.cargarEstado(
            [
                {
                    id: "T-001",
                    secuencia: 1,
                    fechaHoraRegistro: "2026-11-24T10:00:00.000Z",
                    estado: EstadoTurno.EN_ATENCION,
                    ventanillaAsignada: "V02",
                    tiempoAtencionSegundos: 12
                }
            ],
            ventanillasBase
        )).toThrow(ErrorDominio);
    });
});
