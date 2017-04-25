package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

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
            //Se coge la referencia al dato que queramos, en este caso el usuario que se insertará
            final DatabaseReference usuarioRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE).push();
            //Se le pone valor a esa referencia
            usuarioRef.setValue(usuario);
            //Se toma la key de ese usuario insertado para posteriores implementaciones
            String usuarioKey = usuarioRef.getKey();

            //claseOrigen informa a la siguiente actividad desde dónde fue lanzada
            String origen = this.getLocalClassName();

            //Construcción del intent
            Intent intent = new Intent(this, InformacionVehiculoActivity.class);

            //Se pasan los datos necesarios mediante el intent
            intent.putExtra("usuarioKey", usuarioKey);
            intent.putExtra("claseOrigen", origen);

            //Se inicia la actividad
            startActivity(intent);
        }
    }

}
