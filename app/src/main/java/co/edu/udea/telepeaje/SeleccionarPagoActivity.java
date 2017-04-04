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
        //Se manda a una clase intermedia el tag del botón que se seleccionó como método de pago
        ConfiguracionAuto.setPago(view.getTag().toString());
        //Se manda la actividad desde la cual se configuró el auto, para efectos del toast que aparece en MiAutoActivity
        ConfiguracionAuto.setActivity(this.getLocalClassName().toString());
        //Se finaliza esta actividad para que al estar ubicado en en la actividad "MiAuto" y se
        //le de atrás, no se abra esta actividad
        finish();
    }
}
