package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.TextView;

public class MiAutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_auto);
        String tag = getIntent().getStringExtra("tarjetaOrigen");
        String nombrePlaca = "placa"+tag;
        TextView placaT = (TextView) findViewById(getResources().getIdentifier(nombrePlaca, "id", getPackageName()));
        TextView placa = (TextView) findViewById(R.id.placaMiAuto);
        //placa.setText(placaT.getText().toString());
    }
}
