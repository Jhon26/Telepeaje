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
        String pago = ConfiguracionAuto.getPago();
        String peajes = intent.getStringExtra("peajes");
        String placa =  intent.getStringExtra("editTextPlaca");

        //Se obtienen los componentes de la actividad actual
        TextView placaTextView = (TextView) findViewById(R.id.placaMiAuto);
        Button pagoButton= (Button) findViewById(R.id.pagoMiAuto);
        Button peajesButton= (Button) findViewById(R.id.peajesMiAuto);

        //Se les pone valor a los componentes
        peajesButton.setText(peajesButton.getText().toString().concat(peajes));
        placaTextView.setText(placaTextView.getText().toString().concat(placa));
        pagoButton.setText(pago);
    }

    //Cuando se finaliza SeleccionarPagoActivity y vuelve a aparecer esta actividad, se ejecuta este método
    @Override
    protected void onResume() {
        super.onResume();
        String pago = ConfiguracionAuto.getPago();
        Button pagoButton= (Button) findViewById(R.id.pagoMiAuto);
        pagoButton.setText(pago);
        if(!ConfiguracionAuto.getActivity().equals(""))
        Toast.makeText(this, "Método de pago cambiado", Toast.LENGTH_SHORT).show();
    }

    public void openSeleccionarPago(View view){
        Intent intent = new Intent(this, SeleccionarPagoActivity.class);
        startActivity(intent);
    }

    public void openSeleccionarPeajes(View view){
        Intent intent = new Intent(this, SeleccionarPeajesActivity.class);
        startActivity(intent);
    }
}
