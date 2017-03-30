package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
        String placa =  intent.getStringExtra("placa");
        String nombreAuto = intent.getStringExtra("nombreAuto");
        String pago = intent.getStringExtra("pago");

        //Se obtienen los TextView de la actividad actual
        TextView placaTextView = (TextView) findViewById(R.id.placaMiAuto);
        TextView pagoTextView= (TextView) findViewById(R.id.pagoMiAuto);

        //Se les pone valor a los TextView
        placaTextView.setText(placaTextView.getText().toString().concat(placa));
        pagoTextView.setText(pagoTextView.getText().toString().concat(pago));
    }
}
