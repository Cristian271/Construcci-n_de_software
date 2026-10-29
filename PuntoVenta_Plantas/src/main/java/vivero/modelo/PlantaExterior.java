package vivero.modelo;

public class PlantaExterior extends Planta {
    private boolean toleraHeladas;

    // RF-20: Pull Up Constructor Body
    public PlantaExterior(String id, String nombreComun, double precioBase, int stock, String nombreCientifico, boolean toleraHeladas) {
        super(id, nombreComun, precioBase, stock, nombreCientifico);
        this.toleraHeladas = toleraHeladas;
    }

    public boolean isToleraHeladas() {
        return toleraHeladas;
    }

    @Override
    public String getTipoAmbiente() {
        return "EXTERIOR";
    }
}