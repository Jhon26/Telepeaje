package co.edu.udea.telepeaje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Usuario;

public class MiAutoActivity extends AppCompatActivity {

    //Auto del cual se extrae la información para ponerla en esta activity
    Auto auto;
    //Referencia de ese auto en la base de datos
    DataSnapshot autoDS;
    //referencia a los usuarios de la base de datos
    DatabaseReference usuariosRef;
    //UID del usuario actual
    String UID;

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
        final Button pagoButton = (Button) findViewById(R.id.pagoMiAuto);
        final Switch peajesHabilitadosSwitch= (Switch) findViewById(R.id.peajesHabilitadosMiAuto);
        final TextView placaTextView = (TextView) findViewById(R.id.placaMiAuto);

        //Se les pone valor a los componentes
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        UID = misPreferencias.getString("UID", "");
        usuariosRef = FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE);
        DatabaseReference autosRef = usuariosRef.child(UID).child(FirebaseReferences.AUTOS_REFERENCE);
        //Toast.makeText(MiAutoActivity.this, "Entra", Toast.LENGTH_LONG).show();
        autosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> autos = dataSnapshot.getChildren();
                while(autos.iterator().hasNext()){
                    autoDS = autos.iterator().next();
                    if((autoDS.getKey().equals(autoKey))||(autoDS.getKey()==autoKey)){
                        auto = autoDS.getValue(Auto.class);
                        propietarioMiAutoTextView.setText(propietarioMiAutoTextView.getText().toString().concat(auto.getNombrePropietario()));
                        documentoPropietarioMiAutoTextView.setText(documentoPropietarioMiAutoTextView.getText().toString().concat(auto.getTipoDocPropietario()+" "+auto.getNumeroDocPropietario()));
                        pagoButton.setText(pago);
                        ConfiguracionAuto.setPago(pago);
                        if(auto.getPeajesHabilitados()){
                            peajesHabilitadosSwitch.setText("Todos los peajes estan habilitados");
                            peajesHabilitadosSwitch.setChecked(true);
                        }else{
                            peajesHabilitadosSwitch.setText("Todos los peajes estan deshabilitados");
                            peajesHabilitadosSwitch.setChecked(false);
                        }
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
        if(!ConfiguracionAuto.getActivity().equals("")){
            Toast.makeText(this, "Método de pago cambiado", Toast.LENGTH_SHORT).show();
        }
    }

    public void openSeleccionarPago(View view){
        Intent intent = new Intent(this, SeleccionarPagoActivity.class);
        intent.putExtra("autoKey", getIntent().getStringExtra("tagCardView"));
        startActivity(intent);
    }

    public void cambiarEstadoPeajesHabilitados(View view){
        Switch peajesHabilitadosSwitch= (Switch) findViewById(R.id.peajesHabilitadosMiAuto);
        //Se actualiza el campo peajesHabilitados del auto consultado
        //Toast.makeText(this, "Entra", Toast.LENGTH_SHORT).show();
        if(auto.getPeajesHabilitados()){
            auto.setPeajesHabilitados(false);
            peajesHabilitadosSwitch.setText("Todos los peajes estan deshabilitados");
        }else{
            auto.setPeajesHabilitados(true);
            peajesHabilitadosSwitch.setText("Todos los peajes estan habilitados");
        }
        Map<String, Object> autoMap = auto.toMap();
        Map<String, Object> autoActualizacion = new HashMap<>();
        String ruta = "/"+UID+"/"+FirebaseReferences.AUTOS_REFERENCE+"/"+autoDS.getKey();
        autoActualizacion.put(ruta, autoMap);
        usuariosRef.updateChildren(autoActualizacion);
    }

}
