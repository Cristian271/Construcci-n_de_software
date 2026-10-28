package vivero.modelo;

public abstract class Planta {
    protected String id;
    protected String nombreComun;
    protected int stock;
    private double tasaImpuestoAmbiental = 0.05; //RF-05

    // SMELL: Encapsulate Field (PDF diapositiva 83-84)
    // El atributo es público y permite modificaciones directas sin validación desde fuera.
    // REFACTORIZACIÓN SUGERIDA: Volver el campo privado y proveer métodos get y set con validación.
    public double precioBase;

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

    // SMELL: Remove Parameter (PDF diapositiva 133-134)
    // El parámetro 'porcentajeLuz' se recibe obligatoriamente en todas las llamadas pero nunca se utiliza en el cuerpo.
    // REFACTORIZACIÓN SUGERIDA: Eliminar el parámetro 'porcentajeLuz' de la firma del método.
    public double calcularPrecioFinal(double porcentajeLuz, boolean incluyeFertilizante) {
        double total = this.precioBase;
        if (incluyeFertilizante) {
            total += 45.0;
        }
        return total;
    }

    public abstract String getTipoAmbiente();
}