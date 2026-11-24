const INTERVALO_UN_SEGUNDO_MS = 1000;
const INTERVALO_LLEGADA_DEFECTO = 30;

let intervaloLlegadaSegundos = INTERVALO_LLEGADA_DEFECTO;
let segundosParaLlegada = INTERVALO_LLEGADA_DEFECTO;
let llegadasPausadas = false;
let sistemaBloqueado = false;
let turnoPendienteConfirmacion = null;
let operacionEnCurso = false;

const panelMensaje = document.getElementById("panelMensaje");
const iconoMensaje = document.getElementById("iconoMensaje");
const textoMensaje = document.getElementById("textoMensaje");
const contadorLlegada = document.getElementById("contadorLlegada");
const botonPausarLlegadas = document.getElementById("botonPausarLlegadas");
const botonRegistrarTurno = document.getElementById("botonRegistrarTurno");
const botonActualizar = document.getElementById("botonActualizar");
const contenedorVentanillas = document.getElementById("contenedorVentanillas");
const cuerpoTablaFila = document.getElementById("cuerpoTablaFila");
const contadorFila = document.getElementById("contadorFila");
const cuerpoTablaHistorial = document.getElementById("cuerpoTablaHistorial");
const contadorHistorial = document.getElementById("contadorHistorial");
const resumenEstados = document.getElementById("resumenEstados");
const formularioCancelar = document.getElementById("formularioCancelar");
const campoIdCancelar = document.getElementById("campoIdCancelar");
const modalConfirmacion = document.getElementById("modalConfirmacion");
const textoModalConfirmacion = document.getElementById(
    "textoModalConfirmacion"
);
const botonRechazarModal = document.getElementById("botonRechazarModal");
const botonAceptarModal = document.getElementById("botonAceptarModal");

function mostrarMensaje(tipo, texto) {
    panelMensaje.className = "panel-mensaje mensaje-" + tipo;

    if (tipo === "exito") {
        iconoMensaje.textContent = "✓";
    } else if (tipo === "error") {
        iconoMensaje.textContent = "!";
    } else {
        iconoMensaje.textContent = "ℹ";
    }

    textoMensaje.textContent = texto;
}

function formatearFechaHora(fechaIso) {
    const fecha = new Date(fechaIso);

    if (Number.isNaN(fecha.getTime())) {
        return String(fechaIso);
    }

    return fecha.toLocaleString("es-MX", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit"
    });
}

function crearInsigniaEstado(estado) {
    const etiqueta = document.createElement("span");
    let claseEstado = "estado-espera";
    let simbolo = "●";

    if (estado === "En atención") {
        claseEstado = "estado-atencion";
        simbolo = "▶";
    } else if (estado === "Atendido") {
        claseEstado = "estado-atendido";
        simbolo = "✓";
    } else if (estado === "Cancelado") {
        claseEstado = "estado-cancelado";
        simbolo = "✕";
    }

    etiqueta.className = "insignia-estado " + claseEstado;
    etiqueta.textContent = simbolo + " " + estado;

    return etiqueta;
}

function construirTarjetaVentanilla(
    id,
    ocupada,
    textoEstado,
    colorIndicador,
    turnoActual,
    tiempoAsignadoSegundos,
    segundosRestantes
) {
    const tarjeta = document.createElement("article");
    tarjeta.className = ocupada
        ? "tarjeta-ventanilla ocupada"
        : "tarjeta-ventanilla";

    const cabecera = document.createElement("div");
    cabecera.className = "cabecera-ventanilla";

    const titulo = document.createElement("h3");
    titulo.className = "nombre-ventanilla";
    titulo.textContent = id;

    const insignia = document.createElement("span");
    insignia.className = ocupada
        ? "estado-ventanilla estado-ocupada"
        : "estado-ventanilla estado-libre";

    const luz = document.createElement("span");
    luz.className = "indicador-luz indicador-" + colorIndicador;

    const texto = document.createElement("span");
    texto.textContent = textoEstado;

    insignia.appendChild(luz);
    insignia.appendChild(texto);
    cabecera.appendChild(titulo);
    cabecera.appendChild(insignia);

    const detalle = document.createElement("div");
    detalle.className = "detalle-ventanilla";

    if (ocupada) {
        const lineaTurno = document.createElement("div");
        lineaTurno.className = "turno-activo-id";
        lineaTurno.textContent = turnoActual;

        const lineaTiempo = document.createElement("div");
        lineaTiempo.textContent = "Tiempo restante: "
            + segundosRestantes
            + " s / "
            + tiempoAsignadoSegundos
            + " s";

        const barra = document.createElement("div");
        barra.className = "barra-progreso";

        const relleno = document.createElement("div");
        relleno.className = "barra-progreso-relleno";
        const porcentaje = tiempoAsignadoSegundos > 0
            ? Math.round((segundosRestantes / tiempoAsignadoSegundos) * 100)
            : 0;
        relleno.style.width = porcentaje + "%";

        barra.appendChild(relleno);
        detalle.appendChild(lineaTurno);
        detalle.appendChild(lineaTiempo);
        detalle.appendChild(barra);
    } else {
        const lineaLibre = document.createElement("p");
        lineaLibre.className = "texto-libre";
        lineaLibre.textContent = "Sin turno en atención.";

        const lineaNota = document.createElement("p");
        lineaNota.className = "texto-libre";
        lineaNota.textContent = "Disponible para asignar siguiente turno.";

        detalle.appendChild(lineaLibre);
        detalle.appendChild(lineaNota);
    }

    const acciones = document.createElement("div");
    acciones.className = "acciones-ventanilla";

    const botonAsignar = document.createElement("button");
    botonAsignar.type = "button";
    botonAsignar.className = "boton boton-primario";
    botonAsignar.textContent = "Asignar siguiente";
    botonAsignar.addEventListener("click", () => {
        manejarAsignarSiguiente(id);
    });

    const botonFinalizar = document.createElement("button");
    botonFinalizar.type = "button";
    botonFinalizar.className = "boton boton-exito";
    botonFinalizar.textContent = "Finalizar";
    botonFinalizar.addEventListener("click", () => {
        manejarFinalizarAtencion(id);
    });

    acciones.appendChild(botonAsignar);
    acciones.appendChild(botonFinalizar);

    tarjeta.appendChild(cabecera);
    tarjeta.appendChild(detalle);
    tarjeta.appendChild(acciones);

    return tarjeta;
}

function renderizarVentanillas(ventanillas) {
    contenedorVentanillas.textContent = "";

    for (const ventanilla of ventanillas) {
        // Preserve Whole Object
        const tarjeta = construirTarjetaVentanilla(
            ventanilla.id,
            ventanilla.ocupada,
            ventanilla.textoEstado,
            ventanilla.colorIndicador,
            ventanilla.turnoActual,
            ventanilla.tiempoAsignadoSegundos,
            ventanilla.segundosRestantes
        );

        contenedorVentanillas.appendChild(tarjeta);
    }
}

function renderizarFila(fila) {
    cuerpoTablaFila.textContent = "";
    contadorFila.textContent = fila.length + " en espera";

    if (fila.length === 0) {
        const filaVacia = document.createElement("tr");
        const celda = document.createElement("td");
        celda.colSpan = 5;
        celda.className = "celda-vacia";
        celda.textContent = "No hay turnos en espera.";
        filaVacia.appendChild(celda);
        cuerpoTablaFila.appendChild(filaVacia);
        return;
    }

    for (const turno of fila) {
        const renglon = document.createElement("tr");

        const celdaId = document.createElement("td");
        celdaId.textContent = turno.id;

        const celdaSecuencia = document.createElement("td");
        celdaSecuencia.textContent = String(turno.secuencia);

        const celdaFecha = document.createElement("td");
        celdaFecha.textContent = formatearFechaHora(turno.fechaHoraRegistro);

        const celdaEstado = document.createElement("td");
        celdaEstado.appendChild(crearInsigniaEstado(turno.estado));

        const celdaAccion = document.createElement("td");
        const botonCancelar = document.createElement("button");
        botonCancelar.type = "button";
        botonCancelar.className = "boton boton-peligro boton-pequeno";
        botonCancelar.textContent = "Cancelar";
        botonCancelar.addEventListener("click", () => {
            solicitarConfirmacionCancelacion(turno.id);
        });
        celdaAccion.appendChild(botonCancelar);

        renglon.appendChild(celdaId);
        renglon.appendChild(celdaSecuencia);
        renglon.appendChild(celdaFecha);
        renglon.appendChild(celdaEstado);
        renglon.appendChild(celdaAccion);

        cuerpoTablaFila.appendChild(renglon);
    }
}

function renderizarResumenEstados(historial) {
    const conteos = {
        "En espera": 0,
        "En atención": 0,
        "Atendido": 0,
        "Cancelado": 0
    };

    for (const turno of historial) {
        if (Object.prototype.hasOwnProperty.call(conteos, turno.estado)) {
            conteos[turno.estado] += 1;
        }
    }

    resumenEstados.textContent = "";

    for (const estado of Object.keys(conteos)) {
        const tarjeta = document.createElement("div");
        tarjeta.className = "tarjeta-resumen";

        const etiqueta = document.createElement("span");
        etiqueta.textContent = estado;

        const valor = document.createElement("strong");
        valor.textContent = String(conteos[estado]);

        tarjeta.appendChild(etiqueta);
        tarjeta.appendChild(valor);
        resumenEstados.appendChild(tarjeta);
    }
}

function renderizarHistorial(historial) {
    cuerpoTablaHistorial.textContent = "";
    contadorHistorial.textContent = historial.length + " registrados";

    if (historial.length === 0) {
        const filaVacia = document.createElement("tr");
        const celda = document.createElement("td");
        celda.colSpan = 6;
        celda.className = "celda-vacia";
        celda.textContent = "Todavía no hay turnos registrados.";
        filaVacia.appendChild(celda);
        cuerpoTablaHistorial.appendChild(filaVacia);
        renderizarResumenEstados(historial);
        return;
    }

    for (const turno of historial) {
        const renglon = document.createElement("tr");

        const celdaId = document.createElement("td");
        celdaId.textContent = turno.id;

        const celdaSecuencia = document.createElement("td");
        celdaSecuencia.textContent = String(turno.secuencia);

        const celdaFecha = document.createElement("td");
        celdaFecha.textContent = formatearFechaHora(turno.fechaHoraRegistro);

        const celdaEstado = document.createElement("td");
        celdaEstado.appendChild(crearInsigniaEstado(turno.estado));

        const celdaVentanilla = document.createElement("td");
        celdaVentanilla.textContent = turno.ventanillaAsignada || "—";

        const celdaTiempo = document.createElement("td");
        celdaTiempo.textContent = turno.tiempoAtencionSegundos !== null
            ? turno.tiempoAtencionSegundos + " s"
            : "—";

        renglon.appendChild(celdaId);
        renglon.appendChild(celdaSecuencia);
        renglon.appendChild(celdaFecha);
        renglon.appendChild(celdaEstado);
        renglon.appendChild(celdaVentanilla);
        renglon.appendChild(celdaTiempo);

        cuerpoTablaHistorial.appendChild(renglon);
    }

    renderizarResumenEstados(historial);
}

function renderizarEstado(estado) {
    sistemaBloqueado = Boolean(estado.bloqueadoPorErrorCarga);
    intervaloLlegadaSegundos = estado.intervaloLlegadaSegundos
        || INTERVALO_LLEGADA_DEFECTO;

    renderizarVentanillas(estado.ventanillas);
    renderizarFila(estado.fila);
    renderizarHistorial(estado.historial);
}

function actualizarTextoContadorLlegada() {
    if (sistemaBloqueado) {
        contadorLlegada.textContent = "Detenido";
        return;
    }

    if (llegadasPausadas) {
        contadorLlegada.textContent = "Pausado";
        return;
    }

    contadorLlegada.textContent = segundosParaLlegada + " s";
}

function solicitarConfirmacionCancelacion(turnoId) {
    turnoPendienteConfirmacion = turnoId;
    textoModalConfirmacion.textContent = "¿Confirma que desea cancelar el "
        + "turno "
        + turnoId
        + "? Esta acción cambiará su estado a Cancelado.";
    modalConfirmacion.classList.remove("oculto");
}

function cerrarModalConfirmacion() {
    turnoPendienteConfirmacion = null;
    modalConfirmacion.classList.add("oculto");
}

async function manejarRegistrarTurno(esAutomatico) {
    if (operacionEnCurso) {
        return;
    }

    operacionEnCurso = true;

    try {
        const resultado = await window.apiTurnos.registrarTurno();
        renderizarEstado(resultado.estado);

        if (resultado.exito) {
            const prefijo = esAutomatico
                ? "Llegada automática (30 s): "
                : "";
            mostrarMensaje("exito", prefijo + resultado.mensaje);
        } else {
            mostrarMensaje("error", resultado.mensaje);
        }
    } finally {
        operacionEnCurso = false;
    }
}

async function manejarAsignarSiguiente(ventanillaId) {
    if (operacionEnCurso) {
        return;
    }

    operacionEnCurso = true;

    try {
        const resultado = await window.apiTurnos.asignarSiguiente(ventanillaId);
        renderizarEstado(resultado.estado);
        mostrarMensaje(
            resultado.exito ? "exito" : "error",
            resultado.mensaje
        );
    } finally {
        operacionEnCurso = false;
    }
}

async function manejarFinalizarAtencion(ventanillaId) {
    if (operacionEnCurso) {
        return;
    }

    operacionEnCurso = true;

    try {
        const resultado = await window.apiTurnos.finalizarAtencion(
            ventanillaId
        );
        renderizarEstado(resultado.estado);
        mostrarMensaje(
            resultado.exito ? "exito" : "error",
            resultado.mensaje
        );
    } finally {
        operacionEnCurso = false;
    }
}

async function ejecutarCancelacionTurno(turnoId) {
    if (operacionEnCurso) {
        return;
    }

    operacionEnCurso = true;

    try {
        const resultado = await window.apiTurnos.cancelarTurno(turnoId);
        renderizarEstado(resultado.estado);
        mostrarMensaje(
            resultado.exito ? "exito" : "error",
            resultado.mensaje
        );

        if (resultado.exito) {
            campoIdCancelar.value = "";
        }
    } finally {
        operacionEnCurso = false;
    }
}

async function cargarEstadoInicial() {
    const resultado = await window.apiTurnos.obtenerEstado();
    renderizarEstado(resultado.estado);
    segundosParaLlegada = intervaloLlegadaSegundos;
    actualizarTextoContadorLlegada();
    mostrarMensaje(
        resultado.exito ? "info" : "error",
        resultado.mensaje || "Estado actualizado."
    );
}

async function ejecutarPulsoUnSegundo() {
    if (sistemaBloqueado || operacionEnCurso) {
        return;
    }

    const avance = await window.apiTurnos.avanzarSegundoVentanillas();

    if (avance.huboActualizacion) {
        renderizarEstado(avance.estado);

        if (avance.mensaje) {
            mostrarMensaje(
                avance.exito ? "exito" : "error",
                avance.mensaje
            );
        }
    } else if (!avance.exito && avance.mensaje) {
        renderizarEstado(avance.estado);
        mostrarMensaje("error", avance.mensaje);
    }

    if (!llegadasPausadas && !sistemaBloqueado) {
        segundosParaLlegada -= 1;

        if (segundosParaLlegada <= 0) {
            segundosParaLlegada = intervaloLlegadaSegundos;
            actualizarTextoContadorLlegada();
            await manejarRegistrarTurno(true);
            return;
        }

        actualizarTextoContadorLlegada();
    }
}

botonRegistrarTurno.addEventListener("click", () => {
    manejarRegistrarTurno(false);
});

botonActualizar.addEventListener("click", async () => {
    const resultado = await window.apiTurnos.obtenerEstado();
    renderizarEstado(resultado.estado);
    mostrarMensaje(
        resultado.exito ? "info" : "error",
        resultado.exito ? "Vista actualizada correctamente." : resultado.mensaje
    );
});

botonPausarLlegadas.addEventListener("click", () => {
    llegadasPausadas = !llegadasPausadas;
    botonPausarLlegadas.textContent = llegadasPausadas
        ? "Reanudar llegadas"
        : "Pausar llegadas";
    actualizarTextoContadorLlegada();
});

formularioCancelar.addEventListener("submit", (evento) => {
    evento.preventDefault();
    const idIngresado = campoIdCancelar.value.trim();

    if (idIngresado.length === 0) {
        ejecutarCancelacionTurno("");
        return;
    }

    solicitarConfirmacionCancelacion(idIngresado.toUpperCase());
});

botonRechazarModal.addEventListener("click", () => {
    cerrarModalConfirmacion();
    mostrarMensaje(
        "info",
        "Cancelación anulada por el operador. El estado permanece sin cambios."
    );
});

botonAceptarModal.addEventListener("click", () => {
    const idConfirmado = turnoPendienteConfirmacion;
    cerrarModalConfirmacion();

    if (idConfirmado !== null) {
        ejecutarCancelacionTurno(idConfirmado);
    }
});

cargarEstadoInicial();
setInterval(ejecutarPulsoUnSegundo, INTERVALO_UN_SEGUNDO_MS);
