const ErrorDominio = require("./ErrorDominio");
const Turno = require("./Turno");

class Ventanilla {

    static IDS_PREDEFINIDOS = Object.freeze(["V01", "V02", "V03"]);
    static COLOR_LIBRE = "verde";
    static COLOR_OCUPADA = "rojo";
    static TEXTO_LIBRE = "Libre";
    static TEXTO_OCUPADA = "Ocupada";

    #id;
    #turnoActual;
    #tiempoAsignadoSegundos;
    #segundosRestantes;

    constructor(
        id,
        turnoActual = null,
        tiempoAsignadoSegundos = null,
        segundosRestantes = null
    ) {
        if (!Ventanilla.esIdValido(id)) {
            throw new ErrorDominio(
                "La ventanilla "
                    + String(id)
                    + " no existe. Las ventanillas válidas son V01, V02 y V03."
            );
        }

        if (
            turnoActual !== null
                && (
                    typeof turnoActual !== "string"
                        || turnoActual.trim().length === 0
                )
        ) {
            throw new ErrorDominio(
                "El turno actual de la ventanilla "
                    + id
                    + " debe ser nulo o un identificador válido."
            );
        }

        if (turnoActual === null) {
            this.#id = id;
            this.#turnoActual = null;
            this.#tiempoAsignadoSegundos = null;
            this.#segundosRestantes = null;
            return;
        }

        const tiempoValido = tiempoAsignadoSegundos === null
            ? Turno.TIEMPO_MINIMO_SEGUNDOS
            : tiempoAsignadoSegundos;

        if (!Turno.esTiempoAtencionValido(tiempoValido)) {
            throw new ErrorDominio(
                "El tiempo asignado en la ventanilla "
                    + id
                    + " debe estar entre "
                    + Turno.TIEMPO_MINIMO_SEGUNDOS
                    + " y "
                    + Turno.TIEMPO_MAXIMO_SEGUNDOS
                    + " segundos."
            );
        }

        const restantesValidos = segundosRestantes === null
            ? tiempoValido
            : segundosRestantes;

        if (
            !Number.isInteger(restantesValidos)
                || restantesValidos < 0
                || restantesValidos > tiempoValido
        ) {
            throw new ErrorDominio(
                "Los segundos restantes de la ventanilla "
                    + id
                    + " no son válidos."
            );
        }

        this.#id = id;
        this.#turnoActual = turnoActual.trim();
        this.#tiempoAsignadoSegundos = tiempoValido;
        this.#segundosRestantes = restantesValidos;
    }

    static esIdValido(id) {
        return typeof id === "string"
            && Ventanilla.IDS_PREDEFINIDOS.includes(id);
    }

    obtenerId() {
        return this.#id;
    }

    obtenerTurnoActual() {
        return this.#turnoActual;
    }

    obtenerTiempoAsignadoSegundos() {
        return this.#tiempoAsignadoSegundos;
    }

    obtenerSegundosRestantes() {
        return this.#segundosRestantes;
    }

    estaOcupada() {
        return this.#turnoActual !== null;
    }

    estaLibre() {
        return this.#turnoActual === null;
    }

    obtenerColorIndicador() {
        return this.estaOcupada()
            ? Ventanilla.COLOR_OCUPADA
            : Ventanilla.COLOR_LIBRE;
    }

    obtenerTextoEstado() {
        return this.estaOcupada()
            ? Ventanilla.TEXTO_OCUPADA
            : Ventanilla.TEXTO_LIBRE;
    }

    ocupar(turnoId, tiempoSegundos) {
        if (this.estaOcupada()) {
            throw new ErrorDominio(
                "La ventanilla "
                    + this.#id
                    + " ya está ocupada atendiendo el turno "
                    + this.#turnoActual
                    + "."
            );
        }

        if (typeof turnoId !== "string" || turnoId.trim().length === 0) {
            throw new ErrorDominio(
                "Debe indicar un turno válido para ocupar la ventanilla "
                    + this.#id
                    + "."
            );
        }

        if (!Turno.esTiempoAtencionValido(tiempoSegundos)) {
            throw new ErrorDominio(
                "El tiempo de atención debe estar entre "
                    + Turno.TIEMPO_MINIMO_SEGUNDOS
                    + " y "
                    + Turno.TIEMPO_MAXIMO_SEGUNDOS
                    + " segundos."
            );
        }

        this.#turnoActual = turnoId.trim();
        this.#tiempoAsignadoSegundos = tiempoSegundos;
        this.#segundosRestantes = tiempoSegundos;
    }

    descontarSegundo() {
        if (this.estaLibre()) {
            return 0;
        }

        if (this.#segundosRestantes > 0) {
            this.#segundosRestantes -= 1;
        }

        return this.#segundosRestantes;
    }

    liberar() {
        if (this.estaLibre()) {
            throw new ErrorDominio(
                "No se puede finalizar atención porque la ventanilla "
                    + this.#id
                    + " se encuentra libre."
            );
        }

        const turnoLiberado = this.#turnoActual;

        this.#turnoActual = null;
        this.#tiempoAsignadoSegundos = null;
        this.#segundosRestantes = null;

        return turnoLiberado;
    }

    aObjeto() {
        return {
            id: this.#id,
            turnoActual: this.#turnoActual,
            ocupada: this.estaOcupada(),
            textoEstado: this.obtenerTextoEstado(),
            colorIndicador: this.obtenerColorIndicador(),
            tiempoAsignadoSegundos: this.#tiempoAsignadoSegundos,
            segundosRestantes: this.#segundosRestantes
        };
    }

    aRegistroPersistencia() {
        return {
            id: this.#id,
            turnoActual: this.#turnoActual,
            tiempoAsignadoSegundos: this.#tiempoAsignadoSegundos,
            segundosRestantes: this.#segundosRestantes
        };
    }
}

module.exports = Ventanilla;
