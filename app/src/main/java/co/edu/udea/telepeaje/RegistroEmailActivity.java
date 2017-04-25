package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import co.edu.udea.telepeaje.Objetos.Usuario;

public class RegistroEmailActivity extends AppCompatActivity {

    //Referencias a elementos de la interfaz
    EditText editTextEmail, editTextPass;
    FloatingActionButton buttonRegister;

    //Datos para el registro del usuario
    private String email;
    private String pass;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_email);

        //Referencias a elementos de la interfaz
        editTextEmail = (EditText) findViewById(R.id.nombre_propietario_edit_text);
        editTextPass = (EditText) findViewById(R.id.numero_doc_propietario_edit_text);
        buttonRegister = (FloatingActionButton) findViewById(R.id.register_button);

        //Se declara el listener
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){//Sesion iniciada
                    Log.i("SESSION", "Usuaro creado con email: "+ user.getEmail());

                    //Creación del usuario e instauración de sus primeros datos
                    Usuario usuario = new Usuario();
                    usuario.setCorreo(email);

                    //Se limpian los campos de texto
                    editTextEmail.setText("");
                    editTextPass.setText("");

                    //Creación del intent y paso de datos
                    Intent intent = new Intent(RegistroEmailActivity.this, InformacionPersonalActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                }else{//Sesion cerrada
                    Log.i("SESSION", "sesión cerrada");
                }
            }
        };
    }

    private void register(String email, String pass){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("SESSION", "usuario creado correctamente");
                }else{
                    //Email mal formado
                    if(task.getException().getMessage().toString().equals(
                            "The email address is badly formatted."
                    )){
                        editTextEmail.setError("Ingrese un correo electrónico válido");
                    }
                    //Email ya en uso por otra cuenta
                    else if(task.getException().getMessage().toString().equals(
                            "The email address is already in use by another account."
                    )){
                        editTextEmail.setError("Este email ya está en uso por otra cuenta");
                    }
                    Log.e("SESSION", task.getException().getMessage().toString());
                }
            }
        });
    }

    public void actionRegister(View view){
        email = editTextEmail.getText().toString().replaceAll(" ", "");
        pass = editTextPass.getText().toString().replaceAll(" ", "");
        if((email==null)||(email.equals(""))){
            editTextEmail.setError("Ingrese el email");
            Log.e("SESSION", "Ingrese el email");
        }else if((pass==null)||(pass.equals(""))){
            editTextPass.setError("Ingrese una contraseña");
            Log.e("SESSION", "Ingrese una contraseña");
        }else if(pass.length()<6) {
            editTextPass.setError("La contraseña debe contener al menos 6 caracteres");
            Log.e("SESSION", "La contraseña debe contener al menos 6 caracteres");
        }else{
            register(email, pass);
        }

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

    /*
    public void openInformacionPersonal(View view){
        Intent intent = new Intent(this, InformacionPersonalActivity.class);
        startActivity(intent);
    }*/
}
