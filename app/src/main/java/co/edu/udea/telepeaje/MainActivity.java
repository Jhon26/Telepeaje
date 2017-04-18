package co.edu.udea.telepeaje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "co.edu.udea.telepeaje.MESSAGE";

    //Referencias a elementos
    EditText editTextEmail, editTextPass;
    FloatingActionButton buttonSignIn;
    TextView textViewRegister, textViewForgot;

    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se esconde la action bar en esta actividad
        getSupportActionBar().hide();

        //Referencias a elementos
        editTextEmail = (EditText) findViewById(R.id.email_login_edit_text);
        editTextPass = (EditText) findViewById(R.id.pass_login_edit_text);
        buttonSignIn = (FloatingActionButton) findViewById(R.id.sign_in_button);
        textViewRegister = (TextView) findViewById(R.id.register_text_view);
        textViewForgot = (TextView) findViewById(R.id.forgot_pass_text_view);

        //Listener para la autenticación
        mAuthListener = new FirebaseAuth.AuthStateListener(){

            //Cuando cambia la sesion (inicia o cierra)
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){//Sesion iniciada
                    Log.i("SESSION", "sesión iniciada con email: "+ user.getEmail());
                    editTextEmail.setText("");
                    editTextPass.setText("");
                    Intent intent = new Intent(MainActivity.this, MisAutosActivity.class);
                    startActivity(intent);
                }else{//Sesion cerrada
                    Log.i("SESSION", "sesión cerrada");
                }
            }
        };
    }

    private void login(String email, String pass){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("SESSION", "sesión iniciada");
                }else{
                    Log.e("SESSION", task.getException().getMessage()+"");
                }
            }
        });
    }

    public void actionLogin(View view){
        String email = editTextEmail.getText().toString();
        String pass = editTextPass.getText().toString();
        login(email, pass);
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


    public void openRegistroEmail(View view){
        Intent intent = new Intent(this, RegistroEmailActivity.class);
        startActivity(intent);
    }

    /*
    public void openMisAutos(View view){
        Intent intent = new Intent(this, MisAutosActivity.class);
        startActivity(intent);
    }*/
}
