package vivero.modelo;

public class PlantaInterior extends Planta {
    // RF-20: Pull Up Field
    private String nombreCientifico;

    private int nivelHumedadRequerido;

    // RF20: Pull Up Constructor Body
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