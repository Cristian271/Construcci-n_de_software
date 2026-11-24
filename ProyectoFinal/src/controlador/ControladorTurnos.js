const GestorTurnos = require("../modelo/GestorTurnos");

class ControladorTurnos {

    #gestor;
    #repositorio;
    #bloqueadoPorErrorCarga;
    #errorInicial;

    constructor(gestor, repositorio) {
        this.#gestor = gestor;
        this.#repositorio = repositorio;
        this.#bloqueadoPorErrorCarga = false;
        this.#errorInicial = null;
    }

    inicializar() {
        try {
            const datos = this.#repositorio.cargarTodo();

            if (datos.existeEstadoPrevio) {
                this.#gestor.cargarEstado(datos.turnos, datos.ventanillas);
            }

            this.#bloqueadoPorErrorCarga = false;
            this.#errorInicial = null;

            return {
                exito: true,
                mensaje: datos.existeEstadoPrevio
                    ? "Estado recuperado correctamente desde almacenamiento "
                        + "local."
                    : "Sistema listo. No existían registros previos.",
                estado: this.obtenerEstadoSistema()
            };
        } catch (error) {
            this.#bloqueadoPorErrorCarga = true;
            this.#errorInicial = error.message;

            return {
                exito: false,
                mensaje: "No se pudieron cargar los datos guardados: "
                    + error.message
                    + " Los archivos existentes no fueron sobrescritos.",
                estado: this.obtenerEstadoSistema()
            };
        }
    }

    obtenerEstadoSistema() {
        return {
            ventanillas: this.#gestor.consultarVentanillas(),
            fila: this.#gestor.consultarFila(),
            historial: this.#gestor.consultarHistorial(),
            intervaloLlegadaSegundos: GestorTurnos.INTERVALO_LLEGADA_SEGUNDOS,
            bloqueadoPorErrorCarga: this.#bloqueadoPorErrorCarga,
            errorInicial: this.#errorInicial
        };
    }

    registrarTurno() {
        return this.#ejecutarOperacionPersistente(
            () => this.#gestor.registrarTurno(),
            (turno) => "Turno " + turno.id + " registrado en la fila de espera."
        );
    }

    asignarSiguiente(ventanillaId) {
        return this.#ejecutarOperacionPersistente(
            () => this.#gestor.asignarSiguiente(ventanillaId),
            (resultado) => "Turno "
                + resultado.turno.id
                + " asignado a "
                + resultado.ventanilla.id
                + " por "
                + resultado.turno.tiempoAtencionSegundos
                + " segundos."
        );
    }

    finalizarAtencion(ventanillaId) {
        return this.#ejecutarOperacionPersistente(
            () => this.#gestor.finalizarAtencion(ventanillaId),
            (resultado) => "Atención del turno "
                + resultado.turno.id
                + " finalizada. La ventanilla "
                + resultado.ventanilla.id
                + " quedó libre."
        );
    }

    cancelarTurno(turnoId) {
        return this.#ejecutarOperacionPersistente(
            () => this.#gestor.cancelarTurno(turnoId),
            (turno) => "Turno "
                + turno.id
                + " cancelado y retirado de la fila de espera."
        );
    }

    avanzarSegundoVentanillas() {
        if (this.#bloqueadoPorErrorCarga) {
            return {
                exito: false,
                huboActualizacion: false,
                finalizados: [],
                mensaje: this.#errorInicial,
                estado: this.obtenerEstadoSistema()
            };
        }

        const instantanea = this.#gestor.crearInstantanea();

        try {
            const avance = this.#gestor.avanzarSegundoVentanillas();

            if (avance.finalizados.length > 0) {
                const exportado = this.#gestor.exportarEstado();
                this.#repositorio.guardarTodo(
                    exportado.turnos,
                    exportado.ventanillas
                );

                const resumen = avance.finalizados
                    .map(
                        (item) => item.turno.id
                            + " en "
                            + item.ventanilla.id
                    )
                    .join(", ");

                return {
                    exito: true,
                    huboActualizacion: true,
                    finalizados: avance.finalizados,
                    mensaje: "Tiempo de atención completado automáticamente "
                        + "para: "
                        + resumen
                        + ".",
                    estado: this.obtenerEstadoSistema()
                };
            }

            return {
                exito: true,
                huboActualizacion: avance.huboActualizacion,
                finalizados: [],
                mensaje: "",
                estado: this.obtenerEstadoSistema()
            };
        } catch (error) {
            this.#gestor.restaurarInstantanea(instantanea);

            return {
                exito: false,
                huboActualizacion: false,
                finalizados: [],
                mensaje: error.message,
                estado: this.obtenerEstadoSistema()
            };
        }
    }

    guardarAlCerrar() {
        if (this.#bloqueadoPorErrorCarga) {
            return false;
        }

        try {
            const exportado = this.#gestor.exportarEstado();

            if (exportado.turnos.length === 0) {
                return true;
            }

            this.#repositorio.guardarTodo(
                exportado.turnos,
                exportado.ventanillas
            );

            return true;
        } catch (error) {
            return false;
        }
    }

    #ejecutarOperacionPersistente(operacion, construirMensaje) {
        if (this.#bloqueadoPorErrorCarga) {
            return {
                exito: false,
                mensaje: "Operación bloqueada porque los archivos guardados "
                    + "presentan un error. Corrija o respalde los archivos "
                    + "antes de continuar.",
                estado: this.obtenerEstadoSistema()
            };
        }

        const instantanea = this.#gestor.crearInstantanea();

        try {
            const datos = operacion();
            const exportado = this.#gestor.exportarEstado();

            this.#repositorio.guardarTodo(
                exportado.turnos,
                exportado.ventanillas
            );

            return {
                exito: true,
                mensaje: construirMensaje(datos),
                datos: datos,
                estado: this.obtenerEstadoSistema()
            };
        } catch (error) {
            this.#gestor.restaurarInstantanea(instantanea);

            return {
                exito: false,
                mensaje: error.message,
                estado: this.obtenerEstadoSistema()
            };
        }
    }
}

module.exports = ControladorTurnos;
