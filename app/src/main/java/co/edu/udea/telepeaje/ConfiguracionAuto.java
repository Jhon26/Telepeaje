package co.edu.udea.telepeaje;

/**
 * Created by estudiantelis on 4/04/17.
 */

public class ConfiguracionAuto {
    public static String pago;//Expresa el pago seleccionado para un auto
    public static String activity="";//Indica la actividad desde la cual se configuró  algún parámetro del auto

    public static String getActivity() {
        return activity;
    }

    public static void setActivity(String activity) {
        ConfiguracionAuto.activity = activity;
    }

    public static String getPago() {
        return pago;
    }

    public static void setPago(String pago) {
        ConfiguracionAuto.pago = pago;
    }
}
