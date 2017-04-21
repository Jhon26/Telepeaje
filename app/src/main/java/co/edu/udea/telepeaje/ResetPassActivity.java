package co.edu.udea.telepeaje;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
    }

    public void resetPass(View view){
        EditText resetPass = (EditText) findViewById(R.id.edit_text_reset_pass);
        String resetPassText = resetPass.getText().toString().replaceAll(" ","");
        if((resetPassText==null)||resetPassText.equals("")){
            resetPass.setError("Ingrese el email");
            Log.e("SESSION", "Ingrese el email al cual desea resetear la contraseña");
        }else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(resetPassText).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Se envia el email correctamente
                            if(task.isSuccessful()){
                                Toast.makeText(ResetPassActivity.this,"¡Email enviado! Revisa tu bandeja de entrada",Toast.LENGTH_LONG).show();
                            }
                            //No se envia correctamente
                            else{
                                //El correo ingresado no se encuentra en el sistema
                                if(task.getException().getMessage().toString().equals(
                                        "There is no user record corresponding to this identifier. The user may have been deleted."
                                )){
                                    Toast.makeText(ResetPassActivity.this,"Este correo no está registrado en el sistema.",Toast.LENGTH_LONG).show();
                                }
                                //El email está mal copiado
                                else if(task.getException().getMessage().equals(
                                        "An internal error has occurred. [ INVALID_EMAIL ]"
                                )){
                                    Toast.makeText(ResetPassActivity.this,"Ingrese un email correcto.",Toast.LENGTH_LONG).show();
                                }
                                //Otro error
                                else{
                                    Toast.makeText(ResetPassActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                                Log.e("SESSION", task.getException().getMessage().toString());

                            }
                        }
                    }
            );
        }
    }
}
