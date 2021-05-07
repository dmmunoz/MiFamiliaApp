    package app.dmmunoz.mifamilia;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.analytics.FirebaseAnalytics;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;

    public class Login extends AppCompatActivity implements View.OnClickListener{
        Button btnLogin;
        EditText txtEmail, txtPassword;
        TextView tvError, txtForget, txtRegistrar;
        FirebaseAuth mAuth;
        private FirebaseAnalytics analytics;

        @Override
        protected void onCreate(Bundle savedInstanceState) {


            /**
             * Splash de incio
             */
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Volvemos al tema de inicio después del splash
            setTheme(R.style.Theme_MiFamilia);
            super.onCreate(savedInstanceState);
            //Llamamos al activity de Login
            setContentView(R.layout.activity_login);
            analytics = FirebaseAnalytics.getInstance(this);
            btnLogin = (Button)findViewById(R.id.btAcceso);
            txtEmail = findViewById(R.id.etUser);
            txtPassword = findViewById(R.id.etPass);
            txtForget= findViewById(R.id.etForgetLogin);
            txtRegistrar=findViewById(R.id.etRegistrar);
            tvError = findViewById(R.id.etErrorInicio);
            mAuth = FirebaseAuth.getInstance();

            //Asociamos los botones al Listener de click
            btnLogin.setOnClickListener(this);
            txtRegistrar.setOnClickListener(this);
            txtForget.setOnClickListener(this);

        }
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();


                switch (view.getId()){

                    /**
                     * En este caso es cuando se pulsa el botón de acceder. Comprobaremos que los campos de usuario y contraseña sean correctos
                     */
                    case R.id.btAcceso:
                        if (email.isEmpty()) { //Comprobamos si el campo de email está vacío
                            txtEmail.setError("El usuario es obligatorio");
                            txtEmail.requestFocus();
                            return; //No se permite la autentificacion al usuario
                        }

                        if (password.isEmpty()) { //Comprobamos si el campo de password está vacío
                            txtPassword.setError("La contraseña es obligatoria");
                            txtPassword.requestFocus();
                            return; //No se permite la autentificacion al usuario
                        }

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser usuarioVerificado = mAuth.getCurrentUser();
                                if (task.isSuccessful()) {
                                    if(usuarioVerificado.isEmailVerified()){
                                        Intent in = new Intent(Login.this, Home.class);
                                        in.setFlags(in.FLAG_ACTIVITY_NEW_TASK | in.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(in);
                                    } else{
                                        Toast.makeText(Login.this, "Verifica el correo y autentifica cuenta", Toast.LENGTH_LONG).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(Login.this, "Fallo en la autentificación", Toast.LENGTH_SHORT).show();
                                    tvError.setText("Error en el usuario o la contraseña.");
                                    txtPassword.setText("");
                                    txtEmail.setText("");
                                }
                            }
                        });
                        break;

                    /**
                     * Para enviar un correo electrónico de restablecimiento de contraseña a un usuario usaremos el método sendPasswordResetEmail
                     */
                    case R.id.etForgetLogin:
                        Intent in1 = new Intent(Login.this, recuperarPassword.class);
                        in1.setFlags(in1.FLAG_ACTIVITY_NEW_TASK | in1.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in1);
                        break;
                    /**
                     * Entramos a la activity para el registro de un nuevo usuario.
                     */
                    case R.id.etRegistrar:
                        Intent in2 = new Intent(Login.this, comprobacionEmail.class);
                        in2.setFlags(in2.FLAG_ACTIVITY_NEW_TASK | in2.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in2);
                        break;

                }

            }
    }