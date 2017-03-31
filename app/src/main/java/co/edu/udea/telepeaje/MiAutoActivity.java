package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MiAutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_auto);
        //Toast.makeText(this, getIntent().getStringExtra("placaV").toString(), Toast.LENGTH_SHORT).show();

        //Se obtienen los valores de los campos de la cardView
        Intent intent = getIntent();
        String nombreAuto = intent.getStringExtra("nombreAuto");
        String pago = intent.getStringExtra("pago");
        String peajes = intent.getStringExtra("peajes");
        String placa =  intent.getStringExtra("placa");

        //Se obtienen los componentes de la actividad actual
        TextView placaTextView = (TextView) findViewById(R.id.placaMiAuto);
        Button pagoButton= (Button) findViewById(R.id.pagoMiAuto);
        Button peajesButton= (Button) findViewById(R.id.peajesMiAuto);

        //Se les pone valor a los componentes
        peajesButton.setText(peajesButton.getText().toString().concat(peajes));
        placaTextView.setText(placaTextView.getText().toString().concat(placa));
        pagoButton.setText(pagoButton.getText().toString().concat(pago));
    }

    public void openSeleccionarPago(View view){
        Intent intent = new Intent(this, SeleccionarPagoActivity.class);
        startActivity(intent);
    }
}
