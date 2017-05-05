package co.edu.udea.telepeaje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Usuario;

public class InformacionPersonalActivity extends AppCompatActivity {

    /*
        Para las variables que se pasan en un intent, es una buena practica preceder su nombre del nombre del paquete,
        ya que si no se hace esto, la clase que recibe el extra puede recibir otra variable con el mismo nombre y se pueden
        originar conflictos.

    public static final String EXTRA_ORIGEN = "co.edu.udea.telepeaje.InformacionPersonalActivity.CLASE_ORIGEN"; */

    //Referencias a elementos de la interfaz
    EditText editTextNombres;
    EditText editTextApellidos;
    Spinner spinnerCodigoArea;
    EditText editTextCelular;

    //Datos para el registro del usuario
    String nombres;
    String apellidos;
    String celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_personal);

        //Referencias a elementos de la interfaz
        editTextNombres = (EditText) findViewById(R.id.numero_doc_propietario_edit_text);
        editTextApellidos = (EditText) findViewById(R.id.nombre_personalizado_edit_text);
        spinnerCodigoArea = (Spinner) findViewById(R.id.tipo_pago_spinner);
        editTextCelular = (EditText) findViewById(R.id.placa_edit_text);
        //Configuración del spinner
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource( this, R.array.codigos_area, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCodigoArea.setAdapter(spinnerAdapter);
    }

    public void openInformacionVehiculo(View view){
        //Validación de datos
        nombres = editTextNombres.getText().toString().trim();
        apellidos = editTextApellidos.getText().toString().trim();
        celular = spinnerCodigoArea.getSelectedItem().toString().concat(editTextCelular.getText().toString().trim());
        if((nombres==null)||(nombres.equals(""))){
            editTextNombres.setError("Ingrese el nombre");
            Log.e("SESSION", "Ingrese el nombre");
        }else if((apellidos==null)||(apellidos.equals(""))){
            editTextApellidos.setError("Ingrese el apellido");
            Log.e("SESSION", "Ingrese el apellido");
        }else if((celular==null)||(celular.equals(""))){
            editTextCelular.setError("Ingrese el número de celular");
            Log.e("SESSION", "Ingrese el número de celular");
        }else{
            //Se continua con la construcción del usuario
            Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
            usuario.setNombres(nombres);
            usuario.setApellidos(apellidos);
            usuario.setCelular(Long.parseLong(celular));

            //Finalmente se escribe el usuario en la base de datos.
            // Primero se toma la instancia de la BD
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Se coge la referencia al nodo de todos los usuarios
            final DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            //Se carga la referencia al usuario actual mediante el SharedPreferences
            //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
            SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            //Se le añade un hijo que tenga como key el UID asignado para este usuario
            final DatabaseReference usuarioRef = usuariosRef.child(UID);
            //Se le pone valor a esa referencia (a ese usuario)
            usuarioRef.setValue(usuario);

            //Construcción del intent
            Intent intent = new Intent(this, InformacionPagoActivity.class);
            String origen = this.getLocalClassName();
            intent.putExtra("claseOrigen", origen);

            //Se inicia la actividad
            startActivity(intent);
        }
    }

    //Al cerrar está actividad, el registro queda incompleto, por lo tanto...
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Se borran los datos del usuario creado
        SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
        String UID = misPreferencias.getString("UID", "");//Con usuario.getUID() tampoco funciona
        final DatabaseReference borrar = FirebaseDatabase.getInstance().getReference(FirebaseReferences.USUARIOS_REFERENCE).child(UID);
        Log.i("SESSION", "El UID del usuario a eliminar es "+borrar.toString());
        borrar.removeValue();
        //Se borra el usuario creado
        FirebaseAuth sesion = FirebaseAuth.getInstance();
        FirebaseUser usuario = sesion.getCurrentUser();
        usuario.delete();
    }
}
