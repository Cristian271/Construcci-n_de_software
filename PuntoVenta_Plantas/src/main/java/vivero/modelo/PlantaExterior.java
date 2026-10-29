package vivero.modelo;

public class PlantaExterior extends Planta {
    // RF-20: Pull Up Field
    private String nombreCientifico;

    private boolean toleraHeladas;

    // RF-20: Pull Up Constructor Body
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