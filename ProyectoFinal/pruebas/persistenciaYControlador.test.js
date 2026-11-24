const fs = require("fs");
const os = require("os");
const path = require("path");
const EstadoTurno = require("../src/modelo/EstadoTurno");
const ErrorPersistencia = require("../src/modelo/ErrorPersistencia");
const GestorTurnos = require("../src/modelo/GestorTurnos");
const RepositorioArchivos = require(
    "../src/modelo/persistencia/RepositorioArchivos"
);
const ControladorTurnos = require("../src/controlador/ControladorTurnos");

describe("Persistencia en archivos y ControladorTurnos", () => {
    let directorioTemporal = "";

    beforeEach(() => {
        directorioTemporal = fs.mkdtempSync(
            path.join(os.tmpdir(), "sgt-pruebas-")
        );
    });

    afterEach(() => {
        fs.rmSync(directorioTemporal, { recursive: true, force: true });
    });

    test(
        "guarda entidades en archivos turnos.json y ventanillas.json",
        () => {
        const gestor = new GestorTurnos({ generadorAleatorio: () => 0.3 });
        const repo = new RepositorioArchivos(directorioTemporal);
        const controlador = new ControladorTurnos(gestor, repo);

        controlador.inicializar();
        const resRegistro = controlador.registrarTurno();

        expect(resRegistro.exito).toBe(true);
        expect(fs.existsSync(path.join(directorioTemporal, "turnos.json")))
            .toBe(true);
        expect(fs.existsSync(path.join(directorioTemporal, "ventanillas.json")))
            .toBe(true);
    });

    test(
        "CV-07 y CV-08: recupera estados tras reinicio sin reutilizar IDs",
        () => {
        const repo1 = new RepositorioArchivos(directorioTemporal);
        const gestor1 = new GestorTurnos({ generadorAleatorio: () => 0.4 });
        const ctrl1 = new ControladorTurnos(gestor1, repo1);

        ctrl1.inicializar();
        ctrl1.registrarTurno();
        ctrl1.registrarTurno();
        ctrl1.registrarTurno();
        ctrl1.registrarTurno();
        ctrl1.asignarSiguiente("V01");
        ctrl1.asignarSiguiente("V02");
        ctrl1.finalizarAtencion("V02");
        ctrl1.cancelarTurno("T-003");
        ctrl1.guardarAlCerrar();

        const repo2 = new RepositorioArchivos(directorioTemporal);
        const gestor2 = new GestorTurnos({ generadorAleatorio: () => 0.4 });
        const ctrl2 = new ControladorTurnos(gestor2, repo2);
        const inicio = ctrl2.inicializar();

        expect(inicio.exito).toBe(true);
        expect(inicio.estado.fila.map((t) => t.id)).toEqual(["T-004"]);
        expect(inicio.estado.ventanillas[0].turnoActual).toBe("T-001");
        expect(inicio.estado.ventanillas[0].colorIndicador).toBe("rojo");
        expect(inicio.estado.ventanillas[1].ocupada).toBe(false);
        expect(inicio.estado.historial).toHaveLength(4);

        const nuevo = ctrl2.registrarTurno();
        expect(nuevo.exito).toBe(true);
        expect(nuevo.datos.id).toBe("T-005");
        expect(nuevo.datos.secuencia).toBe(5);
    });

    test(
        "CV-09 y RD-02: revierte memoria y disco si falla el guardado",
        () => {
        let provocarFallo = false;
        const repo = new RepositorioArchivos(directorioTemporal, {
            ganchoEscritura: (nombreArchivo) => {
                if (
                    provocarFallo
                        && nombreArchivo
                            === RepositorioArchivos.ARCHIVO_VENTANILLAS
                ) {
                    throw new Error("Fallo simulado de escritura en disco");
                }
            }
        });
        const gestor = new GestorTurnos({ generadorAleatorio: () => 0.5 });
        const ctrl = new ControladorTurnos(gestor, repo);

        ctrl.inicializar();
        ctrl.registrarTurno();
        ctrl.registrarTurno();

        const turnosAntes = fs.readFileSync(repo.obtenerRutaTurnos(), "utf8");
        const ventanillasAntes = fs.readFileSync(
            repo.obtenerRutaVentanillas(),
            "utf8"
        );

        provocarFallo = true;
        const respuesta = ctrl.asignarSiguiente("V01");

        expect(respuesta.exito).toBe(false);
        expect(respuesta.estado.ventanillas[0].ocupada).toBe(false);
        expect(respuesta.estado.fila.map((t) => t.id))
            .toEqual(["T-001", "T-002"]);

        const turnosDespues = fs.readFileSync(repo.obtenerRutaTurnos(), "utf8");
        const ventanillasDespues = fs.readFileSync(
            repo.obtenerRutaVentanillas(),
            "utf8"
        );

        expect(turnosDespues).toBe(turnosAntes);
        expect(ventanillasDespues).toBe(ventanillasAntes);

        const repoVerificacion = new RepositorioArchivos(directorioTemporal);
        const gestorVerificacion = new GestorTurnos();
        const ctrlVerificacion = new ControladorTurnos(
            gestorVerificacion,
            repoVerificacion
        );
        const recarga = ctrlVerificacion.inicializar();

        expect(recarga.exito).toBe(true);
        expect(recarga.estado.ventanillas[0].ocupada).toBe(false);
        expect(recarga.estado.fila).toHaveLength(2);
    });

    test(
        "RD-03: informa error sin sobrescribir archivos dañados al iniciar",
        () => {
        const rutaTurnos = path.join(directorioTemporal, "turnos.json");
        const rutaVentanillas = path.join(
            directorioTemporal,
            "ventanillas.json"
        );
        const contenidoDanado = "{ json roto !!!";

        fs.writeFileSync(rutaTurnos, contenidoDanado, "utf8");
        fs.writeFileSync(rutaVentanillas, "[]", "utf8");

        const repo = new RepositorioArchivos(directorioTemporal);
        const gestor = new GestorTurnos();
        const ctrl = new ControladorTurnos(gestor, repo);

        const inicio = ctrl.inicializar();
        expect(inicio.exito).toBe(false);
        expect(inicio.estado.bloqueadoPorErrorCarga).toBe(true);

        const intento = ctrl.registrarTurno();
        expect(intento.exito).toBe(false);
        expect(ctrl.guardarAlCerrar()).toBe(false);
        expect(fs.readFileSync(rutaTurnos, "utf8")).toBe(contenidoDanado);
    });

    test(
        "RD-03: detecta ausencia de uno de los dos archivos de entidades",
        () => {
        const rutaTurnos = path.join(directorioTemporal, "turnos.json");
        fs.writeFileSync(rutaTurnos, "[]", "utf8");

        const repo = new RepositorioArchivos(directorioTemporal);
        expect(() => repo.cargarTodo()).toThrow(ErrorPersistencia);
    });

    test(
        "RNF-P01: responde en menos de 2 segundos con 200 turnos guardados",
        () => {
        const repo = new RepositorioArchivos(directorioTemporal);
        const gestor = new GestorTurnos({ generadorAleatorio: () => 0.5 });
        const ctrl = new ControladorTurnos(gestor, repo);

        ctrl.inicializar();

        for (let i = 0; i < 200; i += 1) {
            gestor.registrarTurno();
        }

        const estadoInicial = gestor.exportarEstado();
        repo.guardarTodo(estadoInicial.turnos, estadoInicial.ventanillas);

        const inicio = Date.now();
        const reg = ctrl.registrarTurno();
        const fila = ctrl.obtenerEstadoSistema().fila;
        const asig = ctrl.asignarSiguiente("V01");
        const fin = ctrl.finalizarAtencion("V01");
        const duracionMs = Date.now() - inicio;

        expect(reg.exito).toBe(true);
        expect(fila).toHaveLength(201);
        expect(asig.exito).toBe(true);
        expect(fin.exito).toBe(true);
        expect(fin.datos.turno.estado).toBe(EstadoTurno.ATENDIDO);
        expect(duracionMs).toBeLessThan(2000);
    });
});
