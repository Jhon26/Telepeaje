package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class InformacionPagoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_pago);
    }

    public void openMisAutos(View view){
        Intent intent = new Intent(this, MisAutosActivity.class);
        startActivity(intent);
    }
}
