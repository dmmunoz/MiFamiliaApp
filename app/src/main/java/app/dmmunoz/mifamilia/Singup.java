    package app.dmmunoz.mifamilia;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Patterns;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.FirebaseDatabase;

    public class Singup extends AppCompatActivity {


        private FirebaseAuth mAuth;
        private EditText etEmail, etPass1, etPass2, etNombre, etApellidos;
        private TextView etCondiciones, etError;
        private Button btRegistro,checkCondiciones;
        private ImageButton btBack;
        private FirebaseDatabase firebaseDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registro);
            mAuth = FirebaseAuth.getInstance();
            etEmail = findViewById(R.id.formularioEmail);
            etNombre = findViewById(R.id.etNombre);
            etApellidos = findViewById(R.id.etApellidos);
            etPass1  = findViewById(R.id.formularioPass);
            etPass2 = findViewById(R.id.formularioPass2);
            etCondiciones= findViewById(R.id.etCondiciones);
            etError = findViewById(R.id.etErrorRegistro);
            btRegistro = findViewById(R.id.btRegistro);
            btBack = findViewById(R.id.btAtras);
            checkCondiciones = findViewById(R.id.checkCondiciones);
            firebaseDatabase = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();

            btRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String user_name = etNombre.getText().toString().trim();
                    final String user_email = etEmail.getText().toString().trim();
                    String user_apellidos = etApellidos.getText().toString().trim();
                    String user_password = etPass1.getText().toString().trim();
                    String user_cfmPassword = etPass2.getText().toString().trim();

                    if (isValid(user_name, user_apellidos, user_email, user_password, user_cfmPassword)) {
                        mAuth.createUserWithEmailAndPassword(user_email, user_password);
                        mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(Singup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Unit will be kg for now
                                Usuario newUser = new Usuario(FirebaseAuth.getInstance().getCurrentUser().getUid(), user_name, user_email);
                                firebaseDatabase.getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(newUser);
                                mAuth.signOut();
                                Intent in = new Intent(Singup.this, Login.class);
                                //Clear activity stack
                                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                            }
                        });
                    } else {
                        Toast.makeText(Singup.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            etCondiciones.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Singup.this, condiciones.class));
                }
            }));
          //  btRegistro.setOnClickListener(new View.OnClickListener() {
          //      @Override
          //      public void onClick(View view) {
          //          startActivity(new Intent(Singup.this, Login.class));
          //      }
          //  });

            btBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Singup.this, Login.class));
                }
            });
        }

        public boolean isValid(String user_name, String user_apellidos, String user_email, String user_password, String user_cfmPassword){
            if (!checkCondiciones.isActivated()) {
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
            if (user_name.isEmpty()) {
                etApellidos.setError("Tus apellidos son necesario");
                etApellidos.requestFocus();
                //Return Error
                return false;
            }
            //Que el campo del mail esté vacío
            else if (user_email.isEmpty()) {
                etEmail.setError("Email es necesario");
                etEmail.requestFocus();
                //Return Error
                return false;
            }
            //Que el formato del mail esté mal
            else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                etEmail.setError("Pon un Email correcto");
                etEmail.requestFocus();
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
            else if (user_password.length()<6) {
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




