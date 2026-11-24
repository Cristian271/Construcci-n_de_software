class ErrorPersistencia extends Error {

    #causa;

    constructor(mensaje, causa = null) {
        super(mensaje);
        this.name = "ErrorPersistencia";
        this.#causa = causa;
    }

    obtenerCausa() {
        return this.#causa;
    }
}

module.exports = ErrorPersistencia;
