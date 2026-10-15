package vivero.modelo;

public class Membresia {
    private String tipo;
    private double porcentajeDescuento;

    public Membresia(String tipo, double porcentajeDescuento) {
        this.tipo = tipo;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public String getTipo() {
        return tipo;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }
}