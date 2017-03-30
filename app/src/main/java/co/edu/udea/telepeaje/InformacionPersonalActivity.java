package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class InformacionPersonalActivity extends AppCompatActivity {

    /*
        Para las variables que se pasan en un intent, es una buena practica preceder su nombre del nombre del paquete,
        ya que si no se hace esto, la clase que recibe el extra puede recibir otra variable con el mismo nombre y se pueden
        originar conflictos.

    public static final String EXTRA_ORIGEN = "co.edu.udea.telepeaje.InformacionPersonalActivity.CLASE_ORIGEN"; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);
        /*Spinner spinner_animales = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.tiposIdentificacion , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_animales.setAdapter(spinner_adapter);*/
    }

    public void openInformacionVehiculo(View view){
        Intent intent = new Intent(this, InformacionVehiculoActivity.class);
        String origen = this.getLocalClassName();
        intent.putExtra("claseOrigen", origen);
        startActivity(intent);
    }

}
