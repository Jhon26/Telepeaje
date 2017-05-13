package co.edu.udea.telepeaje;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import co.edu.udea.telepeaje.Objetos.Usuario;

public class InfoPersonal extends AppCompatActivity implements InfoPersonalFragment.OnFragmentInteractionListener, MisAutosFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_personal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");

        Bundle bundle = new Bundle();
        bundle.putSerializable("usuario",usuario);
        bundle.putBoolean("origen", false);


        Fragment fragment = new InfoPersonalFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
