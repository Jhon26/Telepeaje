package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import co.edu.udea.telepeaje.Objetos.Pago;

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

    //Referencia al usuario actual en la base de datos
    DatabaseReference usuarioRef;


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
        //Se carga la referencia al usuario actual mediante el SharedPreferences
        //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");
        usuarioRef = usuariosRef.child(UID);
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
                    ponerAuto(auto, autoDS.getKey());

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
        TextView placaTextView = (TextView) view.findViewById(R.id.placa_text_view);
        String placa = placaTextView.getText().toString();
        intent.putExtra("editTextPlaca", placa);
        TextView nombreAutoTextView = (TextView) view.findViewById(R.id.nombre_auto_text_view);
        String nombreAuto = nombreAutoTextView.getText().toString();
        intent.putExtra("nombreAuto", nombreAuto);
        TextView pagoTextView = (TextView) view.findViewById(R.id.pago_text_view);
        String pago = pagoTextView.getText().toString();
        intent.putExtra("pago", pago);
        TextView peajesTextView = (TextView) view.findViewById(R.id.peajes_habilitados_text_view);
        String peajes = peajesTextView.getText().toString();
        intent.putExtra("peajes", peajes);
        CardView autoCardView = (CardView) view.findViewById(R.id.auto_card_view);
        String tagCardView = autoCardView.getTag().toString();
        intent.putExtra("tagCardView", tagCardView);

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
    private void ponerAuto(final Auto auto, final String autoKey){
        //Se coge el pago que está asociado al auto
        //Referencia a todos los pagos del usuario actual
        DatabaseReference pagosRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE);
        pagosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se limpian las cardView infladas
                layout.removeAllViews();
                //Se meten todos los pagos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> pagos = dataSnapshot.getChildren();
                //Se define el pago que se intenta buscar
                Pago pago = null;
                //Se recorre ese Iterable para buscar el pago asociado al auto
                while(pagos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot pagoDS = pagos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo auto
                    if((pagoDS.getKey()==auto.getIdPagoCorrespondiente())||(pagoDS.getKey().equals(auto.getIdPagoCorrespondiente()))){
                        pago = pagoDS.getValue(Pago.class);
                        //Se definen los elementos que van en el layout para cada auto
                        LayoutInflater inflater = LayoutInflater.from(MisAutosActivity.this);
                        int id = R.layout.layout_card_view_auto;
                        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(id, null, false);
                        CardView cardView = (CardView) relativeLayout.findViewById(R.id.auto_card_view);
                        TextView textViewNombreAuto = (TextView) relativeLayout.findViewById(R.id.nombre_auto_text_view);
                        TextView textViewPeajesHabilitados = (TextView) relativeLayout.findViewById(R.id.peajes_habilitados_text_view);
                        TextView textViewPlaca = (TextView) relativeLayout.findViewById(R.id.placa_text_view);
                        TextView textViewPago = (TextView) relativeLayout.findViewById(R.id.pago_text_view);
                        //Se configuran los elementos
                        textViewNombreAuto.setText(auto.getNombrePersonalizado().toString());
                        if(auto.getPeajesHabilitados()){
                            textViewPeajesHabilitados.setText("Todos los peajes estan habilitados");
                        }else{
                            textViewPeajesHabilitados.setText("Todos los peajes estan deshabilitados");
                        }
                        textViewPlaca.setText(auto.getPlaca().toString());
                        //Se formatea el método de pago para que no se muestre completamente
                        String numeroTarjeta = String.valueOf(pago.getNumeroTarjeta());
                        char [] numeroTarjetaFormateada = new char[9];
                        numeroTarjeta.getChars(12, 16, numeroTarjetaFormateada, 0);
                        textViewPago.setText("xxxx "+String.valueOf(numeroTarjetaFormateada));
                        //El tag de la cardView será el id del auto
                        cardView.setTag(autoKey);
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openMiAuto(v);
                            }
                        });
                        layout.addView(relativeLayout);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Al pulsar el botón de atrás, no se irá ni a la MainActivity ni a ninguna de las actividades de registro, sólo se abrirá
    //la actividad HOME de android
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
    public void openConfiguracionPeajes(View view){
        Intent intent = new Intent(this, ConfiguracionPeajesActivity.class);
    }*/
}
