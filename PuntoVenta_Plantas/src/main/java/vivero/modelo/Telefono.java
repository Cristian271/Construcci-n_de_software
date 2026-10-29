package vivero.modelo;

public class Telefono {
    private String telefonoCodigoPais;
    private String telefonoNumero;
    private String telefonoExtension;

    public Telefono(String telefonoCodigoPais, String telefonoNumero, String telefonoExtension) {
        this.telefonoExtension = telefonoExtension;
        this.telefonoNumero = telefonoNumero;
        this.telefonoCodigoPais = telefonoCodigoPais;
    }

    public String getTelefonoCodigoPais() {
        return telefonoCodigoPais;
    }

    public void setTelefonoCodigoPais(String telefonoCodigoPais) {
        this.telefonoCodigoPais = telefonoCodigoPais;
    }

    public String getTelefonoNumero() {
        return telefonoNumero;
    }

    public void setTelefonoNumero(String telefonoNumero) {
        this.telefonoNumero = telefonoNumero;
    }

    public String getTelefonoExtension() {
        return telefonoExtension;
    }

    public void setTelefonoExtension(String telefonoExtension) {
        this.telefonoExtension = telefonoExtension;
    }

    public String getTelefonoCompleto() {
        return "+" + getTelefonoCodigoPais() + " (" + getTelefonoNumero() + ") ext. " + getTelefonoExtension();
    }
}
