package vivero.modelo;

public class PlantaInterior extends Planta {
    private int nivelHumedadRequerido;

    // RF-20: Pull Up Constructor Body
    public PlantaInterior(String id, String nombreComun, double precioBase, int stock, String nombreCientifico, int nivelHumedadRequerido) {
        super(id, nombreComun, precioBase, stock, nombreCientifico);
        this.nivelHumedadRequerido = nivelHumedadRequerido;
    }

    public int getNivelHumedadRequerido() {
        return nivelHumedadRequerido;
    }

    @Override
    public String getTipoAmbiente() {
        return "INTERIOR";
    }
}