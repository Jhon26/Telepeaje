package co.edu.udea.telepeaje.Objetos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class Auto {
    String nombrePropietario;
    String tipoDocPropietario;
    String numeroDocPropietario;
    String placa;
    String nombrePersonalizado;
    boolean peajesHabilitados;
    String idPagoCorrespondiente;

    public Auto() {
    }

    public Auto(String nombrePropietario, String tipoDocPropietario, String numeroDocPropietario, String placa,
                String nombrePersonalizado, boolean peajesHabilitados, String idPagoCorrespondiente) {
        this.nombrePropietario = nombrePropietario;
        this.tipoDocPropietario = tipoDocPropietario;
        this.numeroDocPropietario = numeroDocPropietario;
        this.placa = placa;
        this.nombrePersonalizado = nombrePersonalizado;
        //this.cantidadPeajesHabilitados = cantidadPeajesHabilitados;
        this.peajesHabilitados = peajesHabilitados;
        this.idPagoCorrespondiente = idPagoCorrespondiente;
    }

    public String getIdPagoCorrespondiente() {
        return idPagoCorrespondiente;
    }

    public void setIdPagoCorrespondiente(String idPagoCorrespondiente) {
        this.idPagoCorrespondiente = idPagoCorrespondiente;
    }

    public boolean getPeajesHabilitados() {
        return peajesHabilitados;
    }

    public void setPeajesHabilitados(boolean cantidadPeajesHabilitados) {
        this.peajesHabilitados = cantidadPeajesHabilitados;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getTipoDocPropietario() {
        return tipoDocPropietario;
    }

    public void setTipoDocPropietario(String tipoDocPropietario) {
        this.tipoDocPropietario = tipoDocPropietario;
    }

    public String getNumeroDocPropietario() {
        return numeroDocPropietario;
    }

    public void setNumeroDocPropietario(String numeroDocPropietario) {
        this.numeroDocPropietario = numeroDocPropietario;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNombrePersonalizado() {
        return nombrePersonalizado;
    }

    public void setNombrePersonalizado(String nombrePersonalizado) {
        this.nombrePersonalizado = nombrePersonalizado;
    }

    //Lleva los datos del objeto a un HashMap para efectos de actualizaciones de algun dato en la base de datos
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nombrePropietario", nombrePropietario);
        result.put("tipoDocPropietario", tipoDocPropietario);
        result.put("numeroDocPropietario", numeroDocPropietario);
        result.put("placa", placa);
        result.put("nombrePersonalizado", nombrePersonalizado);
        result.put("peajesHabilitados", peajesHabilitados);
        result.put("idPagoCorrespondiente", idPagoCorrespondiente);
        return result;
    }
}
