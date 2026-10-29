package vivero.modelo;

public abstract class Planta {
    protected String id;
    protected String nombreComun;
    protected int stock;
    private double tasaImpuestoAmbiental = 0.05; //RF-05

    // REFACTORIZACION ENCAPSUALTE FIELD
    private double precioBase;

    public Planta(String id, String nombreComun, double precioBase, int stock) {
        this.id = id;
        this.nombreComun = nombreComun;
        this.precioBase = precioBase;
        this.stock = stock;
    }
    // Getter y Setter para el campo movido (RF-05)
    public double getTasaImpuestoAmbiental() {
        return tasaImpuestoAmbiental;
    }

    public void setTasaImpuestoAmbiental(double tasaImpuestoAmbiental) {
        this.tasaImpuestoAmbiental = tasaImpuestoAmbiental;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public String getId() {
        return id;
    }

    public String getNombreComun() {
        return nombreComun;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // REFACTORIZACION: Remove Parameter
    public double calcularPrecioFinal(boolean incluyeFertilizante) {
        double total = this.precioBase;
        if (incluyeFertilizante) {
            total += 45.0;
        }
        return total;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public abstract String getTipoAmbiente();
}