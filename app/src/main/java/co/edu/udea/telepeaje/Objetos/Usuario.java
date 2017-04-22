package co.edu.udea.telepeaje.Objetos;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class Usuario {
    String email;
    String nombres;
    String apellidos;
    long celular;
    Auto[] autos;
    Pago[] pagos;

    public Usuario() {
    }

    public Usuario(String email, String nombres, String apellidos, long celular, Auto[] autos, Pago[] pagos) {
        this.email = email;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
        this.autos = autos;
        this.pagos = pagos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getCelular() {
        return celular;
    }

    public void setCelular(long celular) {
        this.celular = celular;
    }

    public Auto[] getAutos() {
        return autos;
    }

    public void setAutos(Auto[] autos) {
        this.autos = autos;
    }

    public Pago[] getPagos() {
        return pagos;
    }

    public void setPagos(Pago[] pagos) {
        this.pagos = pagos;
    }
}
