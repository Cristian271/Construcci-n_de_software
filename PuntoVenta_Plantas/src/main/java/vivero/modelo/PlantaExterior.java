package vivero.modelo;

public class PlantaExterior extends Planta {
    // SMELL: Pull Up Field (PDF diapositiva 161, 163)
    // Campo duplicado con PlantaInterior con el mismo significado.
    // REFACTORIZACIÓN SUGERIDA: Subir el campo 'nombreCientifico' a la superclase Planta.
    private String nombreCientifico;

    private boolean toleraHeladas;

    // SMELL: Pull Up Constructor Body (PDF diapositiva 161, 167-168)
    // Asignación manual redundante del campo 'nombreCientifico'.
    // REFACTORIZACIÓN SUGERIDA: Delegar la inicialización completa a la superclase con super(...).
    public PlantaExterior(String id, String nombreComun, double precioBase, int stock, String nombreCientifico, boolean toleraHeladas) {
        super(id, nombreComun, precioBase, stock);
        this.nombreCientifico = nombreCientifico;
        this.toleraHeladas = toleraHeladas;
    }

    public String getNombreCientifico() {
        return nombreCientifico;
    }

    public boolean isToleraHeladas() {
        return toleraHeladas;
    }

    @Override
    public String getTipoAmbiente() {
        return "EXTERIOR";
    }
}