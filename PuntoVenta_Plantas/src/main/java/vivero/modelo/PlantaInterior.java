package vivero.modelo;

public class PlantaInterior extends Planta {
    // SMELL: Pull Up Field (PDF diapositiva 161, 163)
    // Este campo 'nombreCientifico' está declarado de forma idéntica en PlantaInterior y PlantaExterior.
    // REFACTORIZACIÓN SUGERIDA: Subir el atributo 'nombreCientifico' a la superclase Planta.
    private String nombreCientifico;

    private int nivelHumedadRequerido;

    // SMELL: Pull Up Constructor Body (PDF diapositiva 161, 167-168)
    // Se duplica la inicialización manual de campos comunes en lugar de centralizarla mediante super(...).
    // REFACTORIZACIÓN SUGERIDA: Pasar 'nombreCientifico' al constructor de la superclase y llamar a super(...).
    public PlantaInterior(String id, String nombreComun, double precioBase, int stock, String nombreCientifico, int nivelHumedadRequerido) {
        super(id, nombreComun, precioBase, stock);
        this.nombreCientifico = nombreCientifico;
        this.nivelHumedadRequerido = nivelHumedadRequerido;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public int getNivelHumedadRequerido() {
        return nivelHumedadRequerido;
    }

    @Override
    public String getTipoAmbiente() {
        return "INTERIOR";
    }
}