import java.util.ArrayList;

public class GestorReportes {
    private ArrayList<Reporte> listaReportes;
    private int contador = 0;
    public GestorReportes(){
        listaReportes = new ArrayList();
    }

    public void agregarReporte(Reporte nuevoReporte){
        nuevoReporte.setIdentificador("INC-"+ contador);
        contador++;
        listaReportes.add(nuevoReporte);
    }

    public Reporte buscarReporte(String idBuscado){
        for (Reporte reporte: listaReportes) {
            if (reporte.getIdentificador().equals(idBuscado)) {
                return reporte;
            }
        }
        return null;
    }

    public void actualizarEstado(Reporte reporteActualizar, Estado nuevoEstado){
        reporteActualizar.setEstado(nuevoEstado);
    }

    public void imprimirReportes(){
        if(listaReportes == null || listaReportes.isEmpty()){
            System.out.println("No hay incidencias registradas");
        } else{
            for(Reporte reporte: listaReportes){
                System.out.println(reporte.obtenerDetalle());
            }
        }
    }


}
