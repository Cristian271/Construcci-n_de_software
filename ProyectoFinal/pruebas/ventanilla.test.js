const ErrorDominio = require("../src/modelo/ErrorDominio");
const Ventanilla = require("../src/modelo/Ventanilla");

describe("Ventanilla", () => {
    test("inicia libre con indicador verde para V01, V02 y V03", () => {
        for (const id of Ventanilla.IDS_PREDEFINIDOS) {
            const ventanilla = new Ventanilla(id);

            expect(ventanilla.obtenerId()).toBe(id);
            expect(ventanilla.estaLibre()).toBe(true);
            expect(ventanilla.estaOcupada()).toBe(false);
            expect(ventanilla.obtenerTurnoActual()).toBeNull();
            expect(ventanilla.obtenerColorIndicador()).toBe("verde");
            expect(ventanilla.obtenerTextoEstado()).toBe("Libre");
        }
    });

    test(
        "cambia indicador a rojo al ocupar y regresa a verde al liberar",
        () => {
        const ventanilla = new Ventanilla("V01");

        ventanilla.ocupar("T-001", 15);
        expect(ventanilla.estaOcupada()).toBe(true);
        expect(ventanilla.obtenerTurnoActual()).toBe("T-001");
        expect(ventanilla.obtenerColorIndicador()).toBe("rojo");
        expect(ventanilla.obtenerTextoEstado()).toBe("Ocupada");
        expect(ventanilla.obtenerTiempoAsignadoSegundos()).toBe(15);
        expect(ventanilla.obtenerSegundosRestantes()).toBe(15);

        const turnoLiberado = ventanilla.liberar();
        expect(turnoLiberado).toBe("T-001");
        expect(ventanilla.estaLibre()).toBe(true);
        expect(ventanilla.obtenerColorIndicador()).toBe("verde");
        expect(ventanilla.obtenerTextoEstado()).toBe("Libre");
        expect(ventanilla.obtenerTurnoActual()).toBeNull();
    });

    test("descuenta segundos hasta cero sin quedar en negativo", () => {
        const ventanilla = new Ventanilla("V02", "T-005", 10, 2);

        expect(ventanilla.descontarSegundo()).toBe(1);
        expect(ventanilla.descontarSegundo()).toBe(0);
        expect(ventanilla.descontarSegundo()).toBe(0);

        const libre = new Ventanilla("V03");
        expect(libre.descontarSegundo()).toBe(0);
    });

    test("acepta tiempos en límites de 10 y 20 segundos", () => {
        const v1 = new Ventanilla("V01");
        const v2 = new Ventanilla("V02");

        v1.ocupar("T-001", 10);
        v2.ocupar("T-002", 20);

        expect(v1.obtenerSegundosRestantes()).toBe(10);
        expect(v2.obtenerSegundosRestantes()).toBe(20);
    });

    test(
        "rechaza identificadores no predefinidos y operaciones inválidas",
        () => {
        expect(() => new Ventanilla("V04")).toThrow(ErrorDominio);
        expect(() => new Ventanilla("V00")).toThrow(ErrorDominio);
        expect(() => new Ventanilla("")).toThrow(ErrorDominio);
        expect(() => new Ventanilla(null)).toThrow(ErrorDominio);

        const ventanilla = new Ventanilla("V01");
        expect(() => ventanilla.liberar()).toThrow(ErrorDominio);
        expect(() => ventanilla.ocupar("", 15)).toThrow(ErrorDominio);
        expect(() => ventanilla.ocupar("T-001", 9)).toThrow(ErrorDominio);
        expect(() => ventanilla.ocupar("T-001", 21)).toThrow(ErrorDominio);

        ventanilla.ocupar("T-001", 12);
        expect(() => ventanilla.ocupar("T-002", 14)).toThrow(ErrorDominio);
        expect(ventanilla.obtenerTurnoActual()).toBe("T-001");
    });
});
