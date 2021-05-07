            package app.dmmunoz.mifamilia;

            import androidx.annotation.NonNull;
            import androidx.appcompat.app.AppCompatActivity;

            import android.content.Intent;
            import android.os.Bundle;
            import android.util.Log;
            import android.view.View;
            import android.widget.Button;
            import android.widget.CheckBox;
            import android.widget.EditText;
            import android.widget.ImageView;
            import android.widget.TextView;
            import android.widget.Toast;

            import com.google.android.gms.tasks.OnCompleteListener;
            import com.google.android.gms.tasks.OnFailureListener;
            import com.google.android.gms.tasks.OnSuccessListener;
            import com.google.android.gms.tasks.Task;
            import com.google.firebase.auth.AuthResult;
            import com.google.firebase.auth.FirebaseAuth;
            import com.google.firebase.auth.FirebaseAuthException;
            import com.google.firebase.auth.FirebaseUser;
            import com.google.firebase.firestore.DocumentReference;
            import com.google.firebase.firestore.FirebaseFirestore;


            import java.util.HashMap;
            import java.util.Map;

            public class Singup extends AppCompatActivity {


                private FirebaseAuth mAuth;
                private EditText  etPass1, etPass2, etNombre, etApellidos;
                TextView LabelEmail, etCondiciones, etError;
                Button btRegistro;
                private CheckBox checkCondiciones;
                ImageView btBack;
                String etEmail;
                Bundle datos;
                FirebaseAuthException excepcionesFirebase;

                private static final String TAG = "ActivityRegistro";


                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_registro);
                    mAuth = FirebaseAuth.getInstance();
                    datos = getIntent().getExtras();
                    etEmail = datos.getString("envioDatos");
                    LabelEmail = findViewById(R.id.txtEmail);
                    etNombre = findViewById(R.id.etNombre);
                    etApellidos = findViewById(R.id.etApellidos);
                    etPass1 = findViewById(R.id.formularioPass);
                    etPass2 = findViewById(R.id.formularioPass2);
                    etCondiciones = findViewById(R.id.etCondiciones);
                    etError = findViewById(R.id.etErrorRegistro);
                    btRegistro = findViewById(R.id.btRegistro);
                    btBack = findViewById(R.id.btAtras);
                    checkCondiciones = findViewById(R.id.checkCondiciones);
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    LabelEmail.setText(etEmail);


                    btRegistro.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String user_name = etNombre.getText().toString().trim();
                            //final String user_email = etEmail.getText().toString().trim();
                            String user_apellidos = etApellidos.getText().toString().trim();
                            String user_password = etPass1.getText().toString().trim();
                            String user_cfmPassword = etPass2.getText().toString().trim();
                            mAuth = FirebaseAuth.getInstance();

                            /**
                             * Si las condiciones de entrada del formulario es correcta se crea el usuario
                             */
                            if (isValid(user_name, user_apellidos, user_password, user_cfmPassword)) {

                                    //Mejaremos las excepciones  "ERROR_EMAIL_ALREADY_IN_USE" para no duplicar usuarios
                                    // y excepciones genéricas
                                    try {

                                        mAuth.createUserWithEmailAndPassword(etEmail, user_password);
                                        } catch (Exception ex){
                                                etError.setText("Error en la creacion del Usuario");
                                            }
                                        }
                                    try{
                                        mAuth.signInWithEmailAndPassword(etEmail, user_password).addOnCompleteListener(Singup.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                //Instanciamos un objeto del tipo FirebaseUser al que enviaremos un correo de autentificacion.
                                                FirebaseUser userA = FirebaseAuth.getInstance().getCurrentUser();
                                                userA.sendEmailVerification();
                                                //Inserto datos en la base de datos
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("nombre", user_name);
                                                user.put("apellidos", user_apellidos);
                                                user.put("email", etEmail);
                                                //Mensaje por consola de Usuari Creado
                                                Log.d("creacionUsuario", "Usuario creado");
                                                etError.setText("Usuario Creado");
                                                // Añadiendo el documento con el  ID generado
                                                db.collection("usuarios").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Intent in2 = new Intent(Singup.this, AfterSingout.class);
                                                        in2.setFlags(in2.FLAG_ACTIVITY_NEW_TASK | in2.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(in2);
                                                    }
                                                })

                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(Singup.this, "No se puede registrar", Toast.LENGTH_LONG).show();

                                                            }
                                                        });
                                            }
                                        });
                                    } catch (Exception ex2){
                                                        etError.setText("El email que intenta usar está ocupado"+ex2.getMessage());
                                                    }

                                    }
                    });

                    etCondiciones.setOnClickListener((new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Singup.this, condiciones.class));
                        }
                    }));

                    btBack.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Singup.this, Login.class));
                        }
                    });

                }


                public boolean isValid(String user_name, String user_apellidos, String user_password, String user_cfmPassword) {

                    if (!checkCondiciones.isChecked()) {
                        etError.setText("Debes aceptar las condiciones de uso");
                        return false;
                    }
                    //Que el nombre de usuario esté vacío
                    if (user_name.isEmpty()) {
                        etNombre.setError("Tu nombre es necesario");
                        etNombre.requestFocus();
                        //Return Error
                        return false;
                    }
                    //Que el campo apellidos de usuario esté vacío
                    if (user_apellidos.isEmpty()) {
                        etApellidos.setError("Tus apellidos son necesario");
                        etApellidos.requestFocus();
                        //Return Error
                        return false;
                    }

                    //Que la contraseña esté vacía
                    else if (user_password.isEmpty()) {
                        etPass1.setError("Password is required");
                        etPass1.requestFocus();
                        //Return Error
                        return false;
                    }
                    //Que el password tenga menos de 6 caracteres
                    else if (user_password.length() < 6) {
                        etPass1.setError("Debe tener al menos 6 caracteres");
                        etPass1.requestFocus();
                        //Return Error
                        return false;
                    }
                    //Que el password de confirmación sea igual
                    else if (!user_cfmPassword.equals(user_password)) {
                        etPass2.setError("Las contraseñas no son iguales");
                        etPass2.requestFocus();
                        //Return Error
                        return false;
                    }
                    else {
                        return true;
                    }

                }

            }




