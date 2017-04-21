package co.edu.udea.telepeaje;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        /*
        //No permitir espacios en los editText
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if ( !Character.isLetterOrDigit(source.charAt(i)) || !Character.toString(source.charAt(i)).equals("_") || !Character.toString(source.charAt(i)).equals("-")) {
                        return "";
                    }
                }
                return null;
            }
        };
        editTextEmail.setFilters(new InputFilter[]{filter});
        */

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
                    //Contraseña incorrecta
                    if(task.getException().getMessage().toString().equals(
                            "The password is invalid or the user does not have a password.")){
                        editTextPass.setError("Contraseña incorrecta");
                    }
                    //Email no encontrado
                    else if((task.getException().getMessage().toString().equals(
                            "There is no user record corresponding to this identifier. " +
                                    "The user may have been deleted."))){
                        editTextEmail.setError("Email no encontrado");
                    }
                    //Email mal formado
                    else if(task.getException().getMessage().toString().equals(
                                    "The email address is badly formatted."
                            )){
                        editTextEmail.setError("Email inválido");
                    }
                    //Cuenta no habilitada para ingresar
                    else if(task.getException().getMessage().toString().equals(
                            "The user account has been disabled by an administrator."
                    )){
                        Toast.makeText(MainActivity.this, "Esta cuenta de usuario ha sido inhabilitada", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("SESSION", task.getException().getMessage().toString());
                }
            }
        });
    }

    public void actionLogin(View view){
        String email = editTextEmail.getText().toString().replaceAll(" ", "");
        String pass = editTextPass.getText().toString().replaceAll(" ", "");
        if((email==null)||(email.equals(""))){
            editTextEmail.setError("Ingrese el email");
            Log.e("SESSION", "Ingrese el email");
        }else if((pass==null)||(pass.equals(""))){
            editTextPass.setError("Ingrese la contraseña");
            Log.e("SESSION", "Ingrese la contraseña");
        }else{
            login(email, pass);
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


    public void openRegistroEmail(View view){
        editTextEmail.setText("");
        editTextPass.setText("");
        Intent intent = new Intent(this, RegistroEmailActivity.class);
        startActivity(intent);
    }

    public void openResetPass(View view){
        Intent intent = new Intent(this, ResetPassActivity.class);
        startActivity(intent);
    }
}
