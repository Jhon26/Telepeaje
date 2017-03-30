package co.edu.udea.telepeaje;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class InformacionVehiculoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_vehiculo);
        Spinner spinner_animales = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.tiposIdentificacion , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_animales.setAdapter(spinner_adapter);

    }

    public void siguienteActivity(View view){
        String origen = getIntent().getStringExtra("claseOrigen");
        //Se establece cuál activity debe abrir el floating button
        if(origen.equals("InformacionPersonalActivity")){
            Intent intent = new Intent(this, InformacionPagoActivity.class);
            startActivity(intent);
        }else if(origen.equals("MisAutosActivity")){
            finish();
        }
    }
}
