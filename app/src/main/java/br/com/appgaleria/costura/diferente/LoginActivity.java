package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private Button btn_cadastrar;
    private TextView txt_recuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //esconde actcionbar
        getSupportActionBar().hide();
        //tela cheia
       //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        iniciarComponetes();

        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
                startActivity(intent);
            }
        });

        txt_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RecuperarActivity.class);
                startActivity(intent);
            }
        });

    }

    private void iniciarComponetes(){
        btn_cadastrar = findViewById(R.id.btn_cadastrar);
        txt_recuperar = findViewById(R.id.txt_recuperar);
    }

}