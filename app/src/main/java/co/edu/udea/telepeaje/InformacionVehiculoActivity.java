package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.Usuario;

public class InformacionVehiculoActivity extends AppCompatActivity {

    //Referencias a elementos de la interfaz
    EditText editTextNombrePropietario;
    Spinner spinnerTipoDocPropietario;
    EditText editTextNumeroDocPropietario;
    EditText editTextPlaca;
    EditText editTextNombrePersonalizado;

    //Datos para el registro del usuario
    String nombrePropietario;
    String tipoDocPropietario;
    String numeroDocPropietario;
    String placa;
    String nombrePersonalizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_vehiculo);

        //Referencias a elementos de la interfaz
        editTextNombrePropietario = (EditText) findViewById(R.id.nombre_propietario_edit_text);
        spinnerTipoDocPropietario = (Spinner) findViewById(R.id.tipo_pago_spinner);
        editTextNumeroDocPropietario = (EditText) findViewById(R.id.numero_doc_propietario_edit_text);
        editTextPlaca = (EditText) findViewById(R.id.placa_edit_text);
        editTextNombrePersonalizado = (EditText) findViewById(R.id.numero_tarjeta_edit_text);
        //Configuración del spinner
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource( this, R.array.tipos_identificacion , android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoDocPropietario.setAdapter(spinner_adapter);

    }

    /*Este método puede abrir la actividad InformacionPagoActivity si la actividad que dio origen a esta fue*
    /InformacionPersonalActivity o puede abrir MisAutosActivity si la actividad que dio origen a esta fue precisamente
    /la actividad MisAutosActivity*/
    public void siguienteActivity(View view){
        String origen = getIntent().getStringExtra("claseOrigen");
        //Se establece que acción debe realizar el button
        if(origen.equals("InformacionPersonalActivity")){
            //Validación de datos
            nombrePropietario = editTextNumeroDocPropietario.getText().toString().trim();
            tipoDocPropietario = spinnerTipoDocPropietario.getSelectedItem().toString();
            numeroDocPropietario = editTextNumeroDocPropietario.getText().toString().trim();
            placa = editTextPlaca.getText().toString().trim();
            nombrePersonalizado = editTextNombrePersonalizado.getText().toString().trim();
            if((nombrePropietario==null)||(nombrePropietario.equals(""))){
                editTextNombrePropietario.setError("Ingrese el nombre del propietario");
                Log.e("SESSION", "Ingrese el nombre del propietario");
            }else if((numeroDocPropietario==null)||(numeroDocPropietario.equals(""))){
                editTextNumeroDocPropietario.setError("Ingrese el número de documento del propietario");
                Log.e("SESSION", "Ingrese el número de documento del propietario");
            }else if((placa==null)||(placa.equals(""))){
                editTextPlaca.setError("Ingrese la placa del vehículo");
                Log.e("SESSION", "Ingrese la placa del vehículo");
            }else if((nombrePersonalizado==null)||(nombrePersonalizado.equals(""))){
                editTextPlaca.setError("Ingrese el nombre personalizado del vehículo");
                Log.e("SESSION", "Ingrese el nombre personalizado del vehículo");
            }else if(placa.length()!=7) {
                editTextPlaca.setError("Ingrese el número de la placa con el formato (XXX-XXX)");
                Log.e("SESSION", "Ingrese el número de la placa con el formato (XXX-XXX)");
            }else{
                //Se construye un auto para el usuario
                Auto auto = new Auto(nombrePropietario, tipoDocPropietario, numeroDocPropietario, placa, nombrePersonalizado);
                /*
                auto.setNombrePropietario(nombrePropietario);
                auto.setTipoDocPropietario(tipoDocPropietario);
                auto.setNumeroDocPropietario(numeroDocPropietario);
                auto.setPlaca(placa);
                auto.setNombrePersonalizado(nombrePersonalizado);*/


                //Se continua con la construcción del usuario
                Usuario usuario = (Usuario) getIntent().getSerializableExtra("usuario");
                //usuario.setAuto(auto);

                //Construcción del intent
                Intent intent = new Intent(this, InformacionPagoActivity.class);

                //Se pasan los datos del usuario a la siguiente actividad
                intent.putExtra("usuario", usuario);
                startActivity(intent);
            }
        }else if(origen.equals("MisAutosActivity")){
            finish();
        }
    }
}
