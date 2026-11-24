class ErrorDominio extends Error {

    #codigo;

    constructor(mensaje, codigo = "REGLA_DOMINIO") {
        super(mensaje);
        this.name = "ErrorDominio";
        this.#codigo = codigo;
    }

    obtenerCodigo() {
        return this.#codigo;
    }
}

module.exports = ErrorDominio;
