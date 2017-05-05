package co.edu.udea.telepeaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;

public class SeleccionarPagoActivity extends AppCompatActivity {

    //Referencia a elementos de la interfaz
    FloatingActionButton buttonAgregarPago;
    private ViewGroup layout;

    //Referencia al usuario actual en la base de datos
    DatabaseReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_pago);

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
                    ponerPago(pago);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void seleccionarPago(View view) {
        //Se manda a una clase intermedia el tag del botón que se seleccionó como método de pago
        ConfiguracionAuto.setPago(view.getTag().toString());
        //Se manda la actividad desde la cual se configuró el auto, para efectos del toast que aparece en MiAutoActivity
        ConfiguracionAuto.setActivity(this.getLocalClassName().toString());
        //Se finaliza esta actividad para que al estar ubicado en en la actividad "MiAuto" y se
        //le de atrás, no se abra esta actividad
        finish();
    }

    @SuppressLint("InlinedApi")
    private void ponerPago(final Pago pago) {
        //Se definen los elementos que van en el layout para cada auto
        LayoutInflater inflater = LayoutInflater.from(SeleccionarPagoActivity.this);
        int id = R.layout.layout_pago;
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(id, null, false);
        Button pagoButton = (Button) linearLayout.findViewById(R.id.pago_button);
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
                seleccionarPago(v);
            }
        });
        layout.addView(linearLayout);
    }
}
