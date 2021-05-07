package app.dmmunoz.mifamilia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Switch btCerrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btCerrar = findViewById(R.id.btClose);
        //Cerrar Sesion
        if (!btCerrar.isChecked()){
        mAuth.getInstance().signOut();}
    }
}