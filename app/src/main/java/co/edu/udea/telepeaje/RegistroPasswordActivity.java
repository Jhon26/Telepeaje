package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistroPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_password);
    }

    public void openInformacionPersonal(View view){
        Intent intent = new Intent(this, InformacionPersonalActivity.class);
        startActivity(intent);
    }
}
