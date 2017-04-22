package co.edu.udea.telepeaje.Objetos;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class Auto {
    String nombrePropietario;
    String tipoDocPropietario;
    String numeroDocPropietario;
    String placa;
    String nombrePersonalizado;

    public Auto() {
    }

    public Auto(String nombrePropietario, String tipoDocPropietario, String numeroDocPropietario, String placa, String nombrePersonalizado) {
        this.nombrePropietario = nombrePropietario;
        this.tipoDocPropietario = tipoDocPropietario;
        this.numeroDocPropietario = numeroDocPropietario;
        this.placa = placa;
        this.nombrePersonalizado = nombrePersonalizado;
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
}
