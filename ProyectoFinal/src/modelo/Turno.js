const EstadoTurno = require("./EstadoTurno");
const ErrorDominio = require("./ErrorDominio");

class Turno {

    static PREFIJO_ID = "T-";
    static DIGITOS_ID = 3;
    static TIEMPO_MINIMO_SEGUNDOS = 10;
    static TIEMPO_MAXIMO_SEGUNDOS = 20;

    #id;
    #secuencia;
    #fechaHoraRegistro;
    #estado;
    #ventanillaAsignada;
    #tiempoAtencionSegundos;

    // Introduce Parameter Object
    constructor(
        id,
        secuencia,
        fechaHoraRegistro,
        estado = EstadoTurno.EN_ESPERA,
        ventanillaAsignada = null,
        tiempoAtencionSegundos = null
    ) {
        if (typeof id !== "string" || id.trim().length === 0) {
            throw new ErrorDominio(
                "El identificador del turno debe ser un texto no vacío."
            );
        }

        if (!Number.isInteger(secuencia) || secuencia < 1) {
            throw new ErrorDominio(
                "La secuencia del turno debe ser un entero positivo."
            );
        }

        if (
            typeof fechaHoraRegistro !== "string"
                || fechaHoraRegistro.trim().length === 0
        ) {
            throw new ErrorDominio(
                "La fecha y hora de registro del turno es obligatoria."
            );
        }

        if (!EstadoTurno.esValido(estado)) {
            throw new ErrorDominio(
                "El estado indicado para el turno no es válido: "
                    + estado
                    + "."
            );
        }

        if (
            ventanillaAsignada !== null
                && (
                    typeof ventanillaAsignada !== "string"
                        || ventanillaAsignada.trim().length === 0
                )
        ) {
            throw new ErrorDominio(
                "La ventanilla asignada debe ser nula o un identificador "
                    + "válido."
            );
        }

        if (
            estado === EstadoTurno.EN_ESPERA
                && ventanillaAsignada !== null
        ) {
            throw new ErrorDominio(
                "Un turno en espera no puede tener ventanilla asignada."
            );
        }

        if (
            estado === EstadoTurno.EN_ATENCION
                && ventanillaAsignada === null
        ) {
            throw new ErrorDominio(
                "Un turno en atención debe tener una ventanilla asignada."
            );
        }

        if (
            tiempoAtencionSegundos !== null
                && !Turno.esTiempoAtencionValido(tiempoAtencionSegundos)
        ) {
            throw new ErrorDominio(
                "El tiempo de atención debe estar entre "
                    + Turno.TIEMPO_MINIMO_SEGUNDOS
                    + " y "
                    + Turno.TIEMPO_MAXIMO_SEGUNDOS
                    + " segundos."
            );
        }

        this.#id = id.trim();
        this.#secuencia = secuencia;
        this.#fechaHoraRegistro = fechaHoraRegistro.trim();
        this.#estado = estado;
        this.#ventanillaAsignada = ventanillaAsignada === null
            ? null
            : ventanillaAsignada.trim();
        this.#tiempoAtencionSegundos = tiempoAtencionSegundos;
    }

    static construirId(secuencia) {
        if (!Number.isInteger(secuencia) || secuencia < 1) {
            throw new ErrorDominio(
                "La secuencia para construir un ID debe ser un entero positivo."
            );
        }

        const textoSecuencia = String(secuencia).padStart(
            Turno.DIGITOS_ID,
            "0"
        );

        return Turno.PREFIJO_ID + textoSecuencia;
    }

    static esTiempoAtencionValido(segundos) {
        return Number.isInteger(segundos)
            && segundos >= Turno.TIEMPO_MINIMO_SEGUNDOS
            && segundos <= Turno.TIEMPO_MAXIMO_SEGUNDOS;
    }

    obtenerId() {
        return this.#id;
    }

    obtenerSecuencia() {
        return this.#secuencia;
    }

    obtenerFechaHoraRegistro() {
        return this.#fechaHoraRegistro;
    }

    obtenerEstado() {
        return this.#estado;
    }

    obtenerVentanillaAsignada() {
        return this.#ventanillaAsignada;
    }

    obtenerTiempoAtencionSegundos() {
        return this.#tiempoAtencionSegundos;
    }

    estaEnEspera() {
        return this.#estado === EstadoTurno.EN_ESPERA;
    }

    estaEnAtencion() {
        return this.#estado === EstadoTurno.EN_ATENCION;
    }

    iniciarAtencion(ventanillaId, tiempoSegundos) {
        if (this.#estado !== EstadoTurno.EN_ESPERA) {
            throw new ErrorDominio(
                "El turno "
                    + this.#id
                    + " no puede iniciar atención porque su estado es "
                    + this.#estado
                    + "."
            );
        }

        if (
            typeof ventanillaId !== "string"
                || ventanillaId.trim().length === 0
        ) {
            throw new ErrorDominio(
                "Debe indicar una ventanilla válida para iniciar atención."
            );
        }

        if (!Turno.esTiempoAtencionValido(tiempoSegundos)) {
            throw new ErrorDominio(
                "El tiempo de atención asignado debe estar entre "
                    + Turno.TIEMPO_MINIMO_SEGUNDOS
                    + " y "
                    + Turno.TIEMPO_MAXIMO_SEGUNDOS
                    + " segundos."
            );
        }

        this.#estado = EstadoTurno.EN_ATENCION;
        this.#ventanillaAsignada = ventanillaId.trim();
        this.#tiempoAtencionSegundos = tiempoSegundos;
    }

    marcarAtendido() {
        if (this.#estado !== EstadoTurno.EN_ATENCION) {
            throw new ErrorDominio(
                "Solo un turno en atención puede finalizarse. El turno "
                    + this.#id
                    + " está "
                    + this.#estado
                    + "."
            );
        }

        this.#estado = EstadoTurno.ATENDIDO;
    }

    marcarCancelado() {
        if (this.#estado !== EstadoTurno.EN_ESPERA) {
            throw new ErrorDominio(
                "Solo un turno en espera puede cancelarse. El turno "
                    + this.#id
                    + " está "
                    + this.#estado
                    + "."
            );
        }

        this.#estado = EstadoTurno.CANCELADO;
    }

    aObjeto() {
        return {
            id: this.#id,
            secuencia: this.#secuencia,
            fechaHoraRegistro: this.#fechaHoraRegistro,
            estado: this.#estado,
            ventanillaAsignada: this.#ventanillaAsignada,
            tiempoAtencionSegundos: this.#tiempoAtencionSegundos
        };
    }
}

module.exports = Turno;
