package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SeleccionarPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_pago);
    }

    public void seleccionarPago(View view){
        Intent intent = new Intent(this, MiAutoActivity.class);
        //Se pone el pago seleccionado en el botón de la descripción del auto
        Button pago = (Button) findViewById(R.id.pagoMiAuto);
        pago.setText(view.getTag().toString());
        finish();
    }
}
