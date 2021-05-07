package app.dmmunoz.mifamilia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class comprobacionEmail extends AppCompatActivity implements View.OnClickListener{

    EditText comprobacionEmail;
    Button btEnviar, btSalir;
    private String correo;
    private boolean condicion=true;
    FirebaseFirestore db;
    private static final String TAG = "ActivityCheckEmail";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprobacion_email);

        comprobacionEmail = findViewById(R.id.etComprobacionMail);
        btEnviar = findViewById(R.id.btComprobarEmail);
        btSalir = findViewById(R.id.btSalirRegistro);

        btEnviar.setOnClickListener(this);
        btSalir.setOnClickListener(this);
        db=FirebaseFirestore.getInstance();

    }

    @Override
    public void onClick(View v) {
        correo = comprobacionEmail.getText().toString().trim();
        switch(v.getId()){
            case R.id.btComprobarEmail:
                if (!compruebarCorreo(correo)){
                    Toast.makeText(comprobacionEmail.this,"No es válido el correo.", Toast.LENGTH_LONG).show();
                }
                    if(compruebaUsuario(correo)){
                    Toast.makeText(comprobacionEmail.this,"El correo ya está registrado, usa otra cuenta de correo", Toast.LENGTH_LONG).show();
                } else{
                    Intent in = new Intent(comprobacionEmail.this, Singup.class);
                    //Paso datos del correo de un activity a otro
                    in.putExtra("envioDatos", correo);
                    startActivity(in);
                }
                break;
            case R.id.btSalirRegistro:
                finish();
        }
    }

    /**
     * Método para comprobar que no hay duplicidad de email en los usuarios
     * @param correo que comprobará en la base de datos si existe
     * @return true si no hay duplicidad y false si existe en la Base de datos
     */
    private boolean compruebaUsuario(String correo) {
        boolean cond;
        db.collection("usuarios")
                .whereEqualTo("email", correo)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            condicion=false;
                        } else {
                            Toast.makeText(comprobacionEmail.this, "No se ha encontrado el documento", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "No se ha encontrado: ", task.getException());
                        }

                    }
                });
    if (condicion==false){
        return false;} else
        return true;
    }

    /**
     * Comprueba que el correo que introduce el usuario es correcto
     * @param correo introducido por le usuario
     * @return true si está bien formado, false en el caso contrario
     */
    private boolean compruebarCorreo(String correo) {

        //Que el campo del mail esté vacío
        if (correo.isEmpty()) {
            comprobacionEmail.setError("Email es necesario");
            comprobacionEmail.requestFocus();
            //Return Error
            return false;
        }
        //Que el formato del mail esté mal
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            comprobacionEmail.setError("Pon un Email correcto");
            comprobacionEmail.requestFocus();
            //Return Error
            return false;
        }
        return true;
    }

}