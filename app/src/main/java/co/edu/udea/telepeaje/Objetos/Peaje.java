package co.edu.udea.telepeaje.Objetos;

/**
 * Created by JHON on 6/05/2017.
 */

public class Peaje {
    String nombre;
    Double lat;
    Double lon;
    int precio;

    public Peaje() {
    }

    public Peaje(String nombre, Double lat, Double lon, int precio) {
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String id) {
        this.nombre = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
