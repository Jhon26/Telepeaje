package co.edu.udea.telepeaje.Objetos;

import java.io.Serializable;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

//Clase POJO para un usuario. Implementa la interfaz Serializable para que pueda ser pasada a través de activities
public class Usuario implements Serializable{
    String correo;
    String nombres;
    String apellidos;
    long celular;

    public Usuario() {
    }

    public Usuario(String correo, String nombres, String apellidos, long celular) {
        this.correo = correo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
}
