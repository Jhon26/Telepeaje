package co.edu.udea.telepeaje;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.HashMap;
import java.util.Map;

import co.edu.udea.telepeaje.Objetos.Auto;
import co.edu.udea.telepeaje.Objetos.FirebaseReferences;
import co.edu.udea.telepeaje.Objetos.Pago;

public class InformacionPagoActivity extends AppCompatActivity {

    //Referencias a elementos de la interfaz
    Spinner spinnerTipoPago;
    EditText editTextNumeroTarjeta;
    Spinner spinnerMesVencimiento;
    Spinner spinnerAnoVencimiento;
    EditText editTextCVV;
    CircleProgressBar circleProgressBar;

    //EditText editTextEmailPago;
    //EditText editTextPassPago;

    //Datos para el registro del usuario
    //String tipoPago;
    Long numeroTarjeta;
    int mesVencimiento;
    int anoVencimiento;
    String cvv;

    //Referencia al usuario actual de la base de datos
    DatabaseReference usuarioRef;
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
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progress_bar_info_pago);
        circleProgressBar.setColorSchemeColors(R.color.colorPrimary);
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

        Intent intent = getIntent();
        if(intent.getStringExtra("claseOrigen").equals("SeleccionarPagoActivity")){
            editTextNumeroTarjeta.setText(String.valueOf(intent.getLongExtra("numeroTarjeta", 0)));
            spinnerMesVencimiento.setSelection(intent.getIntExtra("mesVencimiento", 0)-1);
            spinnerAnoVencimiento.setSelection(intent.getIntExtra("anoVencimiento", 0)-17);
            editTextCVV.setText(intent.getStringExtra("cvv"));
        }
    }

    public void siguienteActivity(View view){
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

            //Antes de escribir todos los datos se empieza a mostrar la circle progress bar
            circleProgressBar.setVisibility(View.VISIBLE);
            //Finalmente se escribe (o actualiza) el pago en la base de datos para el usuario correspondiente.
            //Primero se toma la instancia de la base de datos
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Se carga la referencia al usuario actual mediante el SharedPreferences
            //El archivo PreferenciasUsuario sólo sera accedido por esta aplicación
            SharedPreferences misPreferencias = getSharedPreferences("PreferenciasUsuario", Context.MODE_PRIVATE);
            String UID = misPreferencias.getString("UID", "");
            //Se toma la referencia de todos los usuarios
            DatabaseReference usuariosRef = database.getReference(FirebaseReferences.USUARIOS_REFERENCE);
            //Se busca el usuario correspondiente dentro de todos los usuarios
            usuarioRef = usuariosRef.child(UID);

            //Se establece cuál activity debe abrir el button y el resto de su acción
            String origen = getIntent().getStringExtra("claseOrigen");
            if(origen.equals("SeleccionarPagoActivity")){
                //Se modifican los datos de pago del usuario
                Map<String, Object> pagoMap = pago.toMap();
                Map<String, Object> pagoActualizacion = new HashMap<>();
                String pagoKey = getIntent().getStringExtra("pagoKey");
                String ruta = "/"+UID+"/"+FirebaseReferences.PAGOS_REFERENCE+"/"+pagoKey;
                pagoActualizacion.put(ruta, pagoMap);
                usuariosRef.updateChildren(pagoActualizacion);
                //Despues de escribir todos los datos se oculta la circle progress bar
                circleProgressBar.setVisibility(View.INVISIBLE);
                //Toast.makeText(SeleccionarPagoActivity.this, "Entra", Toast.LENGTH_LONG).show();


                finish();
            }else{
                // Al usuario se le añade un "hijo" llamado pagos y se le "empuja" un valor
                DatabaseReference pagoRef = usuarioRef.child(FirebaseReferences.PAGOS_REFERENCE).push();
                pagoRef.setValue(pago);
                //Despues de escribir todos los datos se oculta la circle progress bar
                circleProgressBar.setVisibility(View.INVISIBLE);
                if(origen.equals("InfoPersonal")){
                    //Construcción del intent
                    Intent intent = new Intent(this, InformacionVehiculoActivity.class);
                    String claseOrigen = this.getLocalClassName();
                    intent.putExtra("claseOrigen", claseOrigen);
                    startActivity(intent);
                }else if(origen.equals("SeleccionarPagoActivity2")){
                    finish();
                }

            }
        }
    }
}
