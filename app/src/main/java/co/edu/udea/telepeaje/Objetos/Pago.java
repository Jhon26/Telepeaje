package co.edu.udea.telepeaje.Objetos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class Pago {
    String tipo="";
    long numeroTarjeta=0;
    int mesVencimiento=0;
    int anoVencimiento=0;
    String cvv="";
    String correo="";
    String contrasena="";

    public Pago() {
    }

    public Pago(String tipo, long numeroTarjeta, int mesVencimiento, int anoVencimiento, String cvv, String correo, String contrasena) {
        this.tipo = tipo;
        this.numeroTarjeta = numeroTarjeta;
        this.mesVencimiento = mesVencimiento;
        this.anoVencimiento = anoVencimiento;
        this.cvv = cvv;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public long getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(long numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getMesVencimiento() {
        return mesVencimiento;
    }

    public void setMesVencimiento(int mesVencimiento) {
        this.mesVencimiento = mesVencimiento;
    }

    public int getAnoVencimiento() {
        return anoVencimiento;
    }

    public void setAnoVencimiento(int anoVencimiento) {
        this.anoVencimiento = anoVencimiento;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    //Lleva los datos del objeto a un HashMap para efectos de actualizaciones de algun dato en la base de datos
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("tipo", tipo);
        result.put("numeroTarjeta", numeroTarjeta);
        result.put("mesVencimiento", mesVencimiento);
        result.put("anoVencimiento", anoVencimiento);
        result.put("cvv", cvv);
        result.put("correo", correo);
        result.put("contrasena", contrasena);
        return result;
    }
}
