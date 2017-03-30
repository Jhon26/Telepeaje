package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MisAutosActivity extends AppCompatActivity {

    /*
        Para las variables que se pasan en un intent, es una buena practica preceder su nombre del nombre del paquete,
        ya que si no se hace esto, la clase que recibe el extra puede recibir otra variable con el mismo nombre y se pueden
        originar conflictos.

    public static final String EXTRA_ORIGEN = "co.edu.udea.telepeaje.MisAutosActivity.CLASE_ORIGEN";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_autos);
    }

    public void openInformacionVehiculo(View view){
        Intent intent = new Intent(this, InformacionVehiculoActivity.class);
        String origen = this.getLocalClassName();
        intent.putExtra("claseOrigen", origen);
        startActivity(intent);
    }

    //En este caso la view es la cardView que invoca a este metodo
    public void openMiAuto(View view){
        Intent intent = new Intent(this, MiAutoActivity.class);
        //Segun el tag de la cardView, se extrae el texto de los elementos que esta contiene
        TextView placaTextView = (TextView) findViewById(getResources().getIdentifier("placa".concat(view.getTag().toString()), "id", getPackageName()));
        String placa = placaTextView.getText().toString();
        intent.putExtra("placa", placa);
        TextView nombreAutoTextView = (TextView) findViewById(getResources().getIdentifier("nombreAuto".concat(view.getTag().toString()), "id", getPackageName()));
        String nombreAuto = nombreAutoTextView.getText().toString();
        intent.putExtra("nombreAuto", nombreAuto);
        TextView pagoTextView = (TextView) findViewById(getResources().getIdentifier("pago".concat(view.getTag().toString()), "id", getPackageName()));
        String pago = pagoTextView.getText().toString();
        intent.putExtra("pago", pago);
        startActivity(intent);
    }
}
