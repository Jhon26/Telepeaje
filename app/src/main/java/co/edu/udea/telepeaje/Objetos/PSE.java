package co.edu.udea.telepeaje.Objetos;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class PSE extends Pago{
    String correo;
    String contrasena;

    public PSE() {
    }

    public PSE(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
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
}
