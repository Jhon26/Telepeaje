package co.edu.udea.telepeaje;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        FirebaseAuth.getInstance().sendPasswordResetEmail(resetPass.getText().toString().replaceAll(" ","")).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPassActivity.this,"¡Email enviado!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ResetPassActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
