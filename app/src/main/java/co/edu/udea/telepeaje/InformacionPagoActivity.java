package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;

public class InformacionPagoActivity extends AppCompatActivity {

    //Referencias a elementos de la interfaz
    Spinner spinnerTipoPago;
    EditText editTextNumeroTarjeta;
    Spinner spinnerMesVencimiento;
    Spinner spinnerAnoVencimiento;
    EditText editTextCVV;
    //EditText editTextEmailPago;
    //EditText editTextPassPago;

    //Datos para el registro del usuario
    //String tipoPago;
    Long numeroTarjeta;
    int mesVencimiento;
    int anoVencimiento;
    String cvv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_pago);

        //Referencias a elementos de la interfaz
        //spinnerTipoPago = (Spinner) findViewById(R.id.tipo_pago_spinner);
        editTextNumeroTarjeta = (EditText) findViewById(R.id.nombre_personalizado_edit_text);
        spinnerMesVencimiento = (Spinner) findViewById(R.id.mes_vencimiento_spinner);
        spinnerAnoVencimiento = (Spinner) findViewById(R.id.ano_vencimiento_spinner);
        editTextCVV = (EditText) findViewById(R.id.cvv_edit_text);
        //editTextEmailPago = (EditText) findViewById(R.id.email_pago_edit_text);
        //editTextPassPago = (EditText) findViewById(R.id.pass_pago_edit_text);

        //Configuración del spinnerTipoPago
        /*ArrayAdapter spinnerAdapterTipoPago = ArrayAdapter.createFromResource( this, R.array.tipos_pago, android.R.layout.simple_spinner_item);
        spinnerAdapterTipoPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoPago.setAdapter(spinnerAdapterTipoPago);*/
        //Configuración del spinnerMesVencimiento
        ArrayAdapter spinnerAdapterMes = ArrayAdapter.createFromResource( this, R.array.meses, android.R.layout.simple_spinner_item);
        spinnerAdapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMesVencimiento.setAdapter(spinnerAdapterMes);
        //Configuración del spinnerAnoVencimiento
        ArrayAdapter spinnerAdapterAno = ArrayAdapter.createFromResource( this, R.array.anos, android.R.layout.simple_spinner_item);
        spinnerAdapterAno.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnoVencimiento.setAdapter(spinnerAdapterAno);
    }

    public void openMisAutos(View view){
        //Validación de datos
        //tipoPago = spinnerTipoPago.getSelectedItem().toString();
        numeroTarjeta = Long.parseLong(editTextNumeroTarjeta.getText().toString().trim());
        mesVencimiento = Integer.parseInt(spinnerMesVencimiento.getSelectedItem().toString());
        anoVencimiento = Integer.parseInt(spinnerAnoVencimiento.getSelectedItem().toString());
        cvv = editTextCVV.getText().toString().trim();
        if((numeroTarjeta==null)||(numeroTarjeta.equals(""))){
            editTextNumeroTarjeta.setError("Ingrese el número de la tarjeta");
            Log.e("SESSION", "Ingrese el número de la tarjeta");
        }else if((cvv==null)||(cvv.equals(""))){
            editTextCVV.setError("Ingrese el código de seguridad de la tarjeta");
            Log.e("SESSION", "Ingrese el código de seguridad de la tarjeta");
        }else if(numeroTarjeta.toString().length()!=16){
            editTextNumeroTarjeta.setError("El número de la tarjeta debe contener 16 dígitos");
            Log.e("SESSION", "El número de la tarjeta debe contener 16 dígitos");
        }else{
            //Se construye un pago para el usuario
            Pago pago = new Pago();
            //pago.setTipo(tipoPago);
            pago.setNumeroTarjeta(numeroTarjeta);
            pago.setMesVencimiento(mesVencimiento);
            pago.setAnoVencimiento(anoVencimiento);
            pago.setCvv(cvv);

            //Finalmente se escribe el pago en la base de datos para el usuario correspondiente.
            //Primero se toma la instancia de la base de datos
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Se toma la key del usuario definida desde la actividad anterior
            String usuarioKey = getIntent().getStringExtra("usuarioKey");
            //Se toma la referencia de todos los usuarios
            DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            //Se busca el usuario correspondiente dentro de todos los usuarios
            DatabaseReference usuarioRef = usuariosRef.child(usuarioKey);
            // Al usuario se le añade un "hijo" llamado pagos y se le "empuja" un primer valor
            usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE).push().setValue(pago);

            //Construcción del intent
            Intent intent = new Intent(this, MisAutosActivity.class);
            intent.putExtra("usuarioKey", getIntent().getStringExtra("usuarioKey"));
            startActivity(intent);
        }
    }
}
