import java.util.ArrayList;

public class GestorReportes {
    private ArrayList<Reporte> listaReportes;
    private int contador = 0;
    public GestorReportes(){
        listaReportes = new ArrayList<Reporte>();
    }

    public void agregarReporte(Reporte nuevoReporte){
        listaReportes.add(nuevoReporte);
        nuevoReporte.setIdentificador("INC-"+ contador);
        contador++;
    }

    public Reporte buscarReporte(String idBuscado){
        for (Reporte reporte: listaReportes) {
            if (reporte.getIdentificador().equals(idBuscado)) {
                return reporte;
            }
        }
        return null;
    }

    public void actualizarEstado(Reporte reporteAcualizar, Estado nuevoEstado){
        reporteAcualizar.setEstado(nuevoEstado);
    }

    public void imprimirReportes(){
        for(Reporte reporte: listaReportes){
            reporte.obtenerDetalle();
        }
    }


}
