package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.UsuarioDB;

public class MisAutosActivity extends AppCompatActivity {

    /*
        Para las variables que se pasan en un intent, es una buena practica preceder su nombre del nombre del paquete,
        ya que si no se hace esto, la clase que recibe el extra puede recibir otra variable con el mismo nombre y se pueden
        originar conflictos.

    public static final String EXTRA_ORIGEN = "co.edu.udea.telepeaje.MisAutosActivity.CLASE_ORIGEN";*/

    //Referencia a elementos de la interfaz
    TextView textViewNombreUsuario;
    FloatingActionButton buttonAgregarAuto;
    private ViewGroup layout;

    //Listener para la base de datos
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_autos);

        //Referencia a elementos de la interfaz
        textViewNombreUsuario = (TextView) findViewById(R.id.nombre_usuario_text_view);
        buttonAgregarAuto = (FloatingActionButton) findViewById(R.id.agregar_auto_button);
        layout = (ViewGroup) findViewById(R.id.content);

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
                    Intent intent = new Intent(MisAutosActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        //Lectura de los autos desde la BD
        //Instancia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Referencia de todos los usuarios
        DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        //Referencia al usuario actual
        String usuarioKey = getIntent().getStringExtra("usuarioKey");
        DatabaseReference usuarioRef = usuariosRef.child(usuarioKey);
        //Referencia a todos los autos del usuario actual
        DatabaseReference autosRef = usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE);
        autosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se limpian las cardView infladas
                layout.removeAllViews();
                //Se meten todos los autos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                //Se recorre ese Iterable
                while(autos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot autoDS = autos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo auto
                    Auto auto = autoDS.getValue(Auto.class);
                    //Se ponen los atributos de ese objeto en su correspondiente cardView
                    ponerAuto(auto);

                    /*//Se imprimen los atributos del auto
                    Log.i("Auto", auto.getNombrePersonalizado().toString());*/
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*//Finalmente se escribe el auto en la base de datos para el usuario correspondiente.
        //Primero se toma la instancia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Se toma la key del usuario definida desde la actividad anterior
        String usuarioKey = getIntent().getStringExtra("usuarioKey");
        //Se toma la referencia de todos los usuarios
        DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        //Se busca el usuario correspondiente dentro de todos los usuarios
        DatabaseReference usuarioRef = usuariosRef.child(usuarioKey);
        // Al usuario se le añade un "hijo" llamado autos y se le "empuja" un primer valor
        usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE).push().setValue(auto);*/

    }

    public void agregarAuto(View view){
        Intent intent = new Intent(this, InformacionVehiculoActivity.class);
        String origen = this.getLocalClassName();
        intent.putExtra("claseOrigen", origen);
        intent.putExtra("usuarioKey", getIntent().getStringExtra("usuarioKey"));
        startActivity(intent);
    }

    //En este caso la view es la cardView que invoca a este metodo
    public void openMiAuto(View view){
        Intent intent = new Intent(this, MiAutoActivity.class);
        //Segun el tag de la cardView, se extrae el texto de los elementos que esta contiene
        TextView placaTextView = (TextView) findViewById(getResources().getIdentifier("editTextPlaca".concat(view.getTag().toString()), "id", getPackageName()));
        String placa = placaTextView.getText().toString();
        intent.putExtra("editTextPlaca", placa);
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

    @SuppressLint("InlinedApi")
    private void ponerAuto(Auto auto){
        LayoutInflater inflater = LayoutInflater.from(this);
        int id = R.layout.layout_card_view_auto;
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);
        TextView textViewNombreAuto = (TextView) relativeLayout.findViewById(R.id.nombre_auto_text_view);
        TextView textViewCantPeajes = (TextView) relativeLayout.findViewById(R.id.cant_peajes_text_view);
        TextView textViewPlaca = (TextView) relativeLayout.findViewById(R.id.placa_text_view);
        TextView textViewPago = (TextView) relativeLayout.findViewById(R.id.pago_text_view);
        textViewNombreAuto.setText(auto.getNombrePersonalizado().toString());
        textViewCantPeajes.setText("0 peajes habilitados");
        textViewPlaca.setText(auto.getPlaca().toString());
        textViewPago.setText("VISA");
        layout.addView(relativeLayout);
    }

    /*
    public void openConfiguracionPeajes(View view){
        Intent intent = new Intent(this, ConfiguracionPeajesActivity.class);
    }*/
}
