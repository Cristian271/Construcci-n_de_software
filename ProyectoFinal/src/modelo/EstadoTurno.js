class EstadoTurno {

    static EN_ESPERA = "En espera";
    static EN_ATENCION = "En atención";
    static ATENDIDO = "Atendido";
    static CANCELADO = "Cancelado";
    static VALORES_VALIDOS = Object.freeze([
        EstadoTurno.EN_ESPERA,
        EstadoTurno.EN_ATENCION,
        EstadoTurno.ATENDIDO,
        EstadoTurno.CANCELADO
    ]);

    static esValido(estado) {
        return typeof estado === "string"
            && EstadoTurno.VALORES_VALIDOS.includes(estado);
    }
}

module.exports = EstadoTurno;
