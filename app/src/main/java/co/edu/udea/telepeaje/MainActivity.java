package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "co.edu.udea.telepeaje.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
    }


    public void sendMessage(View view){
        /*Intent intent = new Intent(this, AutorizarPagoMapActivity.class);
        startActivity(intent);*/
    }

    public void openRegistroEmail(View view){
        Intent intent = new Intent(this, RegistroEmailActivity.class);
        startActivity(intent);
    }

    public void openMisAutos(View view){
        Intent intent = new Intent(this, MisAutosActivity.class);
        startActivity(intent);
    }

    public void mostrarSolucion(View view){
        Toast.makeText(this, "¡Se embaló!", Toast.LENGTH_SHORT).show();
    }
}
