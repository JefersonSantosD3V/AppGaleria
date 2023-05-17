package br.com.appgaleria.costura.diferente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RecuperarActivity extends AppCompatActivity {

    private ImageView img_btn_fechar;
    private Button btn_recuperar;
    private EditText txt_nome, txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        getSupportActionBar().hide();

        iniciarComponetes();

        img_btn_fechar.setOnClickListener(v -> {
            Intent intent = new Intent(RecuperarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void recuperar(View v) {
        String nome = txt_nome.getText().toString();
        String email = txt_email.getText().toString();

        if (nome.equals("") || email.equals("")) {
            Toast.makeText(RecuperarActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        } else if (verificaEmail(email)) {
            Intent intent = new Intent(RecuperarActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(RecuperarActivity.this, "Link de recuperação enviado para o e-mail cadastrado!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RecuperarActivity.this, "O e-mail informado não esta cadastrado.", Toast.LENGTH_SHORT).show();
        }
    }

    //verifica se o email tem cadastro
    private boolean verificaEmail(String email) {
        return true;
    }

    private void iniciarComponetes() {
        img_btn_fechar = findViewById(R.id.rec_btn_fechar);
        btn_recuperar = findViewById(R.id.rec_btn_recuperar);
        txt_nome = findViewById(R.id.rec_edit_nome);
        txt_email = findViewById(R.id.rec_edit_email);
    }
}