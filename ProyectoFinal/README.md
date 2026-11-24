# Sistema de Gestión de Turnos Universitarios (SGT)

Aplicación de escritorio construida con **Electron** y arquitectura **MVC** (Modelo-Vista-Controlador) para administrar una cola única de turnos con disciplina **FIFO** y tres ventanillas lógicas predefinidas (`V01`, `V02` y `V03`), conforme a la especificación **ERS-SGT-001**.

## Funcionalidades principales

- **Cola única FIFO por secuencia**: Los turnos se ordenan por una secuencia numérica creciente e irrepetible (`T-001`, `T-002`, ...), nunca por orden alfabético del identificador ni por reloj.
- **Llegada automática cada 30 segundos y registro manual**: El sistema incorpora una persona a la fila cada 30 segundos (con opción de pausar/reanudar desde la barra superior) y además permite registrar turnos manualmente en cualquier momento.
- **Tres ventanillas fijas (`V01`, `V02`, `V03`) con indicador visual y textual**:
  - Indicador **verde** + texto **Libre** cuando la ventanilla no tiene turno asignado.
  - Indicador **rojo** + texto **Ocupada** cuando atiende un turno.
- **Tiempo aleatorio de atención (10 a 20 segundos)**: Al asignar un turno a una ventanilla libre, se asigna aleatoriamente un tiempo entero entre 10 y 20 segundos con cuenta regresiva visible. Al llegar a cero se finaliza automáticamente, y también puede finalizarse manualmente con el botón **Finalizar**.
- **Cancelación controlada con confirmación**: Solo pueden cancelarse turnos en estado `En espera` (por botón en la fila o ingresando su ID), solicitando confirmación explícita antes de aplicar el cambio.
- **Historial completo**: Muestra todos los turnos registrados en cualquiera de los cuatro estados (`En espera`, `En atención`, `Atendido`, `Cancelado`), con distinción visual y textual.
- **Persistencia local separada por entidad e íntegra**:
  - `turnos.json` almacena la entidad `Turno`.
  - `ventanillas.json` almacena la entidad `Ventanilla`.
  - Las escrituras se realizan de forma segura mediante archivos temporales y respaldo/reversión para evitar estados parciales ante fallos de guardado.
  - Si un archivo almacenado está dañado o es inconsistente al iniciar, el sistema informa el error y protege los archivos existentes sin sobrescribirlos.

## Estructura del proyecto (Patrón MVC)

```text
ProyectoFinal/
├── index.js                              # Proceso principal de Electron y canales IPC
├── package.json                          # Scripts y configuración de Jest y electron-builder
├── README.md                             # Instrucciones de uso, pruebas y empaquetado
├── estandar_codificacion.txt             # Reglas aplicadas del estándar Oracle Java
├── refactorizaciones_posibles.txt        # Archivo temporal con 5 posibles refactorizaciones
├── src/
│   ├── modelo/
│   │   ├── EstadoTurno.js                # Constantes y validación de estados del turno
│   │   ├── ErrorDominio.js               # Excepción para reglas de negocio
│   │   ├── ErrorPersistencia.js          # Excepción para fallos de almacenamiento
│   │   ├── Turno.js                      # Entidad Turno y sus transiciones válidas
│   │   ├── Ventanilla.js                 # Entidad Ventanilla (V01-V03) e indicador verde/rojo
│   │   ├── GestorTurnos.js               # Lógica pura del dominio FIFO (sin UI ni disco)
│   │   └── persistencia/
│   │       └── RepositorioArchivos.js    # Lectura, validación y guardado atómico en JSON
│   ├── controlador/
│   │   └── ControladorTurnos.js          # Coordina dominio, persistencia y respuestas a la vista
│   └── vista/
│       ├── principal.html                # Interfaz gráfica en español
│       ├── estilos.css                   # Estilos e indicadores verde/rojo y estados
│       ├── aplicacion.js                 # Eventos de interfaz y temporizadores visuales
│       └── puente.js                     # Preload seguro con contextBridge (IPC)
└── pruebas/
    ├── turno.test.js                     # Pruebas unitarias de entidad Turno
    ├── ventanilla.test.js                # Pruebas unitarias de entidad Ventanilla
    ├── gestorTurnos.test.js              # Pruebas de reglas FIFO, estados, límites y rechazos
    └── persistenciaYControlador.test.js  # Pruebas de persistencia, reinicio, fallos y rendimiento
```

## Requisitos previos

- **Node.js** (versión 18 o superior) y **npm**.

## Instalación de dependencias

Desde la carpeta raíz del proyecto:

```bash
npm install
```

## Ejecución normal de la aplicación

Para abrir la aplicación de escritorio en modo desarrollo con Electron:

```bash
npm run dev
```

## Ejecución de pruebas unitarias

Las pruebas unitarias están implementadas con **Jest** y cubren casos normales, inválidos, de límite, recuperación tras reinicio, fallos controlados de escritura y desempeño con 200 turnos.

Para ejecutarlas:

```bash
npm test
```

Para ver el detalle de cada caso de prueba:

```bash
npm test -- --verbose
```

## Empaquetado con Electron

Para generar el ejecutable empaquetado en la carpeta `dist/`:

```bash
npm run build
```

### Decisiones técnicas sobre rutas y persistencia al empaquetar

- **Recursos de interfaz**: Las rutas a `principal.html` y `puente.js` se resuelven con `path.join(__dirname, ...)` para funcionar tanto en ejecución local como dentro del paquete `app.asar` generado por `electron-builder`.
- **Archivos de datos persistentes**: Al empaquetar una aplicación con Electron, el paquete interno `app.asar` es de solo lectura. Por ello, los archivos de entidades (`turnos.json` y `ventanillas.json`) se almacenan en el directorio de datos de usuario administrado por Electron:
  - `app.getPath("userData")/datos/turnos.json`
  - `app.getPath("userData")/datos/ventanillas.json`
  - En Windows corresponde habitualmente a `%APPDATA%/proyectofinal/datos/`.
- **Formato JSON por entidad**: Se eligió JSON formateado por ser texto plano legible, evitar errores de escape de delimitadores y facilitar la validación estructural estricta al iniciar.
- **Separación de responsabilidades**: `GestorTurnos`, `Turno` y `Ventanilla` no dependen de Electron ni del sistema de archivos, lo que permite verificar todas las reglas FIFO y de estado mediante pruebas unitarias aisladas.

## Documentación adicional

- `estandar_codificacion.txt`: Detalle de las reglas aplicadas del estándar *Code Conventions for the Java Programming Language* de Oracle.
- `refactorizaciones_posibles.txt`: Listado temporal de 5 posibles refactorizaciones identificadas en el código fuente.
