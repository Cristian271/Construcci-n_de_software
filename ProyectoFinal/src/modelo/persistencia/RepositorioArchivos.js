const fs = require("fs");
const path = require("path");
const EstadoTurno = require("../EstadoTurno");
const ErrorPersistencia = require("../ErrorPersistencia");
const Turno = require("../Turno");
const Ventanilla = require("../Ventanilla");

// Extract Class
class RepositorioArchivos {

    static ARCHIVO_TURNOS = "turnos.json";
    static ARCHIVO_VENTANILLAS = "ventanillas.json";
    static SUFIJO_TEMPORAL = ".tmp";
    static CODIFICACION = "utf8";

    #directorioDatos;
    #sistemaArchivos;
    #ganchoEscritura;

    constructor(directorioDatos, opciones = {}) {
        if (
            typeof directorioDatos !== "string"
                || directorioDatos.trim().length === 0
        ) {
            throw new ErrorPersistencia(
                "Debe proporcionar una ruta válida para guardar los archivos."
            );
        }

        this.#directorioDatos = directorioDatos.trim();
        this.#sistemaArchivos = opciones.sistemaArchivos || fs;
        this.#ganchoEscritura = typeof opciones.ganchoEscritura === "function"
            ? opciones.ganchoEscritura
            : null;
    }

    obtenerDirectorioDatos() {
        return this.#directorioDatos;
    }

    obtenerRutaTurnos() {
        return path.join(
            this.#directorioDatos,
            RepositorioArchivos.ARCHIVO_TURNOS
        );
    }

    obtenerRutaVentanillas() {
        return path.join(
            this.#directorioDatos,
            RepositorioArchivos.ARCHIVO_VENTANILLAS
        );
    }

    cargarTodo() {
        const rutaTurnos = this.obtenerRutaTurnos();
        const rutaVentanillas = this.obtenerRutaVentanillas();
        const existenTurnos = this.#sistemaArchivos.existsSync(rutaTurnos);
        const existenVentanillas = this.#sistemaArchivos.existsSync(
            rutaVentanillas
        );

        if (!existenTurnos && !existenVentanillas) {
            return {
                existeEstadoPrevio: false,
                turnos: [],
                ventanillas: []
            };
        }

        if (existenTurnos !== existenVentanillas) {
            throw new ErrorPersistencia(
                "Los archivos de persistencia están incompletos. Falta uno de "
                    + "los archivos de entidades (turnos.json o "
                    + "ventanillas.json)."
            );
        }

        const datosTurnos = this.#leerYValidarArchivoJson(
            rutaTurnos,
            RepositorioArchivos.ARCHIVO_TURNOS
        );
        const datosVentanillas = this.#leerYValidarArchivoJson(
            rutaVentanillas,
            RepositorioArchivos.ARCHIVO_VENTANILLAS
        );

        this.#validarListaTurnos(datosTurnos);
        this.#validarListaVentanillas(datosVentanillas);

        return {
            existeEstadoPrevio: true,
            turnos: datosTurnos,
            ventanillas: datosVentanillas
        };
    }

    guardarTodo(turnos, ventanillas) {
        this.#validarListaTurnos(turnos);
        this.#validarListaVentanillas(ventanillas);

        const rutaTurnos = this.obtenerRutaTurnos();
        const rutaVentanillas = this.obtenerRutaVentanillas();
        const rutaTmpTurnos = rutaTurnos + RepositorioArchivos.SUFIJO_TEMPORAL;
        const rutaTmpVentanillas = rutaVentanillas
            + RepositorioArchivos.SUFIJO_TEMPORAL;

        const respaldoTurnos = this.#leerContenidoSiExiste(rutaTurnos);
        const respaldoVentanillas = this.#leerContenidoSiExiste(
            rutaVentanillas
        );

        const contenidoTurnos = JSON.stringify(turnos, null, 4);
        const contenidoVentanillas = JSON.stringify(ventanillas, null, 4);

        try {
            this.#asegurarDirectorio();
            this.#escribirTemporal(rutaTmpTurnos, contenidoTurnos);
            this.#escribirTemporal(rutaTmpVentanillas, contenidoVentanillas);

            this.#aplicarReemplazo(
                rutaTmpTurnos,
                rutaTurnos,
                RepositorioArchivos.ARCHIVO_TURNOS
            );
            this.#aplicarReemplazo(
                rutaTmpVentanillas,
                rutaVentanillas,
                RepositorioArchivos.ARCHIVO_VENTANILLAS
            );
        } catch (error) {
            this.#limpiarArchivoSiExiste(rutaTmpTurnos);
            this.#limpiarArchivoSiExiste(rutaTmpVentanillas);
            this.#restaurarArchivo(rutaTurnos, respaldoTurnos);
            this.#restaurarArchivo(rutaVentanillas, respaldoVentanillas);

            throw new ErrorPersistencia(
                "Error al guardar los datos locales. Se conservó el estado "
                    + "anterior sin cambios parciales.",
                error
            );
        }
    }

    #asegurarDirectorio() {
        if (!this.#sistemaArchivos.existsSync(this.#directorioDatos)) {
            this.#sistemaArchivos.mkdirSync(this.#directorioDatos, {
                recursive: true
            });
        }
    }

    #leerYValidarArchivoJson(rutaArchivo, nombreArchivo) {
        let texto = "";

        try {
            texto = this.#sistemaArchivos.readFileSync(
                rutaArchivo,
                RepositorioArchivos.CODIFICACION
            );
        } catch (error) {
            throw new ErrorPersistencia(
                "No fue posible leer el archivo " + nombreArchivo + ".",
                error
            );
        }

        let contenido = null;

        try {
            contenido = JSON.parse(texto);
        } catch (error) {
            throw new ErrorPersistencia(
                "El archivo "
                    + nombreArchivo
                    + " está dañado o no contiene un formato JSON válido.",
                error
            );
        }

        if (!Array.isArray(contenido)) {
            throw new ErrorPersistencia(
                "El archivo "
                    + nombreArchivo
                    + " debe contener una lista válida de registros."
            );
        }

        return contenido;
    }

    #validarListaTurnos(listaTurnos) {
        if (!Array.isArray(listaTurnos)) {
            throw new ErrorPersistencia(
                "La estructura de turnos no es una lista válida."
            );
        }

        for (let indice = 0; indice < listaTurnos.length; indice += 1) {
            const registro = listaTurnos[indice];

            if (!this.#esRegistroTurnoValido(registro)) {
                throw new ErrorPersistencia(
                    "El registro de turno en la posición "
                        + indice
                        + " tiene datos inválidos o incompletos."
                );
            }
        }
    }

    #validarListaVentanillas(listaVentanillas) {
        if (!Array.isArray(listaVentanillas)) {
            throw new ErrorPersistencia(
                "La estructura de ventanillas no es una lista válida."
            );
        }

        if (listaVentanillas.length !== Ventanilla.IDS_PREDEFINIDOS.length) {
            throw new ErrorPersistencia(
                "El archivo de ventanillas debe contener exactamente las "
                    + "ventanillas V01, V02 y V03."
            );
        }

        for (let indice = 0; indice < listaVentanillas.length; indice += 1) {
            const registro = listaVentanillas[indice];

            if (!this.#esRegistroVentanillaValido(registro)) {
                throw new ErrorPersistencia(
                    "El registro de ventanilla en la posición "
                        + indice
                        + " tiene datos inválidos."
                );
            }
        }
    }

    #esRegistroTurnoValido(registro) {
        if (registro === null || typeof registro !== "object") {
            return false;
        }

        const idValido = typeof registro.id === "string"
            && registro.id.trim().length > 0;
        const secuenciaValida = Number.isInteger(registro.secuencia)
            && registro.secuencia >= 1;
        const fechaValida = typeof registro.fechaHoraRegistro === "string"
            && registro.fechaHoraRegistro.trim().length > 0;
        const estadoValido = EstadoTurno.esValido(registro.estado);
        const ventanillaValida = registro.ventanillaAsignada === null
            || Ventanilla.esIdValido(registro.ventanillaAsignada);
        const tiempoValido = registro.tiempoAtencionSegundos === undefined
            || registro.tiempoAtencionSegundos === null
            || Turno.esTiempoAtencionValido(registro.tiempoAtencionSegundos);

        return idValido
            && secuenciaValida
            && fechaValida
            && estadoValido
            && ventanillaValida
            && tiempoValido;
    }

    #esRegistroVentanillaValido(registro) {
        if (registro === null || typeof registro !== "object") {
            return false;
        }

        const idValido = Ventanilla.esIdValido(registro.id);
        const turnoValido = registro.turnoActual === null
            || (
                typeof registro.turnoActual === "string"
                    && registro.turnoActual.trim().length > 0
            );
        const tiempoValido = registro.tiempoAsignadoSegundos === undefined
            || registro.tiempoAsignadoSegundos === null
            || Turno.esTiempoAtencionValido(registro.tiempoAsignadoSegundos);
        const restantesValidos = registro.segundosRestantes === undefined
            || registro.segundosRestantes === null
            || (
                Number.isInteger(registro.segundosRestantes)
                    && registro.segundosRestantes >= 0
                    && registro.segundosRestantes
                        <= Turno.TIEMPO_MAXIMO_SEGUNDOS
            );

        return idValido && turnoValido && tiempoValido && restantesValidos;
    }

    #leerContenidoSiExiste(rutaArchivo) {
        if (!this.#sistemaArchivos.existsSync(rutaArchivo)) {
            return null;
        }

        return this.#sistemaArchivos.readFileSync(
            rutaArchivo,
            RepositorioArchivos.CODIFICACION
        );
    }

    #escribirTemporal(rutaTemporal, contenido) {
        this.#sistemaArchivos.writeFileSync(
            rutaTemporal,
            contenido,
            RepositorioArchivos.CODIFICACION
        );
    }

    #aplicarReemplazo(rutaTemporal, rutaFinal, nombreArchivo) {
        if (this.#ganchoEscritura !== null) {
            this.#ganchoEscritura(nombreArchivo, rutaFinal);
        }

        this.#sistemaArchivos.renameSync(rutaTemporal, rutaFinal);
    }

    #limpiarArchivoSiExiste(rutaArchivo) {
        try {
            if (this.#sistemaArchivos.existsSync(rutaArchivo)) {
                this.#sistemaArchivos.unlinkSync(rutaArchivo);
            }
        } catch (error) {
            return;
        }
    }

    #restaurarArchivo(rutaArchivo, contenidoOriginal) {
        try {
            if (contenidoOriginal === null) {
                this.#limpiarArchivoSiExiste(rutaArchivo);
            } else {
                this.#sistemaArchivos.writeFileSync(
                    rutaArchivo,
                    contenidoOriginal,
                    RepositorioArchivos.CODIFICACION
                );
            }
        } catch (error) {
            return;
        }
    }
}

module.exports = RepositorioArchivos;
