package co.edu.udea.telepeaje.Objetos;

/**
 * Created by bairon.alvarez on 21/04/17.
 */

public class TarjetaDeCredito extends Pago {
    int numero;
    short mesVencimiento;
    short anoVencimiento;
    short cvv;

    public TarjetaDeCredito() {
    }

    public TarjetaDeCredito(int numero, short mesVencimiento, short anoVencimiento, short cvv) {
        this.numero = numero;
        this.mesVencimiento = mesVencimiento;
        this.anoVencimiento = anoVencimiento;
        this.cvv = cvv;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public short getMesVencimiento() {
        return mesVencimiento;
    }

    public void setMesVencimiento(short mesVencimiento) {
        this.mesVencimiento = mesVencimiento;
    }

    public short getAnoVencimiento() {
        return anoVencimiento;
    }

    public void setAnoVencimiento(short anoVencimiento) {
        this.anoVencimiento = anoVencimiento;
    }

    public short getCvv() {
        return cvv;
    }

    public void setCvv(short cvv) {
        this.cvv = cvv;
    }
}
