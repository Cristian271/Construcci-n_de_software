const EstadoTurno = require("../src/modelo/EstadoTurno");
const ErrorDominio = require("../src/modelo/ErrorDominio");
const Turno = require("../src/modelo/Turno");

describe("Turno", () => {
    const fechaBase = "2026-11-24T10:00:00.000Z";

    test("crea un turno válido en estado En espera por defecto", () => {
        const turno = new Turno("T-001", 1, fechaBase);

        expect(turno.obtenerId()).toBe("T-001");
        expect(turno.obtenerSecuencia()).toBe(1);
        expect(turno.obtenerFechaHoraRegistro()).toBe(fechaBase);
        expect(turno.obtenerEstado()).toBe(EstadoTurno.EN_ESPERA);
        expect(turno.obtenerVentanillaAsignada()).toBeNull();
        expect(turno.obtenerTiempoAtencionSegundos()).toBeNull();
        expect(turno.estaEnEspera()).toBe(true);
        expect(turno.estaEnAtencion()).toBe(false);
    });

    test("construye identificadores consecutivos correctamente", () => {
        expect(Turno.construirId(1)).toBe("T-001");
        expect(Turno.construirId(10)).toBe("T-010");
        expect(Turno.construirId(100)).toBe("T-100");
        expect(Turno.construirId(1000)).toBe("T-1000");
    });

    test(
        "acepta tiempos de atención en los límites de 10 y 20 segundos",
        () => {
        const turnoMin = new Turno("T-001", 1, fechaBase);
        const turnoMax = new Turno("T-002", 2, fechaBase);

        turnoMin.iniciarAtencion("V01", 10);
        turnoMax.iniciarAtencion("V02", 20);

        expect(turnoMin.obtenerTiempoAtencionSegundos()).toBe(10);
        expect(turnoMax.obtenerTiempoAtencionSegundos()).toBe(20);
        expect(turnoMin.obtenerEstado()).toBe(EstadoTurno.EN_ATENCION);
        expect(turnoMax.obtenerEstado()).toBe(EstadoTurno.EN_ATENCION);
    });

    test("permite transiciones válidas hacia Atendido y Cancelado", () => {
        const turnoAtendido = new Turno("T-001", 1, fechaBase);
        const turnoCancelado = new Turno("T-002", 2, fechaBase);

        turnoAtendido.iniciarAtencion("V01", 15);
        turnoAtendido.marcarAtendido();
        turnoCancelado.marcarCancelado();

        expect(turnoAtendido.obtenerEstado()).toBe(EstadoTurno.ATENDIDO);
        expect(turnoAtendido.obtenerVentanillaAsignada()).toBe("V01");
        expect(turnoCancelado.obtenerEstado()).toBe(EstadoTurno.CANCELADO);
        expect(turnoCancelado.obtenerVentanillaAsignada()).toBeNull();
    });

    test("protege el encapsulamiento al devolver copia en aObjeto", () => {
        const turno = new Turno("T-001", 1, fechaBase);
        const copia = turno.aObjeto();

        copia.estado = EstadoTurno.CANCELADO;
        copia.ventanillaAsignada = "V03";

        expect(turno.obtenerEstado()).toBe(EstadoTurno.EN_ESPERA);
        expect(turno.obtenerVentanillaAsignada()).toBeNull();
    });

    test("rechaza datos inválidos en el constructor", () => {
        expect(() => new Turno("", 1, fechaBase)).toThrow(ErrorDominio);
        expect(() => new Turno("T-001", 0, fechaBase)).toThrow(ErrorDominio);
        expect(() => new Turno("T-001", -3, fechaBase)).toThrow(ErrorDominio);
        expect(() => new Turno("T-001", 1.5, fechaBase)).toThrow(ErrorDominio);
        expect(() => new Turno("T-001", 1, "")).toThrow(ErrorDominio);
        expect(() => new Turno("T-001", 1, fechaBase, "Desconocido"))
            .toThrow(ErrorDominio);
        expect(() => new Turno(
            "T-001",
            1,
            fechaBase,
            EstadoTurno.EN_ESPERA,
            "V01"
        )).toThrow(ErrorDominio);
        expect(() => new Turno(
            "T-001",
            1,
            fechaBase,
            EstadoTurno.EN_ATENCION,
            null
        )).toThrow(ErrorDominio);
        expect(() => Turno.construirId(0)).toThrow(ErrorDominio);
    });

    test("rechaza tiempos fuera del rango 10 a 20 segundos", () => {
        const turno = new Turno("T-001", 1, fechaBase);

        expect(() => turno.iniciarAtencion("V01", 9)).toThrow(ErrorDominio);
        expect(() => turno.iniciarAtencion("V01", 21)).toThrow(ErrorDominio);
        expect(() => turno.iniciarAtencion("V01", 14.5)).toThrow(ErrorDominio);
        expect(turno.obtenerEstado()).toBe(EstadoTurno.EN_ESPERA);
    });

    test("rechaza transiciones de estado prohibidas", () => {
        const turnoEspera = new Turno("T-001", 1, fechaBase);
        expect(() => turnoEspera.marcarAtendido()).toThrow(ErrorDominio);

        turnoEspera.iniciarAtencion("V01", 12);
        expect(() => turnoEspera.marcarCancelado()).toThrow(ErrorDominio);
        expect(() => turnoEspera.iniciarAtencion("V02", 12))
            .toThrow(ErrorDominio);

        turnoEspera.marcarAtendido();
        expect(() => turnoEspera.marcarCancelado()).toThrow(ErrorDominio);
        expect(() => turnoEspera.marcarAtendido()).toThrow(ErrorDominio);
        expect(() => turnoEspera.iniciarAtencion("V03", 12))
            .toThrow(ErrorDominio);

        const turnoCancelado = new Turno("T-002", 2, fechaBase);
        turnoCancelado.marcarCancelado();
        expect(() => turnoCancelado.marcarCancelado()).toThrow(ErrorDominio);
        expect(() => turnoCancelado.marcarAtendido()).toThrow(ErrorDominio);
        expect(() => turnoCancelado.iniciarAtencion("V01", 12))
            .toThrow(ErrorDominio);
    });
});
