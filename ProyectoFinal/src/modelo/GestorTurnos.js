const EstadoTurno = require("./EstadoTurno");
const ErrorDominio = require("./ErrorDominio");
const Turno = require("./Turno");
const Ventanilla = require("./Ventanilla");

class GestorTurnos {

    static INTERVALO_LLEGADA_SEGUNDOS = 30;
    static TIEMPO_ATENCION_MINIMO = Turno.TIEMPO_MINIMO_SEGUNDOS;
    static TIEMPO_ATENCION_MAXIMO = Turno.TIEMPO_MAXIMO_SEGUNDOS;

    #turnos;
    #ventanillas;
    #ultimaSecuencia;
    #generadorAleatorio;
    #proveedorFecha;

    constructor(opciones = {}) {
        const generador = opciones.generadorAleatorio || Math.random;
        const proveedorFecha = opciones.proveedorFecha
            || (() => new Date().toISOString());

        if (typeof generador !== "function") {
            throw new ErrorDominio(
                "El generador aleatorio proporcionado debe ser una función."
            );
        }

        if (typeof proveedorFecha !== "function") {
            throw new ErrorDominio(
                "El proveedor de fecha proporcionado debe ser una función."
            );
        }

        this.#turnos = [];
        this.#ventanillas = new Map();
        this.#ultimaSecuencia = 0;
        this.#generadorAleatorio = generador;
        this.#proveedorFecha = proveedorFecha;

        this.#inicializarVentanillas();
    }

    registrarTurno() {
        const nuevaSecuencia = this.#ultimaSecuencia + 1;
        const nuevoId = Turno.construirId(nuevaSecuencia);
        const fechaRegistro = this.#proveedorFecha();
        const turno = new Turno(
            nuevoId,
            nuevaSecuencia,
            fechaRegistro,
            EstadoTurno.EN_ESPERA,
            null,
            null
        );

        this.#ultimaSecuencia = nuevaSecuencia;
        this.#turnos.push(turno);

        return turno.aObjeto();
    }

    consultarVentanillas() {
        const lista = [];

        for (const idVentanilla of Ventanilla.IDS_PREDEFINIDOS) {
            const ventanilla = this.#ventanillas.get(idVentanilla);
            lista.push(ventanilla.aObjeto());
        }

        return lista;
    }

    consultarFila() {
        const enEspera = this.#turnos
            .filter((turno) => turno.estaEnEspera())
            .sort((a, b) => a.obtenerSecuencia() - b.obtenerSecuencia());

        return enEspera.map((turno) => turno.aObjeto());
    }

    consultarHistorial() {
        const ordenados = [...this.#turnos].sort(
            (a, b) => a.obtenerSecuencia() - b.obtenerSecuencia()
        );

        return ordenados.map((turno) => turno.aObjeto());
    }

    asignarSiguiente(ventanillaId) {
        const ventanilla = this.#obtenerVentanillaValida(ventanillaId);

        if (ventanilla.estaOcupada()) {
            throw new ErrorDominio(
                "No se puede asignar un turno porque la ventanilla "
                    + ventanilla.obtenerId()
                    + " está ocupada con el turno "
                    + ventanilla.obtenerTurnoActual()
                    + "."
            );
        }

        const siguienteTurno = this.#obtenerSiguienteTurnoEnEspera();

        if (siguienteTurno === null) {
            throw new ErrorDominio(
                "No hay turnos en espera para asignar en la ventanilla "
                    + ventanilla.obtenerId()
                    + "."
            );
        }

        const tiempoSegundos = this.#generarTiempoAtencion();

        siguienteTurno.iniciarAtencion(ventanilla.obtenerId(), tiempoSegundos);
        ventanilla.ocupar(siguienteTurno.obtenerId(), tiempoSegundos);

        return {
            turno: siguienteTurno.aObjeto(),
            ventanilla: ventanilla.aObjeto()
        };
    }

    finalizarAtencion(ventanillaId) {
        const ventanilla = this.#obtenerVentanillaValida(ventanillaId);

        if (ventanilla.estaLibre()) {
            throw new ErrorDominio(
                "No se puede finalizar atención porque la ventanilla "
                    + ventanilla.obtenerId()
                    + " está libre."
            );
        }

        const turnoId = ventanilla.obtenerTurnoActual();
        const turno = this.#buscarTurnoPorId(turnoId);

        if (turno === null) {
            throw new ErrorDominio(
                "No se encontró el turno activo "
                    + turnoId
                    + " asociado a la ventanilla "
                    + ventanilla.obtenerId()
                    + "."
            );
        }

        turno.marcarAtendido();
        ventanilla.liberar();

        return {
            turno: turno.aObjeto(),
            ventanilla: ventanilla.aObjeto()
        };
    }

    cancelarTurno(turnoId) {
        // Replace Nested Conditional with Guard Clauses
        if (typeof turnoId === "string" && turnoId.trim().length > 0) {
            const idLimpio = turnoId.trim().toUpperCase();
            const turno = this.#buscarTurnoPorId(idLimpio);

            if (turno !== null) {
                if (turno.estaEnEspera()) {
                    turno.marcarCancelado();
                    return turno.aObjeto();
                } else {
                    throw new ErrorDominio(
                        "No se puede cancelar el turno "
                            + turno.obtenerId()
                            + " porque su estado actual es "
                            + turno.obtenerEstado()
                            + "."
                    );
                }
            } else {
                throw new ErrorDominio(
                    "No existe un turno registrado con el identificador "
                        + idLimpio
                        + "."
                );
            }
        } else {
            throw new ErrorDominio(
                "Debe proporcionar un identificador de turno válido."
            );
        }
    }

    avanzarSegundoVentanillas() {
        const finalizados = [];
        let huboActualizacion = false;

        for (const idVentanilla of Ventanilla.IDS_PREDEFINIDOS) {
            const ventanilla = this.#ventanillas.get(idVentanilla);

            if (ventanilla.estaOcupada()) {
                const restantes = ventanilla.descontarSegundo();
                huboActualizacion = true;

                if (restantes === 0) {
                    const resultado = this.finalizarAtencion(idVentanilla);
                    finalizados.push(resultado);
                }
            }
        }

        return {
            huboActualizacion: huboActualizacion,
            finalizados: finalizados
        };
    }

    obtenerUltimaSecuencia() {
        return this.#ultimaSecuencia;
    }

    exportarEstado() {
        const turnos = this.consultarHistorial();
        const ventanillas = [];

        for (const idVentanilla of Ventanilla.IDS_PREDEFINIDOS) {
            const ventanilla = this.#ventanillas.get(idVentanilla);
            ventanillas.push(ventanilla.aRegistroPersistencia());
        }

        return {
            turnos: turnos,
            ventanillas: ventanillas
        };
    }

    cargarEstado(datosTurnos, datosVentanillas) {
        if (!Array.isArray(datosTurnos) || !Array.isArray(datosVentanillas)) {
            throw new ErrorDominio(
                "Los datos a cargar deben ser listas de turnos y ventanillas."
            );
        }

        const turnosCargados = [];
        const mapaTurnos = new Map();
        const secuenciasVistas = new Set();
        let secuenciaMaxima = 0;

        for (const registro of datosTurnos) {
            const turno = new Turno(
                registro.id,
                registro.secuencia,
                registro.fechaHoraRegistro,
                registro.estado,
                registro.ventanillaAsignada,
                registro.tiempoAtencionSegundos === undefined
                    ? null
                    : registro.tiempoAtencionSegundos
            );

            if (mapaTurnos.has(turno.obtenerId())) {
                throw new ErrorDominio(
                    "Se encontró un identificador de turno duplicado: "
                        + turno.obtenerId()
                        + "."
                );
            }

            if (secuenciasVistas.has(turno.obtenerSecuencia())) {
                throw new ErrorDominio(
                    "Se encontró una secuencia de turno duplicada: "
                        + turno.obtenerSecuencia()
                        + "."
                );
            }

            mapaTurnos.set(turno.obtenerId(), turno);
            secuenciasVistas.add(turno.obtenerSecuencia());
            turnosCargados.push(turno);

            if (turno.obtenerSecuencia() > secuenciaMaxima) {
                secuenciaMaxima = turno.obtenerSecuencia();
            }
        }

        const ventanillasCargadas = new Map();

        for (const registro of datosVentanillas) {
            const ventanilla = new Ventanilla(
                registro.id,
                registro.turnoActual,
                registro.tiempoAsignadoSegundos === undefined
                    ? null
                    : registro.tiempoAsignadoSegundos,
                registro.segundosRestantes === undefined
                    ? null
                    : registro.segundosRestantes
            );

            if (ventanillasCargadas.has(ventanilla.obtenerId())) {
                throw new ErrorDominio(
                    "Se encontró una ventanilla duplicada en los datos: "
                        + ventanilla.obtenerId()
                        + "."
                );
            }

            ventanillasCargadas.set(ventanilla.obtenerId(), ventanilla);
        }

        for (const idPredefinido of Ventanilla.IDS_PREDEFINIDOS) {
            if (!ventanillasCargadas.has(idPredefinido)) {
                throw new ErrorDominio(
                    "Falta la ventanilla obligatoria "
                        + idPredefinido
                        + " en los datos cargados."
                );
            }
        }

        // Extract Method
        const turnosAsignadosEnVentanillas = new Set();

        for (const ventanilla of ventanillasCargadas.values()) {
            const turnoId = ventanilla.obtenerTurnoActual();

            if (turnoId !== null) {
                const turnoAsociado = mapaTurnos.get(turnoId);

                if (!turnoAsociado) {
                    throw new ErrorDominio(
                        "La ventanilla "
                            + ventanilla.obtenerId()
                            + " referencia al turno inexistente "
                            + turnoId
                            + "."
                    );
                }

                if (!turnoAsociado.estaEnAtencion()) {
                    throw new ErrorDominio(
                        "La ventanilla "
                            + ventanilla.obtenerId()
                            + " tiene asignado el turno "
                            + turnoId
                            + " que no está En atención."
                    );
                }

                if (
                    turnoAsociado.obtenerVentanillaAsignada()
                        !== ventanilla.obtenerId()
                ) {
                    throw new ErrorDominio(
                        "Inconsistencia de asignación entre el turno "
                            + turnoId
                            + " y la ventanilla "
                            + ventanilla.obtenerId()
                            + "."
                    );
                }

                if (turnosAsignadosEnVentanillas.has(turnoId)) {
                    throw new ErrorDominio(
                        "El turno "
                            + turnoId
                            + " figura asignado en más de una ventanilla."
                    );
                }

                turnosAsignadosEnVentanillas.add(turnoId);
            }
        }

        for (const turno of turnosCargados) {
            if (turno.estaEnAtencion()) {
                const ventanillaId = turno.obtenerVentanillaAsignada();
                const ventanilla = ventanillasCargadas.get(ventanillaId);

                if (
                    !ventanilla
                        || ventanilla.obtenerTurnoActual() !== turno.obtenerId()
                ) {
                    throw new ErrorDominio(
                        "El turno "
                            + turno.obtenerId()
                            + " está En atención pero no coincide con la "
                            + "ventanilla "
                            + String(ventanillaId)
                            + "."
                    );
                }
            }
        }

        this.#turnos = turnosCargados;
        this.#ventanillas = ventanillasCargadas;
        this.#ultimaSecuencia = secuenciaMaxima;
    }

    crearInstantanea() {
        const estado = this.exportarEstado();

        return {
            turnos: estado.turnos,
            ventanillas: estado.ventanillas,
            ultimaSecuencia: this.#ultimaSecuencia
        };
    }

    restaurarInstantanea(instantanea) {
        this.cargarEstado(instantanea.turnos, instantanea.ventanillas);
        this.#ultimaSecuencia = instantanea.ultimaSecuencia;
    }

    #inicializarVentanillas() {
        for (const idVentanilla of Ventanilla.IDS_PREDEFINIDOS) {
            this.#ventanillas.set(idVentanilla, new Ventanilla(idVentanilla));
        }
    }

    #obtenerVentanillaValida(ventanillaId) {
        if (
            typeof ventanillaId !== "string"
                || ventanillaId.trim().length === 0
        ) {
            throw new ErrorDominio(
                "Debe indicar una ventanilla válida (V01, V02 o V03)."
            );
        }

        const idNormalizado = ventanillaId.trim().toUpperCase();

        if (!Ventanilla.esIdValido(idNormalizado)) {
            throw new ErrorDominio(
                "La ventanilla "
                    + idNormalizado
                    + " no es válida. Solo existen V01, V02 y V03."
            );
        }

        return this.#ventanillas.get(idNormalizado);
    }

    #obtenerSiguienteTurnoEnEspera() {
        let elegido = null;

        for (const turno of this.#turnos) {
            if (turno.estaEnEspera()) {
                if (
                    elegido === null
                        || turno.obtenerSecuencia() < elegido.obtenerSecuencia()
                ) {
                    elegido = turno;
                }
            }
        }

        return elegido;
    }

    #buscarTurnoPorId(turnoId) {
        for (const turno of this.#turnos) {
            if (turno.obtenerId() === turnoId) {
                return turno;
            }
        }

        return null;
    }

    #generarTiempoAtencion() {
        const minimo = GestorTurnos.TIEMPO_ATENCION_MINIMO;
        const maximo = GestorTurnos.TIEMPO_ATENCION_MAXIMO;
        const amplitud = (maximo - minimo) + 1;
        const valor = this.#generadorAleatorio();
        const valorAcotado = Math.min(Math.max(valor, 0), 0.999999999);

        return minimo + Math.floor(valorAcotado * amplitud);
    }
}

module.exports = GestorTurnos;
