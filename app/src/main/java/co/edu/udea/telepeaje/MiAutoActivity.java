package co.edu.udea.telepeaje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Usuario;

public class MiAutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_auto);
        //Toast.makeText(this, getIntent().getStringExtra("placaV").toString(), Toast.LENGTH_SHORT).show();

        //Se obtienen los valores de los campos de la cardView
        Intent intent = getIntent();
        final String nombreAuto = intent.getStringExtra("nombreAuto");
        final String pago = intent.getStringExtra("pago");
        final String peajes = intent.getStringExtra("peajes");
        final String placa =  intent.getStringExtra("editTextPlaca");
        final String autoKey = intent.getStringExtra("tagCardView");

        //Se obtienen los componentes de la actividad actual
        final TextView propietarioMiAutoTextView = (TextView) findViewById(R.id.propietarioMiAuto);
        final TextView documentoPropietarioMiAutoTextView = (TextView) findViewById(R.id.documentoPropietarioMiAuto);
        final Button pagoButton= (Button) findViewById(R.id.pagoMiAuto);
        final Button peajesButton= (Button) findViewById(R.id.peajesMiAuto);
        final TextView placaTextView = (TextView) findViewById(R.id.placaMiAuto);

        //Se les pone valor a los componentes
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");
        DatabaseReference autosRef = FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(UID).child(FirebaseReferences.AUTOS_REFERENCE);
        //Toast.makeText(MiAutoActivity.this, "Entra", Toast.LENGTH_LONG).show();
        autosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                while(autos.iterator().hasNext()){
                    DataSnapshot autoDS = autos.iterator().next();
                    if((autoDS.getKey().equals(autoKey))||(autoDS.getKey()==autoKey)){
                        Auto auto = autoDS.getValue(Auto.class);
                        propietarioMiAutoTextView.setText(propietarioMiAutoTextView.getText().toString().concat(auto.getNombrePropietario()));
                        documentoPropietarioMiAutoTextView.setText(documentoPropietarioMiAutoTextView.getText().toString().concat(auto.getTipoDocPropietario()+" "+auto.getNumeroDocPropietario()));
                        pagoButton.setText(pago);
                        peajesButton.setText(peajes);
                        placaTextView.setText(placaTextView.getText().toString().concat(placa));
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Cuando se finaliza SeleccionarPagoActivity y vuelve a aparecer esta actividad, se ejecuta este método
    @Override
    protected void onResume() {
        super.onResume();
        String pago = ConfiguracionAuto.getPago();
        Button pagoButton= (Button) findViewById(R.id.pagoMiAuto);
        pagoButton.setText(pago);
        if(!ConfiguracionAuto.getActivity().equals(""))
        Toast.makeText(this, "Método de pago cambiado", Toast.LENGTH_SHORT).show();
    }

    public void openSeleccionarPago(View view){
        Intent intent = new Intent(this, SeleccionarPagoActivity.class);
        startActivity(intent);
    }

    public void openSeleccionarPeajes(View view){
        Intent intent = new Intent(this, SeleccionarPeajesActivity.class);
        startActivity(intent);
    }
}
