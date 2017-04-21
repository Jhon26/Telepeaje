package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistroEmailActivity extends AppCompatActivity {

    //Referencias a elementos
    EditText editTextEmail, editTextPass;
    FloatingActionButton buttonRegister;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_email);

        //Referencias a elementos
        editTextEmail = (EditText) findViewById(R.id.email_register_edit_text);
        editTextPass = (EditText) findViewById(R.id.pass_register_edit_text);
        buttonRegister = (FloatingActionButton) findViewById(R.id.register_button);

        //Se declara el listener
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){//Sesion iniciada
                    Log.i("SESSION", "sesión iniciada con email: "+ user.getEmail());
                    editTextEmail.setText("");
                    editTextPass.setText("");
                    Intent intent = new Intent(RegistroEmailActivity.this, MisAutosActivity.class);
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
        String email = editTextEmail.getText().toString().replaceAll(" ", "");
        String pass = editTextPass.getText().toString().replaceAll(" ", "");
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
