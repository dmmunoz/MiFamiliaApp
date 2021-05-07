package app.dmmunoz.mifamilia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AfterSingout extends AppCompatActivity {
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_singout);
        home=findViewById(R.id.etCasa);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent(AfterSingout.this, Login.class);
                in2.setFlags(in2.FLAG_ACTIVITY_NEW_TASK | in2.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in2);
            }
        });


    }
}