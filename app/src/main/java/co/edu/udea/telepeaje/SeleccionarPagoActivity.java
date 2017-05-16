package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;

public class SeleccionarPagoActivity extends AppCompatActivity {

    //Referencia a elementos de la interfaz
    FloatingActionButton buttonAgregarPago;
    private ViewGroup layout;

    //Referencia al usuario actual en la base de datos
    DatabaseReference usuarioRef;

    //Contador para la funcionalidad de eliminar un pago
    int cont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_pago);

        //Inicialización del contador para la funcionalidad de eliminar pago
        cont = 0;

        //Referencia a elementos de la interfaz
        buttonAgregarPago = (FloatingActionButton) findViewById(R.id.agregar_auto_button);
        layout = (ViewGroup) findViewById(R.id.content);

        //Lectura de los pagos desde la BD
        //Instancia de la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Referencia de todos los usuarios
        DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        //Se carga la referencia al usuario actual mediante el SharedPreferences
        //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");
        usuarioRef = usuariosRef.child(UID);
        //Referencia a todos los pagos del usuario actual
        DatabaseReference autosRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE);
        autosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se limpian las cardView infladas
                layout.removeAllViews();
                //Se meten todos los pagos en un Iterable de tipo DataSnapshot
                Iterable<DataSnapshot> pagos = dataSnapshot.getChildren();
                //Se recorre ese Iterable
                while (pagos.iterator().hasNext()) {
                    //Se coge uno de esos DataSnapshots (pago) que hay en el Iterable
                    DataSnapshot pagoDS = pagos.iterator().next();
                    //Se le pasa su valor a un objeto de tipo pago
                    Pago pago = pagoDS.getValue(Pago.class);
                    //Se ponen los atributos de ese objeto en su correspondiente layout
                    ponerPago(pago, pagoDS.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void seleccionarPago(View view, final String pagoKey) {
        //Se actualiza el idPagoCorrespondiente del auto en la base de datos
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        final String UID = misPreferencias.getString("UID", "");
        DatabaseReference usuarioRef = usuariosRef.child(UID);
        DatabaseReference autosRef = usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE);
        final String autoKey = getIntent().getStringExtra("autoKey");
        autosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                //Se recorre ese Iterable
                while(autos.iterator().hasNext()){
                    //Se coge uno de esos DataSnapshots (auto) que hay en el Iterable
                    DataSnapshot autoDS = autos.iterator().next();
                    //Se busca el auto que actualmente se está modificando
                    if((autoDS.getKey().equals(autoKey))||(autoDS.getKey()==autoKey)){
                        //Se procede a actualizar su idPagoCorrespondiente
                        Auto auto = autoDS.getValue(Auto.class);
                        auto.setIdPagoCorrespondiente(pagoKey);
                        Map<String, Object> autoMap = auto.toMap();

                        Map<String, Object> autoActualizacion = new HashMap<>();
                        String ruta = "/"+UID+"/"+FirebaseReferences.AUTOS_REFERENCE+"/"+autoKey;
                        autoActualizacion.put(ruta, autoMap);
                        usuariosRef.updateChildren(autoActualizacion);
                        //Toast.makeText(SeleccionarPagoActivity.this, "Entra", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Se manda a una clase intermedia el tag del botón que se seleccionó como método de pago
        ConfiguracionAuto.setPago(view.getTag().toString());
        //Se manda la actividad desde la cual se configuró el auto, para efectos del toast que aparece en MiAutoActivity
        ConfiguracionAuto.setActivity(this.getLocalClassName().toString());
        //Se finaliza esta actividad para que al estar ubicado en en la actividad "MiAuto" y se
        //le de atrás, no se abra esta actividad
        finish();
    }

    @SuppressLint("InlinedApi")
    private void ponerPago(final Pago pago, final String pagoKey) {
        //Se definen los elementos que van en el layout para cada auto
        LayoutInflater inflater = LayoutInflater.from(SeleccionarPagoActivity.this);
        int id = R.layout.layout_pago;
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null, false);
        Button pagoButton = (Button) linearLayout.findViewById(R.id.pago_button);
        Button eliminarPagoButton = (Button) linearLayout.findViewById(R.id.eliminar_pago_button);
        Button editarPagoButton = (Button) linearLayout.findViewById(R.id.editar_pago_button);
        //Se configuran los elementos
        //Se formatea el método de pago para que no se muestre completamente
        String numeroTarjeta = String.valueOf(pago.getNumeroTarjeta());
        char [] numeroTarjetaFormateada = new char[9];
        numeroTarjeta.getChars(12, 16, numeroTarjetaFormateada, 0);
        pagoButton.setText("xxxx "+String.valueOf(numeroTarjetaFormateada));
        //El tag de pagoButton será el string formateado del número de tarjeta
        pagoButton.setTag("xxxx "+String.valueOf(numeroTarjetaFormateada));
        pagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarPago(v, pagoKey);
            }
        });
        //El tag de eliminarPagoButton será el key del pago
        eliminarPagoButton.setTag(pagoKey);
        eliminarPagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarPago(v);
            }
        });
        editarPagoButton.setTag(pagoKey);
        editarPagoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarPago(v);
            }
        });
        layout.addView(linearLayout);
    }

    public void agregarPago(View view){
        Intent intent = new Intent(this, InformacionPagoActivity.class);
        //Se le añade un 2 al nombre de la clase origen para que haga otra funcionalidad
        String origen = this.getLocalClassName()+"2";
        intent.putExtra("claseOrigen", origen);
        startActivity(intent);
    }

    public void eliminarPago(final View view){
        cont++;
        if(cont==2){
            //Pimero se recorren todos los autos para verificar que el medio de pago a eliminar no está asociado a ninguno de ellos
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            final DatabaseReference usuarioRef = usuariosRef.child(UID);
            DatabaseReference autosRef = usuarioRef.child(FirebaseReferences.AUTOS_REFERENCE);
            autosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                    while(autos.iterator().hasNext()){
                        DataSnapshot autoDS = autos.iterator().next();
                        Auto auto = autoDS.getValue(Auto.class);
                        if((auto.getIdPagoCorrespondiente().equals(view.getTag()))||
                                (auto.getIdPagoCorrespondiente()==view.getTag())){
                            Toast.makeText(SeleccionarPagoActivity.this, "No se puede eliminar este pago " +
                                    "porque se encuentra asociado a un auto", Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            //Si ningun auto está asociado a este pago se procede a eliminar el pago
                            final DatabaseReference pagosRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE);
                            pagosRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Iterable<DataSnapshot> pagos = dataSnapshot.getChildren();
                                    while(pagos.iterator().hasNext()){
                                        DataSnapshot pagoDS = pagos.iterator().next();
                                        if((pagoDS.getKey().equals(view.getTag()))||(pagoDS.getKey()==view.getTag())){
                                            //Toast.makeText(SeleccionarPagoActivity.this, "Entra", Toast.LENGTH_LONG).show();
                                            DatabaseReference pago = pagoDS.getRef();
                                            pago.removeValue();
                                            //Se reinicia el contador
                                            cont=0;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            cont=0;
        }else if(cont==1){
            Toast.makeText(this, "Pulse otra vez para eliminar el pago", Toast.LENGTH_SHORT).show();
        }
    }

    public void editarPago(final View view){
        //Se toma la referencia a todos los pagos del usuario
        DatabaseReference pagosRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE);
        pagosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se crea un iterable con todos esos pagos
                Iterable<DataSnapshot> pagos = dataSnapshot.getChildren();
                //Se recorre ese iterable
                while(pagos.iterator().hasNext()){
                    //Se toma cada elemento de ese iterable
                    DataSnapshot pagoDS = pagos.iterator().next();
                    //Si se encuentra el pago al cual se le dio clic en editar, se toman sus datos y se le envian a la actividad
                    if((pagoDS.getKey()==view.getTag().toString())||
                            (pagoDS.getKey().equals(view.getTag().toString()))){
                        Pago pago = pagoDS.getValue(Pago.class);
                        //Toast.makeText(SeleccionarPagoActivity.this, pago.getMesVencimiento())
                        Intent intent = new Intent(SeleccionarPagoActivity.this, InformacionPagoActivity.class);
                        intent.putExtra("numeroTarjeta", pago.getNumeroTarjeta());
                        intent.putExtra("mesVencimiento", pago.getMesVencimiento());
                        intent.putExtra("anoVencimiento", pago.getAnoVencimiento());
                        intent.putExtra("cvv", pago.getCvv());
                        intent.putExtra("pagoKey", view.getTag().toString());
                        intent.putExtra("claseOrigen", SeleccionarPagoActivity.this.getLocalClassName());
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ConfiguracionAuto.setActivity("");
    }
}
