const { contextBridge, ipcRenderer } = require("electron");

contextBridge.exposeInMainWorld("apiTurnos", {
    obtenerEstado: () => ipcRenderer.invoke("sgt:obtenerEstado"),
    registrarTurno: () => ipcRenderer.invoke("sgt:registrarTurno"),
    asignarSiguiente: (ventanillaId) => ipcRenderer.invoke(
        "sgt:asignarSiguiente",
        ventanillaId
    ),
    finalizarAtencion: (ventanillaId) => ipcRenderer.invoke(
        "sgt:finalizarAtencion",
        ventanillaId
    ),
    cancelarTurno: (turnoId) => ipcRenderer.invoke(
        "sgt:cancelarTurno",
        turnoId
    ),
    avanzarSegundoVentanillas: () => ipcRenderer.invoke(
        "sgt:avanzarSegundoVentanillas"
    )
});
