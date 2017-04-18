package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MisAutosActivity extends AppCompatActivity {

    /*
        Para las variables que se pasan en un intent, es una buena practica preceder su nombre del nombre del paquete,
        ya que si no se hace esto, la clase que recibe el extra puede recibir otra variable con el mismo nombre y se pueden
        originar conflictos.

    public static final String EXTRA_ORIGEN = "co.edu.udea.telepeaje.MisAutosActivity.CLASE_ORIGEN";*/

    //Referencia a elementos de la interfaz
    TextView textViewNombreUsuario;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_autos);

        //Referencia a elementos de la interfaz
        textViewNombreUsuario = (TextView) findViewById(R.id.nombre_usuario_text_view);

        //Se ponen los datos de la cuenta en los elementos de la interfaz
        textViewNombreUsuario.setText("¡Hola "+FirebaseAuth.getInstance().getCurrentUser().getEmail()+"!");

        //Listener para la autenticación
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user==null){//Sesion cerrada
                    Log.i("SESSION", "sesión cerrada");
                    finish();
                }
            }
        };
    }

    public void openInformacionVehiculo(View view){
        Intent intent = new Intent(this, InformacionVehiculoActivity.class);
        String origen = this.getLocalClassName();
        intent.putExtra("claseOrigen", origen);
        startActivity(intent);
    }

    //En este caso la view es la cardView que invoca a este metodo
    public void openMiAuto(View view){
        Intent intent = new Intent(this, MiAutoActivity.class);
        //Segun el tag de la cardView, se extrae el texto de los elementos que esta contiene
        TextView placaTextView = (TextView) findViewById(getResources().getIdentifier("placa".concat(view.getTag().toString()), "id", getPackageName()));
        String placa = placaTextView.getText().toString();
        intent.putExtra("placa", placa);
        TextView nombreAutoTextView = (TextView) findViewById(getResources().getIdentifier("nombreAuto".concat(view.getTag().toString()), "id", getPackageName()));
        String nombreAuto = nombreAutoTextView.getText().toString();
        intent.putExtra("nombreAuto", nombreAuto);
        TextView pagoTextView = (TextView) findViewById(getResources().getIdentifier("pago".concat(view.getTag().toString()), "id", getPackageName()));
        String pago = pagoTextView.getText().toString();
        ConfiguracionAuto.setPago(pago);
        //intent.putExtra("pago", pago);
        TextView peajesTextView = (TextView) findViewById(getResources().getIdentifier("cantidadPeajes".concat(view.getTag().toString()), "id", getPackageName()));
        String peajes = peajesTextView.getText().toString();
        intent.putExtra("peajes", peajes);

        //Se pone vacía la actividad desde la cual se configuró el auto, ya que en esta actividad no lo estamos configurando
        ConfiguracionAuto.setActivity("");
        startActivity(intent);
    }

    //Cierra la sesión
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Log.i("SESSION", "sesión cerrada");
    }

    public void actionLogout(View view){
        logout();
    }

    @Override
    protected void onStart(){
        super.onStart();
        //Se inicia el listener
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //Se elimina el listener
        if(mAuthListener!=null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    /*
    public void openConfiguracionPeajes(View view){
        Intent intent = new Intent(this, ConfiguracionPeajesActivity.class);
    }*/
}
