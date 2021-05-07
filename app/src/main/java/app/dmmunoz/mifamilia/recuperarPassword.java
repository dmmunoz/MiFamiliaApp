package app.dmmunoz.mifamilia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class recuperarPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    Button btRecuperarContrasena;
    private FirebaseAuth mAuth;
    ImageView btAtrasPass;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        email = findViewById(R.id.etEmailRecuperacion);
        btRecuperarContrasena = findViewById(R.id.btRecuperarPass);
        btAtrasPass = findViewById(R.id.btAtrasForget);
        mAuth = FirebaseAuth.getInstance();
        btRecuperarContrasena.setOnClickListener(this);
        btAtrasPass.setOnClickListener(this);
        mDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        String campoEmail = email.getText().toString().trim();
        switch (v.getId()) {
            case R.id.btRecuperarPass:
                if (campoEmail.isEmpty()) { //Comprobamos si el campo de email está vacío
                    email.setError("El email es obligatorio");
                    email.requestFocus();
                    return; //No se permite la autentificacion al usuario
                } else{
                    resetPassword(campoEmail);
                }
                break;
            case R.id.btAtrasForget:
                Intent in = new Intent(recuperarPassword.this, Login.class);
                in.setFlags(in.FLAG_ACTIVITY_NEW_TASK | in.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                break;
        }
    }

    private void resetPassword(String email) {


        mAuth.setLanguageCode("ES");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(recuperarPassword.this, "Se está enviando el correo de recuperacion", Toast.LENGTH_LONG).show();
                    mDialog.setMessage("Espere...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }else {
                    Toast.makeText(recuperarPassword.this, "No se pudo enviar el correo", Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
                Intent in2 = new Intent(recuperarPassword.this, Login.class);
                in2.setFlags(in2.FLAG_ACTIVITY_NEW_TASK | in2.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in2);
            }

        });
    }
}