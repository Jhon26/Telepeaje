package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "co.edu.udea.telepeaje.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, AutorizarPagoMapActivity.class);
        startActivity(intent);
    }

    public void openRegistroEmail(View view){
        Intent intent = new Intent(this, RegistroEmailActivity.class);
        startActivity(intent);
    }
}
