const path = require("path");
const { app, BrowserWindow, ipcMain } = require("electron");
const GestorTurnos = require("./src/modelo/GestorTurnos");
const RepositorioArchivos = require(
    "./src/modelo/persistencia/RepositorioArchivos"
);
const ControladorTurnos = require("./src/controlador/ControladorTurnos");

let controlador = null;
let resultadoInicio = null;

function crearControlador() {
    const directorioDatos = path.join(app.getPath("userData"), "datos");
    const gestor = new GestorTurnos();
    const repositorio = new RepositorioArchivos(directorioDatos);
    const instancia = new ControladorTurnos(gestor, repositorio);

    resultadoInicio = instancia.inicializar();

    return instancia;
}

function registrarCanalesIpc() {
    ipcMain.handle("sgt:obtenerEstado", () => {
        return {
            exito: resultadoInicio ? resultadoInicio.exito : true,
            mensaje: resultadoInicio ? resultadoInicio.mensaje : "",
            estado: controlador.obtenerEstadoSistema()
        };
    });

    ipcMain.handle("sgt:registrarTurno", () => {
        return controlador.registrarTurno();
    });

    ipcMain.handle("sgt:asignarSiguiente", (evento, ventanillaId) => {
        return controlador.asignarSiguiente(ventanillaId);
    });

    ipcMain.handle("sgt:finalizarAtencion", (evento, ventanillaId) => {
        return controlador.finalizarAtencion(ventanillaId);
    });

    ipcMain.handle("sgt:cancelarTurno", (evento, turnoId) => {
        return controlador.cancelarTurno(turnoId);
    });

    ipcMain.handle("sgt:avanzarSegundoVentanillas", () => {
        return controlador.avanzarSegundoVentanillas();
    });
}

function crearVentanaPrincipal() {
    const rutaVista = path.join(__dirname, "src", "vista", "principal.html");
    const rutaPuente = path.join(__dirname, "src", "vista", "puente.js");

    const ventana = new BrowserWindow({
        width: 1200,
        height: 820,
        minWidth: 960,
        minHeight: 680,
        title: "Sistema de Gestión de Turnos Universitarios (SGT)",
        webPreferences: {
            preload: rutaPuente,
            contextIsolation: true,
            nodeIntegration: false,
            sandbox: true
        }
    });

    ventana.setMenuBarVisibility(false);
    ventana.loadFile(rutaVista);
}

app.whenReady().then(() => {
    controlador = crearControlador();
    registrarCanalesIpc();
    crearVentanaPrincipal();

    app.on("activate", () => {
        if (BrowserWindow.getAllWindows().length === 0) {
            crearVentanaPrincipal();
        }
    });
});

app.on("before-quit", () => {
    if (controlador !== null) {
        controlador.guardarAlCerrar();
    }
});

app.on("window-all-closed", () => {
    if (process.platform !== "darwin") {
        app.quit();
    }
});
