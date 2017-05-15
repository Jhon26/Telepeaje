package co.edu.udea.telepeaje;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , MisAutosFragment.OnFragmentInteractionListener
        , InfoPersonalFragment.OnFragmentInteractionListener{

    //Listener para la base de datos
    FirebaseAuth.AuthStateListener mAuthListener;

    TextView textViewCorreo, textViewNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){//Sesion cerrada
                    Log.i("SESSION", "sesión cerrada");
                }
            }
        };

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);
        textViewCorreo = (TextView) view.findViewById(R.id.correoTextView);
        textViewNombre = (TextView) view.findViewById(R.id.nombreTextView);

        textViewCorreo.setText( FirebaseAuth.getInstance().getCurrentUser().getEmail());
        textViewNombre.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        //Inicializo el primer fragment
        Fragment fragment = new MisAutosFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        cambiarTitulo(R.string.mis_autos_title);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Bundle bundle = null;
        Fragment fragment = null;
        boolean fragmentoElegido = false;

        if (id == R.id.nav_mis_vehiculos) {

            fragment = new MisAutosFragment();
            fragmentoElegido = true;
            cambiarTitulo(R.string.mis_autos_title);
        }else if(id == R.id.nav_info_personal) {
            fragment = new InfoPersonalFragment();
            fragmentoElegido = true;
            cambiarTitulo(R.string.mis_autos_title);
            bundle = new Bundle();
            bundle.putBoolean("origen",true);
            fragment.setArguments(bundle);
        }

        if(fragmentoElegido){
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, fragment).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cambiarTitulo(int idTituloNuevo){
        this.setTitle(idTituloNuevo);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
